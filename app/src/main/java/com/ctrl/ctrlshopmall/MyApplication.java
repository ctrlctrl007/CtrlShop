package com.ctrl.ctrlshopmall;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.mob.MobApplication;

/**
 * Created by ctrlc on 2017/11/4.
 */

public class MyApplication extends MobApplication {
    @Override
    public void onCreate() {
        super.onCreate();

        Fresco.initialize(this);
    }
}
