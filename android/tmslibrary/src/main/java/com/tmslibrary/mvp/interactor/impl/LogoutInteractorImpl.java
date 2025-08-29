package com.tmslibrary.mvp.interactor.impl;

import com.tmslibrary.entity.VerifyCodeEntity;
import com.tmslibrary.entity.base.BaseHoLiEntity;
import com.tmslibrary.listener.RequestCallBack;
import com.tmslibrary.mvp.interactor.LoginInteractor;
import com.tmslibrary.mvp.interactor.LogoutInteractor;
import com.tmslibrary.repository.network.RetrofitManager;
import com.tmslibrary.utils.TransformUtils;

import javax.inject.Inject;

import rx.Observer;
import rx.Subscription;

/**
 * Created by cty on 2016/12/12.
 */

public class LogoutInteractorImpl implements LogoutInteractor<BaseHoLiEntity> {

    private final String API_TYPE = "logout";


    @Inject
    public LogoutInteractorImpl(){

    }

    @Override
    public Subscription logout(RequestCallBack<BaseHoLiEntity> callback) {
        return RetrofitManager.getInstance(API_TYPE).logout()
                .compose(TransformUtils.<BaseHoLiEntity>defaultSchedulers())
                .subscribe(new Observer<BaseHoLiEntity>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(e.getMessage());
                    }

                    @Override
                    public void onNext(BaseHoLiEntity data) {
                        if (data.isSuccess()) {
                            callback.success(data);
                        } else {
                            callback.onError(data.getMsg(),data.getCode());
                        }
                    }
                });
    }

}
