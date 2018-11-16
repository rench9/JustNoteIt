package com.r4hu7.justnoteit.ui.landingpage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.r4hu7.justnoteit.R;
import com.r4hu7.justnoteit.ui.notepad.NotepadActivity;

public class LandingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
    }

    public void openNotePad(View v) {
        Intent intent = new Intent(getApplicationContext(), NotepadActivity.class);
        startActivity(intent);
    }

}
