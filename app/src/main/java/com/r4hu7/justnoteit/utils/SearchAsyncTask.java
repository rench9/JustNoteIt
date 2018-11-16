package com.r4hu7.justnoteit.utils;

import android.os.AsyncTask;

import com.r4hu7.justnoteit.data.NotesRepository;
import com.r4hu7.justnoteit.data.model.Note;

import java.lang.ref.WeakReference;
import java.util.List;

public class SearchAsyncTask extends AsyncTask<String, Void, List<Note>> {

    private NotesRepository notesRepository;
    private WeakReference<NoteNavigator> navigator;

    public SearchAsyncTask(NotesRepository notesRepository, NoteNavigator navigator) {
        this.notesRepository = notesRepository;
        this.navigator = new WeakReference<>(navigator);
    }

    @Override
    protected List<Note> doInBackground(String... strings) {
        return notesRepository.findNote(strings[0]);
    }

    @Override
    protected void onPostExecute(List<Note> notes) {
        super.onPostExecute(notes);
        if (navigator != null && navigator.get() != null)
            navigator.get().populateSearchList(notes);
    }

    @Override
    protected void onCancelled(List<Note> notes) {
        super.onCancelled(notes);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }
}
