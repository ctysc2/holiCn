package com.tmslibrary.mvp.presenter.impl;


import com.tmslibrary.entity.base.BaseErrorEntity;
import com.tmslibrary.entity.base.BaseHoLiEntity;
import com.tmslibrary.mvp.interactor.SaveScanInteractor;
import com.tmslibrary.mvp.interactor.impl.SaveScanInteractorImpl;
import com.tmslibrary.mvp.presenter.base.BasePresenterImpl;
import com.tmslibrary.mvp.view.SaveScanView;
import com.tmslibrary.mvp.view.SaveScanView;

import javax.inject.Inject;

import okhttp3.RequestBody;

/**
 * Created by cty on 2016/12/12.
 */

public class SaveScanPresenterImpl extends BasePresenterImpl<SaveScanView, BaseHoLiEntity> {

    private SaveScanInteractor mSaveScanInteractorImpl;
    private final String API_TYPE = "saveScan";

    @Inject
    public SaveScanPresenterImpl(SaveScanInteractorImpl SaveScanInteractor){
        mSaveScanInteractorImpl = SaveScanInteractor;
        reqType = API_TYPE;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }


    public void beforeRequest(){
        super.beforeRequest(BaseHoLiEntity.class);
    }

    public void saveScan(RequestBody body){
        mSubscription = mSaveScanInteractorImpl.saveScan(this,body);
    }

    @Override
    public void success(BaseHoLiEntity data) {
        super.success(data);
        mView.saveScanCompleted(data);
    }
}
