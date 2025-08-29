package com.tmslibrary.manager;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.TextUtils;

import androidx.appcompat.app.AlertDialog;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tmslibrary.app.TmsLibraryApp;
import com.tmslibrary.entity.VersionCheckEntity;
import com.tmslibrary.entity.base.BaseErrorEntity;
import com.tmslibrary.listener.VersionUpdateCallback;
import com.tmslibrary.mvp.interactor.impl.UpdateDownloadCountInteractorImpl;
import com.tmslibrary.mvp.interactor.impl.VersionCheckInteractorImpl;
import com.tmslibrary.mvp.presenter.impl.UpdateDownloadCountPresenterImpl;
import com.tmslibrary.mvp.presenter.impl.VersionCheckPresenterImpl;
import com.tmslibrary.mvp.view.UpdateDownloadCountView;
import com.tmslibrary.mvp.view.VersionCheckView;
import com.tmslibrary.utils.DateUtils;
import com.tmslibrary.utils.PermissionUtils;
import com.tmslibrary.utils.ToastUtils;
import com.tmslibrary.utils.install.DownloadAndInstall;

import java.io.File;
import java.util.Date;

public class AppVersionManager {

    public static synchronized String getVersionName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static synchronized int getVersionCode(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static void checkVersion(VersionCheckEntity data, Context context, VersionUpdateCallback callback){
        if(data.getResult() != null && "1".equals(data.getResult().getUpdateFlag())){
            if(data.getResult().getForeceUpdate() == 1){
                new AlertDialog.Builder(context)
                        .setTitle("巴斯夫调漆宝有更新")
                        .setMessage("")
                        .setCancelable(false)
                        .setNegativeButton("更新", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                callback.onVersionUpdate(data.getResult().getUpdateUrl());
                            }
                        })
                        .show();
            }else{
                new AlertDialog.Builder(context)
                        .setTitle("巴斯夫调漆宝有更新")
                        .setCancelable(false)
                        .setMessage("")
                        .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setNegativeButton("更新", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                callback.onVersionUpdate(data.getResult().getUpdateUrl());
                            }
                        })
                        .show();
            }
        }
    }

    public static void getVersion(Context context, RxPermissions permissions){
        VersionCheckPresenterImpl mVersionCheckPresenterImpl = new VersionCheckPresenterImpl(new VersionCheckInteractorImpl());
        mVersionCheckPresenterImpl.attachView(new VersionCheckView() {
            @Override
            public void getVersionCheckCompleted(VersionCheckEntity data) {
                if(data != null){
                    AppVersionManager.checkVersion(data,context,new VersionUpdateCallback(){
                        @Override
                        public void onVersionUpdate(String url) {
//                            PermissionUtils.checkRequestPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE, permissions, new PermissionUtils.OnPermissionResultListener() {
//                                @Override
//                                public void allow() {
//
//                                  DownloadAndInstall.start(url,(Activity)context);
//                                    //for test
//                                    //DownloadAndInstall.start("https://file.trialos.com/resources/8a81811c6f087854016f0d1d80240592.apk",(Activity)context);
//
//                                }
//
//                                @Override
//                                public void cancel() {
//
//                                }
//                            });
                            DownloadAndInstall.start(url,(Activity)context);

                        }
                    });
                }
            }

            @Override
            public void showProgress(String reqType) {

            }

            @Override
            public void hideProgress(String reqType) {

            }

            @Override
            public void showErrorMsg(String reqType, String msg) {
            }
        });
        mVersionCheckPresenterImpl.versionCheck("basf_recipe_android",getVersionName(context));

    }


    /**
     * 比较版本号的大小
     * <p>
     * 1、前者大则返回一个正数
     * 2、后者大返回一个负数
     * 3、相等则返回0
     *
     * @param version1 版本号1
     * @param version2 版本号2
     * @return int
     */
    public static int compareVersion(String version1, String version2) {
        if (version1 == null || version2 == null) {
            return 0;
        }
        try {
            // 注意此处为正则匹配，不能用.
            String[] versionArray1 = version1.split("\\.");
            String[] versionArray2 = version2.split("\\.");
            int idx = 0;
            // 取数组最小长度值
            int minLength = Math.min(versionArray1.length, versionArray2.length);
            int diff = 0;
            // 先比较长度，如果长度相同，再比较大小
            // 如果已经分出大小，则直接返回，如果未分出大小，则再比较位数，有子版本的为大
        /*while (idx < minLength
                && (diff = versionArray1[idx].length() - versionArray2[idx].length()) == 0
                && (diff = Integer.parseInt(versionArray1[idx]) - Integer.parseInt(versionArray2[idx])) == 0) {
            ++idx;
        }*/
            while (idx < minLength
                    && (diff = Integer.parseInt(versionArray1[idx]) - Integer.parseInt(versionArray2[idx])) == 0) {
                ++idx;
            }
            diff = (diff != 0) ? diff : versionArray1.length - versionArray2.length;
            return diff;
        }catch (Exception e){
            return 0;
        }

    }
}
