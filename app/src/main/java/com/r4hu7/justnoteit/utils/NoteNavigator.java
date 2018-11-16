package com.r4hu7.justnoteit.utils;

import com.r4hu7.justnoteit.data.model.Note;

import java.util.List;

public interface NoteNavigator {
    void openNotePad(int noteId);

    void searchNotes(String searchString);

    void populateSearchList(List<Note> notes);

    void clearSearchList();
}
