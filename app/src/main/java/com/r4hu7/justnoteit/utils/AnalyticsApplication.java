package com.r4hu7.justnoteit.utils;

import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.r4hu7.justnoteit.R;

public class AnalyticsApplication extends Application {

    private static GoogleAnalytics sAnalytics;
    private static Tracker sTracker;

    @Override
    public void onCreate() {
        super.onCreate();
        sAnalytics = GoogleAnalytics.getInstance(this);
    }

    synchronized public Tracker getDefaultTracker() {
        if (sTracker == null) {
            sTracker = sAnalytics.newTracker(R.xml.global_tracker);
        }

        return sTracker;
    }
}
