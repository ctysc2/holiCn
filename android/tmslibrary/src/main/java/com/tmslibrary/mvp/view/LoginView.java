package com.tmslibrary.mvp.view;


import com.tmslibrary.entity.LoginEntity;
import com.tmslibrary.entity.VerifyCodeEntity;
import com.tmslibrary.mvp.view.base.BaseView;

public interface LoginView extends BaseView {
    void loginCompleted(LoginEntity data);
}
