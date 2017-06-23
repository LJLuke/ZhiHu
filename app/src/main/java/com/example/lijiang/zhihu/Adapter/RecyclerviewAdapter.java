package com.example.lijiang.zhihu.Adapter;

import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.lijiang.zhihu.Activity.MainActivity;
import com.example.lijiang.zhihu.ObjectClass.BeforeNews;
import com.example.lijiang.zhihu.ObjectClass.NewestNews;
import com.example.lijiang.zhihu.R;

import java.lang.ref.WeakReference;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Handler;

/**
 * Created by lijiang on 2017/5/9.
 */

public class RecyclerviewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private MainActivity context;
    private List<NewestNews> mNewsList;
    private List<ImageView> mImageViewList = new ArrayList<>();
    private ImageHander hander = new ImageHander(new WeakReference<>(this));
    private ViewPager mViewPager;
    private OnItemClickListener mItemClickListener;
    private Toolbar toolbar;
    private int groupCount = 0;

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_NORMAL = 1;
    private static final int TYPE_GROUP = 2;

    public RecyclerviewAdapter(MainActivity context, List<NewestNews> mNewsList,Toolbar toolbar) {
        this.context = context;
        this.mNewsList = mNewsList;
        this.toolbar = toolbar;
    }
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnClickListener(OnItemClickListener onClickListener) {
        this.mItemClickListener = onClickListener;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        }if (position == 1 || !mNewsList.get(position+3).getDate().equals(mNewsList.get(position+4).getDate())) {
            return TYPE_GROUP;
        }
        else
            return TYPE_NORMAL;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_HEADER) {
            View view = LayoutInflater.from(context).inflate(R.layout.recyclerview_header, parent, false);
            HeadHolder headHolder = new HeadHolder(view);
            return headHolder;
        }
        if (viewType == TYPE_NORMAL) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item, parent, false);
            ItemHolder myViewHolder = new ItemHolder(view);
            return myViewHolder;
        }
        if (viewType == TYPE_GROUP){
            View view = LayoutInflater.from(context).inflate((R.layout.recyclerview_with_time_item),parent,false);
            WithTimeItemHolder withTimeItemHolder = new WithTimeItemHolder(view);
            return withTimeItemHolder;
        }
        return null;
    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof HeadHolder) {
            if (position == 0){
                toolbar.setTitle("首页");
            }
            mImageViewList.removeAll(mImageViewList);
            for (int i =0;i<5;i++){
                    final ImageView iv = (ImageView) LayoutInflater.from(context).inflate(R.layout.pager_item, null);
                    Glide.with(context)
                            .load(mNewsList.get(i).getTopImageUrl())
                            .asBitmap()
                            .into(iv);
                    mImageViewList.add(iv);
            }
            setUpViewPager((HeadHolder) holder);
        }
        if (holder instanceof ItemHolder) {
            Glide.with(context)
                    .load(mNewsList.get(position + 4).getImageUrl())
                    .asBitmap()
                    .into(((ItemHolder) holder).itemImage);
            ((ItemHolder) holder).itemText.setText(mNewsList.get(position + 4).getTitle());
            if (mNewsList.get(position + 4).isMoreImages() == false){
                ((ItemHolder) holder).moreImages.setVisibility(View.INVISIBLE);
            }
            if (mItemClickListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = holder.getLayoutPosition();
                        mItemClickListener.onItemClick(holder.itemView, pos);
                    }
                });
            }
        }
        if (holder instanceof WithTimeItemHolder){
            if (position == 1) {
                ((WithTimeItemHolder) holder).itemDate.setText("今日热闻");
                Glide.with(context)
                        .load(mNewsList.get(position + 4).getImageUrl())
                        .asBitmap()
                        .into(((WithTimeItemHolder) holder).itemImage);
                ((WithTimeItemHolder) holder).itemText.setText(mNewsList.get(position + 4 - groupCount).getTitle());
                if (mNewsList.get(position + 4).isMoreImages() == false){
                    ((WithTimeItemHolder) holder).moreImages.setVisibility(View.INVISIBLE);
                }
                if (mItemClickListener != null) {
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int pos = holder.getLayoutPosition();
                            mItemClickListener.onItemClick(holder.itemView, pos);
                        }
                    });
                }
                toolbar.setTitle("今日热闻");
            }
            else {
                try {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd EEE");
                    Date date = new SimpleDateFormat("yyyyMMdd").parse(mNewsList.get(position+4).getDate());
                    Log.d("datetime",date.toString());
                    ((WithTimeItemHolder)holder).itemDate.setText(simpleDateFormat.format(date));
                    Glide.with(context)
                            .load(mNewsList.get(position + 4).getImageUrl())
                            .asBitmap()
                            .into(((WithTimeItemHolder) holder).itemImage);
                    ((WithTimeItemHolder) holder).itemText.setText(mNewsList.get(position + 4 - groupCount).getTitle());
                    if (mNewsList.get(position + 4).isMoreImages() == false){
                        ((WithTimeItemHolder) holder).moreImages.setVisibility(View.INVISIBLE);
                    }
                    if (mItemClickListener != null) {
                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                int pos = holder.getLayoutPosition();
                                mItemClickListener.onItemClick(holder.itemView, pos);
                            }
                        });
                    }
                    toolbar.setTitle(simpleDateFormat.format(date));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        return;
    }

    private void setUpViewPager(final HeadHolder headHolder) {
        TopPagerAdapter topPagerAdapter = new TopPagerAdapter(context, mImageViewList,mNewsList);

        headHolder.viewPager.setAdapter(topPagerAdapter);

        final ImageView[] bottonImages = new ImageView[mImageViewList.size()];
        headHolder.bottonRing.removeAllViews();
        for (int i = 0; i < mImageViewList.size(); i++) {
            ImageView imageView = new ImageView(context);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(20, 20);
            params.setMargins(5, 0, 5, 0);
            imageView.setLayoutParams(params);
            if (i == 0) {
                imageView.setBackgroundResource(R.drawable.ic_ring_white);
            } else {
                imageView.setBackgroundResource(R.drawable.ic_ring_gray);
            }
            bottonImages[i] = imageView;
            headHolder.bottonRing.addView(bottonImages[i]);
        }

        headHolder.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                headHolder.headerTextView.setText(mNewsList.get(position).getTopTitel());
            }

            @Override
            public void onPageSelected(int position) {
                hander.sendMessage(Message.obtain(hander, ImageHander.MSG_PAGE_CHANGED, position, 0));
                int total = bottonImages.length;
                for (int i = 0; i < total; i++) {
                    if (i == position) {
                        bottonImages[i].setBackgroundResource(R.drawable.ic_ring_white);
                    } else {
                        bottonImages[i].setBackgroundResource(R.drawable.ic_ring_gray);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                switch (state) {
                    case ViewPager.SCROLL_STATE_DRAGGING:
                        hander.sendEmptyMessage(ImageHander.MSG_KEEP_SILENT);
                        break;
                    case ViewPager.SCROLL_STATE_IDLE:
                        hander.sendEmptyMessageDelayed(ImageHander.MSG_UPFATE_IMAGE, ImageHander.MSG_DELAY);
                        break;
                    default:
                        break;
                }

            }
        });
        headHolder.viewPager.setCurrentItem(0);
        hander.sendEmptyMessageDelayed(ImageHander.MSG_UPFATE_IMAGE, ImageHander.MSG_DELAY);
    }

    @Override
    public int getItemCount() {
        return mNewsList.size()-mImageViewList.size()+1;
    }

    class ItemHolder extends RecyclerView.ViewHolder {
        TextView itemText;
        ImageView itemImage;
        LinearLayout moreImages;

        public ItemHolder(View itemView) {
            super(itemView);
            itemText = (TextView) itemView.findViewById(R.id.news_title);
            itemImage = (ImageView) itemView.findViewById(R.id.news_image);
            moreImages = (LinearLayout) itemView.findViewById(R.id.more_images);
        }
    }

    class HeadHolder extends RecyclerView.ViewHolder {
        ViewPager viewPager;
        LinearLayout bottonRing;
        TextView headerTextView;

        public HeadHolder(View itemView) {
            super(itemView);
            viewPager = (ViewPager) itemView.findViewById(R.id.view_pager);
            mViewPager = viewPager;
            bottonRing = (LinearLayout) itemView.findViewById(R.id.pager_small_point);
            headerTextView = (TextView) itemView.findViewById(R.id.top_title);

        }
    }

    class WithTimeItemHolder extends RecyclerView.ViewHolder{
        TextView itemDate;
        TextView itemText;
        ImageView itemImage;
        LinearLayout moreImages;
        public WithTimeItemHolder(View itemView) {
            super(itemView);
            itemDate = (TextView) itemView.findViewById(R.id.news_time);
            itemText = (TextView) itemView.findViewById(R.id.news_title);
            itemImage = (ImageView) itemView.findViewById(R.id.news_image);
            moreImages = (LinearLayout) itemView.findViewById(R.id.more_images);
        }
    }
    private class ImageHander extends android.os.Handler {

        protected static final int MSG_UPFATE_IMAGE = 1;
        protected static final int MSG_KEEP_SILENT = 2;
        protected static final int MSG_BREAKE_LILENT = 3;
        protected static final int MSG_PAGE_CHANGED = 4;
        protected static final int MSG_DELAY = 3000;
        private WeakReference<RecyclerviewAdapter> mWeakReference;
        private int currentItem = 0;

        protected ImageHander(WeakReference<RecyclerviewAdapter> mWeakReference) {
            this.mWeakReference = mWeakReference;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            RecyclerviewAdapter myViewHolder = mWeakReference.get();
            if (myViewHolder == null) {
                return;
            }
            if (myViewHolder.hander.hasMessages(MSG_UPFATE_IMAGE)) {
                myViewHolder.hander.removeMessages(MSG_UPFATE_IMAGE);
            }
            switch (msg.what) {
                case MSG_UPFATE_IMAGE:
                    currentItem++;
                    mViewPager.setCurrentItem(currentItem);
                    myViewHolder.hander.sendEmptyMessageDelayed(MSG_UPFATE_IMAGE, MSG_DELAY);
                    break;
                case MSG_KEEP_SILENT:
                    break;
                case MSG_BREAKE_LILENT:
                    myViewHolder.hander.sendEmptyMessageDelayed(MSG_UPFATE_IMAGE, MSG_DELAY);
                    break;
                case MSG_PAGE_CHANGED:
                    currentItem = msg.arg1;
                    break;
                default:
                    break;
            }
        }

    }
}


