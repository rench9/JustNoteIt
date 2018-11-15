package com.r4hu7.justnoteit.ui.notepad;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.r4hu7.justnoteit.R;
import com.r4hu7.justnoteit.data.NotesRepository;
import com.r4hu7.justnoteit.data.local.LocalDataSource;
import com.r4hu7.justnoteit.data.model.Note;
import com.r4hu7.justnoteit.databinding.FragmentNotepadBinding;
import com.r4hu7.justnoteit.di.component.DaggerRepositoryComponent;
import com.r4hu7.justnoteit.di.module.ContextModule;

import java.util.Objects;

public class NotepadFragment extends Fragment implements NotesRepository.SaveNotes {

    private NotepadViewModel mViewModel;
    private FragmentNotepadBinding binding;
    private NotesRepository repository;
    private LiveData<Note> noteLiveData;

    public static NotepadFragment newInstance() {
        return new NotepadFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_notepad, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(NotepadViewModel.class);
        mViewModel.setNavigator(this);
        binding.setVm(mViewModel);
        initData();
        if (Objects.requireNonNull(getActivity()).getIntent().hasExtra(NotepadActivity.NOTE_KEY)) {
            repository.getNote(getActivity().getIntent().getIntExtra(NotepadActivity.NOTE_KEY, -1), new LocalDataSource.LoadItemCallback<Note>() {
                @Override
                public void onLoading() {
                    Log.e("RECEIVED NOTE ID", String.valueOf(getActivity().getIntent().getIntExtra(NotepadActivity.NOTE_KEY, -1)));
                }

                @Override
                public void onItemLoaded(Note note) {
                    mViewModel.setNote(note);
                    Log.e("DATA LOADED", note.getBody());
                }

                @Override
                public void onDataNotAvailable(Throwable e) {
                    Log.e("DATA NOT LOADED", e.getMessage());

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
//        noteLiveData.setValue(note);
        repository.saveNote(note);
    }
}
