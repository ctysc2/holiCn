package com.holimobile.mvp.ui.activity.common;

import android.Manifest;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tmslibrary.common.Const;
import com.tmslibrary.listener.DownloadSuccessCallback;
import com.tmslibrary.utils.PermissionUtils;
import com.tmslibrary.utils.PreferenceUtils;
import com.tmslibrary.utils.ToastUtils;
import com.holimobile.R;
import com.holimobile.app.Application;
import com.holimobile.customerView.BigPhotoViewPager;
import com.holimobile.mvp.ui.activity.base.BaseActivity;
import com.holimobile.mvp.ui.adapter.GalleryAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;




public class ImageGalleryActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolBar;

    @BindView(R.id.back)
    ImageButton mBack;

    @BindView(R.id.midText)
    TextView mMidText;

    @BindView(R.id.view_pager)
    BigPhotoViewPager mViewPager;

    @BindView(R.id.v_top_separate)
    View mTopSeparate;


    private int mPosition;

    private GalleryAdapter mAdapter;

    private List<String> images = new ArrayList<>();

    private RxPermissions rxPermissions = null;


    @OnClick({R.id.back, R.id.ll_download})
    public void onClick(View view){

        switch (view.getId()){
            case R.id.back:
                setResult(RESULT_OK);
                finish();
                break;
            case R.id.ll_download:
                PermissionUtils.checkRequestPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, rxPermissions, new PermissionUtils.OnPermissionResultListener() {
                    @Override
                    public void allow() {
                        String tempName = new Date().getTime()+".png";
                        String resource = images.get(mPosition);
                    }

                    @Override
                    public void cancel() {

                    }
                });
                break;
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_image_gallery;
    }

    @Override
    public void initInjector() {
        mActivityComponent.inject(this);
    }

    @Override
    public void initViews() {
        mBack.setVisibility(View.GONE);
        rxPermissions = new RxPermissions(this);
        mToolBar.setBackgroundColor(getResources().getColor(R.color.black));
        images = getIntent().getStringArrayListExtra("images");
        mPosition = getIntent().getIntExtra("position",0);
        mBack.setImageResource(R.drawable.ico_white_back);
        mTopSeparate.setVisibility(View.GONE);
        mMidText.setTextColor(getResources().getColor(R.color.white));
        if(images != null){
            mMidText.setText((mPosition+1)+"/"+images.size());
        }else{
            finish();
        }
        initViewPager();
        View decor = getWindow().getDecorView();
        int ui = decor.getSystemUiVisibility();
        ui |= View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ui &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        }
        decor.setSystemUiVisibility(ui);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void initViewPager(){
        mAdapter = new GalleryAdapter(this);
        mAdapter.setData(images);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(mPosition);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mPosition = position;
                mMidText.setText((mPosition+1)+"/"+images.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    private void notifyGallery(File file){
        MediaStore.Images.Media.insertImage(getContentResolver(), BitmapFactory.decodeFile(file.getAbsolutePath()), file.getName(), null);
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(file);
        intent.setData(uri);
        sendBroadcast(intent);
        //通知完删除
        if(file.exists()){
            file.delete();
        }
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        finish();
    }
}
