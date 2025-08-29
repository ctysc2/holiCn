package com.holimobile.mvp.ui.fragment.base;

import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.tmslibrary.mvp.presenter.base.BasePresenter;
import com.tmslibrary.utils.DialogUtils;
import com.tmslibrary.utils.DimenUtil;
import com.tmslibrary.utils.RxBus;
import com.holimobile.R;
import com.holimobile.app.Application;
import com.holimobile.di.component.DaggerFragmentComponent;
import com.holimobile.di.component.FragmentComponent;
import com.holimobile.di.module.FragmentModule;
import com.holimobile.event.NetWorkRecoverEvent;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Subscription;
import rx.functions.Action1;


public abstract class BaseFragment<T extends BasePresenter> extends Fragment {
    public FragmentComponent getFragmentComponent() {
        return mFragmentComponent;
    }

    protected FragmentComponent mFragmentComponent;
    protected T mPresenter;
    protected boolean isLoaded;
    protected boolean isVisible;
    protected boolean isFirstLoaded = true;
    protected int page;
    protected boolean hasMore;
    protected View mFragmentView;
    protected DialogUtils mLoadDialog;
    protected Subscription mRefreshSubscription;
    protected boolean hasEditContent = false;

    public abstract void initInjector();

    public abstract void initViews(View view);

    public abstract int getLayoutId();

    protected boolean isShowFirstLoading = true;

    private Subscription mNetWorkRecoverSubscription;

    protected String mFileTemp;
    protected String mPreviewFileTemp;
    public String temp = "";

    public static final String defaultTemp = Environment.getExternalStorageDirectory().
            getAbsolutePath() + "/" + "TrialOps";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFragmentComponent = DaggerFragmentComponent.builder()
                .applicationComponent(((Application) getActivity().getApplication()).getApplicationComponent())
                .fragmentModule(new FragmentModule(this))
                .build();
        initInjector();
        initTemp();
        mNetWorkRecoverSubscription = RxBus.getInstance().toObservable(NetWorkRecoverEvent.class)
                .subscribe(new Action1<NetWorkRecoverEvent>() {
                    @Override
                    public void call(NetWorkRecoverEvent event) {
                        refreshPage();
                    }
                });
    }

    protected void refreshPage(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mFragmentView == null) {
            mFragmentView = inflater.inflate(getLayoutId(), container, false);
            ButterKnife.bind(this, mFragmentView);
            isLoaded = true;
            initViews(mFragmentView);
            adaptToolBarHeight();
        }
        return mFragmentView;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mPresenter != null) {
            mPresenter.onDestroy();
        }

        RxBus.cancelSubscription(mNetWorkRecoverSubscription);

    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isVisible = isVisibleToUser;
    }

    protected void adaptToolBarHeight() {

        Toolbar toolbar = (Toolbar) mFragmentView.findViewById(R.id.toolbar);
        if (toolbar != null) {
            int statusBarHeight = DimenUtil.getStatusBarHeight(getActivity());
            toolbar.setPadding(0, statusBarHeight, 0, 0);
            ViewGroup.LayoutParams params = toolbar.getLayoutParams();
            params.height = (int) DimenUtil.dp2px(44,getActivity())+statusBarHeight;
            toolbar.setLayoutParams(params);
        }

        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT){
            if (toolbar != null) {
                toolbar.setPadding(0, 0, 0, 0);
                ViewGroup.LayoutParams params = toolbar.getLayoutParams();
                params.height = (int)DimenUtil.dp2px(44,getActivity());
                toolbar.setLayoutParams(params);
            }
        }

    }
    protected void initPage(){
        page = 1;
    }
    protected void nextPage(){
        page++;
    }

    protected File createTempFile() {
        Date date = new Date();
//
        if(!new File(temp).exists()){
            new File(temp).mkdirs();
        }

        try {
            boolean result = new File(temp+"/.nomedia").createNewFile();
            Log.i(".nomedia","fragment:"+result);
        }catch (Exception e){
            Log.i(".nomedia","fragment e:"+e);
        }


        mFileTemp = temp + "/" + date.getTime() + ".png";
        File file = new File(mFileTemp);

        return file;

    }


    protected void showLoadingDialog(){
        if(mLoadDialog == null){
            mLoadDialog = DialogUtils.create(getActivity());
        }
        mLoadDialog.showLoadingDialog();
    }
    protected void dismissLoadingDialog(){
        if(mLoadDialog != null){
            if(getActivity()!=null && !getActivity().isDestroyed()){
                mLoadDialog.dismiss();
            }
            mLoadDialog = null;
        }

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
            realName = new Date().getTime()+"."+arr[arr.length-1];
        }else{
            realName = fileName;
        }

        mPreviewFileTemp = temp + "/" + realName;

        if(new File(mPreviewFileTemp).exists()) {
            new File(mPreviewFileTemp).delete();
        }
    }

    public void setShowFirstLoading(boolean isShowFirstLoading){
        this.isShowFirstLoading = isShowFirstLoading;
    }

    private void initTemp(){
        File cache = getActivity().getExternalCacheDir();
        if(cache != null){
            //temp = cache.getAbsolutePath();
            temp = defaultTemp;
        }else{
            temp = defaultTemp;
        }
    }

}