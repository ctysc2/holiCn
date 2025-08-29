package com.tmslibrary.mvp.view;


import com.tmslibrary.entity.DocConfigDetailEntity;
import com.tmslibrary.mvp.view.base.BaseView;

public interface DocConfigDetailView extends BaseView {
    void getDocConfigDetailCompleted(DocConfigDetailEntity data);
}
