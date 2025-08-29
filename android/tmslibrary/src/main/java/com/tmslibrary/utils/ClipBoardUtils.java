package com.tmslibrary.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

import com.tmslibrary.R;


public class ClipBoardUtils {

    public static void copy(Context context,String content){
        //获取剪贴板管理器：
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        // 创建普通字符型ClipData
        ClipData mClipData = ClipData.newPlainText("Label", content);
        // 将ClipData内容放到系统剪贴板里。
        cm.setPrimaryClip(mClipData);
        ToastUtils.showShortSafe(R.string.copy_succeed);
    }
}
