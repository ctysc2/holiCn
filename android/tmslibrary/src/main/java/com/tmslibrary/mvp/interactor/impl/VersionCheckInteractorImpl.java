package com.tmslibrary.mvp.interactor.impl;

import com.tmslibrary.entity.VersionCheckEntity;
import com.tmslibrary.listener.RequestCallBack;
import com.tmslibrary.mvp.interactor.VersionCheckInteractor;
import com.tmslibrary.repository.network.RetrofitManager;
import com.tmslibrary.utils.TransformUtils;

import javax.inject.Inject;

import okhttp3.RequestBody;
import rx.Observer;
import rx.Subscription;

/**
 * Created by cty on 2016/12/12.
 */

public class VersionCheckInteractorImpl implements VersionCheckInteractor<VersionCheckEntity> {

    private final String API_TYPE_PRODUCT = "productCheckVersion";


    @Inject
    public VersionCheckInteractorImpl(){

    }

    @Override
    public Subscription getProductVersionCheck(RequestCallBack<VersionCheckEntity> callback, RequestBody body) {
        return RetrofitManager.getInstance(API_TYPE_PRODUCT).getProductVersionCheck(body)
                .compose(TransformUtils.<VersionCheckEntity>defaultSchedulers())
                .subscribe(new Observer<VersionCheckEntity>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(e.getMessage());
                    }

                    @Override
                    public void onNext(VersionCheckEntity data) {
                        callback.success(data);
                    }

                });
    }

}
