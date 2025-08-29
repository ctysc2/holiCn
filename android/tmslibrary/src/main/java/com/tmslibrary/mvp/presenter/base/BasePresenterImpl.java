package com.tmslibrary.mvp.presenter.base;

import android.text.TextUtils;

import androidx.annotation.NonNull;


import com.tmslibrary.app.TmsLibraryApp;
import com.tmslibrary.event.ReFreshTokenEvent;
import com.tmslibrary.event.ReLoginEvent;
import com.tmslibrary.listener.RequestCallBack;
import com.tmslibrary.mvp.view.base.BaseView;
import com.tmslibrary.utils.NetWorkConfigUtil;
import com.tmslibrary.utils.PreferenceUtils;
import com.tmslibrary.utils.RxBus;

import rx.Subscription;

public class BasePresenterImpl<T extends BaseView, E> implements BasePresenter, RequestCallBack<E> {
    protected T mView;
    protected Subscription mSubscription;
    protected String reqType;
    protected String localKey;
    public E data;
    @Override
    public void onCreate() {

    }

    @Override
    public void onDestroy() {
        RxBus.cancelSubscription(mSubscription);
    }

    @Override
    public void attachView(@NonNull BaseView view) {
        mView = (T) view;
    }

    public void beforeRequest(Class<E> clazz) {
        if(TextUtils.isEmpty(localKey)){
            localKey = reqType;
        }
        if(NetWorkConfigUtil.isLoadCache(reqType)){
            E data = PreferenceUtils.getPrefObject(TmsLibraryApp.getInstances(),localKey,clazz);
            if(data != null){
                this.success(data);
                return;
            }
        }
        mView.showProgress(reqType);
    }

    @Override
    public void success(E data) {
        //如果接口需要缓存则保存到本地
        if(NetWorkConfigUtil.isLoadCache(reqType)){
            PreferenceUtils.setPrefObject(TmsLibraryApp.getInstances(),localKey,data);
        }
        mView.hideProgress(reqType);

    }
    @Override
    public void onError(String errorMsg) {
        mView.hideProgress(reqType);
        mView.showErrorMsg(reqType,errorMsg);
    }

    @Override
    public void onError(String errorMsg, int errCode) {
        mView.hideProgress(reqType);
        if(errCode == 405){
            //弹框提示
            RxBus.getInstance().post(new ReLoginEvent(errCode));
            mView.showErrorMsg(reqType,"");
        }
    }
}
