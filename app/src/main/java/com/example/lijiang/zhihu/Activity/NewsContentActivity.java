package com.example.lijiang.zhihu.Activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerTabStrip;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.lijiang.zhihu.ObjectClass.NewContent;
import com.example.lijiang.zhihu.R;
import com.example.lijiang.zhihu.ToolClass.HttpUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

public class NewsContentActivity extends AppCompatActivity {
    private ImageView top_image;
    private TextView title_text;
    private WebView webView;
    private String contentUrl;

    private Handler handler;
    private static final int LOADFINISH = 0;
    private List<NewContent> mNewContentList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_content);
        Intent intent = getIntent();
        contentUrl = intent.getStringExtra("url");

        Log.d("url",contentUrl);
        top_image = (ImageView) findViewById(R.id.new_content_image);
        title_text = (TextView) findViewById(R.id.news_content_title);
        webView = (WebView) findViewById(R.id.web_view);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDefaultTextEncodingName("UTF-8");
        webView.getSettings().setDefaultFontSize(18);
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        loadNewsContent(contentUrl);
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case LOADFINISH:
                        webView.loadDataWithBaseURL(null,mNewContentList.get(0).getBody(),"text/html","UTF-8",null);
                        Glide.with(NewsContentActivity.this).load(mNewContentList.get(0).getImageUrl())
                                .asBitmap()
                                .into(top_image);
                        title_text.setText(mNewContentList.get(0).getTitle());
                        break;
                    default:
                        break;
                }

            }
        };

        webView.setWebViewClient(new MyWebViewClient());
        Button backButton = (Button) findViewById(R.id.back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void loadNewsContent(String URL){
        HttpUtil.sendOkHttpRequst(URL,new okhttp3.Callback(){

            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(responseData);
                    NewContent newContent = new NewContent();
                    newContent.setBody(jsonObject.getString("body"));
                    newContent.setImageUrl(jsonObject.getString("image"));
                    newContent.setTitle(jsonObject.getString("title"));
                    mNewContentList.add(newContent);
                    handler.sendEmptyMessage(LOADFINISH);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private class MyWebViewClient extends WebViewClient{
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            imageReset();
        }
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            view.loadUrl(url);
            return true;
        }
    }
    private void imageReset(){
        webView.loadUrl("javascript:(function(){" +
                "var objs = document.getElementsByTagName('img'); " +
                "for(var i=0;i<objs.length;i++)  " +
                "{"
                + "var img = objs[i];   " +
                "    img.style.maxWidth = '100%'; img.style.height = 'auto';  " +
                "}" +
                "})()");
    }
}
