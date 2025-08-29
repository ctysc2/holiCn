package com.holimobile.customerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.holimobile.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CommonSearchBar extends LinearLayout {

    @BindView(R.id.et_search)
    EditText mSearch;

    @BindView(R.id.iv_delete)
    ImageView mDelete;

    @BindView(R.id.tv_cancel)
    TextView mCancel;

    private boolean cancelEnable = false;

    private OnSearchConfirmListener listener;

    @OnClick({R.id.iv_delete,R.id.tv_cancel})
    public void onClick(View view){
        Intent intent;
        switch (view.getId()){
            case R.id.iv_delete:
                mSearch.setText("");
                if(listener != null){
                    listener.OnSearchConfirm("");
                    InputMethodManager inputMethodManager = (InputMethodManager)context.getSystemService(Activity.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(mSearch.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
                break;
            case R.id.tv_cancel:
                context.finish();
                break;
            default:
                break;
        }

    }


    private Activity context;

    public CommonSearchBar(Context context) {
        super(context);
    }

    public CommonSearchBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.layout_common_search_bar,this);
        this.context = (Activity) context;
        ButterKnife.bind(this);
        mSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(TextUtils.isEmpty(s)){
                    mDelete.setVisibility(INVISIBLE);
                }else{
                    mDelete.setVisibility(VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    //关闭软键盘
                    InputMethodManager inputMethodManager = (InputMethodManager)context.getSystemService(Activity.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    if(listener != null){
                        listener.OnSearchConfirm(mSearch.getText().toString());
                    }
                    return true;
                }
                return false;
            }
        });

    }

    public void setHint(String hint){
        mSearch.setHint(hint);
    }

    public void setCancelEnable(boolean cancelEnable){
        this.cancelEnable = cancelEnable;
        if(cancelEnable){
            mCancel.setVisibility(VISIBLE);
        }
    }

    public void setOnSearchConfirmListener(OnSearchConfirmListener listener){
        this.listener = listener;
    }


    public interface OnSearchConfirmListener{
        void OnSearchConfirm(String content);
    }
}
