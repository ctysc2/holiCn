package com.tmslibrary.mvp.presenter.base;

import androidx.annotation.NonNull;

import com.tmslibrary.mvp.view.base.BaseView;


public interface BasePresenter {

    void onCreate();

    void attachView(@NonNull BaseView view);

    void onDestroy();
}