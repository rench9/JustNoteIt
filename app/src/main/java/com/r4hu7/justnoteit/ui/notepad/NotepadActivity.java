package com.r4hu7.justnoteit.ui.notepad;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.r4hu7.justnoteit.R;

public class NotepadActivity extends AppCompatActivity {
    public static final String NOTE_KEY = "NOTE_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notepad);
    }
}
