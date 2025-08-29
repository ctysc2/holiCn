package com.tmslibrary.mvp.view;


import com.tmslibrary.entity.LoginEntity;
import com.tmslibrary.entity.base.BaseHoLiEntity;
import com.tmslibrary.mvp.view.base.BaseView;

public interface LogoutView extends BaseView {
    void logoutCompleted(BaseHoLiEntity data);
}
