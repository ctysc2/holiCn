package com.holimobile.mvp.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;
import java.util.List;


import static android.app.Activity.RESULT_OK;

public class GalleryAdapter extends PagerAdapter {

    private Context context;

    public GalleryAdapter(Context context) {
        this.context = context;
    }

    private List<String> images = new ArrayList<>();

    @Override
    public int getCount() {
        return images.size();
    }

    public void setData(List<String> images){
        this.images = images;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
         return view==object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        String filePath = images.get(position);
        return null;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }
}
