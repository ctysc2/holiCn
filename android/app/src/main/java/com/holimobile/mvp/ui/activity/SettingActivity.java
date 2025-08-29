package com.holimobile.mvp.ui.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.holimobile.R;
import com.holimobile.mvp.ui.activity.base.BaseActivity;
import com.holimobile.mvp.ui.activity.splash.SplashActivity;
import com.holimobile.mvp.ui.activity.webview.WebViewActivity;
import com.holimobile.mvp.ui.utils.PushUtils;
import com.tmslibrary.entity.VerifyCodeEntity;
import com.tmslibrary.entity.base.BaseHoLiEntity;
import com.tmslibrary.mvp.interactor.impl.LogoutInteractorImpl;
import com.tmslibrary.mvp.interactor.impl.VerifyCodeInteractorImpl;
import com.tmslibrary.mvp.presenter.impl.LogoutPresenterImpl;
import com.tmslibrary.mvp.presenter.impl.VerifyCodePresenterImpl;
import com.tmslibrary.mvp.view.LogoutView;
import com.tmslibrary.mvp.view.VerifyCodeView;
import com.tmslibrary.utils.AppUtils;
import com.tmslibrary.utils.DataCleanManager;
import com.tmslibrary.utils.NetWorkConfigUtil;
import com.tmslibrary.utils.ToastUtils;
import com.tmslibrary.utils.VersionUtils;

import java.util.Date;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

import static android.app.Notification.EXTRA_CHANNEL_ID;
import static android.provider.Settings.EXTRA_APP_PACKAGE;

public class SettingActivity extends BaseActivity {

    @BindView(R.id.midText)
    TextView midText;

    @BindView(R.id.tv_cache)
    TextView mCache;

    @BindView(R.id.tv_version)
    TextView mVersion;

    @OnClick({R.id.back,R.id.ll_clearCache,R.id.ll_msg_setting,R.id.tv_logout,R.id.ll_bei_an,R.id.ll_privacy,R.id.ll_service})
    public void onClick(View view){
        Intent intent;
        switch (view.getId()){
            case R.id.ll_bei_an:
                handleUri("https://beian.miit.gov.cn/#/Integrated/index");
                break;
            case R.id.back:
                finish();
                break;
            case R.id.ll_msg_setting:
                try {
                    // 根据isOpened结果，判断是否需要提醒用户跳转AppInfo页面，去打开App通知权限
                    intent = new Intent();
                    intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
                    //这种方案适用于 API 26, 即8.0(含8.0)以上可以用
                    intent.putExtra(EXTRA_APP_PACKAGE, getPackageName());
                    intent.putExtra(EXTRA_CHANNEL_ID, getApplicationInfo().uid);

                    //这种方案适用于 API21——25，即 5.0——7.1 之间的版本可以使用
                    intent.putExtra("app_package", getPackageName());
                    intent.putExtra("app_uid", getApplicationInfo().uid);

                    // 小米6 -MIUI9.6-8.0.0系统，是个特例，通知设置界面只能控制"允许使用通知圆点"——然而这个玩意并没有卵用，我想对雷布斯说：I'm not ok!!!
                    //  if ("MI 6".equals(Build.MODEL)) {
                    //      intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    //      Uri uri = Uri.fromParts("package", getPackageName(), null);
                    //      intent.setData(uri);
                    //      // intent.setAction("com.android.settings/.SubSettings");
                    //  }
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                    // 出现异常则跳转到应用设置界面：锤子坚果3——OC105 API25
                    intent = new Intent();
                    //下面这种方案是直接跳转到当前应用的设置界面。
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);
                }
                break;
            case R.id.ll_privacy:
                intent = new Intent(SettingActivity.this, WebViewActivity.class);
                intent.putExtra("url",NetWorkConfigUtil.getH5UrlByCode(1005));
                intent.putExtra("title","隐私政策");
                startActivity(intent);
                break;
            case R.id.ll_service:
                intent = new Intent(SettingActivity.this, WebViewActivity.class);
                intent.putExtra("url",NetWorkConfigUtil.getH5UrlByCode(1001));
                intent.putExtra("title","服务协议");
                startActivity(intent);
                break;
            case R.id.ll_clearCache:
                //清除缓存
                new AlertDialog.Builder(this)
                        .setTitle(this.getString(R.string.hint))
                        .setMessage(R.string.confirm_clear_cache)
                        .setCancelable(true)
                        .setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DataCleanManager.clearAllCache(SettingActivity.this);
                                mCache.setText("0K");
                                ToastUtils.showShortSafe(R.string.clear_success);
                            }
                        })
                        .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();
                break;
            case R.id.tv_logout:
                new AlertDialog.Builder(this)
                        .setTitle(this.getString(R.string.hint))
                        .setMessage(R.string.confirm_log_out)
                        .setCancelable(true)
                        .setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                logout();

                            }
                        })
                        .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();
                break;
            default:
                break;
        }
    }

    private void handleUri(String uri) {
        Uri parsedUri = Uri.parse(uri);
        Intent intent = new Intent(Intent.ACTION_VIEW, parsedUri);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            ToastUtils.showShortSafe("没有系统浏览器");
        }
    }

    private void logout(){
        showLoadingDialog();
        LogoutPresenterImpl mLogoutPresenterImpl = new LogoutPresenterImpl(new LogoutInteractorImpl());
        mLogoutPresenterImpl.attachView(new LogoutView() {
            @Override
            public void logoutCompleted(BaseHoLiEntity data) {
                if(data != null){
                    PushUtils.unbindAccount();
                    AppUtils.setToken("");
                    AppUtils.setUserId("");
                    AppUtils.setPushLoginId("");
                    Intent intent = new Intent(SettingActivity.this,LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }

            @Override
            public void showProgress(String reqType) {

            }

            @Override
            public void hideProgress(String reqType) {
                dismissLoadingDialog();
            }

            @Override
            public void showErrorMsg(String reqType, String msg) {
                ToastUtils.showShortSafe(msg);
            }
        });
        mLogoutPresenterImpl.logout();


    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    public void initInjector() {
        mActivityComponent.inject(this);
    }

    @Override
    public void initViews() {
        midText.setText(R.string.setting);
        try {
            mCache.setText(DataCleanManager.getTotalCacheSize(this));
        }catch (Exception e){
            mCache.setText("0K");
        }
        mVersion.setText("版本号 V"+ VersionUtils.getVersionName(this));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
}