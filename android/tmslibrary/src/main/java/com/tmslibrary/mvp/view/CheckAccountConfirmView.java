package com.tmslibrary.mvp.view;


import com.tmslibrary.entity.CheckAccountConfirmEntity;
import com.tmslibrary.mvp.view.base.BaseView;

public interface CheckAccountConfirmView extends BaseView {
    void checkAccountConfirmCompleted(CheckAccountConfirmEntity data);
}
