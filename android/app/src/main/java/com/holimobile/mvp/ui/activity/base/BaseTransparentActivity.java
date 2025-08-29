package com.holimobile.mvp.ui.activity.base;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
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

import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;
import com.tmslibrary.mvp.presenter.base.BasePresenter;
import com.tmslibrary.utils.DialogUtils;
import com.tmslibrary.utils.DimenUtil;
import com.holimobile.R;
import com.holimobile.annotation.BindValues;
import com.holimobile.app.Application;
import com.holimobile.di.component.ActivityComponent;
import com.holimobile.di.component.DaggerActivityComponent;
import com.holimobile.di.module.ActivityModule;


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


public abstract class BaseTransparentActivity<T extends BasePresenter> extends Activity {

    protected T mPresenter;
    protected DialogUtils mLoadDialog;
    protected boolean mIsStatusTranslucent = true;
    protected int page;
    protected boolean hasMore;
    private Activity mActivity;

    protected ActivityComponent mActivityComponent;

    public abstract int getLayoutId();

    public abstract void initInjector();

    public abstract void initViews();

    protected boolean hasPer;

    protected Subscription mRefreshSubscription;

    private Subscription mReloginSubscription;

    private Subscription mRefreshTokenSubscription;

    private boolean isShowing = false;

    public ActivityComponent getActivityComponent() {
        return mActivityComponent;
    }

    //选择图片过程中创建临时文件
    protected String mFileTemp;


    public String temp = "";

    protected String mPreviewFileTemp;

    public static final String defaultTemp = Environment.getExternalStorageDirectory().
            getAbsolutePath() + "/" + "TrialOps";

    protected File createTempFile() {

        Date date = new Date();

        if(!new File(temp).exists()){
            new File(temp).mkdirs();
        }

        //创建.nomedia文件
        try {
            boolean result = new File(temp+"/.nomedia").createNewFile();
            Log.i(".nomedia","trans activity:"+result);
        }catch (Exception e){
            Log.i(".nomedia","trans activity e:"+e);
        }

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

        setContentView(layout);
        initInjector();
        initTemp();
        ButterKnife.bind(this);
        if(mIsStatusTranslucent == true){
            setStatusBarTranslucent();
        }else{
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(Color.WHITE);
        }

        initViews();
        mActivity = this;

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
        File tempFolder = new File(temp);
        if(tempFolder.exists()){
            for(int i = 0;i<tempFolder.listFiles().length;i++){
                File file = tempFolder.listFiles()[i];
                if(file != null && file.isFile() && file.exists()){
                    getContentResolver().delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, MediaStore.Images.Media.DATA + "=?", new String[]{file.getAbsolutePath()});
                    file.delete();
                }
            }
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
        page = 1;
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

    protected void showLoadingDialog(int res){
        if(mLoadDialog == null){
            mLoadDialog = DialogUtils.create(this);
        }
        mLoadDialog.showLoadingDialog();
    }

    protected void dismissLoadingDialog(){
        if(mLoadDialog != null){
            mLoadDialog.dismiss();
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

        //处理名字过长
        String[] arr = fileName.split("\\.");
        String realName = "";
        if(arr.length > 1){
            for(int i = 0;i<arr.length-1;i++){
                realName+=arr[i];
            }
            if(realName.length() > 60){
                realName = realName.substring(0,60);
            }
            realName = realName+"."+arr[arr.length-1];
        }else{
            realName = fileName;
        }

        mPreviewFileTemp = temp + "/" + realName;

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
        File cache = getExternalCacheDir();
        if(cache != null){
            //temp = cache.getAbsolutePath();
            temp = defaultTemp;
        }else{
            temp = defaultTemp;
        }
    }
}
