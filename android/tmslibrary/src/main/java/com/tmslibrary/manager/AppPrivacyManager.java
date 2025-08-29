package com.tmslibrary.manager;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.tmslibrary.app.TmsLibraryApp;
import com.tmslibrary.common.Const;
import com.tmslibrary.entity.CheckAccountConfirmEntity;
import com.tmslibrary.entity.DocConfigDetailEntity;
import com.tmslibrary.entity.base.BaseErrorEntity;
import com.tmslibrary.listener.OnItemClickListener;
import com.tmslibrary.mvp.interactor.impl.CheckAccountConfirmInteractorImpl;
import com.tmslibrary.mvp.interactor.impl.DocConfigDetailInteractorImpl;
import com.tmslibrary.mvp.interactor.impl.SaveAccountConfirmInteractorImpl;
import com.tmslibrary.mvp.presenter.impl.CheckAccountConfirmPresenterImpl;
import com.tmslibrary.mvp.presenter.impl.DocConfigDetailPresenterImpl;
import com.tmslibrary.mvp.presenter.impl.SaveAccountConfirmPresenterImpl;
import com.tmslibrary.mvp.view.CheckAccountConfirmView;
import com.tmslibrary.mvp.view.DocConfigDetailView;
import com.tmslibrary.mvp.view.SaveAccountConfirmView;
import com.tmslibrary.utils.AppUtils;
import com.tmslibrary.utils.DialogUtils;
import com.tmslibrary.utils.PreferenceUtils;
import com.tmslibrary.utils.ToastUtils;

//隐私政策工具类
public class AppPrivacyManager {

    //
    public static void checkPrivacy(Context context,OnAgreePrivacyListener listener){
        DocConfigDetailPresenterImpl mDocConfigDetailPresenterImpl = new DocConfigDetailPresenterImpl(new DocConfigDetailInteractorImpl());
        mDocConfigDetailPresenterImpl.attachView(new DocConfigDetailView() {
            @Override
            public void getDocConfigDetailCompleted(DocConfigDetailEntity data) {
                if(data != null){
                    String lastPrivacyVersion  = data.getData().getDocVersion();
                    String lastPrivacyId = data.getData().getId();
                    String localPrivacyVersion = PreferenceUtils.getPrefString(TmsLibraryApp.getInstances(), Const.KEY_LOCAL_PRIVACY_VERSION,"");
                    if(TextUtils.isEmpty(localPrivacyVersion) || AppVersionManager.compareVersion(localPrivacyVersion,lastPrivacyVersion) < 0 ){
                        AppPrivacyManager.showPrivacyAlert(context,new OnAgreePrivacyListener() {
                            @Override
                            public void onAgree(int status) {
                                if(status == 1){
                                    PreferenceUtils.setPrefString(TmsLibraryApp.getInstances(),Const.KEY_LOCAL_PRIVACY_VERSION,lastPrivacyVersion);
                                    PreferenceUtils.setPrefString(TmsLibraryApp.getInstances(),Const.KEY_LOCAL_PRIVACY_ID,lastPrivacyId);
                                }
                                if(listener != null){
                                    listener.onAgree(status);
                                }

                            }
                        });
                        return;
                    }else{
                        listener.onAgree(1);
                    }
                }
            }

            @Override
            public void showProgress(String reqType) {

            }

            @Override
            public void hideProgress(String reqType) {

            }

            @Override
            public void showErrorMsg(String reqType, String msg) {

            }
        });


        CheckAccountConfirmPresenterImpl mCheckAccountConfirmPresenterImpl = new CheckAccountConfirmPresenterImpl(new CheckAccountConfirmInteractorImpl());
        mCheckAccountConfirmPresenterImpl.attachView(new CheckAccountConfirmView() {
            @Override
            public void checkAccountConfirmCompleted(CheckAccountConfirmEntity data) {
                if(data != null && data.getData() != null){
                    if(!TextUtils.isEmpty(data.getData().getId())){
                        AppPrivacyManager.showPrivacyAlert(context,new OnAgreePrivacyListener() {
                            @Override
                            public void onAgree(int status) {
                                if(status == 1){
                                    PreferenceUtils.setPrefString(TmsLibraryApp.getInstances(),Const.KEY_LOCAL_PRIVACY_VERSION,data.getData().getDocVersion());
                                    PreferenceUtils.setPrefString(TmsLibraryApp.getInstances(),Const.KEY_LOCAL_PRIVACY_ID,data.getData().getId());
                                    SaveAccountConfirmPresenterImpl mSaveAccountConfirmPresenterImpl = new SaveAccountConfirmPresenterImpl(new SaveAccountConfirmInteractorImpl());
                                    mSaveAccountConfirmPresenterImpl.attachView(new SaveAccountConfirmView() {
                                        @Override
                                        public void saveAccountConfirmCompleted(BaseErrorEntity data) {

                                        }

                                        @Override
                                        public void showProgress(String reqType) {

                                        }

                                        @Override
                                        public void hideProgress(String reqType) {

                                        }

                                        @Override
                                        public void showErrorMsg(String reqType, String msg) {

                                        }
                                    });
                                    mSaveAccountConfirmPresenterImpl.saveAccountConfirm(data.getData().getId(),Const.YSTK);
                                }

                                if(listener != null){
                                    listener.onAgree(status);
                                }
                            }
                        });

                    }else{
                        Log.i("22222222","22222222");
                        if(listener != null){
                            listener.onAgree(1);
                        }
                    }
                }else{
                    if(listener != null){
                        listener.onAgree(1);
                    }
                }
            }

            @Override
            public void showProgress(String reqType) {

            }

            @Override
            public void hideProgress(String reqType) {

            }

            @Override
            public void showErrorMsg(String reqType, String msg) {
                ToastUtils.showShortSafe(msg);
            }
        });

        if(!AppUtils.isLogin()){
            mDocConfigDetailPresenterImpl.getDocConfigDetail(Const.YSTK,Const.APPID);
        }else{
            mCheckAccountConfirmPresenterImpl.checkAccountConfirm(Const.YSTK);
        }
    }


    public static void showPrivacyAlert(Context context, OnAgreePrivacyListener listener){

        DialogUtils.create(context).showPrivacyAlert(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                listener.onAgree(position);
            }
        });

    }

    public interface OnAgreePrivacyListener{
        //status: 0:拒绝 1:同意 2:跳转查看隐私政策
        void onAgree(int status);
    }

}
