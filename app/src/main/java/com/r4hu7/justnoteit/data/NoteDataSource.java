package com.r4hu7.justnoteit.data;

import android.arch.lifecycle.LiveData;

import com.r4hu7.justnoteit.data.local.LocalDataSource;
import com.r4hu7.justnoteit.data.model.Note;

import java.util.List;

public interface NoteDataSource {
    LiveData<List<Note>> getAllNotes();

    void getAllNotes(LocalDataSource.LoadItemCallback<List<Note>> loadItemCallback);

    LiveData<Note> getNote(int noteId);

    List<Note> findNote(String bodySubString);

    void getNote(int noteId, LocalDataSource.LoadItemCallback<Note> loadItemCallback);

    void saveNote(Note note);

    void getLastId(LocalDataSource.LoadItemCallback<Integer> loadItemCallback);
}
