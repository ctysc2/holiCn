package com.tmslibrary.mvp.interactor;




import com.tmslibrary.entity.VerifyCodeEntity;
import com.tmslibrary.listener.RequestCallBack;

import rx.Subscription;

/**
 * Created by cty on 2016/12/12.
 */

public interface LogoutInteractor<T>{
    Subscription logout(RequestCallBack<T> callback);
}
