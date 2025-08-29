package com.holimobile.customerView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.holimobile.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CommonKeyValueView extends LinearLayout {


    @BindView(R.id.tv_key)
    TextView mKey;

    @BindView(R.id.tv_value)
    TextView mValue;

    private Context context;
    public CommonKeyValueView(Context context) {
        this(context,null);
    }

    public CommonKeyValueView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.layout_key_value,this);
        this.context = context;
        ButterKnife.bind(this);

    }

    public void setKeyAndValue(String key,String value){
        mKey.setText(key+":");
        mValue.setText(value);
    }

}
