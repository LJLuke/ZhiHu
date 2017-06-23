package com.example.lijiang.zhihu.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.lijiang.zhihu.ObjectClass.ThemeContent;
import com.example.lijiang.zhihu.R;

import java.util.ArrayList;
import java.util.IllegalFormatCodePointException;
import java.util.List;

/**
 * Created by lijiang on 2017/6/21.
 */

public class ThemeRecyclerviewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int NORMAL_ITEM = 0;
    private static final int WITH_PICTURE_ITEM = 1;
    private static final int HEADER_item = 2;

    private OnItemClickListener mItemClickListener;
    private List<ThemeContent> mThemeContents = new ArrayList<>();
    private Context context;
    private String themeImageUrl;
    private String description;

    public ThemeRecyclerviewAdapter(Context context, String themeImageUrl,String description,List<ThemeContent> mThemeContents){
        this.context = context;
        this.themeImageUrl = themeImageUrl;
        this.description = description;
        this.mThemeContents = mThemeContents;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnClickListener(OnItemClickListener onClickListener) {
        this.mItemClickListener = onClickListener;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == HEADER_item){
            View view = LayoutInflater.from(context).inflate(R.layout.theme_recyclerview_header,parent,false);
            HeaderItem headerItem = new HeaderItem(view);
            return headerItem;
        }
        if (viewType == WITH_PICTURE_ITEM){
            View view = LayoutInflater.from(context).inflate(R.layout.theme_with_picture_item,parent,false);
            WithPictureItem withPictureItem = new WithPictureItem(view);
            return withPictureItem;
        }
        if (viewType == NORMAL_ITEM){
            View view = LayoutInflater.from(context).inflate(R.layout.theme_normal_item,parent,false);
            NormalItem normalItem = new NormalItem(view);
            return normalItem;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof HeaderItem){
            if (position == 0){
                Glide.with(context).load(themeImageUrl)
                        .asBitmap()
                        .into(((HeaderItem) holder).themeImage);
                ((HeaderItem) holder).description.setText(description);
            }
        }
        if (holder instanceof WithPictureItem){
            Glide.with(context).load(mThemeContents.get(position-1).getImageUrl())
                    .asBitmap()
                    .into(((WithPictureItem) holder).mImageView);
            ((WithPictureItem) holder).titleTxet.setText(mThemeContents.get(position-1).getTitel());
            if (mItemClickListener != null){
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = holder.getLayoutPosition();
                        mItemClickListener.onItemClick(holder.itemView, pos);
                    }
                });
            }
        }
        if (holder instanceof NormalItem){
            ((NormalItem) holder).titleText.setText(mThemeContents.get(position-1).getTitel());
            if (mItemClickListener != null){
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = holder.getLayoutPosition();
                        mItemClickListener.onItemClick(holder.itemView, pos);
                    }
                });
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return HEADER_item;
        if (mThemeContents.get(position-1).getImageUrl() != null)
            return WITH_PICTURE_ITEM;
        else
            return NORMAL_ITEM;
    }

    @Override
    public int getItemCount() {
        return mThemeContents.size()+1;
    }
    class HeaderItem extends RecyclerView.ViewHolder {
        ImageView themeImage;
        TextView description;
        public HeaderItem(View itemView) {
            super(itemView);
            themeImage = (ImageView) itemView.findViewById(R.id.theme_image);
            description = (TextView) itemView.findViewById(R.id.theme_description);
        }
    }

    class NormalItem extends RecyclerView.ViewHolder {
        TextView titleText;
        public NormalItem(View itemView) {
            super(itemView);
            titleText = (TextView) itemView.findViewById(R.id.normal_theme_content_title);
        }
    }
    class WithPictureItem extends RecyclerView.ViewHolder {
        TextView titleTxet;
        ImageView mImageView;
        public WithPictureItem(View itemView) {
            super(itemView);
            titleTxet = (TextView) itemView.findViewById(R.id.theme_content_title);
            mImageView = (ImageView) itemView.findViewById(R.id.theme_content_picture);
        }
    }
}
