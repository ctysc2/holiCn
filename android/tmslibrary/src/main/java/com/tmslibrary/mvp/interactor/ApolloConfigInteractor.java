package com.tmslibrary.mvp.interactor;




import com.tmslibrary.listener.RequestCallBack;

import java.util.Map;

import okhttp3.RequestBody;
import rx.Subscription;

/**
 * Created by cty on 2016/12/12.
 */

public interface ApolloConfigInteractor<T>{
    Subscription getApolloConfig(RequestCallBack<T> callback, String appId);
}
