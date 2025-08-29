package com.tmslibrary.mvp.interactor.impl;



import com.tmslibrary.R;
import com.tmslibrary.app.TmsLibraryApp;
import com.tmslibrary.entity.DocConfigDetailEntity;
import com.tmslibrary.entity.base.TenantError;
import com.tmslibrary.listener.RequestCallBack;
import com.tmslibrary.mvp.interactor.DocConfigDetailInteractor;
import com.tmslibrary.repository.network.RetrofitManager;
import com.tmslibrary.utils.TransformUtils;

import java.util.ArrayList;

import javax.inject.Inject;

import rx.Observer;
import rx.Subscription;

/**
 * Created by cty on 2016/12/12.
 */

public class DocConfigDetailInteractorImpl implements DocConfigDetailInteractor<DocConfigDetailEntity> {

    private final String API_TYPE = "getDocConfigDetail";


    @Inject
    public DocConfigDetailInteractorImpl(){

    }

    @Override
    public Subscription getDocConfigDetail(final RequestCallBack<DocConfigDetailEntity> callback, String docConfigType, String appId) {
        return RetrofitManager.getInstance(API_TYPE).getDocConfigDetail(docConfigType,appId)
                .compose(TransformUtils.<DocConfigDetailEntity>defaultSchedulers())
                .subscribe(new Observer<DocConfigDetailEntity>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(e.getMessage());
                    }

                    @Override
                    public void onNext(DocConfigDetailEntity data) {
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
