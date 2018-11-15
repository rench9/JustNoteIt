package com.r4hu7.justnoteit.data.local;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.r4hu7.justnoteit.data.model.Note;

import java.util.List;

@Dao
public interface NotesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Note... notes);

    @Delete
    void deleteNote(Note note);

    @Query("DELETE FROM _notes WHERE id=:noteId")
    void deleteNote(int noteId);

    @Query("SELECT * FROM _notes ORDER BY time DESC")
    LiveData<List<Note>> geNotes();

    @Query("SELECT * FROM _notes WHERE id=:noteId LIMIT 1")
    LiveData<Note> getNote(int noteId);

    @Query("SELECT * FROM _notes WHERE id=:noteId LIMIT 1")
    Note getNoteWithoutLiveData(int noteId);

    @Query("SELECT id FROM _notes ORDER BY id DESC LIMIT 1")
    int getLastId();

}
