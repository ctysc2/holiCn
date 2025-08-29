package com.tmslibrary.utils;

import android.content.Context;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import androidx.annotation.NonNull;

public class TextClick extends ClickableSpan {

    int color;
    Context context;
    boolean isUnderline;


    public TextClick(Context context, int color,boolean isUnderline) {
        this.context = context;
        this.color = color;
        this.isUnderline=isUnderline;
    }

    @Override
    public void onClick(@NonNull View widget) {

    }

    @Override
    public void updateDrawState(@NonNull TextPaint ds) {
        ds.setColor(context.getResources().getColor(color));//颜色
        ds.setUnderlineText(isUnderline);
    }
}
