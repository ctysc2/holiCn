package com.mobilemd.cameralibrary;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.URLUtil;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.lzy.okhttputils.OkHttpUtils;
import com.mobilemd.cameralibrary.util.GlideEngine;
import com.mobilemd.cameralibrary.util.JsonUtil;
import com.mobilemd.cameralibrary.util.UploadUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;

/**
 * @Author: 成晓知
 * @DateTime: 5/29/21 11:04
 * @Description:
 **/
public class TMH5Manager implements UploadUtils.UploadListener{

    private TMH5Listener tmh5Listener;

    private static final int VIDEO_REQUEST_CODE = 1008;
    private static final int SOUND_REQUEST_CODE = 2000;

    private List<String> picList;
    private List<String> videoList;
    private List<String> voiceList;

    private long videoMaxDuration = 15 * 1000;
    private long voiceMaxDuration = 60 * 1000;

    public TMH5Manager(){}

    public TMH5Manager(String baseUrl, String fileUrl, TMH5Listener listener){
        UploadUtils.uploadBaseUrl = baseUrl;
        UploadUtils.fileUploadUrl = fileUrl;
//        UploadUtils.token = token;
        tmh5Listener = listener;
        picList = new ArrayList<>();
        videoList = new ArrayList<>();
        voiceList = new ArrayList<>();
    }

    public TMH5Manager(String baseUrl, String fileUrl, TMH5Listener listener, int videoMaxDuration, int voiceMaxDuration){
        this(baseUrl, fileUrl, listener);
        this.videoMaxDuration = videoMaxDuration * 1000;
        this.voiceMaxDuration = voiceMaxDuration  * 1000;
    }

    public static void init(Application application){
        OkHttpUtils.init(application);
    }


