package com.r4hu7.justnoteit.ui.landingpage;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.r4hu7.justnoteit.R;
import com.r4hu7.justnoteit.data.NotesRepository;
import com.r4hu7.justnoteit.data.model.Note;
import com.r4hu7.justnoteit.databinding.FragmentNotesListBinding;
import com.r4hu7.justnoteit.di.component.DaggerRepositoryComponent;
import com.r4hu7.justnoteit.di.module.ContextModule;
import com.r4hu7.justnoteit.ui.adapter.NoteItemAdapter;
import com.r4hu7.justnoteit.ui.notepad.NotepadActivity;
import com.r4hu7.justnoteit.ui.widget.NoteWidget;
import com.r4hu7.justnoteit.utils.NoteNavigator;
import com.r4hu7.justnoteit.utils.SearchAsyncTask;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotesListFragment extends Fragment implements NoteNavigator {

    private FragmentNotesListBinding binding;
    private NotesListViewModel mViewModel;
    private NoteItemAdapter adapter;
    private NotesRepository repository;

    @BindView(R.id.rvContainer)
    RecyclerView rvContainer;
    @BindView(R.id.etSearch)
    TextInputEditText etSearch;


    public static NotesListFragment newInstance() {
        return new NotesListFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_notes_list, container, false);
        ButterKnife.bind(this, binding.getRoot());
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(NotesListViewModel.class);
        mViewModel.setNoteNavigator(this);
        binding.setVm(mViewModel);
        initData();
        initRecyclerView();
    }

    private void initData() {
        if (repository == null)
            repository = DaggerRepositoryComponent.builder().contextModule(new ContextModule(getContext())).build().getNotesRepository();

        if (adapter == null)
            adapter = new NoteItemAdapter(new ArrayList<>(), this);
        repository.getAllNotes().observe(this, notes -> {
            mViewModel.setNotes(notes);
            if (mViewModel.searchQuery.get() != null && !mViewModel.searchQuery.get().isEmpty())
                adapter.setItems(mViewModel.getFoundNotes());
            else
                adapter.setItems(mViewModel.getNotes());
        });
    }

    private void initRecyclerView() {
        if (rvContainer.getAdapter() == null)
            rvContainer.setAdapter(adapter);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(getContext(), OrientationHelper.VERTICAL) {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.left = 58;
                outRect.right = 58;
                outRect.top = 24;
                outRect.bottom = 24;
            }
        };
        itemDecoration.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.shape_line_h));
        rvContainer.addItemDecoration(itemDecoration);
    }

    @Override
    public void openNotePad(int noteId) {
        Bundle bundle = new Bundle();
        Intent intent = new Intent(getContext(), NotepadActivity.class);
        bundle.putInt(NotepadActivity.NOTE_KEY, noteId);
        intent.putExtras(bundle);
        startActivity(intent);
    }


    @Override
    public void searchNotes(String searchString) {
        if (searchString.isEmpty())
            mViewModel.clearFoundNotes();
        new SearchAsyncTask(repository, this).execute(searchString);
    }

    @Override
    public void populateSearchList(List<Note> notes) {
        mViewModel.setFoundNotes(notes);
        adapter.setItems(notes);
    }

    @Override
    public void clearSearchList() {
        adapter.setItems(mViewModel.getNotes());
    }

    @Override
    public void onPause() {
        NoteWidget.sendRefreshBroadcast(getContext());
        super.onPause();
    }
}
