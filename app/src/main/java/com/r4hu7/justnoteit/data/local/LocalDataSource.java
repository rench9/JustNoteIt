package com.r4hu7.justnoteit.data.local;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.r4hu7.justnoteit.data.NoteDataSource;
import com.r4hu7.justnoteit.data.model.Note;
import com.r4hu7.justnoteit.utils.AppExecutors;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LocalDataSource implements NoteDataSource {
    private static volatile LocalDataSource INSTANCE;
    private NotesDatabase database;
    private AppExecutors mAppExecutors;

    private LocalDataSource(@NonNull AppExecutors appExecutors, @NonNull NotesDatabase database) {
        this.database = database;
        this.mAppExecutors = appExecutors;
    }

    public static LocalDataSource getInstance(@NonNull AppExecutors appExecutors, NotesDatabase notesDatabase) {
        if (INSTANCE == null)
            INSTANCE = new LocalDataSource(appExecutors, notesDatabase);
        return INSTANCE;
    }

    private void execute(Runnable runnable) {
        mAppExecutors.diskIO().execute(runnable);
    }

    @Override
    public LiveData<List<Note>> getAllNotes() {
        return database.notesDao().geNotes();
    }

    @Override
    public void getAllNotes(LoadItemCallback<List<Note>> loadItemCallback) {
        loadItemCallback.onLoading();
        Runnable runnable = () -> {
            List<Note> notes = database.notesDao().geNotesWithoutLiveData();
            if (notes != null && !notes.isEmpty())
                loadItemCallback.onItemLoaded(notes);
            else
                loadItemCallback.onDataNotAvailable(new Exception("Notes not found!"));

        };
        execute(runnable);
    }

    @Override
    public LiveData<Note> getNote(int noteId) {
        return database.notesDao().getNote(noteId);
    }

    @Override
    public List<Note> findNote(String bodySubString) {
        return database.notesDao().findNote("%" + bodySubString + "%");
    }

    @Override
    public void getNote(int noteId, LoadItemCallback<Note> loadItemCallback) {
        loadItemCallback.onLoading();
        Runnable runnable = () -> {
            Note n = database.notesDao().getNoteWithoutLiveData(noteId);
            if (n == null)
                loadItemCallback.onDataNotAvailable(new Exception(String.format("Note with id %d not found!", noteId)));
            else
                loadItemCallback.onItemLoaded(n);
        };
        execute(runnable);
    }

    @Override
    public void saveNote(Note note) {
        Runnable runnable = () -> {
            if (note.getBody() == null || note.getBody().isEmpty())
                database.notesDao().deleteNote(note.getId());
            else {
                note.setTime(System.currentTimeMillis());
                List<String> tD = extractTitle(note.getBody());
                if (tD.size() > 0)
                    note.setTitle(tD.get(0));
                else
                    note.setTitle(null);
                if (tD.size() > 1)
                    note.setShortDescription(tD.get(1));
                else
                    note.setShortDescription(null);
                database.notesDao().insert(note);
            }
        };
        execute(runnable);
    }

    @Override
    public void getLastId(LoadItemCallback<Integer> loadItemCallback) {

        loadItemCallback.onLoading();
        Runnable runnable = () -> {
            int i = database.notesDao().getLastId();
            loadItemCallback.onItemLoaded(i);
        };
        execute(runnable);
    }

    private List<String> extractTitle(String body) {
        List<String> allMatches = new ArrayList<>(2);
        boolean haveSpace = false;
        Matcher m = Pattern.compile("(?m)^[^\n]*").matcher(body);
        while (m.find()) {
            if (allMatches.size() >= 2)
                return allMatches;
            String f = m.group();
            if (!haveSpace && !f.isEmpty()) {
                haveSpace = true;
            }
            if (!f.trim().isEmpty())
                allMatches.add(m.group());
        }
        if (allMatches.isEmpty() && haveSpace)
            allMatches.add(" ");
        return allMatches;
    }

    private void extractDesc(String body) {

    }


    public interface LoadItemCallback<T> {
        void onLoading();

        void onItemLoaded(T t);

        void onDataNotAvailable(Throwable e);
    }
}
