package com.tmslibrary.mvp.presenter.impl;


import com.tmslibrary.entity.CheckAccountConfirmEntity;
import com.tmslibrary.mvp.interactor.CheckAccountConfirmInteractor;
import com.tmslibrary.mvp.interactor.impl.CheckAccountConfirmInteractorImpl;
import com.tmslibrary.mvp.presenter.base.BasePresenterImpl;
import com.tmslibrary.mvp.view.CheckAccountConfirmView;

import javax.inject.Inject;

/**
 * Created by cty on 2016/12/12.
 */

public class CheckAccountConfirmPresenterImpl extends BasePresenterImpl<CheckAccountConfirmView, CheckAccountConfirmEntity> {

    private CheckAccountConfirmInteractor mCheckAccountConfirmInteractorImpl;
    private final String API_TYPE = "checkAccountConfirm";

    @Inject
    public CheckAccountConfirmPresenterImpl(CheckAccountConfirmInteractorImpl CheckAccountConfirmInteractor){
        mCheckAccountConfirmInteractorImpl = CheckAccountConfirmInteractor;
        reqType = API_TYPE;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }


    public void beforeRequest(){
        super.beforeRequest(CheckAccountConfirmEntity.class);
    }

    public void checkAccountConfirm(String confirmType){
        mSubscription = mCheckAccountConfirmInteractorImpl.checkAccountConfirm(this,confirmType);
    }

    @Override
    public void success(CheckAccountConfirmEntity data) {
        super.success(data);
        mView.checkAccountConfirmCompleted(data);
    }
}
