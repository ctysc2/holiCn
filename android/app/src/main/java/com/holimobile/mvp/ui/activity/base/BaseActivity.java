package com.holimobile.mvp.ui.activity.base;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;
import com.holimobile.mvp.ui.activity.LoginActivity;
import com.holimobile.mvp.ui.activity.SettingActivity;
import com.holimobile.mvp.ui.utils.PushUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tmslibrary.event.ReLoginEvent;
import com.tmslibrary.mvp.presenter.base.BasePresenter;
import com.tmslibrary.utils.AppUtils;
import com.tmslibrary.utils.DialogUtils;
import com.tmslibrary.utils.DimenUtil;
import com.tmslibrary.utils.RxBus;
import com.holimobile.R;
import com.holimobile.annotation.BindValues;
import com.holimobile.app.Application;
import com.holimobile.di.component.ActivityComponent;
import com.holimobile.di.component.DaggerActivityComponent;
import com.holimobile.di.module.ActivityModule;
import com.holimobile.event.NetWorkRecoverEvent;

import com.tmslibrary.utils.ToastUtils;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Subscription;
import rx.functions.Action1;

import static android.os.Environment.DIRECTORY_PICTURES;



/**
 * Created by cty on 16/10/18.
 */
public abstract class BaseActivity<T extends BasePresenter> extends AppCompatActivity {
    private RxPermissions rxBasePermissions = null;
    protected T mPresenter;
    protected DialogUtils mLoadDialog;
    protected boolean mIsStatusTranslucent = true;
    protected int page;
    protected boolean hasMore;
    private Activity mActivity;
    private Subscription mCheckNormsSubscription;



    protected ActivityComponent mActivityComponent;

    public abstract int getLayoutId();

    public abstract void initInjector();

    public abstract void initViews();

    protected boolean hasPer;

    protected Subscription mRefreshSubscription;

    private Subscription mNetWorkRecoverSubscription;

    protected boolean isHasMore = false;

    private boolean isShowing = false;

    protected boolean hasEditContent = false;

    private Subscription mReloginSubscription;

    public ActivityComponent getActivityComponent() {
        return mActivityComponent;
    }

    //选择图片过程中创建临时文件
    protected String mFileTemp;

    public static final String defaultTemp = Environment.getExternalStorageDirectory().
            getAbsolutePath() + "/" + "Download";


    public static final String offLineH5Folder = Environment.getExternalStorageDirectory().
            getAbsolutePath() + "/TrialOSApp/OfflineH5";

    public String temp = "";

    protected String mPreviewFileTemp;
    protected File createTempFile() {

        Date date = new Date();

        if(!new File(temp).exists()){
            new File(temp).mkdirs();
        }
        //创建.nomedia文件
//        try {
//            boolean result = new File(temp+"/.nomedia").createNewFile();
//            Log.i(".nomedia","activity:"+result);
//        }catch (Exception e){
//            Log.i(".nomedia","activity e:"+e);
//        }
        mFileTemp = temp + "/" + date.getTime() + ".png";
        return new File(mFileTemp);

    }

    public String getFileTemp(){
        return mFileTemp;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initAnnotation();
        initActivityComponent();
        int layout = getLayoutId();
        rxBasePermissions = new RxPermissions(this);
        setContentView(layout);
        initInjector();
        ButterKnife.bind(this);
        initTemp();
        if(mIsStatusTranslucent == true){
            setStatusBarTranslucent();
        }else{
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setStatusBarColor(Color.WHITE);
            }
        }

