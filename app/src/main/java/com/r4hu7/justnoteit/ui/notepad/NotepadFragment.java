package com.r4hu7.justnoteit.ui.notepad;

import android.Manifest;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.r4hu7.justnoteit.R;
import com.r4hu7.justnoteit.data.NotesRepository;
import com.r4hu7.justnoteit.data.local.LocalDataSource;
import com.r4hu7.justnoteit.data.model.Note;
import com.r4hu7.justnoteit.databinding.FragmentNotepadBinding;
import com.r4hu7.justnoteit.di.component.DaggerRepositoryComponent;
import com.r4hu7.justnoteit.di.module.ContextModule;
import com.r4hu7.justnoteit.ui.view.ReaderToolCardView;
import com.r4hu7.justnoteit.ui.widget.NoteWidget;
import com.r4hu7.justnoteit.utils.AnalyticsApplication;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotepadFragment extends Fragment implements NotesRepository.SaveNotes, ReaderToolCardView.ReaderToolChangeListener {
    public static final int SYSTEM_PERMISSION_REQ_CODE = 9001;

    private NotepadViewModel mViewModel;
    private FragmentNotepadBinding binding;
    private NotesRepository repository;
    private LiveData<Note> noteLiveData;
    @BindView(R.id.tbPrimary)
    Toolbar tbPrimary;
    @BindView(R.id.cvReaderTool)
    ReaderToolCardView cvReaderTool;

    public static NotepadFragment newInstance() {
        return new NotepadFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_notepad, container, false);
        ButterKnife.bind(this, binding.getRoot());
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        tbPrimary.setNavigationOnClickListener(view -> getActivity().onBackPressed());
        cvReaderTool.addReaderToolChangeListener(this);

        mViewModel = ViewModelProviders.of(this).get(NotepadViewModel.class);
        mViewModel.setNavigator(this);
        binding.setVm(mViewModel);
        initData();
        if (Objects.requireNonNull(getActivity()).getIntent().hasExtra(NotepadActivity.NOTE_KEY)) {
            repository.getNote(getActivity().getIntent().getIntExtra(NotepadActivity.NOTE_KEY, -1), new LocalDataSource.LoadItemCallback<Note>() {
                @Override
                public void onLoading() {
                }

                @Override
                public void onItemLoaded(Note note) {
                    mViewModel.setNote(note);
                }

                @Override
                public void onDataNotAvailable(Throwable e) {

                    repository.getLastId(new LocalDataSource.LoadItemCallback<Integer>() {
                        @Override
                        public void onLoading() {

                        }

                        @Override
                        public void onItemLoaded(Integer integer) {
                            mViewModel.setNote(Note.getInstance(++integer));
                        }

                        @Override
                        public void onDataNotAvailable(Throwable e) {
                            mViewModel.setNote(Note.getInstance(1));
                        }
                    });
                }
            });
//            noteLiveData.observe(this, note -> mViewModel.setNote(note));
        } else if (mViewModel.note.get() == null) {
            repository.getLastId(new LocalDataSource.LoadItemCallback<Integer>() {
                @Override
                public void onLoading() {

                }

                @Override
                public void onItemLoaded(Integer integer) {
                    mViewModel.setNote(Note.getInstance(++integer));
                }

                @Override
                public void onDataNotAvailable(Throwable e) {

                }
            });
        }
    }

    private void initData() {
        if (repository == null)
            repository = DaggerRepositoryComponent.builder().contextModule(new ContextModule(getContext())).build().getNotesRepository();
    }

    @Override
    public void saveNote(Note note) {
        repository.saveNote(note);
    }

    @Override
    public void OnFontSizeChange(float sizeInPixel) {
        mViewModel.textSize.set(sizeInPixel);
        trackTheFontSize(sizeInPixel);

    }

    @Override
    public void OnNightModeChange(boolean isEnabled) {
        mViewModel.nightMode.set(isEnabled);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getActivity().getWindow().setStatusBarColor(getResources().getColor(isEnabled ? R.color.tint : R.color.shade0));
        }
    }

    @Override
    public void OnSystemPermissionRequired() {
        checkForSystemWritePermission();
    }

    public boolean checkForSystemWritePermission() {
        boolean permission;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            permission = Settings.System.canWrite(getActivity().getApplicationContext());
        } else {
            permission = ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.WRITE_SETTINGS) == PackageManager.PERMISSION_GRANTED;
        }
        if (permission) {
            cvReaderTool.switchToBrightnessTool();
            return true;
        } else {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:" + getActivity().getApplicationContext().getPackageName()));
                startActivityForResult(intent, SYSTEM_PERMISSION_REQ_CODE);
            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_SETTINGS}, SYSTEM_PERMISSION_REQ_CODE);
            }
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == SYSTEM_PERMISSION_REQ_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            cvReaderTool.switchToBrightnessTool();
        }
    }

    @Override
    public void onPause() {
        NoteWidget.sendRefreshBroadcast(getContext());
        super.onPause();
    }

    private void trackTheFontSize(float fontSize) {

        AnalyticsApplication application = (AnalyticsApplication) getActivity().getApplication();
        Tracker mTracker = application.getDefaultTracker();
        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("Action")
                .setAction("font-size")
                .setLabel(String.valueOf(fontSize))
                .build());
    }
}
