package com.ctrl.ctrlshopmall;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ctrl.ctrlshopmall.adapter.decoration.OrderWareAdapter;
import com.ctrl.ctrlshopmall.bean.Charge;
import com.ctrl.ctrlshopmall.bean.ShoppingCart;
import com.ctrl.ctrlshopmall.http.OkHttpHelper;
import com.ctrl.ctrlshopmall.http.ShoppingCartUtil;
import com.ctrl.ctrlshopmall.http.SpotsCallBack;
import com.ctrl.ctrlshopmall.msg.BaseResMsg;
import com.ctrl.ctrlshopmall.msg.CreateOrderRespMsg;
import com.ctrl.ctrlshopmall.utils.Contants;
import com.ctrl.ctrlshopmall.utils.JSONUtil;
import com.ctrl.ctrlshopmall.utils.PreferencesUtils;
import com.ctrl.ctrlshopmall.utils.ToastUtils;
import com.ctrl.ctrlshopmall.widget.CNiaoToolBar;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.pingplusplus.android.PaymentActivity;
import com.pingplusplus.android.Pingpp;
import com.squareup.okhttp.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewOrderActivity extends AppCompatActivity implements View.OnClickListener{

    /**
     * 银联支付渠道
     */
    private static final String CHANNEL_UPACP = "upacp";
    /**
     * 微信支付渠道
     */
    private static final String CHANNEL_WECHAT = "wx";
    /**
     * 支付支付渠道
     */
    private static final String CHANNEL_ALIPAY = "alipay";
    /**
     * 百度支付渠道
     */
    private static final String CHANNEL_BFB = "bfb";
    /**
     * 京东支付渠道
     */
    private static final String CHANNEL_JDPAY_WAP = "jdpay_wap";

    @ViewInject(R.id.tool_bar)
    private CNiaoToolBar mToolBar;

    @ViewInject(R.id.txt_order)
    private TextView orderTxt;

    @ViewInject(R.id.recycler_view)
    private RecyclerView mRecyclerView;

    @ViewInject(R.id.rl_alipay)
    private RelativeLayout mLayoutAlipay;

    @ViewInject(R.id.rl_wechat)
    private RelativeLayout mLayoutWechat;

    @ViewInject(R.id.rl_bd)
    private RelativeLayout mLayoutBd;

    @ViewInject(R.id.rb_alipay)
    private RadioButton mButtonAlipay;

    @ViewInject(R.id.rb_webchat)
    private RadioButton mButtonWechat;

    @ViewInject(R.id.rb_bd)
    private RadioButton mButtonBd;

    @ViewInject(R.id.btn_createOrder)
    private Button createOrderButton;

    @ViewInject(R.id.txt_total)
    private TextView totalTxt;
    private String currentPay = CHANNEL_ALIPAY;

    private float amount;

    private String orderNum;
    private Charge charge;

    private OrderWareAdapter adapter;
    private HashMap<String,RadioButton> channels = new HashMap<>(3);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order);
        ViewUtils.inject(this);
        init();
    }
    private void init(){

        channels.put(CHANNEL_ALIPAY,mButtonAlipay);
        channels.put(CHANNEL_WECHAT,mButtonWechat);
        channels.put(CHANNEL_BFB,mButtonBd);
        mLayoutAlipay.setOnClickListener(this);
        mLayoutBd.setOnClickListener(this);
        mLayoutWechat.setOnClickListener(this);

        amount = Float.valueOf(getIntent().getStringExtra("total"));
        totalTxt.setText("总计："+amount);
        initToolBar();
        initRecyclerView();
    }
    private void initToolBar(){
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initRecyclerView(){
        List<ShoppingCart> shoppingCarts = new ShoppingCartUtil(this).getAll();
        List<ShoppingCart> data = new ArrayList<>();
        if(shoppingCarts!=null&&shoppingCarts.size()>0)
        for (ShoppingCart shoppingCart: shoppingCarts){
            if (shoppingCart.isChecked()){
                data.add(shoppingCart);
            }
        }
        adapter = new OrderWareAdapter(data,this);
        mRecyclerView.setAdapter(adapter);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayout.HORIZONTAL);
        mRecyclerView.setLayoutManager(manager);
    }

    @OnClick(R.id.btn_createOrder)
    private void toCreateOrder(View view){

            createOrder();

    }
    private void createOrder(){
        final List<ShoppingCart> carts = adapter.getDatas();
        List<WareItem> items = new ArrayList<>();
        for(ShoppingCart cart: carts){
            WareItem item = new WareItem(cart.getId(),cart.getPrice().intValue());
            items.add(item);
        }
        String item_json = JSONUtil.toJson(items);

        Map<String,String> params = new HashMap<>(5);
        params.put("user_id",MyApplication.getInstance().getUser().getId()+"");
        params.put("item_json",item_json);
        params.put("pay_channel",currentPay);
        params.put("amount",(int)amount+"");
        params.put("addr_id",1+"");


        createOrderButton.setEnabled(false);
        OkHttpHelper.getInstance().post(Contants.API.ORDER_CREATE, params, new SpotsCallBack<CreateOrderRespMsg>(this) {
            @Override
            public void onSuccess(Response response, CreateOrderRespMsg orderRespMsg) {
                createOrderButton.setEnabled(true);
                orderNum = orderRespMsg.getData().getOrderNum();
                charge = orderRespMsg.getData().getCharge();
                new ShoppingCartUtil(NewOrderActivity.this).clean();
                openPaymentActivity();
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }


    @Override
    public void onClick(View view) {
        selectChannelPay(view.getTag().toString());
    }
    private void selectChannelPay(String payChannel){
        for(Map.Entry<String, RadioButton> entry : channels.entrySet()){
            RadioButton rb = entry.getValue();
            if(entry.getKey().equals(payChannel)){
                boolean isChecked = rb.isChecked();
                rb.setChecked(!isChecked);
                if (isChecked){
                    currentPay = entry.getKey();
                }
            }else{
                rb.setChecked(false);
            }
        }
    }
    private void openPaymentActivity(){

        Pingpp.createPayment(this,JSONUtil.toJson(charge));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //支付页面返回处理
        if (requestCode == Pingpp.REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getExtras().getString("pay_result");
                if("success".equals(result)){
                    changeOrderStatus(1);
                }
                else if("fail".equals(result)){
                    changeOrderStatus(-1);
                }

                else   if("cancel".equals(result)){
                    changeOrderStatus(-2);
                }
                else{
                    changeOrderStatus(0);
                }

            /* 处理返回值
             * "success" - payment succeed
             * "fail"    - payment failed
             * "cancel"  - user canceld
             * "invalid" - payment plugin not installed
             *
             * 如果是银联渠道返回 invalid，调用 UPPayAssistEx.installUPPayPlugin(this); 安装银联安全支付控件。
             */
//                String errorMsg = data.getExtras().getString("error_msg"); // 错误信息
//                String extraMsg = data.getExtras().getString("extra_msg"); // 错误信息

            }
        }
    }
    private void changeOrderStatus(final int status){
        HashMap<String,String> params = new HashMap<>(2);
        params.put("order_num",orderNum);
        params.put("status",status+"");
        OkHttpHelper.getInstance().post(Contants.API.ORDER_COMPLEPE, params, new SpotsCallBack<BaseResMsg>(this) {
            @Override
            public void onSuccess(Response response, BaseResMsg o) {
               toPayResultActivity(status);
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });

    }
    private void toPayResultActivity(int status){
        Intent intent = new Intent(this, PayResultActivity.class);
        intent.putExtra("status",status);
        startActivity(intent);
        finish();
    }

    class WareItem {
        private  Long ware_id;
        private  int amount;

        public WareItem(Long ware_id, int amount) {
            this.ware_id = ware_id;
            this.amount = amount;
        }

        public Long getWare_id() {
            return ware_id;
        }

        public void setWare_id(Long ware_id) {
            this.ware_id = ware_id;
        }

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }
    }
}
