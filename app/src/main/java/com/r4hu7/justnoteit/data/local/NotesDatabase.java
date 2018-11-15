package com.r4hu7.justnoteit.data.local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.r4hu7.justnoteit.data.model.Note;


@Database(entities = {Note.class}, version = 1, exportSchema = false)
public abstract class NotesDatabase extends RoomDatabase {
    private static NotesDatabase INSTANCE;
    private static final String DATABASE_NAME = "_main";

    public static NotesDatabase getInstance(Context context) {
        if (INSTANCE == null)
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    NotesDatabase.class,
                    DATABASE_NAME
            ).build();
        return INSTANCE;
    }

    public abstract NotesDao notesDao();
}
