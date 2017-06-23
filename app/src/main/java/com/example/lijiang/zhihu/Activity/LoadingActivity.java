package com.example.lijiang.zhihu.Activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.lijiang.zhihu.R;

public class LoadingActivity extends AppCompatActivity {

    private ImageView loadingImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_acitivity);
        loadingImage = (ImageView) findViewById(R.id.image_view);
        //loadingImage.setImageResource(R.drawable.loading);
        mHandler.sendEmptyMessageDelayed(GOTO_MAIN_ACTIVITY,3000);
//        HttpUtil.sendOkHttpRequst("http://news-at.zhihu.com/api/4/start-image/1080*1776", new okhttp3.Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//
//                Log.d("haha","wrong");
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                String responseData = response.body().string();
//                parseJSONObject(responseData);
//            }
//        });
//
    }


    private static final int GOTO_MAIN_ACTIVITY = 1;
    private Handler mHandler = new Handler(){
        public void handleMessage(android.os.Message message){
            switch (message.what){
                case GOTO_MAIN_ACTIVITY:
                    Intent intent = new Intent(LoadingActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                default:
                    break;
            }
        }
    };


}
