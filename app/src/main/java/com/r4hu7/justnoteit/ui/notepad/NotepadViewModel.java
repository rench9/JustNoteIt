package com.r4hu7.justnoteit.ui.notepad;

import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableField;

import com.r4hu7.justnoteit.data.NotesRepository;
import com.r4hu7.justnoteit.data.model.Note;

import java.lang.ref.WeakReference;

public class NotepadViewModel extends ViewModel {
    public ObservableField<Note> note = new ObservableField<>();
    private WeakReference<NotesRepository.SaveNotes> navigator;

    public void setNote(Note note) {
        this.note.set(note);
    }

    public void saveNote(CharSequence s, int start, int before, int count) {
        if (navigator != null && navigator.get() != null)
            navigator.get().saveNote(note.get());
    }

    public void setNavigator(NotesRepository.SaveNotes navigator) {
        this.navigator = new WeakReference<>(navigator);
    }
}
