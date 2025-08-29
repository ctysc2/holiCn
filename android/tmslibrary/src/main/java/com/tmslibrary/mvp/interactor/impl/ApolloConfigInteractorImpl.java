package com.tmslibrary.mvp.interactor.impl;



import com.tmslibrary.R;
import com.tmslibrary.app.TmsLibraryApp;
import com.tmslibrary.entity.ApolloConfigEntity;
import com.tmslibrary.entity.base.TenantError;
import com.tmslibrary.listener.RequestCallBack;
import com.tmslibrary.mvp.interactor.ApolloConfigInteractor;
import com.tmslibrary.repository.network.RetrofitManager;
import com.tmslibrary.utils.TransformUtils;

import java.util.ArrayList;
import java.util.Map;

import javax.inject.Inject;

import okhttp3.RequestBody;
import rx.Observer;
import rx.Subscription;

/**
 * Created by cty on 2016/12/12.
 */

public class ApolloConfigInteractorImpl implements ApolloConfigInteractor<ApolloConfigEntity> {

    private final String API_TYPE = "getApolloConfig";


    @Inject
    public ApolloConfigInteractorImpl(){

    }

    @Override
    public Subscription getApolloConfig(final RequestCallBack<ApolloConfigEntity> callback, String appId) {
        return RetrofitManager.getInstance(API_TYPE).getApolloConfig(appId)
                .compose(TransformUtils.<ApolloConfigEntity>defaultSchedulers())
                .subscribe(new Observer<ApolloConfigEntity>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(e.getMessage());
                    }

                    @Override
                    public void onNext(ApolloConfigEntity data) {
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
