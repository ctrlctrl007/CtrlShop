package com.ctrl.ctrlshopmall.utils;


import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

/**
 * Created by ctrlc on 2017/11/9.
 */

public class HttpUtil {

    public static void setResponseRequest(String addrss, Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(addrss).build();
        client.newCall(request).enqueue(callback);
    }
}
