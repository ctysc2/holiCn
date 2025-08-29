package com.holimobile.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.holimobile.BuildConfig;
import com.holimobile.R;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

public class ShareUtils {

    public static void shareWx(String title, String desc, String url, Context context,int shareType){
        IWXAPI api = WXAPIFactory.createWXAPI(context, BuildConfig.WX_APP_ID);
        //初始化一个WXWebpageObject，填写url
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl =url;

//用 WXWebpageObject 对象初始化一个 WXMediaMessage 对象
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = title;
        msg.description = desc;
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
        msg.setThumbImage(bitmap);

//构造一个Req
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        //req.transaction = buildTransaction("webpage");
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message =msg;
//        分享到对话:
//        SendMessageToWX.Req.WXSceneSession
//        分享到朋友圈:
//        SendMessageToWX.Req.WXSceneTimeline ;
        if(shareType == 1){
            req.scene = SendMessageToWX.Req.WXSceneSession;
        }else{
            req.scene = SendMessageToWX.Req.WXSceneTimeline;
        }
        //req.userOpenId = getOpenId();

//调用api接口，发送数据到微信
        api.sendReq(req);
    }
}
