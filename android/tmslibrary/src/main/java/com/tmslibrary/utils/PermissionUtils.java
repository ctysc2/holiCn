package com.tmslibrary.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tmslibrary.R;

import java.util.Date;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static androidx.core.content.PermissionChecker.PERMISSION_DENIED;

/**
 * Created by cty on 2016/12/22.
 */

public class PermissionUtils {
    public static void checkRequestPermission(Context context, String permission, RxPermissions rxPermissions, OnPermissionResultListener listener){
        Log.d("AAAA","check permission:"+permission);
        if(permission.equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            if(Build.VERSION.SDK_INT >= 33){
                permission = "android.permission.READ_MEDIA_IMAGES";

            }else if(Build.VERSION.SDK_INT >= 30){
                permission = Manifest.permission.READ_EXTERNAL_STORAGE;
            }
        }
        boolean hasPermission =  ContextCompat.checkSelfPermission(context, permission) == PERMISSION_GRANTED;
        if(hasPermission){
            if(listener != null){
                listener.allow();
            }
        }else{

            String hint = "";
            switch (permission){
                case Manifest.permission.CAMERA:
                    hint = context.getString(R.string.hint_permission_camera);
                    break;
                case Manifest.permission.WRITE_EXTERNAL_STORAGE:
                case Manifest.permission.READ_EXTERNAL_STORAGE:
                case "android.permission.READ_MEDIA_IMAGES":
                    hint = context.getString(R.string.hint_permission_read_write);
                    break;
                case Manifest.permission.RECORD_AUDIO:
                    hint = context.getString(R.string.hint_permission_audio);
                    break;
                case Manifest.permission.READ_CONTACTS:
                    hint = context.getString(R.string.hint_permission_contact);
                    break;
                case Manifest.permission.ACCESS_FINE_LOCATION:
                    hint = context.getString(R.string.hint_permission_location);
                    break;

            }

            if(TextUtils.isEmpty(hint)){
                rxPermissions
                        .request(permission)
                        .subscribe(granted -> {
                            if (granted) {
                                // All requested permissions are granted
                                if(listener != null){
                                    listener.allow();
                                }
                            } else {
                                // At least one permission is denied

                            }
                        });
            }else{
                final String p = permission;
                new AlertDialog.Builder(context)
                        .setTitle(context.getString(R.string.hint))
                        .setMessage(hint)
                        .setCancelable(false)
                        .setPositiveButton(context.getString(R.string.go_to_allow), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                long startTs = new Date().getTime();
                                rxPermissions
                                        .requestEach(p)
                                        .subscribe(permission -> {
                                            if (permission.granted) {
                                                // `permission.name` is granted !
                                                if(listener != null){
                                                    listener.allow();
                                                }
                                            } else if (permission.shouldShowRequestPermissionRationale) {
                                                // Denied permission without ask never again
                                            } else {
                                                // Denied permission with ask never again
                                                // Need to go to the settings
                                                checkTime(startTs,context);
                                            }
                                        });
                            }
                        })
                        .setNegativeButton(context.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(listener != null){
                                    listener.cancel();
                                }
                            }
                        })

                        .show();
            }
        }
    }

    private static void checkTime(long start,Context context){
        long end = new Date().getTime();
        //间隔小于500ms，系统自动判断已拒绝且不弹窗
        Log.i("permission","间隔:"+(end - start)+"ms");
        if(end - start < 500){
            Intent intent = new Intent();
            intent.setAction(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.parse("package:" + context.getPackageName()));
            context.startActivity(intent);

        }
    }


    public interface OnPermissionResultListener{
        void allow();
        void cancel();
    }
}
