package com.tmslibrary.utils;

import android.content.Context;

import com.tmslibrary.common.Const;


public class CacheUtils {
    public static void clearCache(Context context){
        clearLoginInfo(context);
        PreferenceUtils.setPrefString(context, Const.KEY_LOCAL_PRIVACY_VERSION,"");
        PreferenceUtils.setPrefString(context, Const.KEY_LOCAL_PRIVACY_ID,"");
    }

    public static void clearLoginInfo(Context context){
        PreferenceUtils.setPrefString(context,Const.KEY_HOLI_T,"");
        PreferenceUtils.setPrefString(context,Const.KEY_USER_ID,"");
        PreferenceUtils.setPrefObject(context,Const.KEY_USER_INFO,null);
        PreferenceUtils.setPrefObject(context,Const.KEY_PREFERENCE,null);
        PreferenceUtils.setPrefObject(context,Const.KEY_MASTER_PERSON_INFO,null);
        PreferenceUtils.setPrefString(context,Const.KEY_NICK_NAME,"");
        PreferenceUtils.setPrefString(context,Const.KEY_PROFILE,"");
        PreferenceUtils.setPrefString(context,Const.KEY_BAC_IMG,"");
        PreferenceUtils.setPrefBoolean(context,Const.KEY_IS_ADMIN,false);
        //AppUtils.clearCookies(context);
    }
}
