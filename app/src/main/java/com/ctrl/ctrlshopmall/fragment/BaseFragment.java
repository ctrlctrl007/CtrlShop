package com.ctrl.ctrlshopmall.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ctrl.ctrlshopmall.LoginActivity;
import com.ctrl.ctrlshopmall.MyApplication;
import com.ctrl.ctrlshopmall.bean.User;
import com.lidroid.xutils.ViewUtils;

/**
 * 碎片基础类
 * Created by ctrlc on 2017/11/7.
 */

public abstract class BaseFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        View view = createView(inflater, container,savedInstanceState);

        ViewUtils.inject(this,view);

        initToolbar();
        init();

        return view ;
    }
    public void initToolbar(){

    }

    public abstract View createView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState);

    public abstract void init();
    public void startActivity(Intent intent,boolean isNeedLogin){
        if(isNeedLogin){
            User user  = MyApplication.getInstance().getUser();
            if(user!=null){
                super.startActivity(intent);
            }else{
                MyApplication.getInstance().putIntent(intent);
                Intent intentLogin = new Intent(getActivity(), LoginActivity.class);
                super.startActivity(intentLogin);
            }
        }else{
            super.startActivity(intent);
        }
    }
}
