package com.ctrl.ctrlshopmall;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ctrl.ctrlshopmall.bean.ShoppingCart;
import com.ctrl.ctrlshopmall.http.ShoppingCartUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class PayResultActivity extends AppCompatActivity {

    @ViewInject(R.id.pay_img)
    ImageView payImg;
    @ViewInject(R.id.pay_txt)
    TextView payTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_result);
        ViewUtils.inject(this);
        init();
    }
    private void init(){
        int status = getIntent().getIntExtra("status",0);
        if(status != 1){
            payImg.setImageResource(R.drawable.icon_cancel_128);
            payTxt.setText("支付失败");
        }
    }

    @OnClick(R.id.finish_btn)
    private void finish(View view){
        finish();
    }
}
