package com.ctrl.ctrlshopmall.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.ctrl.ctrlshopmall.NewOrderActivity;
import com.ctrl.ctrlshopmall.R;
import com.ctrl.ctrlshopmall.adapter.CartAdapter;
import com.ctrl.ctrlshopmall.bean.User;
import com.ctrl.ctrlshopmall.http.OkHttpHelper;
import com.ctrl.ctrlshopmall.http.ShoppingCartUtil;
import com.ctrl.ctrlshopmall.http.SpotsCallBack;
import com.ctrl.ctrlshopmall.utils.Contants;
import com.ctrl.ctrlshopmall.widget.CNiaoToolBar;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.squareup.okhttp.Response;

/**
 * 显示购物车碎片
 * Created by ctrlc on 2017/11/7.
 */

public class CartFragment extends BaseFragment implements View.OnClickListener{
    private static final int ACTION_EDIT = 1;
    private static final int ACTION_COMPLETE = 2;

    @ViewInject(R.id.recycler_view)
    private RecyclerView mRecyclerView;
    @ViewInject(R.id.toolbar)
    private CNiaoToolBar toolBar;
    @ViewInject(R.id.checkbox_all)
    private CheckBox checkBox;
    @ViewInject(R.id.txt_total)
    private TextView totalTxt;
    @ViewInject(R.id.btn_order)
    private Button orderButton;
    @ViewInject(R.id.btn_del)
    private Button deleteButton;

    private ShoppingCartUtil shoppingCartUtil;

    private CartAdapter adapter;

    private OkHttpHelper httpHelper;
    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_carts,container,false);
    }

    @Override
    public void init() {
        httpHelper = OkHttpHelper.getInstance();
        shoppingCartUtil = new ShoppingCartUtil(getContext());
        showData();
        toolBar.getRightButton().setOnClickListener(this);

        toolBar.getRightButton().setTag(ACTION_EDIT);

    }

    public void showData(){
        adapter = new CartAdapter(shoppingCartUtil.getAll(),getContext(),totalTxt,checkBox);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

    }
    public void refreshData(){
        adapter.clear();
        adapter.addData(shoppingCartUtil.getAll());
        adapter.isAllchecked();
        adapter.showTotalPrice();
    }

    @Override
    public void onClick(View view) {
        int action = (int) toolBar.getRightButton().getTag();
        if (action==ACTION_EDIT){
            editStart();
        }else{
            editComplete();
        }


    }
    @OnClick(R.id.btn_order)
    private void toOrder(View view){

        Intent intent = new Intent(getActivity(), NewOrderActivity.class);
        intent.putExtra("total",totalTxt.getText());
        startActivity(intent,true);
    }
    private void editStart(){
        toolBar.getRightButton().setText("完成");
        totalTxt.setVisibility(View.GONE);
        orderButton.setVisibility(View.GONE);
        deleteButton.setVisibility(View.VISIBLE);
        adapter.checkAllOrNone(false);
        toolBar.getRightButton().setTag(ACTION_COMPLETE);
        checkBox.setChecked(false);
    }
    private void editComplete(){
        toolBar.getRightButton().setText("编辑");
        totalTxt.setVisibility(View.VISIBLE);
        orderButton.setVisibility(View.VISIBLE);
        deleteButton.setVisibility(View.GONE);
        adapter.checkAllOrNone(true);
        toolBar.getRightButton().setTag(ACTION_EDIT);
        checkBox.setChecked(true);
        adapter.showTotalPrice();
    }
    @OnClick(R.id.btn_del)
    public void delCart(View view){
        adapter.delet();
    }

}
