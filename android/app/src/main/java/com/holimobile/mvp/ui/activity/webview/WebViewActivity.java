package com.holimobile.mvp.ui.activity.webview;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zxingqr.activity.CodeUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.holimobile.customerView.ShareWithDownloadPopWindow;
import com.holimobile.mvp.entity.LocationEntity;
import com.holimobile.mvp.entity.actionEntity.AppInfoEntity;
import com.holimobile.mvp.ui.activity.LoginActivity;
import com.holimobile.mvp.ui.activity.SettingActivity;
import com.holimobile.mvp.ui.activity.VerifyCodeActivity;
import com.holimobile.mvp.ui.activity.scan.ScanActivity;
import com.holimobile.mvp.ui.utils.PushUtils;
import com.holimobile.utils.ShareUtils;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.tools.ImageModelUtils;
import com.mobilemd.cameralibrary.util.GlideEngine;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tmslibrary.common.Const;
import com.tmslibrary.entity.LoginEntity;
import com.tmslibrary.entity.OnePlatformShareActionEntity;
import com.tmslibrary.entity.ShareActionEntity;
import com.tmslibrary.entity.VerifyCodeEntity;
import com.tmslibrary.entity.base.BaseHoLiEntity;
import com.tmslibrary.listener.DownloadSuccessCallback;
import com.tmslibrary.listener.TimerCallback;
import com.tmslibrary.mvp.interactor.impl.SaveScanInteractorImpl;
import com.tmslibrary.mvp.presenter.impl.SaveScanPresenterImpl;
import com.tmslibrary.mvp.view.SaveScanView;
import com.tmslibrary.utils.AppUtils;
import com.tmslibrary.utils.CacheUtils;
import com.tmslibrary.utils.DialogUtils;
import com.tmslibrary.utils.NetWorkConfigUtil;
import com.tmslibrary.utils.PermissionUtils;
import com.tmslibrary.utils.PhoneUtils;
import com.tmslibrary.utils.PreferenceUtils;
import com.tmslibrary.utils.RxBus;
import com.tmslibrary.utils.TimerUtils;
import com.tmslibrary.utils.ToastUtils;
import com.holimobile.R;
import com.holimobile.app.Application;
import com.holimobile.event.LoginCompletedEvent;
import com.holimobile.event.LoginSuccessEvent;
import com.holimobile.event.WebViewFinishEvent;
import com.holimobile.mvp.entity.actionEntity.LoginSuccessEntity;
import com.holimobile.mvp.entity.base.BaseActionEntity;
import com.holimobile.mvp.ui.activity.base.BaseActivity;
import com.holimobile.mvp.ui.activity.common.ImageGalleryActivity;
import com.tmslibrary.utils.VersionUtils;


import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscription;
import rx.functions.Action1;

import static com.luck.picture.lib.config.PictureConfig.CHOOSE_REQUEST;
import static com.tmslibrary.common.Const.RESULT_PICK_FROM_PHOTO_NORMAL;

public class WebViewActivity extends BaseActivity {

    @BindView(R.id.midText)
    TextView midText;

    @BindView(R.id.webView)
    WebView mWebView;

    @BindView(R.id.pgBar)
    ProgressBar mPgBar;

    @BindView(R.id.toolbar)
    Toolbar mToolBar;

    @BindView(R.id.rl_right)
    RelativeLayout mRight;

    @BindView(R.id.ib_inner_right)
    ImageButton mRightButton;

    @BindView(R.id.fl_video)
    FrameLayout mLayout;

    @BindView(R.id.ll_error_view)
    LinearLayout mErrorContainer;


    private boolean isError = false;

    private Subscription mWebViewFinishSubscription;

    private View mCustomView;    //用于全屏渲染视频的View
    private WebChromeClient.CustomViewCallback mCustomViewCallback;
    private String url = "";
    private RxPermissions rxPermissions = null;
    private ValueCallback<Uri> uploadMessage;
    private ValueCallback<Uri[]> uploadMessageAboveL;

