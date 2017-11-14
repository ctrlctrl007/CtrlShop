package com.ctrl.ctrlshopmall.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lidroid.xutils.ViewUtils;

/**
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
}
