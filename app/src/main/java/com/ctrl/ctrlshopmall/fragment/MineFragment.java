package com.ctrl.ctrlshopmall.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ctrl.ctrlshopmall.AddressListActivity;
import com.ctrl.ctrlshopmall.LoginActivity;
import com.ctrl.ctrlshopmall.MyApplication;
import com.ctrl.ctrlshopmall.MyOderActivity;
import com.ctrl.ctrlshopmall.R;
import com.ctrl.ctrlshopmall.bean.User;
import com.ctrl.ctrlshopmall.utils.Contants;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ctrlc on 2017/11/7.
 */

public class MineFragment extends BaseFragment {
    @ViewInject(R.id.img_head)
    private CircleImageView mLogoImageView;

    @ViewInject(R.id.txt_username)
    private TextView userNameTxt;

    @ViewInject(R.id.btn_logout)
    private Button logoutButton;

    private User user;


    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mine,container,false);
    }

    @Override
    public void init() {
        user = MyApplication.getInstance().getUser();
        showUser(user);

    }
    @OnClick(R.id.txt_my_address)
    private void toAddressList(View view){
        Intent intent = new Intent(getActivity(), AddressListActivity.class);
        startActivity(intent,true);
    }

    @OnClick(value = {R.id.img_head,R.id.txt_username})
    private void toLoginActivity(View view){

        if (user==null) {
            Intent intent = new Intent(getActivity(), LoginActivity.class);

            startActivityForResult(intent, Contants.REQUEST_CODE);
        }

    }
    @OnClick(R.id.txt_my_orders)
    private void toMyOrderActivity(View view){
        Intent intent = new Intent(getActivity(), MyOderActivity.class);
        startActivity(intent,true);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        User user = MyApplication.getInstance().getUser();
        showUser(user);
    }
    private void showUser(User user){
        if (user!=null){
            logoutButton.setVisibility(View.VISIBLE);
            userNameTxt.setText(user.getUsername());
            if(!TextUtils.isEmpty(user.getLogo_url())) {
                Picasso.with(getContext()).load(user.getLogo_url()).into(mLogoImageView);
            }
        }else{
            logoutButton.setVisibility(View.GONE);
            userNameTxt.setText("点击登陆");
            mLogoImageView.setImageResource(R.drawable.default_head);
        }
    }

    /**
     * 退出登陆
     */
    @OnClick(R.id.btn_logout)
    private void logOut(View view){
        MyApplication.getInstance().clearUser();
        showUser(null);

    }
}
