package com.tmslibrary.mvp.view;


import com.tmslibrary.entity.base.BaseErrorEntity;
import com.tmslibrary.mvp.view.base.BaseView;

public interface UpdateDownloadCountView extends BaseView {
    void updateDownloadCountCompleted(BaseErrorEntity data);
}
