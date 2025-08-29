package com.holimobile.mvp.ui.activity;

import static com.tmslibrary.common.Const.RESULT_PICK_FROM_PHOTO_NORMAL;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;

import com.holimobile.BuildConfig;
import com.holimobile.R;
import com.holimobile.customerView.CustomerLinkMovementMethod;
import com.holimobile.customerView.ShareWithDownloadPopWindow;
import com.holimobile.mvp.ui.activity.base.BaseActivity;
import com.holimobile.mvp.ui.activity.scan.ScanActivity;
import com.holimobile.mvp.ui.activity.webview.WebViewActivity;
import com.holimobile.utils.ShareUtils;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.mobilemd.cameralibrary.util.GlideEngine;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tmslibrary.common.Const;
import com.tmslibrary.entity.VerifyCodeEntity;
import com.tmslibrary.entity.VersionCheckEntity;
import com.tmslibrary.manager.AppVersionManager;
import com.tmslibrary.mvp.interactor.impl.VerifyCodeInteractorImpl;
import com.tmslibrary.mvp.presenter.impl.VerifyCodePresenterImpl;
import com.tmslibrary.mvp.view.VerifyCodeView;
import com.tmslibrary.mvp.view.VersionCheckView;
import com.tmslibrary.utils.DimenUtil;
import com.tmslibrary.utils.KeyBoardUtils;
import com.tmslibrary.utils.NetWorkConfigUtil;
import com.tmslibrary.utils.PreferenceUtils;
import com.tmslibrary.utils.TextClick;
import com.tmslibrary.utils.ToastUtils;

