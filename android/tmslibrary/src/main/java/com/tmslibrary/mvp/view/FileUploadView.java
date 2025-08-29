package com.tmslibrary.mvp.view;


import com.tmslibrary.entity.FileUploadEntity;
import com.tmslibrary.mvp.view.base.BaseView;

public interface FileUploadView extends BaseView {
    void fileUploadCompleted(FileUploadEntity data);
}