        initViews();
        mActivity = this;
        mNetWorkRecoverSubscription = RxBus.getInstance().toObservable(NetWorkRecoverEvent.class)
                .subscribe(new Action1<NetWorkRecoverEvent>() {
                    @Override
                    public void call(NetWorkRecoverEvent event) {
                        refreshPage();
                    }
                });




    }

    @Override
    protected void onResume() {
        super.onResume();
        mReloginSubscription = RxBus.getInstance().toObservable(ReLoginEvent.class)
                .subscribe(new Action1<ReLoginEvent>() {
                    @Override
                    public void call(ReLoginEvent event) {
                        PushUtils.unbindAccount();
                        //弹框是就清空数据
                        AppUtils.setToken("");
                        AppUtils.setUserId("");
                        AppUtils.setPushLoginId("");
                        Intent intent = new Intent(mActivity, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });
    }

    @Override
    protected void onPause() {
        super.onPause();
        RxBus.cancelSubscription(mReloginSubscription);
    }

    protected void refreshPage(){

    }

    private void initAnnotation() {
        if (getClass().isAnnotationPresent(BindValues.class)) {
            BindValues annotation = getClass().getAnnotation(BindValues.class);
            mIsStatusTranslucent = annotation.mIsStatusTranslucent();

        }
    }
    private void initActivityComponent() {
        mActivityComponent = DaggerActivityComponent.builder()
                .applicationComponent(((Application) getApplication()).getApplicationComponent())
                .activityModule(new ActivityModule(this))
                .build();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus.cancelSubscription(mNetWorkRecoverSubscription);
        RxBus.cancelSubscription(mReloginSubscription);
        RxBus.cancelSubscription(mCheckNormsSubscription);
    }
    public void setStatusBarTranslucent(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (toolbar != null) {
            int statusBarHeight = DimenUtil.getStatusBarHeight(Application.getInstances());
            toolbar.setPadding(0, statusBarHeight, 0, 0);
            ViewGroup.LayoutParams params = toolbar.getLayoutParams();
            params.height = (int) DimenUtil.dp2px(44,Application.getInstances())+statusBarHeight;
            toolbar.setLayoutParams(params);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//透明状态栏
            // 状态栏字体设置为深色，SYSTEM_UI_FLAG_LIGHT_STATUS_BAR 为SDK23增加
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().setStatusBarColor(Color.TRANSPARENT);// SDK21

        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            // 设置状态栏透明
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        }else{
            if (toolbar != null) {
                toolbar.setPadding(0, 0, 0, 0);
                ViewGroup.LayoutParams params = toolbar.getLayoutParams();
                params.height = (int) DimenUtil.dp2px(44,Application.getInstances());
                toolbar.setLayoutParams(params);
            }
        }

        //
    }
    public void setStatusBarDarkMode(boolean darkmode, Activity activity) {
        Class<? extends Window> clazz = activity.getWindow().getClass();
        try {
            int darkModeFlag = 0;
            Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            darkModeFlag = field.getInt(layoutParams);
            Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
            extraFlagField.invoke(activity.getWindow(), darkmode ? darkModeFlag : 0, darkModeFlag);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    protected void deleteTempFile(){
        if(rxBasePermissions != null){
            rxBasePermissions
                    .request(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .subscribe(granted -> {
                        if (granted) {
                            File tempFolder = new File(defaultTemp);
                            if(tempFolder.exists()){
                                for(int i = 0;i<tempFolder.listFiles().length;i++){
                                    File file = tempFolder.listFiles()[i];
                                    if(file != null && file.isFile() && file.exists()){
                                        getContentResolver().delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, MediaStore.Images.Media.DATA + "=?", new String[]{file.getAbsolutePath()});
                                        file.delete();
                                    }
                                }
                            }
                        } else {

                        }
                    });
        }
    }


    protected void setFullScreen(){
        View decorView =getWindow().getDecorView();

        //decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);

        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }

    protected void initPage(){
        page = 0;
    }
    protected void nextPage(){
        page++;
    }



    protected String getFileName(String path){

        String name = "";
        if(TextUtils.isEmpty(path)){
            return name;
        }
        String[] temp1 = path.split("\\.");
        if(temp1.length > 1){
            String pre = temp1[temp1.length-2];
            String[] paths = pre.split("/");
            if(paths.length > 0){
                name = paths[paths.length-1];
            }
        }
        return name;
    }

    protected RequestBody createRequestBody(HashMap<String,Object> input){
        String jsonString = new Gson().toJson(input);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonString);
        return  body;
    }

    protected RequestBody createRequestBody(ArrayList<String> input){
        String jsonString = new Gson().toJson(input);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonString);
        return  body;
    }

    protected boolean isShowing(){
        if(mLoadDialog != null){
            return mLoadDialog.isShowing();
        }else{
            return false;
        }

    }

    protected void showLoadingDialog(){
        if(mLoadDialog == null){
            mLoadDialog = DialogUtils.create(this);
        }
        mLoadDialog.showLoadingDialog();
    }

    protected void dismissLoadingDialog(){
        if(mLoadDialog != null){
            if(!isDestroyed()){
                mLoadDialog.dismiss();
            }
            mLoadDialog = null;
        }

    }

    private boolean isActivityTop(Context context){
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        String name = manager.getRunningTasks(1).get(0).topActivity.getClassName();
        return name.equals(mActivity.getClass().getName());
    }

    protected void createPreviewFile(String origin,String fileName){
        if(TextUtils.isEmpty(origin)){
            mPreviewFileTemp = "";
            return;
        }
        File file = new File(temp);

        if(!file.exists()){
            file.mkdirs();
        }

        if(TextUtils.isEmpty(fileName)){
            mPreviewFileTemp = "";
            return;
        }


        mPreviewFileTemp = temp + "/" + fileName;

        if(new File(mPreviewFileTemp).exists()) {
            new File(mPreviewFileTemp).delete();
        }

    }

    protected boolean hideKeyboard(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            return  im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
        return false;
    }


    protected boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
        return false;
    }

    public void openKeyBoard(EditText editText){
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        Timer timer=new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                InputMethodManager imm=
                        (InputMethodManager)
                                Application.getInstances()
                                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(editText,0);
                editText.setSelection(editText.getText().length());
            }
        },200);
    }


    private void initTemp(){
        File cache = getExternalFilesDir(DIRECTORY_PICTURES);
        if(cache != null){
            temp = defaultTemp;
        }else{
            temp = defaultTemp;
        }
    }


}
