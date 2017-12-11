package com.ctrl.ctrlshopmall;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ctrl.ctrlshopmall.utils.Contants;
import com.ctrl.ctrlshopmall.widget.CNiaoToolBar;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class AddressListActivity extends AppCompatActivity {

    @ViewInject(R.id.tool_bar)
    private CNiaoToolBar mToolBar;

    @ViewInject(R.id.recycler_view)
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_list);
        ViewUtils.inject(this);
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
        mToolBar.setRightButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toAddAddressActivity();
            }
        });
    }

    private void initRecyclerView(){

    }

    private void toAddAddressActivity(){
        Intent intent = new Intent(this,AddAddressActivity.class);
        startActivityForResult(intent, Contants.REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == Contants.REQUEST_CODE){
            initRecyclerView();
        }
    }
}
