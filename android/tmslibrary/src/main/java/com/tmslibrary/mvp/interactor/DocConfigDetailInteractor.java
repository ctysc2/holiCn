package com.tmslibrary.mvp.interactor;




import com.tmslibrary.listener.RequestCallBack;

import rx.Subscription;

/**
 * Created by cty on 2016/12/12.
 */

public interface DocConfigDetailInteractor<T>{
    Subscription getDocConfigDetail(RequestCallBack<T> callback, String docConfigType, String appId);
}
