package com.tmslibrary.mvp.presenter.impl;

import com.tmslibrary.entity.VerifyCodeEntity;
import com.tmslibrary.entity.base.BaseHoLiEntity;
import com.tmslibrary.mvp.interactor.LogoutInteractor;
import com.tmslibrary.mvp.interactor.impl.LogoutInteractorImpl;
import com.tmslibrary.mvp.presenter.base.BasePresenterImpl;
import com.tmslibrary.mvp.view.LogoutView;
import com.tmslibrary.mvp.view.LogoutView;

import javax.inject.Inject;

/**
 * Created by cty on 2016/12/12.
 */

public class LogoutPresenterImpl extends BasePresenterImpl<LogoutView, BaseHoLiEntity> {

    private LogoutInteractor mLogoutInteractorImpl;
    private final String API_TYPE = "logout";

    @Inject
    public LogoutPresenterImpl(LogoutInteractorImpl LogoutInteractor){
        mLogoutInteractorImpl = LogoutInteractor;
        reqType = API_TYPE;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }


    public void beforeRequest(){
        super.beforeRequest(BaseHoLiEntity.class);
    }

    public void logout(){
        mSubscription = mLogoutInteractorImpl.logout(this);
    }

    @Override
    public void success(BaseHoLiEntity data) {
        super.success(data);
        mView.logoutCompleted(data);
    }
}
