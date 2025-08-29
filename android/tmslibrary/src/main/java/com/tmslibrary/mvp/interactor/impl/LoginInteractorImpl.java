package com.tmslibrary.mvp.interactor.impl;

import com.tmslibrary.entity.LoginEntity;
import com.tmslibrary.entity.VerifyCodeEntity;
import com.tmslibrary.listener.RequestCallBack;
import com.tmslibrary.mvp.interactor.LoginInteractor;
import com.tmslibrary.repository.network.RetrofitManager;
import com.tmslibrary.utils.TransformUtils;

import javax.inject.Inject;

import okhttp3.RequestBody;
import rx.Observer;
import rx.Subscription;

/**
 * Created by cty on 2016/12/12.
 */

public class LoginInteractorImpl implements LoginInteractor<LoginEntity> {

    private final String API_TYPE = "login";


    @Inject
    public LoginInteractorImpl(){

    }

    @Override
    public Subscription login(RequestCallBack<LoginEntity> callback, VerifyCodeEntity.DataEntity body) {
        return RetrofitManager.getInstance(API_TYPE).login(body)
                .compose(TransformUtils.<LoginEntity>defaultSchedulers())
                .subscribe(new Observer<LoginEntity>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(e.getMessage());
                    }

                    @Override
                    public void onNext(LoginEntity data) {
                        if (data.isSuccess()) {
                            callback.success(data);
                        } else {
                            callback.onError(data.getMsg());
                        }
                    }

                });
    }

}
