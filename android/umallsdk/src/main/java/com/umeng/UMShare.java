package com.umeng;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.umeng.callback.OnWxAuthListener;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareConfig;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.common.ResContainer;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Map;

public class UMShare {

    //分享到某个指定平台
    public static void share(Activity activity,final String text, final String img, final String weburl, final String title, final int sharemedia, final UMShareListener shareListener){
        ShareAction shareAction = new ShareAction(activity);
        shareAction.setPlatform(getShareMedia(sharemedia));
        if(shareListener == null){
            shareAction.setCallback(getUMShareListener(activity));
        }
        else{
            shareAction.setCallback(shareListener);
        }

        if (!TextUtils.isEmpty(weburl)){
            UMWeb web = new UMWeb(weburl);
            web.setTitle(title);
            web.setDescription(text);
            if (UMShare.getImage(activity,img)!=null){
                web.setThumb(getImage(activity,img));
            }
            shareAction.withMedia(web);
        }else if (UMShare.getImage(activity,img)!=null){
            shareAction.withMedia(getImage(activity,img));
        }
        shareAction.share();
    }

    //显示分享面板
    public static void shareboard(Activity activity,final String text, final String img, final String weburl, final String title, final ArrayList sharemedias, final UMShareListener shareListener){
        ShareAction shareAction = new ShareAction(activity);
        shareAction.withText(text);
        shareAction.setDisplayList(getShareMedias(sharemedias));

        if(shareListener == null){
            shareAction.setCallback(getUMShareListener(activity));
        }
        else{
            shareAction.setCallback(shareListener);
        }


        if (!TextUtils.isEmpty(weburl)){
            UMWeb web = new UMWeb(weburl);
            web.setTitle(title);
            web.setDescription(text);
            if (getImage(activity,img)!=null){
                web.setThumb(getImage(activity,img));
            }
            shareAction.withMedia(web);
        }else if (getImage(activity,img)!=null){
            shareAction.withMedia(getImage(activity,img));
        }
        shareAction.open();
    }

