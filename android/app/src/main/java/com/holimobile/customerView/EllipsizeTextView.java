package com.holimobile.customerView;

import android.content.Context;
import android.graphics.Canvas;
import android.text.SpannableStringBuilder;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class EllipsizeTextView extends TextView {


    public EllipsizeTextView(Context context) {
        super(context);
    }

    public EllipsizeTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        CharSequence charSequence = getText() ;
        try {
            int count = getLayout().getLineCount();
            int lastCharDown = getLayout().getLineVisibleEnd(count == 1?0:1) ;
            if (charSequence.length() > lastCharDown){
                SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder() ;
                spannableStringBuilder.append(charSequence.subSequence(0,lastCharDown-2)).append("...") ;
                setText(spannableStringBuilder);
            }
        }catch (Exception e){

        }

        super.onDraw(canvas);
    }

    public  boolean mLinkHitFlag = false;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mLinkHitFlag=false;
        super.onTouchEvent(event);//textview会调用设置的LinkMovementMethod的onTouchevent
        return  mLinkHitFlag;
    }

//    public EllipsizeTextView(Context context) {
//        super(context);
//    }
//
//    public EllipsizeTextView(Context context, AttributeSet attrs) {
//        super(context, attrs);
//    }
//
//    public EllipsizeTextView(Context context, AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//    }
//
//    @Override
//    protected void onDetachedFromWindow() {
//        super.onDetachedFromWindow();
//    }
//
//    public EllipsizeTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//    }
//
//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        StaticLayout layout = null;
//        Field field = null;
//        try {
//            Field staticField = DynamicLayout.class.getDeclaredField("sStaticLayout");
//            staticField.setAccessible(true);
//            layout = (StaticLayout) staticField.get(DynamicLayout.class);
//        } catch (NoSuchFieldException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        }
//
//        if (layout != null) {
//            try {
//                field =   StaticLayout.class.getDeclaredField("mMaximumVisibleLineCount");
//                field.setAccessible(true);
//                field.setInt(layout, getMaxLines());
//            } catch (NoSuchFieldException e) {
//                e.printStackTrace();
//            } catch (IllegalAccessException e) {
//                e.printStackTrace();
//            }
//        }
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        if (layout != null && field != null) {
//            try {
//                field.setInt(layout, Integer.MAX_VALUE);
//            } catch (IllegalAccessException e) {
//                e.printStackTrace();
//            }
//        }
//    }

}
