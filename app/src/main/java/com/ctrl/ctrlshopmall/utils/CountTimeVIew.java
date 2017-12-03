package com.ctrl.ctrlshopmall.utils;

import android.os.CountDownTimer;
import android.widget.TextView;

import com.ctrl.ctrlshopmall.R;

/**
 * Created by ctrlc on 2017/11/30.
 */

public class CountTimeVIew extends CountDownTimer {

    private TextView textView;
    private int endStrId;
    public static final int TIME_COUNT = 61000;//时间防止从59s开始显示（以倒计时60s为例子）


    /**
     * 参数 millisInFuture       倒计时总时间（如60S，120s等）
     * 参数 countDownInterval    渐变时间（每次倒计1s）
     */
    public CountTimeVIew(long millisInFuture, long countDownInterval,TextView textView,int endStrId) {
        super(millisInFuture, countDownInterval);
        this.endStrId = endStrId;
        this.textView = textView;
    }
    public CountTimeVIew(TextView textView,int endStrId){
        super(TIME_COUNT,1000);
        this.endStrId = endStrId;
        this.textView = textView;
    }
    public CountTimeVIew(TextView textView){
        super(TIME_COUNT,1000);
        this.endStrId = R.string.smssdk_resend_identify_code;
        this.textView = textView;
    }



    @Override
    public void onTick(long l) {
        textView.setEnabled(false);
        textView.setText(l / 1000 + " 秒后可重新发送");
    }

    @Override
    public void onFinish() {
        textView.setEnabled(true);
        textView.setText(endStrId);

    }
}
