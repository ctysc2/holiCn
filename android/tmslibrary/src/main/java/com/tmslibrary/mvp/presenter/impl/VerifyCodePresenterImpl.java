package com.tmslibrary.mvp.presenter.impl;

import com.tmslibrary.entity.VerifyCodeEntity;
import com.tmslibrary.mvp.interactor.VerifyCodeInteractor;
import com.tmslibrary.mvp.interactor.impl.VerifyCodeInteractorImpl;
import com.tmslibrary.mvp.presenter.base.BasePresenterImpl;
import com.tmslibrary.mvp.view.VerifyCodeView;

import javax.inject.Inject;

import okhttp3.RequestBody;

/**
 * Created by cty on 2016/12/12.
 */

public class VerifyCodePresenterImpl extends BasePresenterImpl<VerifyCodeView, VerifyCodeEntity> {

    private VerifyCodeInteractor mVerifyCodeInteractorImpl;
    private final String API_TYPE = "verifyCode";

    @Inject
    public VerifyCodePresenterImpl(VerifyCodeInteractorImpl VerifyCodeInteractor){
        mVerifyCodeInteractorImpl = VerifyCodeInteractor;
        reqType = API_TYPE;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }


    public void beforeRequest(){
        super.beforeRequest(VerifyCodeEntity.class);
    }

    public void verifyCode(RequestBody body){
        mSubscription = mVerifyCodeInteractorImpl.verifyCode(this,body);
    }

    @Override
    public void success(VerifyCodeEntity data) {
        super.success(data);
        mView.sendVerifyCodeCompleted(data);
    }
}