    @OnClick({R.id.back, R.id.ib_inner_right, R.id.tv_reload, R.id.tv_close_page})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.back:
            case R.id.tv_close_page:
                finish();
                break;
            case R.id.ib_inner_right:
                break;
            case R.id.tv_reload:
                mWebView.reload();
                break;
            default:
                break;
        }

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_web_view;
    }

    @Override
    public void initInjector() {
        mActivityComponent.inject(this);
    }

    private long startTime;

    private long endTime;

    private void setStatusBarColor(){
        String color = getIntent().getStringExtra("statusBarColor");
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if(!TextUtils.isEmpty(color)){
                getWindow().setStatusBarColor(Color.parseColor(color));
            }else{
                getWindow().setStatusBarColor(Color.WHITE);
            }
        }

    }
    @Override
    public void initViews() {
        //X5内核如果未被创建无法注入cookie，所以每次加载webview需要重新注入
//        AppUtils.setCookies(this);
        String url = getIntent().getStringExtra("url");
        String title = getIntent().getStringExtra("title");
        rxPermissions = new RxPermissions(this);
        setStatusBarColor();
        initToolBar();
        //url统一加上navigatorEnv=app
        HashMap<String, Object> params = new HashMap<>();
        if (!TextUtils.isEmpty(AppUtils.getToken())) {
            params.put("token", AppUtils.getToken());
        }
        params.put("loginType", 1);
        url = AppUtils.appendParams(url, params);
        Log.i("CmnWebView", "url:" + url + " token:" + AppUtils.getToken());
        this.url = url;
        WebSettings settings = mWebView.getSettings();
        mWebView.loadUrl(url);
        settings.setSupportZoom(false);
        settings.setJavaScriptEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setAppCacheEnabled(true);
        settings.setDomStorageEnabled(true);
        startTime = new Date().getTime();
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                hideProgress();
                isError = true;
                Log.i("WebViewErr", "description:" + description);
                mErrorContainer.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Log.i("WebViewErr", "onPageFinished");
                hideProgress();
                if (!isError) {
                    mErrorContainer.setVisibility(View.GONE);
                }
                isError = false;
                endTime = new Date().getTime();
                Log.i("WebResource", "时间差:" + (endTime - startTime));
            }


            @Nullable
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                Log.i("InterceptWebView", "request:" + request.getUrl());
                return super.shouldInterceptRequest(view, request);
            }
        });
        mPgBar.setProgress(0);
        mWebView.addJavascriptInterface(new JSApi(), "HoLiObject");
        mWebViewFinishSubscription = RxBus.getInstance().toObservable(WebViewFinishEvent.class)
                .subscribe(new Action1<WebViewFinishEvent>() {
                    @Override
                    public void call(WebViewFinishEvent event) {
                        finish();
                    }
                });

        mWebView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                Log.i("进度", "progress:" + newProgress);
                mPgBar.setProgress(newProgress);
            }

            @Override
            public void onShowCustomView(View view, CustomViewCallback callback) {
                super.onShowCustomView(view, callback);
                //如果view 已经存在，则隐藏
                if (mCustomView != null) {
                    callback.onCustomViewHidden();
                    return;
                }

                mCustomView = view;
                mCustomView.setVisibility(View.VISIBLE);
                mCustomViewCallback = callback;
                mLayout.addView(mCustomView);
                mLayout.setVisibility(View.VISIBLE);
                mLayout.bringToFront();

                //设置横屏
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }

            @Override
            public void onHideCustomView() {
                super.onHideCustomView();
                if (mCustomView == null) {
                    return;
                }
                mCustomView.setVisibility(View.GONE);
                mLayout.removeView(mCustomView);
                mCustomView = null;
                mLayout.setVisibility(View.GONE);
                try {
                    mCustomViewCallback.onCustomViewHidden();
                } catch (Exception e) {
                }
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//竖屏
            }

            //For Android  >= 4.1
            public void openFileChooser(ValueCallback<Uri> valueCallback, String acceptType, String capture) {
                uploadMessage = valueCallback;
                openImageChooserActivity();
            }

            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
                Log.i("h5picker", "onShowFileChooser");
                uploadMessageAboveL = filePathCallback;
                openImageChooserActivity();
                return true;
            }


        });

        mRefreshSubscription = RxBus.getInstance().toObservable(LoginCompletedEvent.class)
                .subscribe(new Action1<LoginCompletedEvent>() {
                    @Override
                    public void call(LoginCompletedEvent event) {

                    }
                });
    }

    private void openImageChooserActivity() {

        PermissionUtils.checkRequestPermission(WebViewActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE, rxPermissions, new PermissionUtils.OnPermissionResultListener() {
            @Override
            public void allow() {
                PictureSelector.create(WebViewActivity.this)
                        .openGallery(PictureMimeType.ofImage())
                        .isWeChatStyle(false)
                        .isCamera(true)
                        .selectionMode(PictureConfig.SINGLE)
                        .isSingleDirectReturn(true)
                        .isGif(true)
                        .enableCrop(false)
                        .rotateEnabled(false)
                        .previewImage(false)
                        .maxSelectNum(1)
                        .loadImageEngine(GlideEngine.createGlideEngine()) // 请参考Demo GlideEngine.java
                        .forResult(RESULT_PICK_FROM_PHOTO_NORMAL);
//                Intent intent;
//                if (Build.VERSION.SDK_INT < 19) {
//                    intent = new Intent(Intent.ACTION_GET_CONTENT);
//                } else {
//                    intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                }
//                intent.setType("image/*");
//                startActivityForResult(intent, Const.RESULT_PICK_FROM_PHOTO_NORMAL);
            }

            @Override
            public void cancel() {
                if (uploadMessageAboveL != null) {
                    uploadMessageAboveL.onReceiveValue(null);
                } else if (uploadMessage != null) {
                    uploadMessage.onReceiveValue(null);
                }
            }
        });

    }

    private double lon = 0;
    private double lat = 0;


    private void hideProgress() {
        TimerUtils.delay(200, new TimerCallback() {
            @Override
            public void onTimerEnd() {
                mPgBar.setVisibility(View.GONE);
            }
        });
    }

    private Dialog mZmDialog;

    private int scanType;
    private String codeType;
    private String code;



    public class JSApi {

        @JavascriptInterface
        public void h5SaveImg(String resource) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    PermissionUtils.checkRequestPermission(WebViewActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE, rxPermissions, new PermissionUtils.OnPermissionResultListener() {
                        @Override
                        public void allow() {
                            File file = saveBase64Img(resource);
                            if(file != null){
                                notifyGallery(file);
                                ToastUtils.showShortSafe(getString(R.string.download_successful));
                            }
                        }

                        @Override
                        public void cancel() {

                        }
                    });
                }
            });
        }

        @JavascriptInterface
        public void goBack(){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            });
        }

