package com.r4hu7.justnoteit.ui.landingpage;

import android.arch.lifecycle.ViewModel;

import com.r4hu7.justnoteit.data.model.Note;
import com.r4hu7.justnoteit.utils.NoteNavigator;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class NotesListViewModel extends ViewModel {

    List<Note> notes;
    WeakReference<NoteNavigator> noteNavigator;

    void setNoteNavigator(NoteNavigator noteNavigator) {
        this.noteNavigator = new WeakReference<>(noteNavigator);
        this.notes = new ArrayList<>();
    }

    public void searchNote(CharSequence s, int start, int before, int count) {
        if (noteNavigator != null && noteNavigator.get() != null)
            if (s.length() == 0)
                noteNavigator.get().clearSearchList();
            else
                noteNavigator.get().searchNotes(s.toString());
    }

    void setNotes(List<Note> notes) {
        this.notes = notes;
    }

    List<Note> getNotes() {
        return notes;
    }
}
