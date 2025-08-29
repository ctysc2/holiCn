package com.tmslibrary.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import com.tmslibrary.R;


public class PhoneUtils {
    public static void startCall(String number, Context context){
        if(TextUtils.isEmpty(number)){
            ToastUtils.showShortSafe(R.string.invalid_mobile);
            return;
        }
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + number);
        intent.setData(data);
        context.startActivity(intent);
    }
}
