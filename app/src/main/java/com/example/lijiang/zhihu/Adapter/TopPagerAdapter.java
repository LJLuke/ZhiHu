package com.example.lijiang.zhihu.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.lijiang.zhihu.Activity.MainActivity;
import com.example.lijiang.zhihu.Activity.NewsContentActivity;
import com.example.lijiang.zhihu.ObjectClass.NewestNews;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lijiang on 2017/4/25.
 */

public class TopPagerAdapter extends PagerAdapter {

    private List<NewestNews> mNewsList = new ArrayList<>();
    private Context mContext;
    private List<ImageView> mImageViews = new ArrayList<>();

    public TopPagerAdapter(Context context, List<ImageView> imageViews,List<NewestNews> mNewsList) {
        mContext = context;
        mImageViews = imageViews;
        this.mNewsList = mNewsList;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View view = mImageViews.get(position);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newsContentUrl = "http://news-at.zhihu.com/api/4/news/"+mNewsList.get(position).getTopId();
                Intent intent = new Intent(mContext,NewsContentActivity.class);
                intent.putExtra("url",newsContentUrl);
                Log.d("url",mNewsList.get(2).getTopId()+"");
                mContext.startActivity(intent);
            }
        });
        container.addView(mImageViews.get(position));
        return mImageViews.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mImageViews.get(position));
    }

    @Override
    public int getCount() {
        return mImageViews.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {

        return view == object;
    }
}
