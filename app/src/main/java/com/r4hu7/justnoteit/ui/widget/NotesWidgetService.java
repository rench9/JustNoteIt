package com.r4hu7.justnoteit.ui.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class NotesWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new NotesWidgetDataProvider(getApplicationContext(), intent);
    }
}
