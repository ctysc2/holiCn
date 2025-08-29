package com.tmslibrary.mvp.interactor;




import com.tmslibrary.entity.VerifyCodeEntity;
import com.tmslibrary.listener.RequestCallBack;

import okhttp3.RequestBody;
import rx.Subscription;

/**
 * Created by cty on 2016/12/12.
 */

public interface LoginInteractor<T>{
    Subscription login(RequestCallBack<T> callback, VerifyCodeEntity.DataEntity body);
}
