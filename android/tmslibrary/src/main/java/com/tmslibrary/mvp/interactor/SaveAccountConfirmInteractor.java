package com.tmslibrary.mvp.interactor;



import com.tmslibrary.listener.RequestCallBack;

import rx.Subscription;

/**
 * Created by cty on 2016/12/12.
 */

public interface SaveAccountConfirmInteractor<T>{
    Subscription saveAccountConfirm(RequestCallBack<T> callback, String docConfigDetailId, String confirmType);
}
