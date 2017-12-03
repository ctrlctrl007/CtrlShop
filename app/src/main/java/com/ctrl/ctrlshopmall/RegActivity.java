package com.ctrl.ctrlshopmall;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.ctrl.ctrlshopmall.utils.ToastUtils;
import com.ctrl.ctrlshopmall.widget.CNiaoToolBar;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.utils.SMSLog;
import dmax.dialog.SpotsDialog;

public class RegActivity extends AppCompatActivity {

    @ViewInject(R.id.txt_phone)
    private EditText phoneTxt;

    @ViewInject(R.id.txt_pwd)
    private EditText pwdTxt;

    @ViewInject(R.id.txt_countryCode)
    private TextView countryCodeTxt;

    @ViewInject(R.id.txt_country)
    private TextView countryTxt;

    @ViewInject(R.id.tool_bar)
    private CNiaoToolBar mToolBar;

    private SMSEventHandler smsHandler;

    private SpotsDialog spotsDialog;
    // 默认使用中国区号
    private static final String DEFAULT_COUNTRY_ID = "42";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_res);
        ViewUtils.inject(this);
        smsHandler = new SMSEventHandler();
        SMSSDK.registerEventHandler(smsHandler);
        String[] country = SMSSDK.getCountry(DEFAULT_COUNTRY_ID);
        if (country != null) {

            countryCodeTxt.setText("+"+country[1]);

            countryTxt.setText(country[0]);

        }
        initToolBar();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(smsHandler);
    }

    private void initToolBar(){
        mToolBar.setRightButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCode();
            }
        });
    }

    private void getCode(){
        spotsDialog = new SpotsDialog(this);
        spotsDialog.setMessage("正在获取验证码");

        String phone = phoneTxt.getText().toString().trim().replaceAll("\\s*", "");
        String code = countryCodeTxt.getText().toString().trim();
        String pwd = pwdTxt.getText().toString().trim();
        if (TextUtils.isEmpty(pwd)){
            ToastUtils.show(this,"密码不能为空");
        }

        checkPhoneNum(phone,code);

        //not 86   +86
        SMSSDK.getVerificationCode(code,phone);

    }

    private void checkPhoneNum(String phone, String code) {
        if (code.startsWith("+")) {
            code = code.substring(1);
        }

        if (TextUtils.isEmpty(phone)) {
            ToastUtils.show(this, "请输入手机号码");
            return;
        }

        if (code == "86") {
            if(phone.length() != 11) {
                ToastUtils.show(this,"手机号码长度不对");
                return;
            }

        }

        String rule = "^1(3|5|7|8|4)\\d{9}";
        Pattern p = Pattern.compile(rule);
        Matcher m = p.matcher(phone);

        if (!m.matches()) {
            ToastUtils.show(this,"您输入的手机号码格式不正确");
            return;
        }

    }
    class SMSEventHandler extends EventHandler{

        @Override
        public void afterEvent(final int event, final int result,
                               final Object data) {



            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    if (result == SMSSDK.RESULT_COMPLETE) {
                        if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {

                            onCountryListGot((ArrayList<HashMap<String, Object>>) data);

                        } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                            // 请求验证码后，跳转到验证码填写页面

                            afterVerificationCodeRequested((Boolean) data);

                        } else if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {

                        }
                    } else {

                        // 根据服务器返回的网络错误，给toast提示
                        try {
                            ((Throwable) data).printStackTrace();
                            Throwable throwable = (Throwable) data;

                            JSONObject object = new JSONObject(
                                    throwable.getMessage());
                            String des = object.optString("detail");
                            if (!TextUtils.isEmpty(des)) {
                                ToastUtils.show(RegActivity.this, des);
                                return;
                            }
                        } catch (Exception e) {
                            SMSLog.getInstance().w(e);
                        }

                    }


                }
            });
        }
    }

    private void onCountryListGot(ArrayList<HashMap<String, Object>> data) {
        // 解析国家列表
        for (HashMap<String, Object> country : data) {
            String code = (String) country.get("zone");
            String rule = (String) country.get("rule");
            if (TextUtils.isEmpty(code) || TextUtils.isEmpty(rule)) {
                continue;
            }

          //  Log.d(TAG,"code="+code + "rule="+rule);


        }
    }
    /** 请求验证码后，跳转到验证码填写页面 */
    private void afterVerificationCodeRequested(boolean smart) {
        if (spotsDialog != null && spotsDialog.isShowing()){
            spotsDialog.dismiss();
        }


        String phone = phoneTxt.getText().toString().trim().replaceAll("\\s*", "");
        String code = countryCodeTxt.getText().toString().trim();
        String pwd = pwdTxt.getText().toString().trim();

        if (code.startsWith("+")) {
            code = code.substring(1);
        }

        Intent intent = new Intent(this,RegSecondActivity.class);
        intent.putExtra("phone",phone);
        intent.putExtra("pwd",pwd);
        intent.putExtra("countryCode",code);

        startActivity(intent);
        MyApplication.getInstance().putActivity(this);
    }
}
