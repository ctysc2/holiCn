package com.tmslibrary.mvp.presenter.impl;

import com.tmslibrary.entity.LoginEntity;
import com.tmslibrary.entity.VerifyCodeEntity;
import com.tmslibrary.mvp.interactor.LoginInteractor;
import com.tmslibrary.mvp.interactor.impl.LoginInteractorImpl;
import com.tmslibrary.mvp.presenter.base.BasePresenterImpl;
import com.tmslibrary.mvp.view.LoginView;

import javax.inject.Inject;

import okhttp3.RequestBody;

/**
 * Created by cty on 2016/12/12.
 */

public class LoginPresenterImpl extends BasePresenterImpl<LoginView, LoginEntity> {

    private LoginInteractor mLoginInteractorImpl;
    private final String API_TYPE = "login";

    @Inject
    public LoginPresenterImpl(LoginInteractorImpl LoginInteractor){
        mLoginInteractorImpl = LoginInteractor;
        reqType = API_TYPE;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }


    public void beforeRequest(){
        super.beforeRequest(LoginEntity.class);
    }

    public void login(VerifyCodeEntity.DataEntity body){
        mSubscription = mLoginInteractorImpl.login(this,body);
    }

    @Override
    public void success(LoginEntity data) {
        super.success(data);
        mView.loginCompleted(data);
    }
}
