package com.tmslibrary.utils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;

import com.google.gson.Gson;
import com.tmslibrary.app.TmsLibraryApp;
import com.tmslibrary.common.Const;
import com.tmslibrary.listener.TimerCallback;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Subscription;
import rx.functions.Action1;

public class AppUtils {

    public static String getUserId(){
        return PreferenceUtils.getPrefString(TmsLibraryApp.getInstances(),Const.KEY_USER_ID,"");
    }

    
    public static void setUserId(String userId){
        PreferenceUtils.setPrefString(TmsLibraryApp.getInstances(),Const.KEY_USER_ID,userId);
    }

    //{"userName":"13764764202","password":"96e79218965eb72c92a549dd5a330112","byType":"2","captchaKey":"","captcha":""}
    //json转JSONObject
    //冒泡排序


    public static void setPushLoginId(String pushLoginId){
        PreferenceUtils.setPrefString(TmsLibraryApp.getInstances(),Const.KEY_PUSH_LOGIN_ID,pushLoginId);
    }

    public static String getPushLoginId(){
        return PreferenceUtils.getPrefString(TmsLibraryApp.getInstances(),Const.KEY_PUSH_LOGIN_ID,"");
    }


    public static boolean isLogin(){
        String token = PreferenceUtils.getPrefString(TmsLibraryApp.getInstances(), Const.KEY_TOKEN,"");
        if(TextUtils.isEmpty(token)){
            return false;
        }else{
            return true;
        }
    }

    public static String getToken(){
        String token = PreferenceUtils.getPrefString(TmsLibraryApp.getInstances(),Const.KEY_TOKEN,"");
        return token;
    }

    public static void setToken(String token){
        PreferenceUtils.setPrefString(TmsLibraryApp.getInstances(),Const.KEY_TOKEN,token);
    }

    public static void setCookies(Context context){
        Log.i("ManagerCookies","setCookies");
        clearCookies(context);
        TimerUtils.delay(300, new TimerCallback() {
            @Override
            public void onTimerEnd() {

                CookieSyncManager.createInstance(context);
                CookieManager cookieManager = CookieManager.getInstance();
                cookieManager.setAcceptCookie(true);

                //设置域名
                String url = NetWorkConfigUtil.getBaseUrlByHostName("tms");
                cookieManager.setCookie(url, "token="+getToken());
                cookieManager.setCookie(url, "Domain="+url);
                cookieManager.setCookie(url, "Path=/");

                //设置新域名
                String newUrl = NetWorkConfigUtil.getBaseUrlByHostName("newDomain");
                cookieManager.setCookie(newUrl, "token="+getToken());
                cookieManager.setCookie(newUrl, "Domain="+url);
                cookieManager.setCookie(newUrl, "Path=/");


                //设置文件域名
                String fileUrl = NetWorkConfigUtil.getBaseUrlByHostName("file");
                cookieManager.setCookie(fileUrl, "token="+getToken());
                cookieManager.setCookie(fileUrl, "Domain="+fileUrl);
                cookieManager.setCookie(fileUrl, "Path=/");

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    cookieManager.flush();
                } else {
                    CookieSyncManager.getInstance().sync();
                }
            }
        });

    }

    public static void clearCookies(Context context){
        Log.i("ManagerCookies","clearCookies");
        CookieSyncManager.createInstance(context);
        CookieManager.getInstance().removeAllCookie();
    }

    public static String getUrlParams(String url,String key){
        if(TextUtils.isEmpty(url) || !url.contains("?")){
            return "";
        }

        String[] urlArr = url.split("\\?");
        if(urlArr.length > 1){
            String params = urlArr[1];
            String[] paramsArr = params.split("&");
            for(int i = 0;i<paramsArr.length;i++){
                String param = paramsArr[i];
                String[] keyValue = param.split("=");
                if(keyValue.length > 1){
                    String k = keyValue[0];
                    String v = keyValue[1];
                    if(k.equals(key)){
                        return v;
                    }
                }
            }
        }
        return "";
    }

    public static String appendParams(String url,HashMap<String,Object> params){
        Log.i("appendParams","before url:"+url);
        if(TextUtils.isEmpty(url)){
            return "";
        }

        if(params != null){
            if(!url.contains("?")){
                url+="?";
            }else{
                url+="&";
            }

            for (String key : params.keySet()) {
                Object value = params.get(key);
                url+=(key+"="+value+"&");
            }

            if(url.endsWith("&")){
                url = url.substring(0,url.length()-1);
            }

        }
        Log.i("appendParams","after url:"+url);
        return url;

    }

    public static String getCacheApolloConfig(Context context,String key){
        return PreferenceUtils.getPrefString(context,key,"");
    }

    //1=明码 2=暗码  -1:异常
    public static int getCodeTypeByQrCode(String qrcode){

        if(TextUtils.isEmpty(qrcode)){
            return -1;
        }

        if(qrcode.contains(NetWorkConfigUtil.getBaseUrlByHostName("codeType1"))){
            return 1;
        }
        if(qrcode.contains(NetWorkConfigUtil.getBaseUrlByHostName("h5"))){
            return 2;
        }
        return -1;
    }



}
