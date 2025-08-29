package com.luck.picture.lib.tools;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.luck.picture.lib.entity.LocalMedia;

import java.io.File;

import static android.provider.CalendarContract.CalendarCache.URI;

public class ImageModelUtils {

    public static String getPath(Context context,LocalMedia model){

        if(model == null){
            return "";
        }

        if(!TextUtils.isEmpty(model.getPath())){
            Log.i("setPathsetPath","获取原路径:"+model.getPath());
            if(SdkVersionUtils.checkedAndroid_Q()){
                String result =  PictureFileUtils.getPath(context, Uri.parse(model.getPath()));
                if(TextUtils.isEmpty(result)){
                    return model.getPath();
                }else{
                    return result;
                }
            }else{
                return model.getPath();
            }
        }
        return "";
    }

    public static void setPath(Context context,String path,LocalMedia model){

        if(model == null){
            return;
        }

        if(!TextUtils.isEmpty(model.getPath())){
            if(SdkVersionUtils.checkedAndroid_Q()){
                Log.i("setPathsetPath","设置前:"+path);
                Uri uri = PictureFileUtils.getImageContentUri(context,new File(path));
                if(uri != null){
                    Log.i("setPathsetPath","设置后:"+uri.toString());
                    model.setPath(uri.toString());
                }else {
                    model.setPath(path);
                }
            }else{
                model.setPath(path);
            }
            return;
        }
    }

}
