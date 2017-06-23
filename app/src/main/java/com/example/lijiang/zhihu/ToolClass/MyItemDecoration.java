package com.example.lijiang.zhihu.ToolClass;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.PaintCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by lijiang on 2017/5/11.
 */

public class MyItemDecoration extends RecyclerView.ItemDecoration {
    private Drawable mDrawable;
    public MyItemDecoration(Drawable drawable){
       mDrawable = drawable;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int childCount = parent.getChildCount();
        Rect rect = new Rect();
        rect.left = parent.getPaddingLeft();
        rect.right = parent.getWidth() - parent.getPaddingRight();
        for (int i=0;i<childCount-2;i++){
            View childView = parent.getChildAt(i);
            rect.top = childView.getBottom();
            rect.bottom = rect.top + mDrawable.getIntrinsicHeight();
            mDrawable.draw(c);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.bottom += mDrawable.getIntrinsicHeight();
    }
}
