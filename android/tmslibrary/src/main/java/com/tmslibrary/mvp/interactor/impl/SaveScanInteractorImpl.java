package com.tmslibrary.mvp.interactor.impl;

import com.tmslibrary.R;
import com.tmslibrary.app.TmsLibraryApp;
import com.tmslibrary.entity.base.BaseErrorEntity;
import com.tmslibrary.entity.base.BaseHoLiEntity;
import com.tmslibrary.entity.base.TenantError;
import com.tmslibrary.listener.RequestCallBack;
import com.tmslibrary.mvp.interactor.SaveAccountConfirmInteractor;
import com.tmslibrary.mvp.interactor.SaveScanInteractor;
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

public class SaveScanInteractorImpl implements SaveScanInteractor<BaseHoLiEntity> {

    private final String API_TYPE = "saveScan";


    @Inject
    public SaveScanInteractorImpl(){

    }

    @Override
    public Subscription saveScan(final RequestCallBack<BaseHoLiEntity> callback, RequestBody body) {
        return RetrofitManager.getInstance(API_TYPE).saveScan(body)
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
                            callback.onError(TmsLibraryApp.getInstances().getString(R.string.request_failed));
                        }
                    }

                });
    }
}
