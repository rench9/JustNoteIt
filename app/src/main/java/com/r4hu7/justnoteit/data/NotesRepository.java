package com.r4hu7.justnoteit.data;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.r4hu7.justnoteit.data.local.LocalDataSource;
import com.r4hu7.justnoteit.data.model.Note;

import java.util.List;

public class NotesRepository implements NoteDataSource {
    private static NotesRepository INSTANCE;
    private LocalDataSource localDataSource;

    public static NotesRepository getInstance(@NonNull LocalDataSource localDataSource) {
        if (INSTANCE == null)
            INSTANCE = new NotesRepository(localDataSource);
        return INSTANCE;
    }

    private NotesRepository(LocalDataSource localDataSource) {
        this.localDataSource = localDataSource;
    }

    @Override
    public LiveData<List<Note>> getAllNotes() {
        return localDataSource.getAllNotes();
    }

    @Override
    public LiveData<Note> getNote(int noteId) {
        return localDataSource.getNote(noteId);
    }

    @Override
    public void getNote(int noteId, LocalDataSource.LoadItemCallback<Note> loadItemCallback) {
        localDataSource.getNote(noteId, loadItemCallback);
    }

    @Override
    public void saveNote(Note note) {
        localDataSource.saveNote(note);
    }

    @Override
    public void getLastId(LocalDataSource.LoadItemCallback<Integer> loadItemCallback) {
        localDataSource.getLastId(loadItemCallback);
    }

    public interface SaveNotes {
        void saveNote(Note note);
    }
}
