package com.tmslibrary.mvp.view;


import com.tmslibrary.entity.VerifyCodeEntity;
import com.tmslibrary.mvp.view.base.BaseView;

public interface VerifyCodeView extends BaseView {
    void sendVerifyCodeCompleted(VerifyCodeEntity data);
}
