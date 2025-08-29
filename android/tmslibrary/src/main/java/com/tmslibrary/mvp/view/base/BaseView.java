package com.tmslibrary.mvp.view.base;

public interface BaseView {
    void showProgress(String reqType);

    void hideProgress(String reqType);

    void showErrorMsg(String reqType, String msg);
}