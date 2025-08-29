package com.tmslibrary.mvp.view;


import com.tmslibrary.entity.VersionCheckEntity;
import com.tmslibrary.mvp.view.base.BaseView;

public interface VersionCheckView extends BaseView {
    void getVersionCheckCompleted(VersionCheckEntity data);
}
