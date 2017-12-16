package com.example.networktest;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by 12191 on 2017/12/15.
 */

public class HttpUtil {
    public static void sendOkHttpRequest(String address, Callback callback){
        OkHttpClient client= new OkHttpClient();
        Request request= new Request.Builder()
                .url(address)
                .build();
        client.newCall(request).enqueue(callback);
    }
}
