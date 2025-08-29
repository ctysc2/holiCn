package com.mobilemd.cameralibrary.util;

import android.app.Activity;
import android.view.Gravity;
import android.widget.Toast;

/**
 * @Author: 成晓知
 * @DateTime: 6/10/21 10:46
 * @Description:
 **/
public class ToastUtils {

    public static void showToast(Activity activity, String message){
        Toast toast = Toast.makeText(activity, message, Toast.LENGTH_SHORT );
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }


}