import java.io.File;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.et_username)
    EditText mUserName;

    @BindView(R.id.et_pwd)
    EditText mPwd;

    @BindView(R.id.id_del)
    ImageView mDelete;

    @BindView(R.id.tv_privacy)
    TextView mPrivacy;

    @BindView(R.id.iv_eye)
    ImageView mEye;

    @BindView(R.id.iv_check)
    ImageView mCheck;

    @BindView(R.id.iv_logo)
    ImageView mLogo;

    private RxPermissions rxPermissions;

    @OnClick({R.id.id_del,R.id.tv_login,R.id.iv_eye,R.id.ll_check,R.id.tv_privacy,R.id.tv_forget,R.id.tv_register})
    public void onClick(View view){
        Intent intent;
        switch (view.getId()){
            case R.id.tv_register:
                intent = new Intent(this,WebViewActivity.class);
                intent.putExtra("url",NetWorkConfigUtil.getH5UrlByCode(1003));
                startActivity(intent);
                break;
            case R.id.id_del:
                mUserName.setText("");
                break;
            case R.id.ll_check:
                mCheck.setSelected(!mCheck.isSelected());
                break;
            case R.id.tv_login:
                if(!mCheck.isSelected()){
                    ToastUtils.showShortSafe(R.string.hint_basf_ystk);
                    return;
                }

                if(TextUtils.isEmpty(mUserName.getText())){
                    ToastUtils.showShortSafe(R.string.hint_input_mobile);
                    return;
                }

                if(TextUtils.isEmpty(mPwd.getText())){
                    ToastUtils.showShortSafe(R.string.hint_input_password);
                    return;
                }

                showLoadingDialog();
                VerifyCodePresenterImpl mVerifyCodePresenterImpl = new VerifyCodePresenterImpl(new VerifyCodeInteractorImpl());
                mVerifyCodePresenterImpl.attachView(new VerifyCodeView() {
                    @Override
                    public void sendVerifyCodeCompleted(VerifyCodeEntity data) {
                        if(data.getResult() != null && !TextUtils.isEmpty(data.getResult().getPhone())){
                            Intent intent = new Intent(LoginActivity.this,VerifyCodeActivity.class);
                            intent.putExtra("loginInfo",data.getResult());
                            PreferenceUtils.setPrefObject(LoginActivity.this,"loginInfo",data.getResult());
                            startActivity(intent);
                        }else{
                            ToastUtils.showShortSafe(R.string.login_failed);
                        }
                    }

                    @Override
                    public void showProgress(String reqType) {

                    }

                    @Override
                    public void hideProgress(String reqType) {
                        dismissLoadingDialog();
                    }

                    @Override
                    public void showErrorMsg(String reqType, String msg) {
                        ToastUtils.showShortSafe(msg);
                    }
                });
                HashMap<String,Object> input = new HashMap<>();
                input.put("phone",mUserName.getText().toString());
                input.put("password",mPwd.getText().toString());
                mVerifyCodePresenterImpl.verifyCode(createRequestBody(input));
                break;
            case R.id.iv_eye:
                if(mEye.isSelected()){
                    mPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }else{
                    mPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                mEye.setSelected(!mEye.isSelected());
                mPwd.setSelection(mPwd.getText().length());
                break;
            case R.id.tv_privacy:
//                intent = new Intent(this, WebViewActivity.class);
//                String url = NetWorkConfigUtil.getH5UrlByCode(1001);
//                intent.putExtra("url",url);
//                intent.putExtra("title","服务协议");
//                startActivity(intent);
                break;
            case R.id.tv_forget:
                intent = new Intent(this, WebViewActivity.class);
                intent.putExtra("url",NetWorkConfigUtil.getH5UrlByCode(1002));
                intent.putExtra("title","忘记密码");
                startActivity(intent);
                break;
            default:
                break;
        }

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void initInjector() {
        mActivityComponent.inject(this);
    }

    @Override
    public void initViews() {
        rxPermissions = new RxPermissions(this);
        int screenWidth = DimenUtil.getScreenWidth(this);
        int height = screenWidth/2;
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)mLogo.getLayoutParams();
        params.height = height;
        params.width = screenWidth;
        mLogo.setLayoutParams(params);
        mDelete.setVisibility(View.GONE);
        mUserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() > 0){
                    mDelete.setVisibility(View.VISIBLE);
                }else{
                    mDelete.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        AppVersionManager.getVersion(this,rxPermissions);
        //for test
//        mLogo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent sendIntent = new Intent();
//                sendIntent.setAction(Intent.ACTION_SEND);
//
//                Uri fileUri;
//                String filePath = "/storage/emulated/0/Download/【CESH15】的病程整理.pdf";
//                File file = new File(filePath);
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                    fileUri = FileProvider.getUriForFile(LoginActivity.this, getApplicationContext().getPackageName()+".fileprovider", file);
//                } else {
//                    fileUri = Uri.fromFile(file);
//                }
//                grantUriPermission("com.tencent.mm",fileUri,Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//                sendIntent.setPackage("com.tencent.mm");
//                sendIntent.putExtra(Intent.EXTRA_STREAM, fileUri);
//                sendIntent.setDataAndType(fileUri,"application/pdf");
//                //sendIntent.setType("application/pdf");
//                Intent shareIntent = Intent.createChooser(sendIntent, null);
//                startActivity(shareIntent);
//            }
//        });
        String origin = getString(R.string.basf_ystk);
        SpannableString textSpanned = new SpannableString(origin);
        int start = origin.indexOf("《隐私政策》");
        int end = start + 6;
        textSpanned.setSpan(new TextClick(this, R.color.colorPrimaryDark, false) {
            @Override
            public void onClick(@NonNull View widget) {
                Intent intent = new Intent(LoginActivity.this, WebViewActivity.class);
                String url = NetWorkConfigUtil.getH5UrlByCode(1005);
                intent.putExtra("url",url);
                intent.putExtra("title","隐私政策");
                startActivity(intent);
            }
        }, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        int start2 = origin.indexOf("《服务协议》");
        int end2 = start2 + 6;

        textSpanned.setSpan(new TextClick(this, R.color.colorPrimaryDark, false) {
            @Override
            public void onClick(@NonNull View widget) {
                Intent intent = new Intent(LoginActivity.this, WebViewActivity.class);
                String url = NetWorkConfigUtil.getH5UrlByCode(1001);
                intent.putExtra("url",url);
                intent.putExtra("title","服务协议");
                startActivity(intent);
            }
        }, start2, end2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        mPrivacy.setMovementMethod(CustomerLinkMovementMethod.getInstance());
        mPrivacy.setText(textSpanned);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        View v = getCurrentFocus();
        new KeyBoardUtils(event,im,v).hideKeyBoardIfNecessary();
        return super.dispatchTouchEvent(event);
    }




}