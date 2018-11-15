package com.r4hu7.justnoteit.data.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Entity(tableName = "_notes")
public class Note {
    private static final String TABLE_NAME = "_notes";
    @PrimaryKey
    private int id;
    private String title;
    private String shortDescription;
    private String body;
    private long time;
    private int colorCode;

    public static Note getInstance(int id) {
        Note n = new Note();
        n.setId(id);
        n.setTime(System.currentTimeMillis());
        return n;
    }

    public String getSimpleTime() {
        return new SimpleDateFormat("HH:mm a").format(new Date(time));
    }

    public String getSimpleDate() {
        return new SimpleDateFormat("dd MMM yyyy").format(new Date(time));
    }

    public String getSmartTime() {
        long timeNow = System.currentTimeMillis();
        Date date = new Date(time);
        long mDiff = TimeUnit.MILLISECONDS.toMinutes(timeNow) - TimeUnit.MILLISECONDS.toMinutes(date.getTime());
        if (mDiff < 60L) {
            if (mDiff == 0L)
                return "Just now";
            return "$mDiff min ago";
        }

        long hDiff = TimeUnit.MILLISECONDS.toHours(timeNow) - TimeUnit.MILLISECONDS.toHours(date.getTime());
        if (hDiff < 48) {
            if (hDiff < 24)
                return new SimpleDateFormat(String.format("'%d hour ago at' HH:mm a", hDiff)).format(date);
            else
                return new SimpleDateFormat("1 day ago at' HH:mm a").format(date);
        }

        return new SimpleDateFormat("dd MMM yyyy 'at' HH:mm a").format(date);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getColorCode() {
        return colorCode;
    }

    public void setColorCode(int colorCode) {
        this.colorCode = colorCode;
    }
}
