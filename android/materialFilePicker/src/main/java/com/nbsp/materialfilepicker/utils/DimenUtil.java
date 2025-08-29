package com.nbsp.materialfilepicker.utils;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.text.TextPaint;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;



/**
 * Created by cty on 2016/12/12.
 */

public class DimenUtil {

    public static float dp2px(float dp,Context context) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return dp * scale + 0.5f;
    }

    public static float sp2px(float sp,Context context) {
        final float scale = context.getResources().getDisplayMetrics().scaledDensity;
        return sp * scale;
    }

    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static int getScreenWidthFull(Context context) {
        WindowManager windowManager =
                (WindowManager) context.getSystemService(Context.
                        WINDOW_SERVICE);
        final Display display = windowManager.getDefaultDisplay();
        Point outPoint = new Point();
        if (Build.VERSION.SDK_INT >= 19) {
            // 可能有虚拟按键的情况
            display.getRealSize(outPoint);
        } else {
            // 不可能有虚拟按键
            display.getSize(outPoint);
        }

        return outPoint.x;

    }

    public static int getScreenHeightFull(Context context) {
        WindowManager windowManager =
                (WindowManager) context.getSystemService(Context.
                        WINDOW_SERVICE);
        final Display display = windowManager.getDefaultDisplay();
        Point outPoint = new Point();
        if (Build.VERSION.SDK_INT >= 19) {
            // 可能有虚拟按键的情况
            display.getRealSize(outPoint);
        } else {
            // 不可能有虚拟按键
            display.getSize(outPoint);
        }

        return outPoint.y;

    }

//    public static int getScreenHeight() {
//        WindowManager windowManager =
//                (WindowManager) Application.getInstances().getSystemService(Context.
//                        WINDOW_SERVICE);
//        final Display display = windowManager.getDefaultDisplay();
//        Point outPoint = new Point();
//        if (Build.VERSION.SDK_INT >= 19) {
//            // 可能有虚拟按键的情况
//            display.getRealSize(outPoint);
//        } else {
//            // 不可能有虚拟按键
//            display.getSize(outPoint);
//        }
//        return outPoint.y;
//    }

    public static int getScreenHeight(Context context) {
        WindowManager windowManager =
                (WindowManager) context.getSystemService(Context.
                        WINDOW_SERVICE);
        DisplayMetrics dm =  context.getResources().getDisplayMetrics();

        windowManager.getDefaultDisplay().getRealMetrics(dm);
        float height = dm.heightPixels;
        return (int)height;
    }


//    public static int getScreenHeight() {
//        return Application.getInstances().getResources().getDisplayMetrics().heightPixels;
//    }


    public static  int getStatusBarHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen","android");
        int height = resources.getDimensionPixelSize(resourceId);
        return height;
    }

    public static boolean isWidthOverScreen(String content,int textSize,Context context){
        return getContentWidth(content,sp2px(textSize,context)) >= getScreenWidthFull(context)-dp2px(80,context);
    }

    public static int getContentWidth(String content,float textSize){
        Log.i("textSizetextSize","getContentWidth:"+textSize);
        TextPaint tp = new TextPaint();
        tp.setTextSize(textSize);
        tp.measureText(content);
        Rect rect = new Rect();
        tp.getTextBounds(content,0,content.length(),rect);
        return rect.width();
    }



}
