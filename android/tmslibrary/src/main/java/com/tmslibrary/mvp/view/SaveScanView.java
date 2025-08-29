package com.tmslibrary.mvp.view;


import com.tmslibrary.entity.base.BaseErrorEntity;
import com.tmslibrary.entity.base.BaseHoLiEntity;
import com.tmslibrary.mvp.view.base.BaseView;

public interface SaveScanView extends BaseView {
    void saveScanCompleted(BaseHoLiEntity data);
}
