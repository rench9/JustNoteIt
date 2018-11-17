package com.r4hu7.justnoteit.ui.widget;

import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.r4hu7.justnoteit.R;
import com.r4hu7.justnoteit.data.local.LocalDataSource;
import com.r4hu7.justnoteit.data.model.Note;
import com.r4hu7.justnoteit.di.component.DaggerRepositoryComponent;
import com.r4hu7.justnoteit.di.module.ContextModule;
import com.r4hu7.justnoteit.ui.notepad.NotepadActivity;

import java.util.List;

public class NotesWidgetDataProvider implements RemoteViewsService.RemoteViewsFactory, LocalDataSource.LoadItemCallback<List<Note>> {
    private Context context;
    private Intent intent;
    private List<Note> notes;

    NotesWidgetDataProvider(Context context, Intent intent) {
        this.context = context;
        this.intent = intent;
    }


    private void loadData() {
        final long identityToken = Binder.clearCallingIdentity();
        DaggerRepositoryComponent.builder().contextModule(new ContextModule(context)).build().getNotesRepository().getAllNotes(this);
        Binder.restoreCallingIdentity(identityToken);
    }

    @Override
    public void onCreate() {
        loadData();
    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if (notes == null)
            return 0;
        return notes.size();
    }

    @Override
    public RemoteViews getViewAt(int i) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.adapter_widget_note_item);

        remoteViews.setTextViewText(R.id.tvTitle, notes.get(i).getTitle());
        remoteViews.setTextViewText(R.id.tvTime, notes.get(i).getSimpleDate());

        Bundle extras = new Bundle();
        extras.putInt(NotepadActivity.NOTE_KEY, notes.get(i).getId());
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);
        remoteViews.setOnClickFillInIntent(R.id.llRoot, fillInIntent);
        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return i;
//        return notes.get(i).getId();
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public void onLoading() {

    }

    @Override
    public void onItemLoaded(List<Note> notes) {
        this.notes = notes;
        NoteWidget.sendRefreshBroadcast(context);
    }

    @Override
    public void onDataNotAvailable(Throwable e) {

    }
}
