package com.example.lijiang.zhihu.ToolClass;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by lijiang on 2017/4/24.
 */

public class HttpUtil {
    public static void sendOkHttpRequst(String adress,okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(adress)
                .build();
        client.newCall(request).enqueue(callback);
    }
}
