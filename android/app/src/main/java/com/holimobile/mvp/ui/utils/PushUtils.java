package com.holimobile.mvp.ui.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.sdk.android.push.CloudPushService;
import com.alibaba.sdk.android.push.CommonCallback;
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory;
import com.holimobile.R;

public class PushUtils {
    public static void bindAccount(String account,Context context){
        if(TextUtils.isEmpty(account)){
            return;
        }

        CloudPushService mPushService = PushServiceFactory.getCloudPushService();
        mPushService.setNotificationSmallIcon(R.drawable.icon_small);
        mPushService.bindAccount(account, new CommonCallback() {
            @Override
            public void onSuccess(String s) {
                Log.i("CloudChannel","bind account success: "+account);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    // 通知渠道的id。
                    String id = "holi_mobile_android";
                    // 用户可以看到的通知渠道的名字。
                    CharSequence name = "notification channel";
                    // 用户可以看到的通知渠道的描述。
                    String description = "notification description";
                    int importance = NotificationManager.IMPORTANCE_HIGH;
                    NotificationChannel mChannel = new NotificationChannel(id, name, importance);
                    // 配置通知渠道的属性。
                    mChannel.setDescription(description);
                    // 设置通知出现时的闪灯（如果Android设备支持的话）。
                    mChannel.enableLights(true);
                    mChannel.setLightColor(Color.RED);
                    // 设置通知出现时的震动（如果Android设备支持的话）。
                    mChannel.enableVibration(true);
                    mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                    // 最后在notificationmanager中创建该通知渠道。
                    mNotificationManager.createNotificationChannel(mChannel);
                }
            }

            @Override
            public void onFailed(String errorCode, String errorMsg) {
                Log.i("CloudChannel","bind account failed:"+errorMsg);
            }
        });

    }

    public static void unbindAccount(){
        CloudPushService mPushService = PushServiceFactory.getCloudPushService();
        mPushService.unbindAccount(new CommonCallback() {
            @Override
            public void onSuccess(String s) {
                Log.i("CloudChannel","bind account success");
            }

            @Override
            public void onFailed(String errorCode, String errorMsg) {
                Log.i("CloudChannel","bind account failed:"+errorMsg);
            }
        });

    }
}
