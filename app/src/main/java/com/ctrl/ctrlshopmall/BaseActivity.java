package com.ctrl.ctrlshopmall;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.ctrl.ctrlshopmall.bean.User;

/**
 * Created by ctrlc on 2017/11/29.
 */

public class BaseActivity extends AppCompatActivity {
    public void startActivity(Intent intent, boolean isNeedLogin){
        if(isNeedLogin){
            User user  = MyApplication.getInstance().getUser();
            if(user!=null){
                super.startActivity(intent);
            }else{
                MyApplication.getInstance().putIntent(intent);
                Intent intentLogin = new Intent(this, LoginActivity.class);
                super.startActivity(intentLogin);
            }
        }else{
            super.startActivity(intent);
        }
    }
}
