package com.ctrl.ctrlshopmall;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ctrl.ctrlshopmall.bean.User;
import com.ctrl.ctrlshopmall.http.OkHttpHelper;
import com.ctrl.ctrlshopmall.http.SpotsCallBack;
import com.ctrl.ctrlshopmall.msg.LoginResMsg;
import com.ctrl.ctrlshopmall.utils.Contants;
import com.ctrl.ctrlshopmall.utils.DESUtil;
import com.ctrl.ctrlshopmall.utils.ToastUtils;
import com.ctrl.ctrlshopmall.utils.UserLocalData;
import com.ctrl.ctrlshopmall.utils.Utility;
import com.ctrl.ctrlshopmall.widget.CNiaoToolBar;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.squareup.okhttp.Response;

import java.util.HashMap;
import java.util.Map;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.RegisterPage;

public class LoginActivity extends BaseActivity {
    @ViewInject(R.id.txt_phone)
    private TextView userNameTxt;

    @ViewInject(R.id.txt_pwd)
    private TextView pwdTxt;

    @ViewInject(R.id.tool_bar)
    private CNiaoToolBar mToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ViewUtils.inject(this);
        initToolBar();
    }

    private void initToolBar(){
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    @OnClick(R.id.btn_login)
    private void login(View view){
        String userName = userNameTxt.getText().toString();
        String pwd = pwdTxt.getText().toString();
       if ("".equals(userName)){
           ToastUtils.show(this,"请输入用户名");
            return;
       }
        if("".equals(pwd)){
            ToastUtils.show(this,"请输入密码");
            return;
        }
        Map<String, Object> params = new HashMap<>(2);
        params.put("phone",userName);
        params.put("password", DESUtil.encode(Contants.DES_KEY,pwd));
        OkHttpHelper.getInstance().post(Contants.API.LOGIN, params, new SpotsCallBack<LoginResMsg<User>>(this) {


            @Override
            public void onSuccess(Response response, LoginResMsg<User> userLoginResMsg) {
                MyApplication.getInstance().putUser(userLoginResMsg.getData(),userLoginResMsg.getToken());
                if(MyApplication.getInstance().getIntent() == null){
                    setResult(RESULT_OK);
                }else{
                    MyApplication.getInstance().jumpToTargetActivity(LoginActivity.this);
                }

                finish();
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });

    }
    @OnClick(R.id.txt_toReg)
    private void toReg(View view){

        Intent intent = new Intent(this,RegActivity.class);
        startActivity(intent);
        MyApplication.getInstance().putActivity(this);
    }


}
