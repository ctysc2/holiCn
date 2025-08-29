package com.tmslibrary.mvp.view;


import com.tmslibrary.entity.base.BaseErrorEntity;
import com.tmslibrary.mvp.view.base.BaseView;

public interface SaveAccountConfirmView extends BaseView {
    void saveAccountConfirmCompleted(BaseErrorEntity data);
}
