package com.holimobile.mvp.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.holimobile.R;
import com.holimobile.mvp.ui.activity.base.BaseActivity;
import com.holimobile.mvp.ui.activity.webview.WebViewActivity;
import com.holimobile.mvp.ui.utils.PushUtils;
import com.tmslibrary.entity.LoginEntity;
import com.tmslibrary.entity.VerifyCodeEntity;
import com.tmslibrary.listener.TimerCallback;
import com.tmslibrary.mvp.interactor.impl.LoginInteractorImpl;
import com.tmslibrary.mvp.interactor.impl.VerifyCodeInteractorImpl;
import com.tmslibrary.mvp.presenter.impl.LoginPresenterImpl;
import com.tmslibrary.mvp.presenter.impl.VerifyCodePresenterImpl;
import com.tmslibrary.mvp.view.LoginView;
import com.tmslibrary.mvp.view.VerifyCodeView;
import com.tmslibrary.utils.AppUtils;
import com.tmslibrary.utils.NetWorkConfigUtil;
import com.tmslibrary.utils.RxBus;
import com.tmslibrary.utils.TimerUtils;
import com.tmslibrary.utils.ToastUtils;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscription;

import static android.view.KeyEvent.ACTION_DOWN;

public class VerifyCodeActivity extends BaseActivity {

    @BindView(R.id.et_1)
    EditText mEt1;

    @BindView(R.id.et_2)
    EditText mEt2;

    @BindView(R.id.et_3)
    EditText mEt3;

    @BindView(R.id.et_4)
    EditText mEt4;

    @BindView(R.id.tv_send_to)
    TextView mSendTo;

    @BindView(R.id.tv_re_get_sms)
    TextView mResetSms;

    private static int MAX_COUNT = 60;


    private VerifyCodeEntity.DataEntity loginInfo;

    @OnClick({R.id.back,R.id.tv_re_get_sms})
    public void onClick(View view){
        Intent intent;
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.tv_re_get_sms:
                if(loginInfo != null){
                    showLoadingDialog();
                    VerifyCodePresenterImpl mVerifyCodePresenterImpl = new VerifyCodePresenterImpl(new VerifyCodeInteractorImpl());
                    mVerifyCodePresenterImpl.attachView(new VerifyCodeView() {
                        @Override
                        public void sendVerifyCodeCompleted(VerifyCodeEntity data) {
                            if(data.getResult() != null && !TextUtils.isEmpty(data.getResult().getPhone())){
                                loginInfo = data.getResult();
                                startTimer();
                                ToastUtils.showShortSafe(R.string.send_succeed);
                            }else{
                                ToastUtils.showShortSafe(R.string.get_sms_code_failed);
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
                    input.put("phone",loginInfo.getPhone());
                    input.put("password",loginInfo.getPassword());
                    mVerifyCodePresenterImpl.verifyCode(createRequestBody(input));
                }
                break;
            default:
                break;
        }
    }

    private void sendSms(){
        String code = getInput();
        if(!TextUtils.isEmpty(code)){
            showLoadingDialog();
            LoginPresenterImpl mLoginPresenterImpl  = new LoginPresenterImpl(new LoginInteractorImpl());
            mLoginPresenterImpl.attachView(new LoginView() {
                @Override
                public void loginCompleted(LoginEntity data) {
                    if(data != null && data.getResult() != null && !TextUtils.isEmpty(data.getResult().getAccessToken())){
                        AppUtils.setToken(data.getResult().getAccessToken());
                        AppUtils.setUserId(String.valueOf(data.getResult().getUid()));
                        AppUtils.setPushLoginId(data.getResult().getDeviceLoginId());
                        PushUtils.bindAccount(data.getResult().getDeviceLoginId(),VerifyCodeActivity.this);
                        Intent intent = new Intent(VerifyCodeActivity.this, WebViewActivity.class);
                        String url = NetWorkConfigUtil.getH5UrlByCode(1000);
                        HashMap<String,Object> params = new HashMap<>();
                        params.put("userId",AppUtils.getUserId());
                        url = AppUtils.appendParams(url,params);
                        intent.putExtra("url",url);
                        startActivity(intent);
                        finish();
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
            loginInfo.setCaptcha(code);
            mLoginPresenterImpl.login(loginInfo);
        }else{
            ToastUtils.showShortSafe(R.string.hint_input_4_code);
        }
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_verify_code;
    }

    @Override
    public void initInjector() {
        mActivityComponent.inject(this);
    }

    @Override
    public void initViews() {

        startTimer();
        loginInfo = (VerifyCodeEntity.DataEntity)getIntent().getSerializableExtra("loginInfo");
        if(loginInfo != null){
            mSendTo.setText(String.format(getString(R.string.sms_send_to),loginInfo.getPhone()));
        }
        TimerUtils.delay(300, new TimerCallback() {
            @Override
            public void onTimerEnd() {
                showInputTips(mEt1);
            }
        });

        mEt1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().length() == 1){
                    showInputTips(mEt2);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mEt2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().length() == 1){
                    showInputTips(mEt3);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mEt2.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL && +event.getAction() == ACTION_DOWN) {
                    String content = mEt2.getText().toString();
                    if(TextUtils.isEmpty(content)){
                        showInputTips(mEt1);
                    }

                }
                return false;
            }
        });


        mEt3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().length() == 1){
                    showInputTips(mEt4);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mEt3.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL && +event.getAction() == ACTION_DOWN) {
                    String content = mEt3.getText().toString();
                    if(TextUtils.isEmpty(content)){
                        showInputTips(mEt2);
                    }

                }
                return false;
            }
        });

        mEt4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().length() == 1){
                    if(isInputCompleted()){
                        hideInput(mEt4);
                        sendSms();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mEt4.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL && +event.getAction() == ACTION_DOWN) {
                    String content = mEt4.getText().toString();
                    if(TextUtils.isEmpty(content)){
                        showInputTips(mEt3);
                    }

                }
                return false;
            }
        });



    }

    private void hideInput(EditText editText){
        InputMethodManager inputMethodManager = (InputMethodManager)this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private boolean isInputCompleted(){
        return mEt1.getText().length() > 0 &&
                mEt2.getText().length() > 0 &&
                mEt3.getText().length() > 0 &&
                mEt4.getText().length() > 0 ;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void showInputTips(EditText editText) {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        InputMethodManager inputManager =
                (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(editText, 0);
    }

    public String getInput(){
        if(isInputCompleted()){
            return mEt1.getText().toString()+
                    mEt2.getText().toString()+
                    mEt3.getText().toString()+
                    mEt4.getText().toString();
        }else {
            return "";
        }
    }
    private int second = MAX_COUNT;
    private Subscription mTimer;
    private void startTimer(){
        mResetSms.setEnabled(false);
        mResetSms.setText(String.format(getString(R.string.send_after_second),second));
        mTimer = TimerUtils.interval(1000, new TimerCallback() {
            @Override
            public void onTimerEnd() {
                second--;
                if(second == 0){
                    second = MAX_COUNT;
                    RxBus.cancelSubscription(mTimer);
                    mResetSms.setEnabled(true);
                    mResetSms.setText(R.string.reget_sms_code);
                    return;
                }
                mResetSms.setText(String.format(getString(R.string.send_after_second),second));
            }
        });
    }

}