package com.ctrl.ctrlshopmall;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.ctrl.ctrlshopmall.adapter.AddressAdapter;
import com.ctrl.ctrlshopmall.adapter.decoration.DividerItemDecoration;
import com.ctrl.ctrlshopmall.bean.Address;
import com.ctrl.ctrlshopmall.http.OkHttpHelper;
import com.ctrl.ctrlshopmall.http.SpotsCallBack;
import com.ctrl.ctrlshopmall.msg.BaseResMsg;
import com.ctrl.ctrlshopmall.utils.Contants;
import com.ctrl.ctrlshopmall.widget.CNiaoToolBar;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.squareup.okhttp.Response;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddressListActivity extends AppCompatActivity {

    @ViewInject(R.id.tool_bar)
    private CNiaoToolBar mToolBar;

    @ViewInject(R.id.recycler_view)
    private RecyclerView mRecyclerView;

    private AddressAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_list);
        ViewUtils.inject(this);
        initToolBar();
        initAddresses();
    }

    private void initToolBar(){
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mToolBar.setRightButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toAddAddressActivity();
            }
        });
    }

    private void initAddresses(){
        final Map<String,Object> params = new HashMap<>(1);
        params.put("user_id",MyApplication.getInstance().getUser().getId());


        OkHttpHelper.getInstance().get(Contants.API.ADDRESS_LIST, params, new SpotsCallBack<List<Address>>(this) {

            @Override
            public void onSuccess(Response response, List<Address> addresses) {
                Log.d("aaaaaaa", "success"+response.toString());

                showAddress(addresses);
            }

            @Override
            public void onError(Response response, int code, Exception e) {
            }
        });

    }
    private void showAddress(List<Address> data){

        Collections.sort(data);
        if (adapter==null) {

            adapter = new AddressAdapter(data, this, new AddressAdapter.AddressListener() {
                @Override
                public void chooseDefault(Address address) {
                    updateDefaultAddress(address);
                }

                @Override
                public void deleteAddress(Address address) {
                    showDeleteDialog(address);
                }

                @Override
                public void updateAddress(Address address) {
                    toEditAddress(address);
                }


            });

            mRecyclerView.setAdapter(adapter);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        }else{
            adapter.refreshData(data);
            mRecyclerView.setAdapter(adapter);
        }

    }

    private void toEditAddress(Address address){
        Intent intent = new Intent(this,AddAddressActivity.class);
        intent.putExtra("address",address);
        startActivityForResult(intent,Contants.REQUEST_CODE);
    }

    private void toAddAddressActivity(){
        Intent intent = new Intent(this,AddAddressActivity.class);
        startActivityForResult(intent, Contants.REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == Contants.REQUEST_CODE){
            initAddresses();
        }
    }


    private void updateDefaultAddress(Address address){
        Map<String,Object> params = new HashMap<>(6);
        params.put("id",address.getId());
        params.put("consignee",address.getConsignee());
        params.put("phone",address.getPhone());
        params.put("addr",address.getAddr());
        params.put("zip_code",address.getZipCode());
        params.put("is_default",address.getIsDefault());

        OkHttpHelper.getInstance().post(Contants.API.ADDRESS_UPDATE, params, new SpotsCallBack<BaseResMsg>(this) {
            @Override
            public void onSuccess(Response response, BaseResMsg o) {
                initAddresses();
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }
    private void showDeleteDialog(final Address address){
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage("确定要删除该地址吗？");
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deleteAddress(address);
            }
        });
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        dialog.show();

    }
    private void deleteAddress(Address address){
        Map<String,Object> params = new HashMap<>(1);
        params.put("id",address.getId());
        OkHttpHelper.getInstance().post(Contants.API.ADDRESS_DELETE, params, new SpotsCallBack<BaseResMsg>(this) {
            @Override
            public void onSuccess(Response response, BaseResMsg o) {
                initAddresses();
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                Log.d("bbbbbbb", response.toString());
            }
        });
    }
}
