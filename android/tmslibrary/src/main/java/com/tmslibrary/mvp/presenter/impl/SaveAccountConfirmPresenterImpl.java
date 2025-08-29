package com.tmslibrary.mvp.presenter.impl;


import com.tmslibrary.entity.base.BaseErrorEntity;
import com.tmslibrary.mvp.interactor.SaveAccountConfirmInteractor;
import com.tmslibrary.mvp.interactor.impl.SaveAccountConfirmInteractorImpl;
import com.tmslibrary.mvp.presenter.base.BasePresenterImpl;
import com.tmslibrary.mvp.view.SaveAccountConfirmView;

import javax.inject.Inject;

/**
 * Created by cty on 2016/12/12.
 */

public class SaveAccountConfirmPresenterImpl extends BasePresenterImpl<SaveAccountConfirmView, BaseErrorEntity> {

    private SaveAccountConfirmInteractor mSaveAccountConfirmInteractorImpl;
    private final String API_TYPE = "SaveAccountConfirm";

    @Inject
    public SaveAccountConfirmPresenterImpl(SaveAccountConfirmInteractorImpl SaveAccountConfirmInteractor){
        mSaveAccountConfirmInteractorImpl = SaveAccountConfirmInteractor;
        reqType = API_TYPE;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }


    public void beforeRequest(){
        super.beforeRequest(BaseErrorEntity.class);
    }

    public void saveAccountConfirm(String docConfigDetailId,String confirmType){
        mSubscription = mSaveAccountConfirmInteractorImpl.saveAccountConfirm(this,docConfigDetailId,confirmType);
    }

    @Override
    public void success(BaseErrorEntity data) {
        super.success(data);
        mView.saveAccountConfirmCompleted(data);
    }
}
