package com.mobilemd.cameralibrary.util;

import android.text.Html;
import android.text.Spanned;

/**
 * @Author: 成晓知
 * @DateTime: 2021/5/21 14:39
 * @Description:
 **/
public class TimeUtils {

    public static Spanned formatTime(long millions, long duration){

        int second = (int) (millions / 1000);
        int yushu = (int) ((millions % 1000) / 100);

        int total = Math.round(duration / 1000.0f);

        String html = "<font color=\"black\">" + (second + "." + yushu) + "</font><font color=\"#B4BEC7\" }}>/" + (total) + "s</font>";

        return Html.fromHtml(html);
    }
}