//        @JavascriptInterface
//        public void getAppInfo() {
//            mWebView.post(new Runnable() {
//                @Override
//                public void run() {
//                    AppInfoEntity e = new AppInfoEntity();
//                    e.setAppSystem("Android");
//                    e.setAppVersion(VersionUtils.getVersionName(WebViewActivity.this));
//                    String json  = new Gson().toJson(e);
//                    Log.i("WWWW","getAppInfo");
//                    mWebView.loadUrl("javascript:getAppInfo('"+json+"')");
//                }
//            });
//        }
        @JavascriptInterface
        public void getAppInfoAsync(){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    AppInfoEntity e = new AppInfoEntity();
                    e.setAppSystem("Android");
                    e.setAppVersion(VersionUtils.getVersionName(WebViewActivity.this));
                    String json  = new Gson().toJson(e);
                    mWebView.loadUrl("javascript:getAppInfo('"+json+"')");
                    Log.i("versionInfo",json);
                }
            });
        }

        @JavascriptInterface
        public void h5RegisterLogin(String loginStr){
            Log.i("JSApi","h5RegisterLogin:"+loginStr);
            LoginEntity.DataEntity data = new Gson().fromJson(loginStr,new TypeToken<LoginEntity.DataEntity>(){}.getType());
            if(data != null){
                AppUtils.setToken(data.getAccessToken());
                AppUtils.setUserId(String.valueOf(data.getUid()));
                AppUtils.setPushLoginId(data.getDeviceLoginId());
                PushUtils.bindAccount(data.getDeviceLoginId(), WebViewActivity.this);

//                Intent intent = new Intent(WebViewActivity.this, WebViewActivity.class);
//                String url = NetWorkConfigUtil.getH5UrlByCode(1000);
//                HashMap<String,Object> params = new HashMap<>();
//                params.put("userId",AppUtils.getUserId());
//                url = AppUtils.appendParams(url,params);
//                intent.putExtra("url",url);
//                startActivity(intent);
//                finish();
            }

        }

        @JavascriptInterface
        public void wxShareUrl(String title,String desc,String url){
            Log.i("wxShareUrl","title:"+title+" desc:"+desc+" url:"+url);
            TimerUtils.delay(100, new TimerCallback() {
                @Override
                public void onTimerEnd() {
                    ShareWithDownloadPopWindow withDownloadPopWindow = new ShareWithDownloadPopWindow(WebViewActivity.this,title,desc,url);
                    withDownloadPopWindow.showAtLocation(getWindow().getDecorView(),
                            Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                }
            });

        }

        @JavascriptInterface
        public void h5QrCode(int scanType,String codeType,String code) {

            WebViewActivity.this.scanType = scanType;
            WebViewActivity.this.codeType = codeType;
            WebViewActivity.this.code = code;

            Log.i("h5QrCode","scanType:"+scanType+" codeType:"+codeType+" code:"+code);

            TimerUtils.delay(200, new TimerCallback() {
                @Override
                public void onTimerEnd() {
                    //首次扫码
                    if(scanType == 1){
                        PermissionUtils.checkRequestPermission(WebViewActivity.this, Manifest.permission.CAMERA, rxPermissions, new PermissionUtils.OnPermissionResultListener() {
                            @Override
                            public void allow() {
                                //首次扫码，进入页面
                                saveScan("","","");
                                Intent intent = new Intent(WebViewActivity.this, ScanActivity.class);
                                intent.putExtra("scanType",scanType);
                                intent.putExtra("hintText","将二维码/条码放入框内，即可自动扫描");
                                startActivityForResult(intent,Const.QR_REQUEST);
                            }
                            @Override
                            public void cancel() {

                            }
                        });
                    }else if(scanType == 2){
                        //防伪扫码
                        PermissionUtils.checkRequestPermission(WebViewActivity.this, Manifest.permission.ACCESS_FINE_LOCATION, rxPermissions, new PermissionUtils.OnPermissionResultListener() {
                            @Override
                            public void allow() {
                                Log.i("LocationApp","111");
                                PermissionUtils.checkRequestPermission(WebViewActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION, rxPermissions, new PermissionUtils.OnPermissionResultListener() {
                                    @Override
                                    public void allow() {
                                        Log.i("LocationApp","222");
                                        if (ActivityCompat.checkSelfPermission(WebViewActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                                            Log.i("LocationApp","333");

                                            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                                            LocationListener locationListener = new LocationListener() {

                                                public void onLocationChanged(Location location) {
                                                    lon = location.getLongitude();
                                                    lat = location.getLatitude();
//                                                LocationEntity e = new LocationEntity();
//                                                e.setLat(latitude);
//                                                e.setLon(longitude);
//                                                String jsonStr = new Gson().toJson(e);
//                                                Log.i("LocationApp","jsonStr:"+jsonStr);
//                                                mWebView.loadUrl("javascript:appSendLocation(('"+jsonStr+"')");
//                                                // 处理经纬度数据
//                                                Log.i("LocationApp", "Latitude: " + latitude + ", Longitude: " + longitude);
//                                                ToastUtils.showShortSafe("Latitude:"+latitude+" Longitude"+longitude);
                                                }

                                                public void onStatusChanged(String provider, int status, Bundle extras) {}

                                                public void onProviderEnabled(String provider) {}

                                                public void onProviderDisabled(String provider) {}
                                            };
                                            try {
                                                locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, locationListener, null);
                                            } catch (SecurityException e) {
                                                e.printStackTrace();
                                                Log.i("LocationApp","err:"+e.getMessage());
                                            }
                                        }

                                        PermissionUtils.checkRequestPermission(WebViewActivity.this, Manifest.permission.CAMERA, rxPermissions, new PermissionUtils.OnPermissionResultListener() {
                                            @Override
                                            public void allow() {
                                                Intent intent = new Intent(WebViewActivity.this, ScanActivity.class);
                                                intent.putExtra("scanType",scanType);
                                                intent.putExtra("hintText","将二维码/条码放入框内，即可自动扫描");
                                                startActivityForResult(intent,Const.QR_REQUEST);

                                                WebViewActivity.this.codeType = codeType;
                                                WebViewActivity.this.code = code;

                                                //防伪扫码，进入页面
                                                saveScan("","1".equals(WebViewActivity.this.codeType)?code:"","2".equals(WebViewActivity.this.codeType)?code:"");
                                            }
                                            @Override
                                            public void cancel() {

                                            }
                                        });
                                    }
                                    @Override
                                    public void cancel() {

                                    }
                                });


                            }
                            @Override
                            public void cancel() {

                            }
                        });


                    }








                }
            });
        }


        @JavascriptInterface
        public void h5PushSetAction()  {
            Log.i("JSApi","h5PushSetAction");
            Intent intent = new Intent(WebViewActivity.this, SettingActivity.class);
            startActivity(intent);
        }

        @JavascriptInterface
        public void h5LogOut()  {
            Log.i("JSApi","h5LogOut");
            PushUtils.unbindAccount();
            AppUtils.setToken("");
            AppUtils.setUserId("");
            AppUtils.setPushLoginId("");
            Intent intent = new Intent(WebViewActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

        @JavascriptInterface
        public void h5ChangePasswordCompleted()  {
            Log.i("JSApi","h5ChangePasswordCompleted");
            Intent intent = new Intent(WebViewActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    private String jsEventId = "";



    private void scan(){
        PermissionUtils.checkRequestPermission(this, Manifest.permission.CAMERA, rxPermissions, new PermissionUtils.OnPermissionResultListener() {
            @Override
            public void allow() {
            }

            @Override
            public void cancel() {

            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mIsStatusTranslucent = false;
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void onPause() {
        super.onPause();
        String status = "background";
        //mWebView.loadUrl("javascript:appStateChange('"+status+"')");
        mWebView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mWebView.onResume();
        String status = "active";
        //mWebView.loadUrl("javascript:appStateChange('"+status+"')");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWebView.clearCache(true);
        mWebView.destroy();
        RxBus.cancelSubscription(mWebViewFinishSubscription);

    }

    private void initToolBar(){
        String title = getIntent().getStringExtra("title");
        String url = getIntent().getStringExtra("url");
        if(!TextUtils.isEmpty(title) || isShowShare(url) || isShowHeader(url)){
            //setStatusBarTranslucent();
            mToolBar.setVisibility(View.VISIBLE);
            if(!TextUtils.isEmpty(title)){
                midText.setText(title);
            }
            if(isShowShare(url)){
                mRight.setVisibility(View.VISIBLE);
                mRightButton.setVisibility(View.VISIBLE);
                mRightButton.setImageResource(R.drawable.icon_share);
            }
        }else{
            mToolBar.setVisibility(View.GONE);
        }
    }

    private boolean isShowShare(String url){
        return "1".equals(AppUtils.getUrlParams(url,"showShare"));
    }


    private boolean isShowHeader(String url){
        return "1".equals(AppUtils.getUrlParams(url,"showHeader"));
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
//            System.out.println("按下了back键 onKeyDown()");
//            if(mWebView.canGoBack()){
//                return super.onKeyDown(keyCode, event);
//            }
//            return false;
//        }else {
//            return super.onKeyDown(keyCode, event);
//        }
//    }

    @Override
    public void onBackPressed() {
        goBack();
    }

    private boolean isExit = false;

    private void goBack(){
//        if(mWebView.canGoBack()){
//            mWebView.goBack();
//            return;
//        }

        if(isExit || !url.contains("/app-h5-cn/page/assignWork")){
            finish();
        }else{
            ToastUtils.showShortSafe("再按一次退出");
            isExit = true;
            TimerUtils.delay(2000, new TimerCallback() {
                @Override
                public void onTimerEnd() {
                    isExit = false;
                }
            });
        }
    }


    private void saveImg(String type,String resource){
        if(type.equals("base64")){
            File file = saveBase64Img(resource);
            if(file != null){
                notifyGallery(file);
                ToastUtils.showShortSafe(getString(R.string.download_successful));
            }
        }else{
            String tempName = new Date().getTime()+".png";
        }
    }

    private File saveBase64Img(String base64str){
        try {
            File file = createTempFile();
            byte[] buffer = Base64.decode(base64str.split("data:image/png;base64,")[1], Base64.DEFAULT);
            FileOutputStream out = new FileOutputStream(mFileTemp);
            out.write(buffer);
            out.close();
            return file;
        }catch (Exception e){
            return null;
        }
    }

    private void notifyGallery(File file){
        MediaStore.Images.Media.insertImage(getContentResolver(), BitmapFactory.decodeFile(file.getAbsolutePath()), file.getName(), null);
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(file);
        intent.setData(uri);
        sendBroadcast(intent);
        //通知完删除
        if(file.exists()){
            file.delete();
        }
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            // 从相册选择图片后返回图片的uri 并且执行
            case RESULT_PICK_FROM_PHOTO_NORMAL:
                Log.i("h5picker","resultCode:"+resultCode);
                if(resultCode == RESULT_CANCELED){
                    if(uploadMessageAboveL != null){
                        uploadMessageAboveL.onReceiveValue(null);
                    }else if(uploadMessage != null){
                        uploadMessage.onReceiveValue(null);
                    }
                }else if (resultCode == RESULT_OK) {
                    ArrayList<LocalMedia> images = data.getParcelableArrayListExtra(PictureConfig.EXTRA_RESULT_SELECTION);
                    if(images != null && images.size()>0){
                          String dataString =  images.get(0).getPath();
                        //String dataString =  ImageModelUtils.getPath(WebViewActivity.this,images.get(0));
                        Log.i("chooseFile","fileName:"+images.get(0).getFileName());
                        Log.i("chooseFile","dataString:"+dataString);
                        if (!TextUtils.isEmpty(dataString)){
                            Uri[] results = new Uri[]{Uri.parse(dataString)};
                            if(uploadMessageAboveL != null){
                                uploadMessageAboveL.onReceiveValue(results);
                            }else if(uploadMessage != null){
                                uploadMessage.onReceiveValue(Uri.parse(dataString));
                            }
                        }else{
                            if(uploadMessageAboveL != null){
                                uploadMessageAboveL.onReceiveValue(null);
                            }else if(uploadMessage != null){
                                uploadMessage.onReceiveValue(null);
                            }
                        }
                    }
                }
                break;
            case Const.QR_REQUEST:
                if(data != null){
                    Bundle bundle = data.getExtras();
                    String code = bundle.getString(CodeUtils.RESULT_STRING);
                    int result = bundle.getInt(CodeUtils.RESULT_TYPE);
                    if(result == CodeUtils.RESULT_SUCCESS){
                        TimerUtils.delay(200, new TimerCallback() {
                            @Override
                            public void onTimerEnd() {
                                afterQrCodeJump(code);
                            }
                        });

                    }
                }
                break;
            case Const.IMAGE_PREVIEW_REQUEST:
                if(!TextUtils.isEmpty(jsEventId)){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.i("exeCallBack","jsEventId:"+jsEventId);
                            String s = "";
                            mWebView.loadUrl("javascript:exeCallBack('"+jsEventId+"','"+ s +"')");
                            jsEventId = "";
                        }
                    });
                }
                break;

        }
    }


    private void getPhotos(boolean isCamera,int maxCount){

    }

    private void afterQrCodeJump(String qrcode){
        Intent intent = new Intent(WebViewActivity.this, WebViewActivity.class);
        Log.i("afterQrCodeJump","qrcode:"+qrcode);
        int codeType = AppUtils.getCodeTypeByQrCode(qrcode);
        Log.i("afterQrCodeJump","scanType:"+scanType);
        Log.i("afterQrCodeJump","codeType:"+codeType);
        if(codeType < 0){
            ToastUtils.showShortSafe("无效二维码，请重新扫码");
            return;
        }
        String filterCode = "";
        String[] arr = qrcode.split("/");
        if(arr != null && arr.length >= 2){
            filterCode = arr[arr.length-2];
        }
        if(scanType == 1){

            String url = NetWorkConfigUtil.getBaseUrlByHostName("h5")+"/QRcode/"+filterCode+"/"+codeType;
            intent.putExtra("url",url);
            startActivity(intent);

            //首次扫码，获取扫码结果
            saveScan(qrcode,codeType == 1?filterCode:"",codeType == 2?filterCode:"");

        }else{

            String primaryCode = "";
            String secretCode = "";

            //防伪扫码，获取扫码结果
            if(WebViewActivity.this.codeType.equals(String.valueOf(codeType))){
                if(codeType == 1){
                    primaryCode = code;
                }else{
                    secretCode = code;
                }
                saveScan(qrcode,primaryCode,secretCode);
            }else{
                if(codeType == 1){
                    saveScan(qrcode,filterCode,code);
                }else{
                    saveScan(qrcode,code,filterCode);
                }
            }

            //如果新扫描结果里包含上次扫码内容，视为重复扫码
            if(qrcode.contains(this.code)){
                new AlertDialog.Builder(this)
                        .setTitle(this.getString(R.string.hint))
                        .setMessage("请勿重复扫描一码通/暗码")
                        .setCancelable(false)
                        .setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(WebViewActivity.this, ScanActivity.class);
                                intent.putExtra("scanType",scanType);
                                intent.putExtra("hintText","将二维码/条码放入框内，即可自动扫描");
                                startActivityForResult(intent,Const.QR_REQUEST);

                            }
                        })
                        .show();
                return;
            }

            String url = NetWorkConfigUtil.getH5UrlByCode(1004);
            HashMap<String,Object> params = new HashMap<>();
            params.put("code",this.code);
            params.put("codeType",this.codeType);
            params.put("scanText",Uri.encode(qrcode));
            params.put("lon",String.valueOf(lon));
            params.put("lat",String.valueOf(lat));
            url = AppUtils.appendParams(url, params);
            intent.putExtra("url",url);
            startActivity(intent);
        }
    }


    private void saveScan(String identifyContent,String primaryCode,String secretCode){
        VerifyCodeEntity.DataEntity data = PreferenceUtils.getPrefObject(WebViewActivity.this,"loginInfo",VerifyCodeEntity.DataEntity.class);
        if(data == null) return;

        SaveScanPresenterImpl mSaveScanPresenterImpl = new SaveScanPresenterImpl(new SaveScanInteractorImpl());
        mSaveScanPresenterImpl.attachView(new SaveScanView() {
            @Override
            public void saveScanCompleted(BaseHoLiEntity data) {

            }

            @Override
            public void showProgress(String reqType) {

            }

            @Override
            public void hideProgress(String reqType) {

            }

            @Override
            public void showErrorMsg(String reqType, String msg) {

            }
        });
        HashMap<String,Object> input = new HashMap<>();
        input.put("userId",data.getUserId());
        input.put("businessCode",data.getBusinessCode());
        input.put("operationType",10401006);
        input.put("optSystem",10411001);
        input.put("scanType",scanType);
        if(!TextUtils.isEmpty(codeType)){
            input.put("codeType",codeType);
        }
        if(!TextUtils.isEmpty(identifyContent)){
            input.put("identifyContent",identifyContent);
        }
        if(!TextUtils.isEmpty(primaryCode)){
            input.put("primaryCode",primaryCode);
        }
        if(!TextUtils.isEmpty(secretCode)){
            input.put("secretCode",secretCode);
        }
        mSaveScanPresenterImpl.saveScan(createRequestBody(input));

    }
}