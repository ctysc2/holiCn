package com.tmslibrary.mvp.interactor;




import com.tmslibrary.listener.RequestCallBack;

import okhttp3.RequestBody;
import rx.Subscription;

/**
 * Created by cty on 2016/12/12.
 */

public interface VerifyCodeInteractor<T>{
    Subscription verifyCode(RequestCallBack<T> callback, RequestBody body);
}
