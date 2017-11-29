package com.ctrl.ctrlshopmall.utils;

import android.content.Context;
import android.text.TextUtils;

import com.ctrl.ctrlshopmall.bean.User;

/**
 * Created by ctrlc on 2017/11/23.
 */

public class UserLocalData {

    public static void putUser(Context context,User user){
        String userJson = JSONUtil.toJson(user);
        PreferencesUtils.putString(context,Contants.USER_JSON,userJson);

    }
    public static void putToken(Context context,String token){
        PreferencesUtils.putString(context,Contants.TOKEN,token);
    }

    public static User getUser(Context context){
        String userJson = PreferencesUtils.getString(context,Contants.USER_JSON);

        if (!TextUtils.isEmpty(userJson)){
            return JSONUtil.fromGson(userJson,User.class);
        }
        return null;
    }

    public static String getToken(Context context){
        return PreferencesUtils.getString(context,Contants.TOKEN);
    }

    public static void clearUser(Context context){
        PreferencesUtils.putString(context,Contants.USER_JSON,"");
    }

    public static void clearToken(Context context){
        PreferencesUtils.putString(context,Contants.TOKEN,"");
    }

}
