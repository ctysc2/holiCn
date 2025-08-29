package com.tmslibrary.mvp.interactor.impl;



import com.tmslibrary.R;
import com.tmslibrary.app.TmsLibraryApp;
import com.tmslibrary.entity.FileUploadEntity;
import com.tmslibrary.entity.base.TenantError;
import com.tmslibrary.listener.RequestCallBack;
import com.tmslibrary.mvp.interactor.FileUploadInteractor;
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

public class FileUploadInteractorImpl implements FileUploadInteractor<FileUploadEntity> {

    private final String API_TYPE = "fileUpload";


    @Inject
    public FileUploadInteractorImpl(){

    }

    @Override
    public Subscription fileUpload(final RequestCallBack<FileUploadEntity> callback, Map<String, RequestBody> body) {
        return RetrofitManager.getInstance(API_TYPE).fileUpload(body)
                .compose(TransformUtils.<FileUploadEntity>defaultSchedulers())
                .subscribe(new Observer<FileUploadEntity>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(e.getMessage());
                    }

                    @Override
                    public void onNext(FileUploadEntity data) {
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
