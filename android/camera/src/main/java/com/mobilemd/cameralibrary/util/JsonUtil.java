package com.mobilemd.cameralibrary.util;

import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.bumptech.glide.Glide;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.tools.StringUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: 成晓知
 * @DateTime: 5/29/21 11:23
 * @Description:
 **/
public class JsonUtil {

    /**
     *
     * @param json
     * @return
     */
    public static String formatJSON(String json, String key){
        String value = "";
        try {
            JSONObject jsonObject = new JSONObject(json);
            value = jsonObject.getString(key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     *
     * @param json
     * @return
     */
    public static int formatIntJSON(String json, String key){
        int value = -1;
        try {
            JSONObject jsonObject = new JSONObject(json);
            value = jsonObject.getInt(key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * 获取本地要上传的文件列表
     * @param json
     * @return
     */
    public static List<Map<String, String>> getFileList(String json) {
        List<Map<String, String>> list = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONObject j1 = jsonObject.getJSONObject("resource");
            if (j1 != null) {
                JSONArray ja1 = j1.getJSONArray("pic");
                if (ja1 != null) {
                    for (int i = 0; i < ja1.length(); i++) {
                        Map<String, String> map = new HashMap<>();
                        map.put("type", "image");
                        map.put("path", ja1.getString(i));
                        list.add(map);
                    }
                }
                JSONArray ja2 = j1.getJSONArray("video");
                if (ja2 != null) {
                    for (int i = 0; i < ja2.length(); i++) {
                        Map<String, String> map = new HashMap<>();
                        map.put("type", "video");
                        map.put("path", ja2.getString(i));
                        list.add(map);
                    }
                }
                JSONArray ja3 = j1.getJSONArray("voice");
                if (ja3 != null) {
                    for (int i = 0; i < ja3.length(); i++) {
                        Map<String, String> map = new HashMap<>();
                        map.put("type", "voice");
                        map.put("path", ja3.getString(i));
                        list.add(map);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static String toJSON(String key1, Object value1, String key2, Object value2){
        JSONObject j1 = new JSONObject();
        try {
            j1.put(key1, value1);
            j1.put(key2, value2);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return j1.toString();
    }

    public static String toJSON(String key1, Object value1, String key2, Object value2, String key3, String value3){
        JSONObject j1 = new JSONObject();
        try {
            j1.put(key1, value1);
            j1.put(key2, value2);
            j1.put(key3, value3);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return j1.toString();
    }


    public static void saveArray(String picJson, List<String> picList){
        try {
            JSONArray j1 = new JSONArray(picJson);
            int length = j1.length();
            for(int i=0; i<length; i++){
                picList.add(j1.getString(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public static String parseJson(List<String> picList, List<String> videoList, List<String> voiceList){
        String json = "";
        try {
            JSONObject jo = new JSONObject();
            JSONObject jo1 = new JSONObject();
            JSONArray ja1 = new JSONArray();
            JSONArray ja2 = new JSONArray();
            JSONArray ja3 = new JSONArray();
            for(String str : picList){
                ja1.put(str);
            }
            for(String str : videoList){
//                JSONObject obj = new JSONObject();
//                obj.put("first", map.get("first"));
//                obj.put("path", map.get("path"));
                ja2.put(str);
            }
            for(String str : voiceList){
                ja3.put(str);
            }
            jo1.put("pic", ja1);
            jo1.put("video", ja2);
            jo1.put("voice", ja3);
            jo.put("resource", jo1);
            json = jo.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    public static void savePicOrVideo(List<LocalMedia> media, List<String> picList, List<String> videoList){
        if(media != null){
            for(LocalMedia localMedia : media){
                String path = localMedia.getRealPath();
                if(TextUtils.isEmpty(path)){
                    path = localMedia.getPath();
                }
                String mimeType = localMedia.getMimeType();
                if(mimeType.contains("image")){
                    picList.add(path);
                }else if(mimeType.contains("video")){
                    Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(path, MediaStore.Video.Thumbnails.MICRO_KIND);
                    if(bitmap != null){
//                        Map<String, String> map = new HashMap<>();
//                        map.put("first",  FileUtil.saveBitmap("eVisit", bitmap));
//                        map.put("path", localMedia.getRealPath());
                        videoList.add(path);
                    }
                }
            }
        }
    }

}
