package com.tmslibrary.utils;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.tmslibrary.R;
import com.tmslibrary.listener.AlertDialogListener;
import com.tmslibrary.listener.OnItemClickListener;
import com.wang.avi.AVLoadingIndicatorView;


/**
 * Created by cty on 2016/12/12.
 */

public class DialogUtils {
    public static final int TYPE_COMMOM_LOADING = 0;
    public static final int TYPE_ALERT = 1;
    public static final int TYPE_UPDATE = 2;
    public static final int TYPE_LOADING = 3;
    public static final int TYPE_DEL = 4;
    public static final int TYPE_CREATE = 5;
    public static final int TYPE_ADD = 6;
    public static final int TYPE_DOOR_SEARCH = 7;
    public static final int TYPE_DOOR_OPEN= 8;
    public static final int TYPE_KEY_LIST = 9;

    private  Context mContext;
    private  int mDialogType;
    private Dialog mDialog;
    //private int resID = R.string.loading;
    public static DialogUtils create(Context context){

        return new DialogUtils(context);


    }
    private DialogUtils(Context context){
        mContext = context;
    }
    public boolean isShowing(){
        return mDialog.isShowing();

    }

    public void show(AlertDialogListener listenerString, String hint1, String hint2){

        switch (mDialogType){
            case TYPE_ALERT:
                //showAlertDialog(listenerString,hint1,hint2);
            default:
                break;
        }

    }
    public void dismiss(){
        if(mDialog!=null)
            mDialog.dismiss();


    }
    private TextView mHint;
    private AVLoadingIndicatorView mAvLoading;

    public void setText(int resID){
        if(mHint!=null)
            mHint.setText(mContext.getString(resID));
    }



    private void initDialog(){
        if(mDialog != null && mDialog.isShowing()){
            mDialog.dismiss();
            mDialog = null;
        }
    }
//    //加载中弹框
    public void showLoadingDialog(){

        initDialog();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View contentView = inflater.inflate(R.layout.dialog_common_loading, null);
        //mHint = ((TextView)contentView.findViewById(R.id.tv_loading));
        mAvLoading = (AVLoadingIndicatorView)contentView.findViewById(R.id.av_loading);
        //mProgress.setValueAnimated(90.0f);
        //mHint.setText(mContext.getString(resID));
        mDialog = new AlertDialog.Builder(mContext).create();


        if(DimenUtil.getScreenWidth(mContext) >DimenUtil.getScreenHeight(mContext)){
            mDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
            mDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialog) {
                    fullScreenImmersive(mDialog.getWindow().getDecorView());
                    mDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
                }
            });
        }
        mDialog.show();
        mDialog.setCancelable(false);
        mDialog.setContentView(contentView);
        Window window = mDialog.getWindow();
        window.setBackgroundDrawableResource(android.R.color.transparent); //背景透明
        WindowManager.LayoutParams params = mDialog.getWindow().getAttributes();

        params.width = (int)(DimenUtil.getScreenWidth(mContext)*0.4);
        window.setAttributes(params);
        window.setWindowAnimations(R.style.loadingPopinStyle);
    }



    public void showPrivacyAlert(OnItemClickListener itemClick){
        Log.i("VersionCheck","showPrivacyAlert");
        LayoutInflater inflater = LayoutInflater.from(mContext);

        View contentView = inflater.inflate(R.layout.alert_dialog_privacy, null);
        TextView mLeft =   (TextView)contentView.findViewById(R.id.tv_left);
        TextView mContent = (TextView)contentView.findViewById(R.id.tv_content);

        String originString = "巴斯夫调漆宝APP基本功能为配方搜索，配方上传等。请在使用前阅读和同意《服务协议》和《基本功能隐私政策》，同意基本功能隐私政策仅代表同意使用基本功能时，我们收集和处理相关必要信息，使用扩展功能时我们会根据扩展功能收集的信息，另行征求您的同意。";
        int index = originString.indexOf("《服务协议》");
        SpannableString textSpanned = new SpannableString(originString);
        textSpanned.setSpan(new TextClick(mContext,R.color.colorPrimaryDark,false){
            @Override
            public void onClick(@NonNull View widget) {
                //跳转服务协议
                if(itemClick != null){
                    itemClick.onItemClick(2);
                }

            }
        },index,index+"《服务协议》".length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);


        int index2 = originString.indexOf("《基本功能隐私政策》");

        textSpanned.setSpan(new TextClick(mContext,R.color.colorPrimaryDark,false){
            @Override
            public void onClick(@NonNull View widget) {
                //跳转隐私政策
                if(itemClick != null){
                    itemClick.onItemClick(3);
                }

            }
        },index2,index2+"《基本功能隐私政策》".length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

//        textSpanned.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.black)),index2,index2+mContext.getString(R.string.privacy_index2).length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        textSpanned.setSpan(new StyleSpan(Typeface.BOLD),index2,index2+"《基本功能隐私政策》".length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//
//        mContent.setHintTextColor(mContext.getResources().getColor(android.R.color.transparent));
        mContent.setMovementMethod(LinkMovementMethod.getInstance());
        mContent.setText(textSpanned);

        mLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                if(itemClick != null){
                    itemClick.onItemClick(0);
                }
            }
        });

        TextView mRight =  ((TextView)contentView.findViewById(R.id.tv_right));
        mRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                if(itemClick != null){
                    itemClick.onItemClick(1);
                }
            }
        });
        mDialog = new AlertDialog.Builder(mContext).create();
        if(DimenUtil.getScreenWidth(mContext) >DimenUtil.getScreenHeight(mContext)){
            mDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
            mDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialog) {
                    fullScreenImmersive(mDialog.getWindow().getDecorView());
                    mDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
                }
            });
        }
        mDialog.show();
        mDialog.setCancelable(false);
        mDialog.setContentView(contentView);
        Window window = mDialog.getWindow();
        window.setBackgroundDrawableResource(android.R.color.transparent);
        WindowManager.LayoutParams params = mDialog.getWindow().getAttributes();
        int width = DimenUtil.getScreenWidth(mContext) > DimenUtil.getScreenHeight(mContext)?DimenUtil.getScreenHeight(mContext):DimenUtil.getScreenWidth(mContext);
        params.width = (int)(width*0.7);
        window.setAttributes(params);
    }








    private void fullScreenImmersive(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_FULLSCREEN;
            view.setSystemUiVisibility(uiOptions);
        }
    }
    public void showKeyboard(EditText editText) {
        if(editText!=null){
            //设置可获得焦点
            editText.setFocusable(true);
            editText.setFocusableInTouchMode(true);
            //请求获得焦点
            editText.requestFocus();
            //调用系统输入法
            InputMethodManager inputManager = (InputMethodManager) editText                .getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
        }
    }

}
