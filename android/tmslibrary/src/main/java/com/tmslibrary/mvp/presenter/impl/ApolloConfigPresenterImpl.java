package com.tmslibrary.mvp.presenter.impl;

import com.tmslibrary.entity.ApolloConfigEntity;
import com.tmslibrary.mvp.interactor.ApolloConfigInteractor;
import com.tmslibrary.mvp.interactor.impl.ApolloConfigInteractorImpl;
import com.tmslibrary.mvp.presenter.base.BasePresenterImpl;
import com.tmslibrary.mvp.view.ApolloConfigView;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import okhttp3.RequestBody;

/**
 * Created by cty on 2016/12/12.
 */

public class ApolloConfigPresenterImpl extends BasePresenterImpl<ApolloConfigView, ApolloConfigEntity> {

    private ApolloConfigInteractor mApolloConfigInteractorImpl;
    private final String API_TYPE = "getApolloConfig";

    @Inject
    public ApolloConfigPresenterImpl(ApolloConfigInteractorImpl ApolloConfigInteractor){
        mApolloConfigInteractorImpl = ApolloConfigInteractor;
        reqType = API_TYPE;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }


    public void beforeRequest(){
        super.beforeRequest(ApolloConfigEntity.class);
    }

    public void getApolloConfig(String appId){
        mSubscription = mApolloConfigInteractorImpl.getApolloConfig(this,appId);
    }

    @Override
    public void success(ApolloConfigEntity data) {
        super.success(data);
        mView.getApolloConfigCompleted(data);
    }
}
