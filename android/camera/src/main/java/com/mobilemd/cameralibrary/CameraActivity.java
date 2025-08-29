package com.mobilemd.cameralibrary;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.mobilemd.cameralibrary.listener.ClickListener;
import com.mobilemd.cameralibrary.listener.JCameraListener;
import com.mobilemd.cameralibrary.util.FileUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: 成晓知
 * @Date:  2021-05-21 15：00
 * @Description:
 **/
public class CameraActivity extends AppCompatActivity {
    private final int GET_PERMISSION_REQUEST = 100; //权限申请自定义码
    private JCameraView jCameraView;
    private boolean granted = false;

    private List<String> filePathList;

    private List<String> recordList;

    private int picMax = 9;
    private int videoMax = 9;

    public static void startActivity(Activity activity, Bundle bundle, int requestCode){
        if(activity != null){
            Intent it = new Intent(activity, CameraActivity.class);
            it.putExtra("bundle", bundle);
            activity.startActivityForResult(it, requestCode);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        filePathList = new ArrayList<>();
        recordList = new ArrayList<>();

        Bundle bundle = getIntent().getBundleExtra("bundle");
        int duration = bundle.getInt("duration");
        picMax = bundle.getInt("picMax", 9);
        videoMax = bundle.getInt("videoMax", 9);
        ImageView tvBack = findViewById(R.id.tv_back);
        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        jCameraView = findViewById(R.id.jcameraview);

        //设置最大时长
        jCameraView.setDuration(duration);

        //设置视频保存路径
        jCameraView.setSaveVideoPath(Environment.getExternalStorageDirectory().getPath() + File.separator + "eVisit");

        jCameraView.setLeftClickListener(new ClickListener() {
            @Override
            public void onClick() {
                finish();
            }
        });

        //JCameraView监听
        jCameraView.setJCameraListener(new JCameraListener() {
            @Override
            public void captureSuccess(Bitmap bitmap) {
                String path = FileUtil.saveBitmap("eVisit", bitmap);
                filePathList.add(path);
                //获取图片bitmap
                Log.i("JCameraView", "bitmap = " + bitmap.getWidth());

                jCameraView.changeCameraNum(filePathList.size() + recordList.size());
            }

            @Override
            public void recordSuccess(String url, Bitmap firstFrame) {
//                Map<String, String> video = new HashMap<>();
//                video.put("path", url);
//                video.put("first", FileUtil.saveBitmap("eVisit", firstFrame));
                recordList.add(url);

                jCameraView.changeCameraNum(filePathList.size() + recordList.size());

                Log.i("JCameraView", "bitmap = " + firstFrame);
            }

            @Override
            public void onConfirm() {
                try {
                    JSONArray ja = new JSONArray();
                    if(filePathList != null){
                        for(String str : filePathList){
                            ja.put(str);
                        }
                    }
                    JSONArray ja2 = new JSONArray();
                    if(recordList != null){
                        for(String str : recordList){
                            ja2.put(str);
                        }
                    }
                    Intent it = new Intent();
                    it.putExtra("picJson", ja.toString());
                    it.putExtra("videoJson", ja2.toString());
                    setResult(RESULT_OK, it);
                    finish();
                }catch (Exception e){

                }
            }

            @Override
            public boolean imageIsMax() {
                boolean can = picMax > filePathList.size();
                if(!can){
                    Toast.makeText(CameraActivity.this, "最多可拍照" + picMax, Toast.LENGTH_SHORT ).show();
                }
                return can;
            }

            @Override
            public boolean videoIsMax() {
                boolean can = videoMax > recordList.size();
                if(!can){
                    Toast.makeText(CameraActivity.this, "最多可录像" + videoMax, Toast.LENGTH_SHORT ).show();
                }
                return can;
            }
        });
        //6.0动态权限获取
        getPermissions();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //全屏显示
        View decorView = getWindow().getDecorView();
        if (Build.VERSION.SDK_INT >= 19) {
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        } else {
            int option = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(option);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (granted) {
            jCameraView.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        jCameraView.onPause();
    }

    /**
     * 获取权限
     */
    private void getPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
//                    ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                //具有权限
                granted = true;
            } else {
                //不具有获取权限，需要进行权限申请
                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.CAMERA}, GET_PERMISSION_REQUEST);
                granted = false;
            }
        }
    }

    @TargetApi(23)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == GET_PERMISSION_REQUEST) {
            int size = 0;
            if (grantResults.length >= 1) {
                int writeResult = grantResults[0];
                //读写内存权限
                boolean writeGranted = writeResult == PackageManager.PERMISSION_GRANTED;//读写内存权限
                if (!writeGranted) {
                    size++;
                }
                //录音权限
                int recordPermissionResult = grantResults[1];
                boolean recordPermissionGranted = recordPermissionResult == PackageManager.PERMISSION_GRANTED;
                if (!recordPermissionGranted) {
                    size++;
                }
                //相机权限
                int cameraPermissionResult = grantResults[2];
                boolean cameraPermissionGranted = cameraPermissionResult == PackageManager.PERMISSION_GRANTED;
                if (!cameraPermissionGranted) {
                    size++;
                }
                if (size == 0) {
                    granted = true;
                    jCameraView.onResume();
                }else{
                    Toast.makeText(this, "请到设置-权限管理中开启", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }
    }
}
