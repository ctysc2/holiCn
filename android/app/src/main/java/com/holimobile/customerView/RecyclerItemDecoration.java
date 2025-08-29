package com.holimobile.customerView;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class RecyclerItemDecoration extends RecyclerView.ItemDecoration{
    private int itemSpace;
    private int itemNum;
    private boolean hasSidesPadding;

    /**
     *
     * @param itemSpace item间隔
     * @param itemNum 每行item的个数
     */
    public RecyclerItemDecoration(int itemSpace, int itemNum, boolean hasSidesPadding) {
        this.itemSpace = itemSpace;
        this.itemNum = itemNum;
        this.hasSidesPadding = hasSidesPadding;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        int index = parent.getChildLayoutPosition(view);
        outRect.bottom = itemSpace;
        if(hasSidesPadding){
            outRect.left = itemSpace;
            outRect.right = itemSpace;
            if(index < itemNum){
                //outRect.top = itemSpace;
            }
        }else{
            if ((index%itemNum == 0)){
                outRect.left = 0;
            } else {
                outRect.left = itemSpace;
            }
        }

    }
}
