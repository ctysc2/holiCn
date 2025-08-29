package com.tmslibrary.mvp.interactor.impl;

import com.tmslibrary.R;
import com.tmslibrary.app.TmsLibraryApp;
import com.tmslibrary.entity.VerifyCodeEntity;
import com.tmslibrary.entity.base.TenantError;
import com.tmslibrary.listener.RequestCallBack;
import com.tmslibrary.mvp.interactor.VerifyCodeInteractor;
import com.tmslibrary.repository.network.RetrofitManager;
import com.tmslibrary.utils.TransformUtils;

import java.util.ArrayList;

import javax.inject.Inject;

import okhttp3.RequestBody;
import rx.Observer;
import rx.Subscription;

/**
 * Created by cty on 2016/12/12.
 */

public class VerifyCodeInteractorImpl implements VerifyCodeInteractor<VerifyCodeEntity> {

    private final String API_TYPE = "verifyCode";


    @Inject
    public VerifyCodeInteractorImpl(){

    }

    @Override
    public Subscription verifyCode(RequestCallBack<VerifyCodeEntity> callback, RequestBody body) {
        return RetrofitManager.getInstance(API_TYPE).verifyCode(body)
                .compose(TransformUtils.<VerifyCodeEntity>defaultSchedulers())
                .subscribe(new Observer<VerifyCodeEntity>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(e.getMessage());
                    }

                    @Override
                    public void onNext(VerifyCodeEntity data) {
                        if (data.isSuccess()) {
                            callback.success(data);
                        } else {
                            callback.onError(data.getMsg());
                        }
                    }

                });
    }

}
