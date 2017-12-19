package com.ctrl.ctrlshopmall;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ctrl.ctrlshopmall.adapter.MyOrderAdapter;
import com.ctrl.ctrlshopmall.adapter.decoration.CardViewtemDecortion;
import com.ctrl.ctrlshopmall.bean.Order;
import com.ctrl.ctrlshopmall.bean.User;
import com.ctrl.ctrlshopmall.http.OkHttpHelper;
import com.ctrl.ctrlshopmall.http.SpotsCallBack;
import com.ctrl.ctrlshopmall.utils.Contants;
import com.ctrl.ctrlshopmall.widget.CNiaoToolBar;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.http.client.multipart.FormBodyPart;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okio.BufferedSink;

public class MyOderActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener {



    public static final int STATUS_ALL=1000;
    public static final int STATUS_SUCCESS=1; //支付成功的订单
    public static final int STATUS_PAY_FAIL=-2; //支付失败的订单
    public static final int STATUS_PAY_WAIT=0; //：待支付的订单
    private int status = STATUS_ALL;


    @ViewInject(R.id.tool_bar)
    private CNiaoToolBar mToolbar;


    @ViewInject(R.id.tab_layout)
    private TabLayout mTablayout;


    @ViewInject(R.id.recycler_view)
    private RecyclerView mRecyclerview;


    private MyOrderAdapter mAdapter;



    private OkHttpHelper okHttpHelper = OkHttpHelper.getInstance();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_oder);
        ViewUtils.inject(this);


        initToolBar();
        initTab();



        getOrders();
    }



    private void initToolBar(){

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initTab(){


        TabLayout.Tab tab= mTablayout.newTab();
        tab.setText("全部");
        tab.setTag(STATUS_ALL);
        mTablayout.addTab(tab);


        tab= mTablayout.newTab();
        tab.setText("支付成功");
        tab.setTag(STATUS_SUCCESS);
        mTablayout.addTab(tab);

        tab= mTablayout.newTab();
        tab.setText("待支付");
        tab.setTag(STATUS_PAY_WAIT);
        mTablayout.addTab(tab);

        tab= mTablayout.newTab();
        tab.setText("支付失败");
        tab.setTag(STATUS_PAY_FAIL);
        mTablayout.addTab(tab);


        mTablayout.setOnTabSelectedListener(this);


    }




    private void getOrders(){



        Long userId = MyApplication.getInstance().getUser().getId();

        Map<String, Object> params = new HashMap<>();

        params.put("user_id",userId);
        params.put("status",status);


        okHttpHelper.get(Contants.API.ORDER_LIST, params, new SpotsCallBack<List<Order>>(this) {
            @Override
            public void onSuccess(Response response, List<Order> orders) {
                showOrders(orders);
            }

            @Override
            public void onError(Response response, int code, Exception e) {

                    LogUtils.d("code:"+e.toString());

            }
        });

    }
    private void get(){

        Long userId = MyApplication.getInstance().getUser().getId();
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormEncodingBuilder().add("user_id",userId+"").add("status",status+"").build();
        Request request = new Request.Builder().url("http://112.124.22.238:8081/course_api/order/list?user_id=279" +
                "097&status=1000&token=cca8bd78-b042-4bca-9d82-d7bfc436d091").get().build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                LogUtils.d("code:"+e.toString());
            }

            @Override
            public void onResponse(Response response) throws IOException {
                LogUtils.d("code:"+response.toString());
            }
        });
    }



    private void showOrders(List<Order> orders){

        if(mAdapter ==null) {
            mAdapter = new MyOrderAdapter(orders,this);
            mRecyclerview.setAdapter(mAdapter);
            mRecyclerview.setLayoutManager(new LinearLayoutManager(this));
            mRecyclerview.addItemDecoration(new CardViewtemDecortion());

          /*  mAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {

                    toDetailActivity(position);
                }
            });*/
        }
        else{
            mAdapter.refreshData(orders);
            mRecyclerview.setAdapter(mAdapter);
        }
    }


    private void toDetailActivity(int position){

       /* Intent intent = new Intent(this,OrderDetailActivity.class);

        Order order = mAdapter.getItem(position);
        intent.putExtra("order",order);
        startActivity(intent,true);*/
    }



    @Override
    public void onTabSelected(TabLayout.Tab tab) {

        status = (int) tab.getTag();
        getOrders();
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }


}
