package com.tmslibrary.mvp.presenter.impl;




import com.tmslibrary.entity.DocConfigDetailEntity;
import com.tmslibrary.mvp.interactor.DocConfigDetailInteractor;
import com.tmslibrary.mvp.interactor.impl.DocConfigDetailInteractorImpl;
import com.tmslibrary.mvp.presenter.base.BasePresenterImpl;
import com.tmslibrary.mvp.view.DocConfigDetailView;

import javax.inject.Inject;

/**
 * Created by cty on 2016/12/12.
 */

public class DocConfigDetailPresenterImpl extends BasePresenterImpl<DocConfigDetailView, DocConfigDetailEntity> {

    private DocConfigDetailInteractor mDocConfigDetailInteractorImpl;
    private final String API_TYPE = "getDocConfigDetail";

    @Inject
    public DocConfigDetailPresenterImpl(DocConfigDetailInteractorImpl docConfigDetailInteractor){
        mDocConfigDetailInteractorImpl = docConfigDetailInteractor;
        reqType = API_TYPE;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }


    public void beforeRequest(){
        super.beforeRequest(DocConfigDetailEntity.class);
    }

    public void getDocConfigDetail(String docConfigType,String appId){
        mSubscription = mDocConfigDetailInteractorImpl.getDocConfigDetail(this,docConfigType,appId);
    }

    @Override
    public void success(DocConfigDetailEntity data) {
        super.success(data);
        mView.getDocConfigDetailCompleted(data);
    }
}
