package com.ctrl.ctrlshopmall.http;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import com.ctrl.ctrlshopmall.MyApplication;
import com.google.gson.Gson;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by ctrlc on 2017/11/10.
 */

public class OkHttpHelper {

    private  static  OkHttpHelper mInstance;
    private OkHttpClient mHttpClient;
    private Gson mGson;
    private static final int TOKEN_MISS = 401;//token丢失
    private static final int TOKEN_ERROR = 402;//token错误
    private static final int TOKEN_EXPRIRE = 403;//token过期


    private Handler mHandler;



    static {
        mInstance = new OkHttpHelper();
    }

    private OkHttpHelper(){

        mHttpClient = new OkHttpClient();
        mHttpClient.setConnectTimeout(10, TimeUnit.SECONDS);
        mHttpClient.setReadTimeout(10,TimeUnit.SECONDS);
        mHttpClient.setWriteTimeout(30,TimeUnit.SECONDS);

        mGson = new Gson();

        mHandler = new Handler(Looper.getMainLooper());

    };

    public static  OkHttpHelper getInstance(){
        return  mInstance;
    }




    public void get(String url, Map<String,String> param,BaseCallBack callback){


        Request request = buildGetRequest(url,param);

        request(request,callback);

    }
    public void get(String url,BaseCallBack callback){


      get(url,null,callback);

    }


    public void post(String url, Map<String,String> param, BaseCallBack callback){

        Request request = buildPostRequest(url,param);
        request(request,callback);
    }





    public  void request(final Request request,final  BaseCallBack callback){

        callback.onBeforeRequest(request);

        mHttpClient.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Request request, IOException e) {
                callbackFailure(callback, request, e);

            }

            @Override
            public void onResponse(Response response) throws IOException {

//                    callback.onResponse(response);
                callbackResponse(callback,response);
                if(response.isSuccessful()) {

                    String resultStr = response.body().string();



                    if (callback.mType == String.class){
                        callbackSuccess(callback,response,resultStr);
                    }
                    else {
                        try {

                            Object obj = mGson.fromJson(resultStr, callback.mType);
                            callbackSuccess(callback,response,obj);
                        }
                        catch (com.google.gson.JsonParseException e){ // Json解析的错误
                            callback.onError(response,response.code(),e);
                        }
                    }
                }
                else if(response.code() ==TOKEN_ERROR||response.code() ==TOKEN_EXPRIRE||response.code() ==TOKEN_MISS  ){
                    callbackTokenError(callback,response);
                }
                else {
                    callbackError(callback,response,null);
                }

            }
        });


    }
    private void callbackTokenError(final  BaseCallBack callback , final Response response ){

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onTokenError(response,response.code());
            }
        });
    }


    private void callbackSuccess(final  BaseCallBack callback , final Response response, final Object obj ){

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onSuccess(response, obj);
            }
        });
    }


    private void    callbackError(final  BaseCallBack callback , final Response response, final Exception e ){

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onError(response,response.code(),e);
            }
        });
    }



    private void callbackFailure(final  BaseCallBack callback , final Request request, final IOException e ){

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onFailure(request,e);
            }
        });
    }


    private void callbackResponse(final  BaseCallBack callback , final Response response ){

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onResponse(response);
            }
        });
    }



    private  Request buildPostRequest(String url,Map<String,String> params){

        return  buildRequest(url,HttpMethodType.POST,params);
    }

    private  Request buildGetRequest(String url, Map<String,String> param){

        return  buildRequest(url,HttpMethodType.GET,param);
    }

    private  Request buildRequest(String url,HttpMethodType methodType,Map<String,String> params){


        Request.Builder builder = new Request.Builder()
                .url(url);

        if (methodType == HttpMethodType.POST){
            RequestBody body = builderFormData(params);
            builder.post(body);
        }
        else if(methodType == HttpMethodType.GET){
            url = buildUrlParams(url,params);
            builder.url(url);
            builder.get();
        }

        return builder.build();
    }

    private   String buildUrlParams(String url ,Map<String,String> params) {

        if(params == null)
            params = new HashMap<>(1);

        String token = MyApplication.getInstance().getToken();
        if(!TextUtils.isEmpty(token))
            params.put("token",token);


        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            sb.append(entry.getKey() + "=" + (entry.getValue()==null?"":entry.getValue().toString()));
            sb.append("&");
        }
        String s = sb.toString();
        if (s.endsWith("&")) {
            s = s.substring(0, s.length() - 1);
        }

        if(url.indexOf("?")>0){
            url = url +"&"+s;
        }else{
            url = url +"?"+s;
        }

        return url;
    }

    private RequestBody builderFormData(Map<String,String> params){


        FormEncodingBuilder builder = new FormEncodingBuilder();

        if(params !=null){

            for (Map.Entry<String,String> entry :params.entrySet() ){

                builder.add(entry.getKey(),entry.getValue());
            }
            String token = MyApplication.getInstance().getToken();
            if(!TextUtils.isEmpty(token)){
                builder.add("token",token);
            }
        }

        return  builder.build();

    }



    enum  HttpMethodType{

        GET,
        POST,

    }
}
