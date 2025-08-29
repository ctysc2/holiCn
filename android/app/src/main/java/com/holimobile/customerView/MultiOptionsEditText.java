package com.holimobile.customerView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.EditText;

public class MultiOptionsEditText extends EditText {
    public MultiOptionsEditText(Context context) {
        super(context);
    }

    public MultiOptionsEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        InputConnection connection = super.onCreateInputConnection(outAttrs);

        if (connection == null) return null;

    //移除EditorInfo.IME_FLAG_NO_ENTER_ACTION标志位

        outAttrs.imeOptions &= ~EditorInfo.IME_FLAG_NO_ENTER_ACTION;

        return connection;
    }
}
