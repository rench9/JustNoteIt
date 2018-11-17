package com.r4hu7.justnoteit.utils;

import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.r4hu7.justnoteit.R;

import java.util.Objects;

public class BindingAdapters {
    @BindingAdapter("setColor")
    public static void setColor(View v, int color) {
        Drawable d = Objects.requireNonNull(ContextCompat.getDrawable(v.getContext(), R.drawable.shape_circle));
        DrawableCompat.setTint(d, ContextCompat.getColor(v.getContext(), R.color.colorPrimaryDark));
        v.setBackground(d);
    }

    @BindingAdapter("textSize")
    public static void changeFontSize(TextView view, float size) {
        if (size > 0)
            view.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
    }
}
