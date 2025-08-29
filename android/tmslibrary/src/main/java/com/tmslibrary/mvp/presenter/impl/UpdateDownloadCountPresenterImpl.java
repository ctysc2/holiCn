package com.tmslibrary.mvp.presenter.impl;


import com.tmslibrary.entity.base.BaseErrorEntity;
import com.tmslibrary.mvp.interactor.UpdateDownloadCountInteractor;
import com.tmslibrary.mvp.interactor.impl.UpdateDownloadCountInteractorImpl;
import com.tmslibrary.mvp.presenter.base.BasePresenterImpl;
import com.tmslibrary.mvp.view.UpdateDownloadCountView;

import javax.inject.Inject;

/**
 * Created by cty on 2016/12/12.
 */

public class UpdateDownloadCountPresenterImpl extends BasePresenterImpl<UpdateDownloadCountView, BaseErrorEntity> {

    private UpdateDownloadCountInteractor mUpdateDownloadCountInteractorImpl;
    private final String API_TYPE = "updateDownLoadCount";

    @Inject
    public UpdateDownloadCountPresenterImpl(UpdateDownloadCountInteractorImpl UpdateDownloadCountInteractor){
        mUpdateDownloadCountInteractorImpl = UpdateDownloadCountInteractor;
        reqType = API_TYPE;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }


    public void beforeRequest(){
        super.beforeRequest(BaseErrorEntity.class);
    }

    public void updateDownloadCount(String id){
        mSubscription = mUpdateDownloadCountInteractorImpl.updateDownloadCount(this,id);
    }

    @Override
    public void success(BaseErrorEntity data) {
        super.success(data);
        mView.updateDownloadCountCompleted(data);
    }
}
