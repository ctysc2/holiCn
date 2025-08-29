package com.holimobile.customerView;

import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.MotionEvent;
import android.widget.TextView;

public class CustomerLinkMovementMethod extends LinkMovementMethod {



    @Override
    public boolean onTouchEvent(TextView widget, Spannable buffer, MotionEvent event) {
        int action = event.getAction();
        if (action == MotionEvent.ACTION_UP ||
                action == MotionEvent.ACTION_DOWN) {
            int x = (int) event.getX();
            int y = (int) event.getY();
            x -= widget.getTotalPaddingLeft();
            y -= widget.getTotalPaddingTop();
            x += widget.getScrollX();
            y += widget.getScrollY();
            Layout layout = widget.getLayout();
            int line = layout.getLineForVertical(y);
            int off = layout.getOffsetForHorizontal(line, x);
            ClickableSpan[] link = buffer.getSpans(off, off, ClickableSpan.class);
            if (link.length != 0) {
                if (action == MotionEvent.ACTION_UP) {
                    link[0].onClick(widget);
                } else if (action == MotionEvent.ACTION_DOWN) {
                    Selection.setSelection(buffer,
                            buffer.getSpanStart(link[0]),
                            buffer.getSpanEnd(link[0]));
                }
                // tiantian add to modify link hit flag
                //statusTextview是我们需要使用的自定义的textview，里面写一个标志位
                //mLinkHitFlag为true代表span点击，所以点击span设置。
                //statusTextview你的自定义textview类名（自己起）
                if(widget instanceof EllipsizeTextView){
                    ((EllipsizeTextView)widget).mLinkHitFlag=true;
                }
                return true;
            } else {
                Selection.removeSelection(buffer);
            }
        }
        return super.onTouchEvent(widget, buffer, event);
    }

    public static CustomerLinkMovementMethod getInstance() {
        if (null == sInstance) {
            sInstance = new CustomerLinkMovementMethod();
        }
        return sInstance;
    }

    private static CustomerLinkMovementMethod sInstance;


}