    public static WebResourceResponse parseToResponse(WebResourceRequest request){
        FileInputStream input;
        String url = request.getUrl().toString();
        String key = "evisitlocalfile://";
        /*如果请求包含约定的字段 说明是要拿本地的图片*/
        if (url.contains(key) || url.contains("/storage/emulated")) {
            String imgPath = url.replace(key, "").replace(UploadUtils.uploadBaseUrl, "");
            Log.i("webview", "本地图片路径：" + imgPath.trim());
            try {
                /*重新构造WebResourceResponse  将数据已流的方式传入*/
                input = new FileInputStream(new File(imgPath.trim()));
                String mineType = "image/jpeg";
                if(url.endsWith("mp4")){
                    mineType = "video/mpeg";
                }
                /*返回WebResourceResponse*/
                return new WebResourceResponse(mineType, "utf-8", input);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    public static void cacheToken(int newProgress, String url){
        if(newProgress == 100){
            CookieManager cookieManager = CookieManager.getInstance();
            String cookie = cookieManager.getCookie(url);
            Log.i("TAG", "onPageFinished cookie :" + cookie);
            //将cookie存到数据库
            if(!TextUtils.isEmpty(cookie)){
                String[] cookies = cookie.split(";");
                for(String str : cookies){
                    if(str.contains("token")){
                        String[] tokens = str.split("=");
                        if(tokens != null && tokens.length > 1){
                            UploadUtils.token = tokens[1];
                        }
                        break;
                    }
                }
            }
        }
    }


    private void openFileChoseType(Activity activity, String message){

        final String[] types = new String[]{"选择相册", "选择视频"};//创建item
        AlertDialog alertDialog3 = new AlertDialog.Builder(activity)
                .setTitle("选择图片方式")
//                .setIcon(R.mipmap.ic_launcher)
                .setItems(types, new DialogInterface.OnClickListener() {//添加列表
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(types[i].equals("选择相册")){
                            openImage(activity, message);
                        }
                        else if(types[i].equals("选择视频")){
                            openVideo(activity, message);
                        }
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .create();
        alertDialog3.setCancelable(false);
        alertDialog3.setCanceledOnTouchOutside(false);
        alertDialog3.show();
    }

    private void openImage(Activity activity, String message){
        int picMax = JsonUtil.formatIntJSON(message,"picMax");
        PictureSelector.create(activity)
                .openGallery(PictureMimeType.ofImage())
                .maxSelectNum(picMax)
                .isCamera(false)
//                .isMaxSelectEnabledMask(true)
                .loadImageEngine(GlideEngine.createGlideEngine()) // Please refer to the Demo GlideEngine.java
                .forResult(PictureConfig.CHOOSE_REQUEST);
    }

    private void openVideo(Activity activity, String message){
        int videoMax = JsonUtil.formatIntJSON(message,"videoMax");
        PictureSelector.create(activity)
                .openGallery(PictureMimeType.ofVideo())
                .maxSelectNum(videoMax)
                .maxVideoSelectNum(videoMax)
                .isCamera(false)
                .isWithVideoImage(true)
//                .isMaxSelectEnabledMask(true)
                .loadImageEngine(GlideEngine.createGlideEngine()) // Please refer to the Demo GlideEngine.java
                .forResult(PictureConfig.CHOOSE_REQUEST);
    }


    /**
     * 获取H5 发送过来的消息
     * @param activity
     * @param message
     */
    public void receiveH5Manager(Activity activity, String message){
        String action = JsonUtil.formatJSON(message, "action");
        if (action.equals("close")) {
            if(activity != null){
                activity.finish();
            }
        }
        //打开相册选取视频图片
        else if (action.equals("selectpicture")) {
            openFileChoseType(activity, message);
        }
        //提交请求
        else if (action.equals("submit")) {
            if(voiceList.size() == 0 && videoList.size() ==0 && picList.size() == 0){
                tmh5Listener.postMessage(JsonUtil.toJSON("finished", "1", "info", "finished"));
            }else {
                UploadUtils.upload(activity, JsonUtil.getFileList(message), this);
            }
        }
        //去拍照
        else if (action.equals("camera")) {
            Bundle bundle = new Bundle();
            bundle.putLong("duration", videoMaxDuration);
            bundle.putInt("picMax", JsonUtil.formatIntJSON(message,"picMax"));
            bundle.putInt("videoMax", JsonUtil.formatIntJSON(message,"videoMax"));
            CameraActivity.startActivity(activity, bundle, VIDEO_REQUEST_CODE);
        }
        //删除img
        else if (action.equals("deleteImg")) {
            String path = JsonUtil.formatJSON(message, "path");
            String type = JsonUtil.formatJSON(message, "type");
            if("pic".equals(type) && path != null && picList.size() > 0){
                int index = -1;
                for(int i = 0;i<picList.size();i++){
                    if(picList.get(i).equals(path)){
                        index = i;
                        break;
                    }
                }
                if(index > -1) {
                    picList.remove(index);
                }
            }
            if("video".equals(type) && path !=null && videoList.size() > 0){
                int index = -1;
                for(int i = 0;i<videoList.size();i++){
                    if(Objects.equals(videoList.get(i), path)){
                        index = i;
                        break;
                    }
                }
                if(index > -1) {
                    videoList.remove(index);
                }
            }
            if("voice".equals(type) && path != null && voiceList.size() > 0){
                int index = -1;
                for(int i = 0;i<voiceList.size();i++){
                    if(voiceList.get(i).equals(path)){
                        index = i;
                        break;
                    }
                }
                if(index > -1) {
                    voiceList.remove(index);
                }
            }
        }
        //预览视频
        else if (action.equals("showvideo")) {
            String path = JsonUtil.formatJSON(message, "path");
            VideoActivity.startVideoActivity(activity, path);
        }
        //打开语音
        else if (action.equals("showvoice")) {
            String path = JsonUtil.formatJSON(message, "path");
            int max = JsonUtil.formatIntJSON(message, "voiceMax");
            SoundActivity.startSoundActivity(activity, path, voiceMaxDuration, max, SOUND_REQUEST_CODE);
        }
        //提交任务
        else if (action.equals("finishtask") || action.equals("clearall")) {
            picList.clear();
            videoList.clear();
            voiceList.clear();
        }
        //取消任务
        else if (action.equals("canceltask")) {
            UploadUtils.cancelHttp();
        }
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == VIDEO_REQUEST_CODE && resultCode == RESULT_OK){
            if(data != null){
                String pic = data.getStringExtra("picJson");
                String video  = data.getStringExtra("videoJson");
                JsonUtil.saveArray(pic, picList);
                JsonUtil.saveArray(video, videoList);
                tmh5Listener.postMessage(JsonUtil.parseJson(picList, videoList, voiceList));
            }
        }else if (requestCode == PictureConfig.CHOOSE_REQUEST && resultCode == RESULT_OK) {
            // onResult Callback
            List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
            JsonUtil.savePicOrVideo(selectList, picList, videoList);
            tmh5Listener.postMessage(JsonUtil.parseJson(picList, videoList, voiceList));
        }else if (requestCode == SOUND_REQUEST_CODE && resultCode == RESULT_OK) {
            // onResult Callback
            String json = data.getStringExtra("json");
            if(json != null) {
                JsonUtil.saveArray(json, voiceList);
                tmh5Listener.postMessage(JsonUtil.parseJson(picList, videoList, voiceList));
            }
        }
    }

    @Override
    public void onBegin(int current, int size) {
        tmh5Listener.postMessage(JsonUtil.toJSON("current", current, "size", size));
    }

    @Override
    public void onProgress(float progress, long time) {
        tmh5Listener.postMessage(JsonUtil.toJSON("progress", progress, "time", time));
    }

    @Override
    public void onFinish(String fileName, String fileUrl, String fileType) {
        tmh5Listener.postMessage(JsonUtil.toJSON("fileName", fileName, "fileUrl", fileUrl, "fileType", fileType));
    }

    @Override
    public void onComplete() {
        tmh5Listener.postMessage(JsonUtil.toJSON("finished", "1", "actionInfo", "finished"));
    }




    public interface TMH5Listener {
        void postMessage(String message);
    }

}