   // 显示分享面板，带自定义"复制"按钮
    public static void shareboardWithCopy(Activity activity, final String text, final String img, final String weburl, final String title, final ArrayList sharemedias, final UMShareListener shareListener){
        ShareAction shareAction = new ShareAction(activity);
        shareAction.withText(text);
        shareAction.setDisplayList(UMShare.getShareMedias(sharemedias));
        if(shareListener == null){
            shareAction.setCallback(getUMShareListener(activity));
        }
        else{
            shareAction.setCallback(shareListener);
        }

        if (!TextUtils.isEmpty(weburl)){
            UMWeb web = new UMWeb(weburl);
            web.setTitle(title);
            web.setDescription(text);
            if (UMShare.getImage(activity,img)!=null){
                web.setThumb(UMShare.getImage(activity,img));
            }
            shareAction.withMedia(web);
        }else if (UMShare.getImage(activity,img)!=null){
            shareAction.withMedia(UMShare.getImage(activity,img));
        }else {
        }
        shareAction.addButton("复制链接","umeng_sharebutton_copy","ico_share_copy","ico_share_copy");
        shareAction.setShareboardclickCallback(new ShareBoardlistener() {
            @Override
            public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
                if(share_media==null){
                    //根据key来区分自定义按钮的类型，并进行对应的操作
                    if(snsPlatform.mKeyword.equals("umeng_sharebutton_copy")){
                        ClipboardManager clipboard = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clipData = ClipData.newPlainText(null, weburl);
                        clipboard.setPrimaryClip(clipData);
                        Toast.makeText(activity,"链接复制成功",Toast.LENGTH_SHORT).show();
                    }
                }
                else{//社交平台的分享行为
                    UMShare.share(activity,text,img,weburl,title,getMediaInt(share_media),shareListener);
                }
            }
        });//面板点击监听器
        shareAction.open();
    }

    private static UMShareListener getUMShareListener(Activity activity){
        return new UMShareListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {

            }

            @Override
            public void onResult(SHARE_MEDIA share_media) {
                Toast.makeText(activity,"分享成功",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                Toast.makeText(activity,"分享失败",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media) {
            }
        };
    }

    public static Bitmap getBitmap(Activity activity,String base64Data) {
        byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);
        Bitmap origin =  BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

        Bitmap newBitmap = Bitmap.createBitmap(origin.getWidth(),origin.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawColor(activity.getResources().getColor(android.R.color.white));
        Paint paint=new Paint();
        canvas.drawBitmap(origin, 0, 0, paint); //将原图使用给定的画笔画到画布上
        return newBitmap;
    }



    private static UMImage getImage(Activity activity,String url){
        if (TextUtils.isEmpty(url)){
            return null;
        }else if(url.startsWith("http")){
            return new UMImage(activity,url);
        }else if(url.startsWith("/")){
            return new UMImage(activity,url);
        }else if(url.startsWith("res")){
            return new UMImage(activity, ResContainer.getResourceId(activity,"drawable",url.replace("res/","")));
        }else if(url.startsWith("data:image/")){
            String base64 = url.split("data:image/png;base64,")[1];
            return new UMImage(activity,getBitmap(activity,base64));
            //return new UMImage(activity,Base64.decode(url.split("data:image/png;base64,")[1], Base64.DEFAULT));
        }else {
            return new UMImage(activity,url);
        }
    }

    private static SHARE_MEDIA getShareMedia(int num){
        switch (num){
            case 0:
                return SHARE_MEDIA.QQ;
            case 1:
                return SHARE_MEDIA.SINA;
            case 2:
                return SHARE_MEDIA.WEIXIN;
            case 3:
                return SHARE_MEDIA.WEIXIN_CIRCLE;
            case 4:
                return SHARE_MEDIA.QZONE;
            case 5:
                return SHARE_MEDIA.EMAIL;
            case 6:
                return SHARE_MEDIA.SMS;
            case 7:
                return SHARE_MEDIA.FACEBOOK;
            case 8:
                return SHARE_MEDIA.TWITTER;
            case 9:
                return SHARE_MEDIA.WEIXIN_FAVORITE;
            case 10:
                return SHARE_MEDIA.GOOGLEPLUS;
            case 11:
                return SHARE_MEDIA.RENREN;
            case 12:
                return SHARE_MEDIA.TENCENT;
            case 13:
                return SHARE_MEDIA.DOUBAN;
            case 14:
                return SHARE_MEDIA.FACEBOOK_MESSAGER;
            case 15:
                return SHARE_MEDIA.YIXIN;
            case 16:
                return SHARE_MEDIA.YIXIN_CIRCLE;
            case 17:
                return SHARE_MEDIA.INSTAGRAM;
            case 18:
                return SHARE_MEDIA.PINTEREST;
            case 19:
                return SHARE_MEDIA.EVERNOTE;
            case 20:
                return SHARE_MEDIA.POCKET;
            case 21:
                return SHARE_MEDIA.LINKEDIN;
            case 22:
                return SHARE_MEDIA.FOURSQUARE;
            case 23:
                return SHARE_MEDIA.YNOTE;
            case 24:
                return SHARE_MEDIA.WHATSAPP;
            case 25:
                return SHARE_MEDIA.LINE;
            case 26:
                return SHARE_MEDIA.FLICKR;
            case 27:
                return SHARE_MEDIA.TUMBLR;
            case 28:
                return SHARE_MEDIA.ALIPAY;
            case 29:
                return SHARE_MEDIA.KAKAO;
            case 30:
                return SHARE_MEDIA.DROPBOX;
            case 31:
                return SHARE_MEDIA.VKONTAKTE;
            case 32:
                return SHARE_MEDIA.DINGTALK;
            case 33:
                return SHARE_MEDIA.MORE;
            default:
                return SHARE_MEDIA.QQ;
        }
    }

    private static int getMediaInt(SHARE_MEDIA share_media){
        switch (share_media){
            case QQ:
                return 0;
            case SINA:
                return 1;
            case WEIXIN:
                return 2;
            case WEIXIN_CIRCLE:
                return 3;
            case QZONE:
                return 4;
//            case 5:
//                return SHARE_MEDIA.EMAIL;
            case SMS:
                return 6;
//            case 7:
//                return SHARE_MEDIA.FACEBOOK;
//            case 8:
//                return SHARE_MEDIA.TWITTER;
//            case 9:
//                return SHARE_MEDIA.WEIXIN_FAVORITE;
//            case 10:
//                return SHARE_MEDIA.GOOGLEPLUS;
//            case 11:
//                return SHARE_MEDIA.RENREN;
//            case 12:
//                return SHARE_MEDIA.TENCENT;
//            case 13:
//                return SHARE_MEDIA.DOUBAN;
//            case 14:
//                return SHARE_MEDIA.FACEBOOK_MESSAGER;
//            case 15:
//                return SHARE_MEDIA.YIXIN;
//            case 16:
//                return SHARE_MEDIA.YIXIN_CIRCLE;
//            case 17:
//                return SHARE_MEDIA.INSTAGRAM;
//            case 18:
//                return SHARE_MEDIA.PINTEREST;
//            case 19:
//                return SHARE_MEDIA.EVERNOTE;
//            case 20:
//                return SHARE_MEDIA.POCKET;
//            case 21:
//                return SHARE_MEDIA.LINKEDIN;
//            case 22:
//                return SHARE_MEDIA.FOURSQUARE;
//            case 23:
//                return SHARE_MEDIA.YNOTE;
//            case 24:
//                return SHARE_MEDIA.WHATSAPP;
//            case 25:
//                return SHARE_MEDIA.LINE;
//            case 26:
//                return SHARE_MEDIA.FLICKR;
//            case 27:
//                return SHARE_MEDIA.TUMBLR;
//            case 28:
//                return SHARE_MEDIA.ALIPAY;
//            case 29:
//                return SHARE_MEDIA.KAKAO;
//            case 30:
//                return SHARE_MEDIA.DROPBOX;
//            case 31:
//                return SHARE_MEDIA.VKONTAKTE;
            case DINGTALK:
                return 32;
//            case 33:
//                return SHARE_MEDIA.MORE;
            default:
                return 0;// SHARE_MEDIA.QQ;
        }
    }

    private static SHARE_MEDIA[] getShareMedias(ArrayList num){

        SHARE_MEDIA[] medias = new SHARE_MEDIA[num.size()];
        for (int i = 0 ; i <num.size();i++){
            Double dValue = (Double) num.get(i);
            medias[i] = UMShare.getShareMedia(dValue.intValue());
        }
        return medias;
    }

    public static void auth(Activity activity, int sharemedia, OnWxAuthListener listener){
        UMShareConfig config = new UMShareConfig();
        config.isNeedAuthOnGetUserInfo(true);
        UMShareAPI.get(activity).setShareConfig(config);
        UMShareAPI.get(activity).getPlatformInfo(activity, getShareMedia(sharemedia), new UMAuthListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {

            }

            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                Log.i("WXAuth","onComplete");
                if(listener != null){
                    listener.onItemClick(1,map);
                }

            }

            @Override
            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                Log.i("WXAuth","onError:"+throwable.getMessage());
                Toast.makeText(activity,throwable.getMessage(),Toast.LENGTH_SHORT).show();
                if(listener != null){
                    listener.onItemClick(2,null);
                }
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media, int i) {
                Log.i("WXAuth","onCancel");
                if(listener != null){
                    listener.onItemClick(3,null);
                }
            }
        });


    }
}
