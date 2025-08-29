package com.tmslibrary.listener;

public interface RequestCallBack<T> {

//    void beforeRequest();

    void success(T data);

    void onError(String errorMsg);

    void onError(String errorMsg,int errCode);
}