package com.example.lijiang.zhihu.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.lijiang.zhihu.ObjectClass.BeforeNews;
import com.example.lijiang.zhihu.ObjectClass.Item;
import com.example.lijiang.zhihu.ObjectClass.NewestNews;
import com.example.lijiang.zhihu.ObjectClass.ThemeList;
import com.example.lijiang.zhihu.R;
import com.example.lijiang.zhihu.Adapter.RecyclerviewAdapter;
import com.example.lijiang.zhihu.ToolClass.HttpUtil;
import com.example.lijiang.zhihu.Adapter.TopPagerAdapter;
import com.example.lijiang.zhihu.Adapter.listViewItemAdapter;
import com.example.lijiang.zhihu.ToolClass.MyItemDecoration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Response;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private List<Item> mItemList = new ArrayList<>();
    private LayoutInflater mInflater;
    private List<NewestNews> mNewestNewsList = new ArrayList<>();
    private List<ThemeList> mThemeLists = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private RecyclerviewAdapter recyclerviewAdapter;
    private SwipeRefreshLayout mRefreshLayout;
    private Handler handler;
    private static final int LOADFINISH = 0;
    private static final int THEME_FINISH = 1;
    private final String NewNewsUrl = "http://news-at.zhihu.com/api/4/news/latest";
    private final String BeforeNewsUrl = "http://news.at.zhihu.com/api/4/news/before/";
    private final String ThemeUrl = " http://news-at.zhihu.com/api/4/themes";
    private int newDate;
    private Date date;
    private SimpleDateFormat dateFormat;
    private listViewItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //toolbar
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("首页");
        setSupportActionBar(toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }

        date = new Date();
        dateFormat = new SimpleDateFormat("yyyyMMdd");
        newDate = Integer.parseInt(dateFormat.format(date)) + 1;

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mLayoutManager = new LinearLayoutManager(MainActivity.this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        loadNewnews(NewNewsUrl);
        loadThemes(ThemeUrl);
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case LOADFINISH:
                        recyclerviewAdapter = new RecyclerviewAdapter(MainActivity.this,mNewestNewsList,toolbar);
                        recyclerviewAdapter.setOnClickListener(new RecyclerviewAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                String newsContentUrl = "http://news-at.zhihu.com/api/4/news/"+mNewestNewsList.get(position+4).getId();
                                Intent intent = new Intent(MainActivity.this,NewsContentActivity.class);
                                intent.putExtra("url",newsContentUrl);
                                startActivity(intent);
                            }
                        });
                        mRecyclerView.setAdapter(recyclerviewAdapter);
                        break;
                    case THEME_FINISH:
                        adapter = new listViewItemAdapter(MainActivity.this,R.layout.listview_item,mItemList,mThemeLists);
                        break;
                    default:
                        break;
                }
            }
        };
        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.top_reflesh);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                date = new Date();
                dateFormat = new SimpleDateFormat("yyyyMMdd");
                newDate = Integer.parseInt(dateFormat.format(date)) + 1;
                loadNewnews(NewNewsUrl);
                recyclerviewAdapter.notifyDataSetChanged();
                mRefreshLayout.setRefreshing(false);

            }
        });

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastVisibleItem;
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem+1 == recyclerviewAdapter.getItemCount()){
                    --newDate;
                    loadBeforeNews(BeforeNewsUrl+newDate);
                    recyclerviewAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
            }
        });


        //抽屉listview
        mInflater = LayoutInflater.from(this);
        LinearLayout listviewHeader = (LinearLayout) mInflater.inflate(R.layout.listview_header,null);
        initItems();
        adapter = new listViewItemAdapter(MainActivity.this,R.layout.listview_item,mItemList,mThemeLists);
        ListView listView = (ListView) findViewById(R.id.left_listview);
        listView.addHeaderView(listviewHeader);
        listView.setAdapter(adapter);
    }

    private void loadNewnews(String newNewsUrl){
        HttpUtil.sendOkHttpRequst(newNewsUrl,new okhttp3.Callback(){
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(MainActivity.this,"加载失败",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                try {

                    JSONObject jsonObject = new JSONObject(responseData);
                    JSONArray jsonArray = new JSONArray(jsonObject.getString("stories"));
                    JSONArray jsonArray1 = new JSONArray(jsonObject.getString("top_stories"));
                    mNewestNewsList.removeAll(mNewestNewsList);
                    for (int i = 0;i<jsonArray1.length();i++){
                        NewestNews newestNews = new NewestNews();
                        JSONObject jsonObject1 = jsonArray1.getJSONObject(i);
                        newestNews.setDate(jsonObject.getString("date"));
                        newestNews.setTopImageUrl(jsonObject1.getString("image"));
                        newestNews.setTopId(jsonObject1.getInt("id"));
                        newestNews.setTopTitle(jsonObject1.getString("title"));
                        mNewestNewsList.add(newestNews);
                    }
                    for (int i = 0;i<jsonArray.length();i++){
                        NewestNews newestNews = new NewestNews();
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        if (jsonObject1.has("multipic")){
                            newestNews.setMoreImages(true);
                        }else
                            newestNews.setMoreImages(false);
                        newestNews.setDate(jsonObject.getString("date"));
                        JSONArray jsonArray2 = new JSONArray(jsonObject1.getString("images"));
                        newestNews.setImageUrl(jsonArray2.get(0).toString());
                        newestNews.setId(jsonObject1.getInt("id"));
                        newestNews.setTitle(jsonObject1.getString("title"));
                        mNewestNewsList.add(newestNews);
                    }
                    handler.sendEmptyMessage(LOADFINISH);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    private void loadBeforeNews(String beforeNewsUrl){
        HttpUtil.sendOkHttpRequst(beforeNewsUrl,new okhttp3.Callback(){
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(MainActivity.this,"加载失败",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                try {

                    JSONObject jsonObject = new JSONObject(responseData);
                    JSONArray jsonArray = new JSONArray(jsonObject.getString("stories"));
                    for (int i = 0;i<jsonArray.length();i++){
                        NewestNews newestNews = new NewestNews();
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        if (jsonObject1.has("multipic")){
                            newestNews.setMoreImages(true);
                        }else
                            newestNews.setMoreImages(false);
                        newestNews.setDate(jsonObject.getString("date"));
                        JSONArray jsonArray2 = new JSONArray(jsonObject1.getString("images"));
                        newestNews.setImageUrl(jsonArray2.get(0).toString());
                        newestNews.setId(jsonObject1.getInt("id"));
                        newestNews.setTitle(jsonObject1.getString("title"));
                        mNewestNewsList.add(newestNews);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    private void loadThemes(String URL){
        HttpUtil.sendOkHttpRequst(URL,new okhttp3.Callback(){
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(responseData);
                    JSONArray jsonArray = new JSONArray(jsonObject.getString("others"));
                    for (int i = 0;i<jsonArray.length();i++){
                        ThemeList themes = new ThemeList();
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        themes.setHeadImageUrl(jsonObject1.getString("thumbnail"));
                        themes.setDescription(jsonObject1.getString("description"));
                        themes.setId(jsonObject1.getInt("id"));
                        themes.setName(jsonObject1.getString("name"));
                        mThemeLists.add(themes);
                    }
                    handler.sendEmptyMessage(THEME_FINISH);
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.head,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.notification:
                Toast.makeText(this,"hello",Toast.LENGTH_SHORT).show();
                break;
            case R.id.setting:
                Toast.makeText(this,"hello",Toast.LENGTH_SHORT).show();
                break;
            case R.id.dark_mordel:
                Toast.makeText(this,"hello",Toast.LENGTH_SHORT).show();
                break;
            default:
        }
        return true;
    }

}
