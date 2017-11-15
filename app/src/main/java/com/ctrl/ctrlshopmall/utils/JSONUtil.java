package com.ctrl.ctrlshopmall.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;

/**
 * Created by ctrlc on 2017/11/14.
 */

public class JSONUtil {
    private static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    public static Gson getGson() {
        return gson;
    }

    public static <T> T fromGson(String json,Class<T> classT){
        return gson.fromJson(json,classT);
    }
    public static <T> T fromGson(String json, Type type){
        return gson.fromJson(json,type);
    }
    public static String toJson(Object o){
        return gson.toJson(o);
    }
}
