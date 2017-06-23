package com.example.lijiang.zhihu.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lijiang.zhihu.Activity.ThemesActivity;
import com.example.lijiang.zhihu.ObjectClass.Item;
import com.example.lijiang.zhihu.ObjectClass.ThemeList;
import com.example.lijiang.zhihu.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lijiang on 2017/4/24.
 */

public class listViewItemAdapter extends ArrayAdapter<Item> {

    private List<ThemeList> themes = new ArrayList<>();
    private Context mContext;
    private int imageID;
    public listViewItemAdapter(Context context, int resource, List<Item> items, List<ThemeList> themes) {
        super(context, resource,items);
        imageID = resource;
        this.mContext = context;
        this.themes = themes;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Item item = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(imageID,parent,false);
        TextView itemName = (TextView) view.findViewById(R.id.text_view);
        ImageView itemImage = (ImageView) view.findViewById(R.id.image_view);
        itemImage.setImageResource(item.getImageID());
        itemName.setText(item.getTitle());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ThemesActivity.class);
                intent.putExtra("name",themes.get(position).getName());
                intent.putExtra("image_url",themes.get(position).getHeadImageUrl());
                intent.putExtra("description",themes.get(position).getDescription());
                Bundle bundle = new Bundle();
                bundle.putInt("id",themes.get(position).getId());
                intent.putExtra("bundle",bundle);
                mContext.startActivity(intent);
            }
        });
        return view;
    }
}
