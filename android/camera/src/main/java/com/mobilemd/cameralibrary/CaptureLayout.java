package com.mobilemd.cameralibrary;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mobilemd.cameralibrary.listener.CaptureListener;
import com.mobilemd.cameralibrary.listener.TypeListener;
import com.mobilemd.cameralibrary.util.TimeUtils;


public class CaptureLayout extends FrameLayout {

    private CaptureListener captureListener;    //拍照按钮监听
    private TypeListener typeListener;

    public void setCaptureListener(CaptureListener captureListener) {
        this.captureListener = captureListener;
    }

    public void setTypeListener(TypeListener typeListener) {
        this.typeListener = typeListener;
    }

    private TextView tvConfirm;            //确定
    private CaptureButton btn_capture;      //拍照按钮
    private TextView txt_tip;                    //提示文本
    private TextView countTime;

    private int layout_width;
    private int layout_height;
    private int button_size;

    private boolean isFirst = true;

    public CaptureLayout(Context context) {
        this(context, null);
    }

    public CaptureLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CaptureLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);

        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            layout_width = outMetrics.widthPixels;
        } else {
            layout_width = outMetrics.widthPixels / 2;
        }
        button_size = (int) (layout_width / 4.5f);
        layout_height = button_size + (button_size / 5) * 2 + 250;

        initView();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(layout_width, layout_height);
    }


    private void initView() {
        setWillNotDraw(false);
        //拍照按钮
        btn_capture = new CaptureButton(getContext(), button_size);
        LayoutParams btn_capture_param = new LayoutParams(LayoutParams.MATCH_PARENT, button_size);
        btn_capture_param.setMargins(0, 0, 0, button_size / 4);
        btn_capture_param.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        btn_capture.setLayoutParams(btn_capture_param);
        btn_capture.setCaptureListener(new CaptureListener() {
            @Override
            public boolean canTakePhoto() {
                if (captureListener != null) {
                    return captureListener.canTakePhoto();
                }
                return false;
            }

            @Override
            public boolean canRecorder() {
                if (captureListener != null) {
                    return captureListener.canRecorder();
                }
                return false;
            }

            @Override
            public void takePictures() {
                if (captureListener != null) {
                    captureListener.takePictures();
                }
            }

            @Override
            public void recordShort(long time) {
                if (captureListener != null) {
                    captureListener.recordShort(time);
                }
//                startAlphaAnimation();
            }

            @Override
            public void recordStart() {
                if (captureListener != null) {
                    captureListener.recordStart();
                }
                if(countTime != null){
                    countTime.setVisibility(View.VISIBLE);
                    countTime.setText(TimeUtils.formatTime(0, btn_capture.getDuration()));
                    txt_tip.setVisibility(GONE);
                }
//                startAlphaAnimation();
            }

            @Override
            public void recordMillions(long time) {
                if(captureListener != null){
                    captureListener.recordMillions(time);
                }
                if(countTime != null){
                    countTime.setText(TimeUtils.formatTime(time, btn_capture.getDuration()));
                }
            }

            @Override
            public void recordEnd(long time) {
                if (captureListener != null) {
                    captureListener.recordEnd(time);
                }
                if(countTime != null){
                    txt_tip.setVisibility(VISIBLE);
                    countTime.setVisibility(View.GONE);
                }
//                startAlphaAnimation();
            }

            @Override
            public void recordZoom(float zoom) {
                if (captureListener != null) {
                    captureListener.recordZoom(zoom);
                }
            }

            @Override
            public void recordError() {
                if (captureListener != null) {
                    captureListener.recordError();
                }
                if(countTime != null){
                    txt_tip.setVisibility(VISIBLE);
                    countTime.setVisibility(View.GONE);
                }
            }
        });


        //确认按钮
        tvConfirm = new TextView(getContext());
        tvConfirm.setText("确定");
        tvConfirm.setBackgroundResource(R.drawable.time_count_bg);
        LayoutParams btn_confirm_param = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        btn_confirm_param.gravity = Gravity.CENTER_VERTICAL | Gravity.RIGHT;
        tvConfirm.setTextColor(Color.BLACK);
        btn_confirm_param.setMargins(0, 60, layout_width / 10, 0);
        tvConfirm.setLayoutParams(btn_confirm_param);
        tvConfirm.setPadding(60, 18, 60, 18);
        tvConfirm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (typeListener != null) {
                    typeListener.confirm();
                }
//                startAlphaAnimation();
//                resetCaptureLayout();
            }
        });
        tvConfirm.setVisibility(GONE);


        txt_tip = new TextView(getContext());
        LayoutParams txt_param = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        txt_param.gravity = Gravity.CENTER_HORIZONTAL;
        txt_tip.setPadding(0, 130, 0, 0);
        txt_tip.setText("轻触拍照，长按摄像");
        txt_tip.setTextColor(0xFFFFFFFF);
        txt_tip.setGravity(Gravity.CENTER);
        txt_tip.setLayoutParams(txt_param);
        txt_tip.setVisibility(VISIBLE);


        countTime = new TextView(getContext());
        LayoutParams count_param = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        txt_param.setMargins(0, 0, 0, 0);
        count_param.gravity = Gravity.CENTER_HORIZONTAL;
        countTime.setText(TimeUtils.formatTime(0, btn_capture.getDuration()));
        countTime.setLayoutParams(count_param);
        countTime.setVisibility(GONE);
        countTime.setTextSize(20);
        countTime.setPadding(40, 10, 40, 10);
        countTime.setBackgroundResource(R.drawable.time_count_bg);

        this.addView(tvConfirm);
        this.addView(btn_capture);
        this.addView(txt_tip);
        this.addView(countTime);
    }

    /**************************************************
     * 对外提供的API                      *
     **************************************************/
    public void resetCaptureLayout() {
        btn_capture.resetState();
        btn_capture.setVisibility(VISIBLE);
    }

    public void changeCameraNum(int number){
        if(tvConfirm != null && number > 0){
            tvConfirm.setVisibility(VISIBLE);
            tvConfirm.setText("确定(" + number + ")");
            txt_tip.setVisibility(VISIBLE);
            txt_tip.setText("轻触拍照，长按摄像");
        }else {
            tvConfirm.setVisibility(GONE);
        }
    }



    public void startAlphaAnimation() {
        if (isFirst) {
            ObjectAnimator animator_txt_tip = ObjectAnimator.ofFloat(txt_tip, "alpha", 1f, 0f);
            animator_txt_tip.setDuration(500);
            animator_txt_tip.start();
            isFirst = false;
        }
    }

    public void setTextWithAnimation(String tip) {
        txt_tip.setText(tip);
        ObjectAnimator animator_txt_tip = ObjectAnimator.ofFloat(txt_tip, "alpha", 0f, 1f, 1f, 0f);
        animator_txt_tip.setDuration(2500);
        animator_txt_tip.start();
    }

    public void setDuration(int duration) {
        btn_capture.setDuration(duration);
    }

    public void setButtonFeatures(int state) {
        btn_capture.setButtonFeatures(state);
    }

    public void setTip(String tip) {
        txt_tip.setText(tip);
    }

    public void showTip() {
        txt_tip.setVisibility(VISIBLE);
    }

}
