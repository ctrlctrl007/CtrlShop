package com.ctrl.ctrlshopmall;

import android.app.Application;
import android.content.Context;
import android.print.PageRange;

import com.ctrl.ctrlshopmall.bean.User;
import com.ctrl.ctrlshopmall.utils.Contants;
import com.ctrl.ctrlshopmall.utils.UserLocalData;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.mob.MobApplication;

/**
 * Created by ctrlc on 2017/11/4.
 */

public class MyApplication extends MobApplication {

    private User user;

    private static MyApplication mInstance;

    public static MyApplication getInstance(){
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        initUser();
        Fresco.initialize(this);
    }
    private void initUser(){
        user = UserLocalData.getUser(this);
    }
    public User getUser(){
        return user;
    }
    public void putUser(User user,String token){
        this.user = user;
        UserLocalData.putUser(this,user);
        UserLocalData.putToken(this,token);
    }
    public void clearUser(){
        this.user = null;
        UserLocalData.clearUser(this);
        UserLocalData.clearToken(this);
    }

    public String getToken(){
        return UserLocalData.getToken(this);
    }


}
