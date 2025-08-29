package com.holimobile.mvp.ui.activity.splash;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alibaba.sdk.android.push.CloudPushService;
import com.alibaba.sdk.android.push.CommonCallback;
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory;
import com.example.zxingqr.activity.ZXingLibrary;
import com.holimobile.BuildConfig;
import com.holimobile.mvp.ui.activity.SettingActivity;
import com.holimobile.mvp.ui.activity.VerifyCodeActivity;
import com.holimobile.mvp.ui.activity.webview.WebViewActivity;
import com.holimobile.mvp.ui.utils.PushUtils;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tmslibrary.app.TmsLibraryApp;
import com.tmslibrary.common.Const;
import com.tmslibrary.entity.VersionCheckEntity;
import com.tmslibrary.listener.OnItemClickListener;
import com.tmslibrary.listener.TimerCallback;
import com.tmslibrary.listener.VersionUpdateCallback;
import com.tmslibrary.manager.AppPrivacyManager;
import com.tmslibrary.manager.AppVersionManager;
import com.tmslibrary.mvp.interactor.impl.VersionCheckInteractorImpl;
import com.tmslibrary.mvp.presenter.impl.VersionCheckPresenterImpl;
import com.tmslibrary.mvp.view.VersionCheckView;
import com.tmslibrary.utils.AppUtils;
import com.tmslibrary.utils.DialogUtils;
import com.tmslibrary.utils.DimenUtil;
import com.tmslibrary.utils.NetWorkConfigUtil;
import com.tmslibrary.utils.PermissionUtils;
import com.tmslibrary.utils.PreferenceUtils;
import com.tmslibrary.utils.SystemTool;
import com.tmslibrary.utils.TimerUtils;
import com.holimobile.R;
import com.holimobile.mvp.ui.activity.LoginActivity;
import com.holimobile.mvp.ui.activity.base.BaseTransparentActivity;
import com.tmslibrary.utils.ToastUtils;
import com.tmslibrary.utils.install.DownloadAndInstall;

import java.util.HashMap;

import butterknife.BindView;

public class SplashActivity extends BaseTransparentActivity {

    @BindView(R.id.iv_logo)
    ImageView mLogo;

    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    public void initInjector() {
        mActivityComponent.inject(this);
    }

    @Override
    public void initViews() {
        if (!isTaskRoot()) {
            finish();
            return;
        }
        int screenWidth = DimenUtil.getScreenWidth(this) - (int)DimenUtil.dp2px(60,this);
        int height = screenWidth/2;
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)mLogo.getLayoutParams();
        params.height = height;
        params.width = screenWidth;
        mLogo.setLayoutParams(params);
        NetWorkConfigUtil.init(this);
        privacyVersionCheck();

    }

    private void initCloudChannel(Context applicationContext) {
        PushServiceFactory.init(applicationContext);
        CloudPushService pushService = PushServiceFactory.getCloudPushService();
        //pushService.setLogLevel(CloudPushService.LOG_DEBUG);   //仅适用于Debug包，正式包不需要此行
        pushService.register(applicationContext, new CommonCallback() {
            @Override
            public void onSuccess(String response) {
                Log.i("CloudChannel", "init cloudchannel success");
            }
            @Override
            public void onFailed(String errorCode, String errorMessage) {
                Log.d("CloudChannel", "init cloudchannel failed -- errorcode:" + errorCode + " -- errorMessage:" + errorMessage);
            }
        });

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void startLogin(){
        ZXingLibrary.initDisplayOpinion(this);
        TimerUtils.delay(500, new TimerCallback() {
            @Override
            public void onTimerEnd() {
                if(AppUtils.isLogin()){
                    PushUtils.bindAccount(AppUtils.getPushLoginId(),SplashActivity.this);
                    Intent intent = new Intent(SplashActivity.this, WebViewActivity.class);
                    String url = NetWorkConfigUtil.getH5UrlByCode(1000);
                    HashMap<String,Object> params = new HashMap<>();
                    params.put("userId",AppUtils.getUserId());
                    url = AppUtils.appendParams(url,params);
                    intent.putExtra("url",url);
                    startActivity(intent);
                }else{
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                }
                finish();
            }
        });
    }

    private void showPrivacyDialog(String versionCode){
        AppPrivacyManager.showPrivacyAlert(SplashActivity.this,new AppPrivacyManager.OnAgreePrivacyListener() {
            @Override
            public void onAgree(int status) {

                if(status == 1){
                    //初始化sdk
                    PreferenceUtils.setPrefString(SplashActivity.this,"privacy_version",versionCode);
                    initCloudChannel(getApplicationContext());
//                    PreferenceUtils.setPrefString(TmsLibraryApp.getInstances(), Const.KEY_LOCAL_PRIVACY_VERSION,lastPrivacyVersion);
//                    PreferenceUtils.setPrefString(TmsLibraryApp.getInstances(),Const.KEY_LOCAL_PRIVACY_ID,lastPrivacyId);
                    startLogin();
                }else if(status == 2){
                    Intent intent = new Intent(SplashActivity.this, WebViewActivity.class);
                    String url = NetWorkConfigUtil.getH5UrlByCode(1001);
                    intent.putExtra("url",url);
                    intent.putExtra("title","服务协议");
                    startActivity(intent);
                }else if(status == 3){
                    Intent intent = new Intent(SplashActivity.this, WebViewActivity.class);
                    String url = NetWorkConfigUtil.getH5UrlByCode(1005);
                    intent.putExtra("url",url);
                    intent.putExtra("title","隐私政策");
                    startActivity(intent);
                }else{
                    finish();
                }

            }
        });
    }

    private void privacyVersionCheck(){
        String currentPrivacyVersion = PreferenceUtils.getPrefString(SplashActivity.this,"privacy_version","0.0.1");
//        if(TextUtils.isEmpty(currentPrivacyVersion)){
//            showPrivacyDialog();
//            return;
//        }
        VersionCheckPresenterImpl mVersionCheckPresenterImpl = new VersionCheckPresenterImpl(new VersionCheckInteractorImpl());
        mVersionCheckPresenterImpl.attachView(new VersionCheckView() {
            @Override
            public void getVersionCheckCompleted(VersionCheckEntity data) {

                if(data != null){
                    if(data.getResult() != null && "1".equals(data.getResult().getUpdateFlag())){
                        showPrivacyDialog(data.getResult().getVersionCode());
                    }else{
                        startLogin();
                    }
                }
            }

            @Override
            public void showProgress(String reqType) {

            }

            @Override
            public void hideProgress(String reqType) {

            }

            @Override
            public void showErrorMsg(String reqType, String msg) {
                Log.i("AAAAA","msg:"+msg);
                ToastUtils.showShortSafe(msg);
            }
        });
        mVersionCheckPresenterImpl.versionCheck("basf_recipe_privacy_polocy",currentPrivacyVersion);
    }
}