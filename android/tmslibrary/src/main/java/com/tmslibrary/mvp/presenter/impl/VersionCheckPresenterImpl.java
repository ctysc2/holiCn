package com.tmslibrary.mvp.presenter.impl;


import android.content.Context;

import com.google.gson.Gson;
import com.tmslibrary.BuildConfig;
import com.tmslibrary.app.TmsLibraryApp;
import com.tmslibrary.entity.VersionCheckEntity;
import com.tmslibrary.mvp.interactor.VersionCheckInteractor;
import com.tmslibrary.mvp.interactor.impl.VersionCheckInteractorImpl;
import com.tmslibrary.mvp.presenter.base.BasePresenterImpl;
import com.tmslibrary.mvp.view.VersionCheckView;
import com.tmslibrary.utils.AppUtils;
import com.tmslibrary.utils.VersionUtils;

import java.util.HashMap;

import javax.inject.Inject;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by cty on 2016/12/12.
 */

public class VersionCheckPresenterImpl extends BasePresenterImpl<VersionCheckView, VersionCheckEntity> {

    private VersionCheckInteractor mVersionCheckInteractorImpl;

    @Inject
    public VersionCheckPresenterImpl(VersionCheckInteractorImpl VersionCheckInteractor){
        mVersionCheckInteractorImpl = VersionCheckInteractor;
        reqType = "productCheckVersion";
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }


    public void beforeRequest(){
        super.beforeRequest(VersionCheckEntity.class);
    }

    public void versionCheck(String appName,String versionCode){
        HashMap<String,Object> input = new HashMap<>();
        input.put("appName",appName);
        input.put("versionCode",versionCode);
        mSubscription = mVersionCheckInteractorImpl.getProductVersionCheck(this, RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(input)));

    }

    @Override
    public void success(VersionCheckEntity data) {
        super.success(data);
        mView.getVersionCheckCompleted(data);
    }
}
