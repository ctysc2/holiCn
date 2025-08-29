package com.mobilemd.cameralibrary.util;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.cache.CacheMode;
import com.lzy.okhttputils.callback.AbsCallback;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

/**
 * @Author: 成晓知
 * @DateTime: 2021/5/19 20:09
 * @Description:
 **/
public class UploadUtils {

    public static String token = "";

    private static int current = 0;

    private static int totalSize ;

    public static String uploadBaseUrl;

    public static String fileUploadUrl;

    public static List<String> tags = new ArrayList<>();

    public static String flag = "";

    public static void upload(Activity context, List<Map<String, String>> pathList, UploadListener uploadListener){
        if(pathList.size() > 0) {
            current = 0;
            totalSize = pathList.size();
            for (int i = 0; i < totalSize; i++) {
                if(flag.equals("cancel_request")){
                    break;
                }
                upload(context, pathList.get(i), uploadListener);
            }
            uploadListener.onBegin(0, totalSize);
        }
    }

    public static void cancelHttp(){
        flag = "cancel_request";
        for(String tag : tags){
            OkHttpUtils.getInstance().cancelTag(tag);
        }
        flag = "";
    }

    public static void upload(Activity context, final Map<String, String> fileMap,final UploadListener uploadListener){
        Log.e("UploadUtils---1", System.currentTimeMillis() + "");
        final String type = fileMap.get("type");
        tags.add(type + current);
        OkHttpUtils.post(fileUploadUrl)
                .tag(type + current)
                .readTimeOut(1000 * 1000 * 1000)
                .writeTimeOut(1000 * 1000 * 1000)
                .cacheMode(CacheMode.NO_CACHE)
                .headers("TM-Header-Token", token)
                .params("appId", "eVisit")
                .params("processor", "resources")
//                .params("videoCodeType", "1")
                .params("file", getFile(context, fileMap))
                .execute(new AbsCallback<Map<String, Object>>() {

                    @Override
                    public void upProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
                        super.upProgress(currentSize, totalSize, progress, networkSpeed);

                        long time =  (totalSize - currentSize) / (1024 * 1024) * 600;

                        uploadListener.onProgress(progress, time);
                    }

                    @Override
                    public Map<String, Object> parseNetworkResponse(Response response) throws Exception {
                        String json = response.body().string();
                        Log.e("UploadUtils--2", json);
                        Map<String, Object> map = new HashMap<>();
                        JSONObject js = new JSONObject(json);
                        js = js.getJSONObject("data");
                        if(js != null){
                            map.put("fileId", js.getString("fileId"));
                            map.put("fileName", js.getString("originFileName"));
                            map.put("fileUrl", js.getString("relativePreviewUrl"));
                            map.put("type", type);
                            return  map;
                        }
                        return null;
                    }

                    @Override
                    public void onSuccess(Map<String, Object> o, Call call, Response response) {
                        Log.e("UploadUtils--2", (System.currentTimeMillis() - current) + "");
                        if(o != null) {
                            uploadListener.onFinish((String) o.get("fileName"), (String) o.get("fileUrl"), fileMap.get("type"));
                        }
                        current += 1;
                        uploadListener.onBegin(current, totalSize);
                        if(current == totalSize){
                            uploadListener.onComplete();
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        current += 1;
                        uploadListener.onBegin(current, totalSize);
                        if(current == totalSize){
                            uploadListener.onComplete();
                        }
                    }
                });
    }

    public static File getFile(Activity activity, Map<String, String> file){
        if("image".equals(file.get("type"))){
            return new File(file.get("path"));
        }else {
            Uri mCurrentUri = null;
            if("video".equals(file.get("type"))){
                mCurrentUri = getVideoContentUri(activity, file.get("path"));
                Cursor cursor = activity.managedQuery(mCurrentUri, new String[] { MediaStore.Video.Media.DATA }, null, null, null );
                int index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
                cursor.moveToFirst();
                try {
                    String path = cursor.getString(index);
                    return new File(path);
                }catch (Exception e){

                }
            }else {
                mCurrentUri = getAudioContentUri(activity, file.get("path"));
                Cursor cursor = activity.managedQuery(mCurrentUri, new String[] { MediaStore.Audio.Media.DATA }, null, null, null );
                int index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
                cursor.moveToFirst();
                try {
                    String path = cursor.getString(index);
                    return new File(path);
                }catch (Exception e){

                }
            }
        }
        return new File(file.get("path"));
    }

    public static Uri getVideoContentUri(Context context, String filePath) {
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                new String[] { MediaStore.Video.Media._ID },
                MediaStore.Video.Media.DATA + "=? ",
                new String[] { filePath }, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (filePath != null) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Video.Media.DATA, filePath);
                return context.getContentResolver().insert(
                        MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }

    public static Uri getAudioContentUri(Context context, String filePath) {
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                new String[] { MediaStore.Audio.Media._ID },
                MediaStore.Audio.Media.DATA + "=? ",
                new String[] { filePath }, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (filePath != null) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Audio.Media.DATA, filePath);
                return context.getContentResolver().insert(
                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }

    public interface UploadListener{

        void onBegin(int current, int size);

        void onProgress(float progress, long time);

        void onFinish(String fileId, String fileUrl,  String fileType);

        void onComplete();
    }
}
