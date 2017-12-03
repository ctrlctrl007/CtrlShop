package com.ctrl.ctrlshopmall;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.print.PageRange;

import com.ctrl.ctrlshopmall.bean.User;
import com.ctrl.ctrlshopmall.utils.Contants;
import com.ctrl.ctrlshopmall.utils.UserLocalData;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.mob.MobApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ctrlc on 2017/11/4.
 */

public class MyApplication extends MobApplication {

    private User user;

    private static MyApplication mInstance;

    public static MyApplication getInstance(){
        return mInstance;
    }

    private List<Activity> activities;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        activities = new ArrayList<>();
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

    private Intent intent;

    public Intent getIntent() {
        return intent;
    }
    public void putIntent(Intent intent){
        this.intent = intent;
    }
    public void jumpToTargetActivity(Context context){
        context.startActivity(intent);
        putIntent(null);
    }
    public void putActivity(Activity activity){
        activities.add(activity);
    }
    public void clearActivity(){
        for (Activity activity :activities){
            activity.finish();
        }
    }

}
