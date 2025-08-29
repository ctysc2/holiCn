package com.tmslibrary.mvp.interactor.impl;


import com.tmslibrary.R;
import com.tmslibrary.app.TmsLibraryApp;
import com.tmslibrary.entity.CheckAccountConfirmEntity;
import com.tmslibrary.entity.base.TenantError;
import com.tmslibrary.listener.RequestCallBack;
import com.tmslibrary.mvp.interactor.CheckAccountConfirmInteractor;
import com.tmslibrary.repository.network.RetrofitManager;
import com.tmslibrary.utils.TransformUtils;

import java.util.ArrayList;

import javax.inject.Inject;

import rx.Observer;
import rx.Subscription;

/**
 * Created by cty on 2016/12/12.
 */

public class CheckAccountConfirmInteractorImpl implements CheckAccountConfirmInteractor<CheckAccountConfirmEntity> {

    private final String API_TYPE = "checkAccountConfirm";


    @Inject
    public CheckAccountConfirmInteractorImpl(){

    }

    @Override
    public Subscription checkAccountConfirm(final RequestCallBack<CheckAccountConfirmEntity> callback, String confirmType) {
        return RetrofitManager.getInstance(API_TYPE).checkAccountConfirm(confirmType)
                .compose(TransformUtils.<CheckAccountConfirmEntity>defaultSchedulers())
                .subscribe(new Observer<CheckAccountConfirmEntity>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(e.getMessage());
                    }

                    @Override
                    public void onNext(CheckAccountConfirmEntity data) {
                        if (data.isSuccess()) {
                            callback.success(data);
                        } else {
                            ArrayList<TenantError> errors = data.getErrors();
                            if(errors != null && errors.size() > 0){
                                callback.onError(errors.get(0).getMessage());
                            }else{
                                callback.onError(TmsLibraryApp.getInstances().getString(R.string.request_failed));
                            }
                        }
                    }

                });
    }
}
