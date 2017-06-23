package com.example.lijiang.zhihu.Activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.lijiang.zhihu.Adapter.ThemeRecyclerviewAdapter;
import com.example.lijiang.zhihu.Adapter.listViewItemAdapter;
import com.example.lijiang.zhihu.ObjectClass.Item;
import com.example.lijiang.zhihu.ObjectClass.ThemeContent;
import com.example.lijiang.zhihu.ObjectClass.ThemeList;
import com.example.lijiang.zhihu.R;
import com.example.lijiang.zhihu.ToolClass.HttpUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

public class ThemesActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private LayoutInflater mInflater;
    private List<Item> mItemList = new ArrayList<>();
    private listViewItemAdapter adapter;
    private List<ThemeList> mThemeLists = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private ThemeRecyclerviewAdapter mThemeRecyclerviewAdapter;
    private SwipeRefreshLayout mRefreshLayout;
    private List<ThemeContent> mThemeContents = new ArrayList<>();

    private final String themeContentUrl = "http://news-at.zhihu.com/api/4/theme/";
    private Handler handler;
    private String imageUrl;
    private String description;
    private int id;
    private static final int LOADFINISH = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_themes);

        Intent intent = getIntent();
        final String name = intent.getStringExtra("name");
        imageUrl  = intent.getStringExtra("image_url");
        description = intent.getStringExtra("description");
        Bundle bundle = intent.getBundleExtra("bundle");
        id = bundle.getInt("id");

        mInflater = LayoutInflater.from(this);
        LinearLayout listviewHeader = (LinearLayout) mInflater.inflate(R.layout.listview_header,null);
        initItems();
        adapter = new listViewItemAdapter(ThemesActivity.this,R.layout.listview_item,mItemList,mThemeLists);
        ListView listView = (ListView) findViewById(R.id.theme_listview);
        listView.addHeaderView(listviewHeader);
        listView.setAdapter(adapter);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.theme_drawerlayout);
        Button toolbarHomeButton = (Button) findViewById(R.id.theme_home);
        toolbarHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });
        TextView themeName = (TextView) findViewById(R.id.theme_title);
        themeName.setText(name);


        loadThemeContent(themeContentUrl+id);
        mRecyclerView = (RecyclerView) findViewById(R.id.theme_recyclerview);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case LOADFINISH:
                        mThemeRecyclerviewAdapter = new ThemeRecyclerviewAdapter(ThemesActivity.this,imageUrl,description,mThemeContents);
                        mThemeRecyclerviewAdapter.setOnClickListener(new ThemeRecyclerviewAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                String newsContentUrl = "http://news-at.zhihu.com/api/4/news/"+9017209;
                                Log.d("id",mThemeContents.get(position-1).getId()+"");
                                Log.d("id",mThemeContents.get(position).getId()+"");
                                Intent intent1 = new Intent(ThemesActivity.this,NewsContentActivity.class);
                                intent1.putExtra("url",newsContentUrl);
                                startActivity(intent1);
                            }
                        });
                        mRecyclerView.setAdapter(mThemeRecyclerviewAdapter);
                        break;
                    default:
                        break;
                }
            }
        };
    }

    private void loadThemeContent(String URL){
        HttpUtil.sendOkHttpRequst(URL,new okhttp3.Callback(){
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(responseData);
                    JSONArray jsonArray = new JSONArray(jsonObject.getString("stories"));
                    for (int i = 0;i<jsonArray.length();i++){
                        ThemeContent themeContent = new ThemeContent();
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        if (jsonObject1.has("images")){
                            JSONArray jsonArray1 = new JSONArray(jsonObject1.getString("images"));
                            themeContent.setImageUrl(jsonArray1.get(0).toString());
                        }
                        themeContent.setId(jsonObject1.getInt("id"));
                        themeContent.setTitel(jsonObject1.getString("title"));
                        mThemeContents.add(themeContent);
                        handler.sendEmptyMessage(LOADFINISH);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initItems(){
        Item item1 = new Item("日常心理学",R.drawable.ic_add_black_24dp);
        mItemList.add(item1);
        Item item2 = new Item("用户推荐日报",R.drawable.ic_add_black_24dp);
        mItemList.add(item2);
        Item item3 = new Item("电影日报",R.drawable.ic_add_black_24dp);
        mItemList.add(item3);
        Item item4 = new Item("不许无聊",R.drawable.ic_add_black_24dp);
        mItemList.add(item4);
        Item item5 = new Item("设计日报",R.drawable.ic_add_black_24dp);
        mItemList.add(item5);
        Item item6 = new Item("大公司日报",R.drawable.ic_add_black_24dp);
        mItemList.add(item6);
        Item item7 = new Item("财经日报",R.drawable.ic_add_black_24dp);
        mItemList.add(item7);
        Item item8 = new Item("互联网安全",R.drawable.ic_add_black_24dp);
        mItemList.add(item8);
        Item item9 = new Item("开始游戏",R.drawable.ic_add_black_24dp);
        mItemList.add(item9);
        Item item10 = new Item("音乐日报",R.drawable.ic_add_black_24dp);
        mItemList.add(item10);
        Item item11 = new Item("动漫日报",R.drawable.ic_add_black_24dp);
        mItemList.add(item11);
        Item item12 = new Item("体育日报",R.drawable.ic_add_black_24dp);
        mItemList.add(item12);
    }
}
