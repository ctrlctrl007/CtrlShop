package com.ctrl.ctrlshopmall;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ctrl.ctrlshopmall.bean.User;
import com.ctrl.ctrlshopmall.http.OkHttpHelper;
import com.ctrl.ctrlshopmall.http.SpotsCallBack;
import com.ctrl.ctrlshopmall.msg.LoginResMsg;
import com.ctrl.ctrlshopmall.utils.Contants;
import com.ctrl.ctrlshopmall.utils.CountTimeVIew;
import com.ctrl.ctrlshopmall.utils.DESUtil;
import com.ctrl.ctrlshopmall.utils.ToastUtils;
import com.ctrl.ctrlshopmall.widget.CNiaoToolBar;
import com.ctrl.ctrlshopmall.widget.ClearEditText;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.squareup.okhttp.Response;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.utils.SMSLog;
import dmax.dialog.SpotsDialog;

public class RegSecondActivity extends AppCompatActivity {

    @ViewInject(R.id.tool_bar)
    private CNiaoToolBar mToolBar;

    @ViewInject(R.id.txt_tip)
    private TextView tipText;

    @ViewInject(R.id.txt_code)
    private ClearEditText verifyEditText;

    @ViewInject(R.id.btn_reSend)
    private Button mBtn;


    private String countyCode;
    private String phone;
    private String pwd;

    private SpotsDialog spotsDialog;

    private SMSEventHandler mHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_second);
        ViewUtils.inject(this);
        init();
    }
    private void init(){
        CountTimeVIew countTimeVIew = new CountTimeVIew(mBtn);
        countTimeVIew.start();

        mHandler = new SMSEventHandler();
        SMSSDK.registerEventHandler(mHandler);

        countyCode = getIntent().getStringExtra("countryCode");
        phone = getIntent().getStringExtra("phone");
        pwd = getIntent().getStringExtra("pwd");
        String formatedPhone = "+" + countyCode + " " + splitPhoneNum(phone);
        String text = getString(R.string.smssdk_send_mobile_detail)+formatedPhone;
        tipText.setText(Html.fromHtml(text));

        spotsDialog = new SpotsDialog(this);
        initToolBar();
    }
    private void initToolBar(){
        mToolBar.setRightButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spotsDialog.setMessage("正在验证验证码");
                spotsDialog.show();
                submitCode();
            }
        });
    }
    @OnClick(R.id.btn_reSend)
    private void reSendCode(View view){
        SMSSDK.getVerificationCode("+"+countyCode,phone);
        CountTimeVIew countTimerView = new CountTimeVIew(mBtn,R.string.smssdk_resend_identify_code);
        countTimerView.start();

        spotsDialog.setMessage("正在重新获取验证码");
        spotsDialog.show();
    }
    private void submitCode(){
        String code = verifyEditText.getText().toString().trim();
        if(TextUtils.isEmpty(code)){
            ToastUtils.show(this, R.string.smssdk_write_identify_code);
            return;
        }
        SMSSDK.submitVerificationCode(countyCode,phone,code);

    }
    /** 分割电话号码 */
    private String splitPhoneNum(String phone) {
        StringBuilder builder = new StringBuilder(phone);
        builder.reverse();
        for (int i = 4, len = builder.length(); i < len; i += 5) {
            builder.insert(i, ' ');
        }
        builder.reverse();
        return builder.toString();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(mHandler);
    }


    private void doReg(){
        Map<String,Object> params = new HashMap<>(2);

        params.put("phone",phone);
        params.put("password", DESUtil.encode(Contants.DES_KEY, pwd));
        OkHttpHelper.getInstance().post(Contants.API.REG, params, new SpotsCallBack<LoginResMsg<User>>(this) {

            @Override
            public void onSuccess(Response response, LoginResMsg<User> userLoginResMsg) {
                if (userLoginResMsg.getStatus() == LoginResMsg.STATUS_ERROR){
                    ToastUtils.show(RegSecondActivity.this,"注册失败"+userLoginResMsg.getMessage());
                    return;
                }
                MyApplication application = MyApplication.getInstance();
                application.putUser(userLoginResMsg.getData(),userLoginResMsg.getToken());
               // application.clearActivity();
                startActivity(new Intent(RegSecondActivity.this,MainActivity.class));
                finish();

            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }

            @Override
            public void onTokenError(Response response, int code) {
                super.onTokenError(response, code);
            }
        });
    }
    class SMSEventHandler extends EventHandler{
        @Override
        public void afterEvent(final int event, final int result, final Object data) {
            super.afterEvent(event, result, data);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(spotsDialog!=null&&spotsDialog.isShowing()){
                        spotsDialog.dismiss();
                    }
                    if (result ==  SMSSDK.RESULT_COMPLETE){
                        if(event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE){
                            doReg();

                        }
                    }else {

                        // 根据服务器返回的网络错误，给toast提示
                        try {
                            ((Throwable) data).printStackTrace();
                            Throwable throwable = (Throwable) data;

                            JSONObject object = new JSONObject(
                                    throwable.getMessage());
                            String des = object.optString("detail");
                            if (!TextUtils.isEmpty(des)) {
//                                ToastUtils.show(RegActivity.this, des);
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
}
