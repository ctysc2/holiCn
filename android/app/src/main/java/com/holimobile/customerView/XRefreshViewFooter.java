package com.holimobile.customerView;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.andview.refreshview.XRefreshView;
import com.andview.refreshview.callback.IFooterCallBack;
import com.andview.refreshview.utils.Utils;
import com.holimobile.R;
import com.wang.avi.AVLoadingIndicatorView;

public class XRefreshViewFooter extends LinearLayout implements IFooterCallBack {
    private Context mContext;

    private View mContentView;
    private AVLoadingIndicatorView mProgressBar;
    private LinearLayout mLoadingContainer;
    private TextView mHintView;
    private TextView mClickView;
    private boolean showing = true;
    private String footerMode = "";
    public XRefreshViewFooter(Context context) {
        super(context);
        initView(context);
    }

    public XRefreshViewFooter(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public void setMode(String mode){
        this.footerMode = mode;
    }

    @Override
    public void callWhenNotAutoLoadMore(final XRefreshView xRefreshView) {
        View childView = xRefreshView.getChildAt(1);
        if (childView != null && childView instanceof RecyclerView) {
            show(Utils.isRecyclerViewFullscreen((RecyclerView) childView));
            xRefreshView.enableReleaseToLoadMore(Utils.isRecyclerViewFullscreen((RecyclerView) childView));
        }
        mClickView.setText(com.andview.refreshview.R.string.xrefreshview_footer_hint_click);
        mClickView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                xRefreshView.notifyLoadMore();
            }
        });
    }

    @Override
    public void onStateReady() {
        Log.i("LoadMore22","onStateReady");
        mHintView.setVisibility(View.GONE);
        mLoadingContainer.setVisibility(View.GONE);
        mClickView.setText("");
        mClickView.setVisibility(View.GONE);
//        show(true);
    }

    @Override
    public void onStateRefreshing() {
        Log.i("LoadMore22","onStateRefreshing");
        mHintView.setVisibility(View.GONE);
        mLoadingContainer.setVisibility(View.VISIBLE);
        mClickView.setVisibility(View.GONE);
        show(true);
    }

    @Override
    public void onReleaseToLoadMore() {
        Log.i("LoadMore22","onReleaseToLoadMore");
        mHintView.setVisibility(View.GONE);
        mLoadingContainer.setVisibility(View.GONE);
        mClickView.setText(com.andview.refreshview.R.string.xrefreshview_footer_hint_release);
        mClickView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onStateFinish(boolean hideFooter) {
        Log.i("LoadMore22","onStateFinish");
        if (hideFooter) {
            mHintView.setText(com.andview.refreshview.R.string.xrefreshview_footer_hint_normal);
        } else {
            //处理数据加载失败时ui显示的逻辑，也可以不处理，看自己的需求
            mHintView.setText(com.andview.refreshview.R.string.xrefreshview_footer_hint_fail);
        }
        mHintView.setVisibility(View.VISIBLE);
        mLoadingContainer.setVisibility(View.GONE);
        mClickView.setVisibility(View.GONE);
    }

    @Override
    public void onStateComplete() {
        Log.i("LoadMore22","onStateComplete");
        if("class_list".equals(footerMode)){
            mHintView.setText("上拉继续浏览一下个分类");
        }else{
            mHintView.setText(R.string.xrefreshview_footer_hint_complete);
            hide();
        }

        mHintView.setVisibility(View.VISIBLE);
        mLoadingContainer.setVisibility(View.GONE);
        mClickView.setVisibility(View.GONE);
    }

    @Override
    public void show(final boolean show) {
        if (show == showing) {
            return;
        }
        showing = show;
        LayoutParams lp = (LayoutParams) mContentView
                .getLayoutParams();
        //lp.height = show ? LayoutParams.WRAP_CONTENT : 0;
        if("class_list".equals(footerMode)){
            lp.height = show ? LayoutParams.WRAP_CONTENT : LayoutParams.WRAP_CONTENT;
        }else{
            lp.height = show ? LayoutParams.WRAP_CONTENT : 0;
        }
        mContentView.setLayoutParams(lp);
//        setVisibility(show?VISIBLE:GONE);

    }

    public void hide(){
        LayoutParams lp = (LayoutParams) mContentView
                .getLayoutParams();
        lp.height = 0;
        mContentView.setLayoutParams(lp);
    }

    @Override
    public boolean isShowing() {
        return showing;
    }

    private void initView(Context context) {
        mContext = context;
        ViewGroup moreView = (ViewGroup) LayoutInflater.from(mContext).inflate(R.layout.xrefreshview_footer, this);
        moreView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        mContentView = moreView.findViewById(R.id.xrefreshview_footer_content);

        mLoadingContainer = moreView.findViewById(R.id.ll_loading_container);
        mProgressBar = moreView
                .findViewById(R.id.xrefreshview_footer_progressbar);
        mHintView = (TextView) moreView
                .findViewById(R.id.xrefreshview_footer_hint_textview);
        mClickView = (TextView) moreView
                .findViewById(R.id.xrefreshview_footer_click_textview);
    }

    @Override
    public int getFooterHeight() {
        return getMeasuredHeight();
    }
}