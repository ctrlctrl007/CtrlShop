package com.ctrl.ctrlshopmall.http;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.ctrl.ctrlshopmall.LoginActivity;
import com.ctrl.ctrlshopmall.MyApplication;
import com.ctrl.ctrlshopmall.utils.ToastUtils;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import dmax.dialog.SpotsDialog;

/**
 * Created by ctrlc on 2017/11/10.
 */

public abstract class SpotsCallBack<T> extends BaseCallBack<T> {
    private Context mContext;

    private SpotsDialog mDialog;

    public SpotsCallBack(Context context){

        mContext = context;

        initSpotsDialog();
    }



    private  void initSpotsDialog(){

        mDialog = new SpotsDialog(mContext,"拼命加载中...");

    }

    public  void showDialog(){
        mDialog.show();
    }

    public  void dismissDialog(){
        mDialog.dismiss();
    }


    public void setLoadMessage(int resId){
        mDialog.setMessage(mContext.getString(resId));
    }
    @Override
    public void onBeforeRequest(Request request) {
        showDialog();
    }

    @Override
    public void onFailure(Request request, Exception e) {
        dismissDialog();
        Log.d("aaaaaa", "onFailure: ==");
    }

    @Override
    public void onResponse(Response response) {
        dismissDialog();

    }

    @Override
    public void onTokenError(Response response, int code) {
        ToastUtils.show(mContext,"请先登录再进行该操作");
        Intent intent = new Intent(mContext, LoginActivity.class);
        mContext.startActivity(intent);
        MyApplication.getInstance().clearUser();
    }
}
