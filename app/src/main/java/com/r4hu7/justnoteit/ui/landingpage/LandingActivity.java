package com.r4hu7.justnoteit.ui.landingpage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.r4hu7.justnoteit.R;
import com.r4hu7.justnoteit.ui.notepad.NotepadActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LandingActivity extends AppCompatActivity {

    @BindView(R.id.adView)
    AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        ButterKnife.bind(this);
        MobileAds.initialize(getApplicationContext(), getResources().getString(R.string.ad_mob_app_id));
        setupAdView();
    }

    public void openNotePad(View v) {
        Intent intent = new Intent(getApplicationContext(), NotepadActivity.class);
        startActivity(intent);
    }


    private void setupAdView() {
        adView.setVisibility(View.VISIBLE);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        adView.loadAd(adRequest);
    }

}
