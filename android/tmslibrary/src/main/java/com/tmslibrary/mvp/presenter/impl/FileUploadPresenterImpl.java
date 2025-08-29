package com.tmslibrary.mvp.presenter.impl;

import com.tmslibrary.entity.FileUploadEntity;
import com.tmslibrary.mvp.interactor.FileUploadInteractor;
import com.tmslibrary.mvp.interactor.impl.FileUploadInteractorImpl;
import com.tmslibrary.mvp.presenter.base.BasePresenterImpl;
import com.tmslibrary.mvp.view.FileUploadView;

import java.util.Map;

import javax.inject.Inject;

import okhttp3.RequestBody;

/**
 * Created by cty on 2016/12/12.
 */

public class FileUploadPresenterImpl extends BasePresenterImpl<FileUploadView, FileUploadEntity> {

    private FileUploadInteractor mFileUploadInteractorImpl;
    private final String API_TYPE = "fileUpload";

    @Inject
    public FileUploadPresenterImpl(FileUploadInteractorImpl FileUploadInteractor){
        mFileUploadInteractorImpl = FileUploadInteractor;
        reqType = API_TYPE;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }


    public void beforeRequest(){
        super.beforeRequest(FileUploadEntity.class);
    }

    public void fileUpload(Map<String, RequestBody> body){
        mSubscription = mFileUploadInteractorImpl.fileUpload(this,body);
    }

    @Override
    public void success(FileUploadEntity data) {
        super.success(data);
        mView.fileUploadCompleted(data);
    }
}
