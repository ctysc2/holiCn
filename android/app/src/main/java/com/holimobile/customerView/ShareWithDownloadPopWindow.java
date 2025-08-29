package com.holimobile.customerView;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.holimobile.R;
import com.holimobile.utils.ShareUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tmslibrary.utils.DimenUtil;
import com.yalantis2.ucrop.util.ScreenUtils;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by cty on 2016/12/15.
 */

public class ShareWithDownloadPopWindow extends PopupWindow {

    private View mMenuView;
    private Activity context;

    public ShareWithDownloadPopWindow(Activity context, String title,String desc,String url) {
        super(context);
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.alert_share_list, null);
        ImageView mExit = (ImageView) mMenuView.findViewById(R.id.iv_exit);
        LinearLayout hideContainer = (LinearLayout)mMenuView.findViewById(R.id.ll_hide_container);
        LinearLayout mDownload = (LinearLayout)mMenuView.findViewById(R.id.ll_download_container);


        LinearLayout mFriend = (LinearLayout)mMenuView.findViewById(R.id.ll_friend);
        LinearLayout mWx = (LinearLayout)mMenuView.findViewById(R.id.ll_wx);
        LinearLayout mMini = (LinearLayout)mMenuView.findViewById(R.id.ll_mini);
        LinearLayout mCopyLink = (LinearLayout)mMenuView.findViewById(R.id.ll_copy_link);


        this.setContentView(mMenuView);

        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);

        this.setFocusable(true);

        this.setAnimationStyle(R.style.headerTakerStyle);

        ColorDrawable dw = new ColorDrawable(0x00000000);

        this.setBackgroundDrawable(dw);

        mMenuView.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int height = mMenuView.findViewById(R.id.pop_layout).getTop();
                int y=(int) event.getY();
                if(event.getAction()==MotionEvent.ACTION_UP){
                    if(y<height){
                        preDismiss();
                    }
                }
                return true;
            }
        });

        mExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        this.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                preDismiss();
            }
        });

        mFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareUtils.shareWx(title,desc,url,context,2);
            }
        });

        mWx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareUtils.shareWx(title,desc,url,context,1);
            }
        });




        ArrayList<View> viewList = new ArrayList<>();
        viewList.add(mFriend);
        viewList.add(mWx);
        for(View v:viewList){
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)v.getLayoutParams();
            params.height = (ScreenUtils.getScreenWidth(context) - (int) DimenUtil.dp2px(80,context))/4;
            v.setLayoutParams(params);
        }
    }


    private void preShow(){
        Window mWindow = context.getWindow();
        WindowManager.LayoutParams lp = mWindow.getAttributes();
        lp.alpha = 0.4f;
        mWindow.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        mWindow.setAttributes(lp);
    }

    private void preDismiss(){
        Window mWindow = context.getWindow();
        WindowManager.LayoutParams lp = mWindow.getAttributes();
        lp.alpha = 1.0f;
        mWindow.setAttributes(lp);
        mWindow.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dismiss();

    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        preShow();
        super.showAtLocation(parent, gravity, x, y);
    }






}
