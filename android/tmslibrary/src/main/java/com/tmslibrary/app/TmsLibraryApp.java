package com.tmslibrary.app;

import android.content.Context;

import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.common.util.ByteConstants;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.tmslibrary.utils.NetWorkConfigUtil;
import com.tmslibrary.utils.OkHttpNetworkFetcher;

public class TmsLibraryApp {

    private static Context context;

    //当前编译环境
    private static String env;

    //当前环境下的报名
    private static String applicationId;


    private static String versionName;

    private static int versionCode;

    //预初始化，app启动时初始化sdk所需的必要参数
    public static void preInit(Context context,String env,String applicationId,String versionName,int versionCode){
        TmsLibraryApp.context = context;
        TmsLibraryApp.env = env;
        TmsLibraryApp.applicationId = applicationId;
        TmsLibraryApp.versionName = versionName;
        TmsLibraryApp.versionCode = versionCode;
        DiskCacheConfig diskCacheConfig = DiskCacheConfig.newBuilder(context)
                .setBaseDirectoryName("rsSystemPicCache").setMaxCacheSize(200 * ByteConstants.MB)
                .setMaxCacheSizeOnLowDiskSpace(100 * ByteConstants.MB)
                .setMaxCacheSizeOnVeryLowDiskSpace(50 * ByteConstants.MB)
                .setMaxCacheSize(80 * ByteConstants.MB).build();

        ImagePipelineConfig config = ImagePipelineConfig.newBuilder(context)
                .setDownsampleEnabled(true)
                .setMainDiskCacheConfig(diskCacheConfig)
                .setNetworkFetcher(new OkHttpNetworkFetcher(context))
                .build();
        Fresco.initialize(context, config);
    }

    //同意隐私政策后进行的初始化
    public static void init(){

    }

    public static Context getInstances(){
        return context;
    }

    public static String getEnv(){
        return env;
    }

    public static String getApplicationId(){
        return applicationId;
    }

    public static String getVersionName(){
        return versionName;
    }

    public static int getVersionCode(){
        return versionCode;
    }

}
