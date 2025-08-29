package com.tmslibrary.mvp.view;


import com.tmslibrary.entity.ApolloConfigEntity;
import com.tmslibrary.entity.FileUploadEntity;
import com.tmslibrary.mvp.view.base.BaseView;

public interface ApolloConfigView extends BaseView {
    void getApolloConfigCompleted(ApolloConfigEntity data);
}
