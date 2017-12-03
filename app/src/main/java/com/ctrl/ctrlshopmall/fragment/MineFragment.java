package com.ctrl.ctrlshopmall.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ctrl.ctrlshopmall.LoginActivity;
import com.ctrl.ctrlshopmall.MyApplication;
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


    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mine,container,false);
    }

    @Override
    public void init() {
        User user = MyApplication.getInstance().getUser();
        showUser(user);

    }

    @OnClick(value = {R.id.img_head,R.id.txt_username})
    public void toLoginActivity(View view){


        Intent intent = new Intent(getActivity(), LoginActivity.class);

        startActivityForResult(intent, Contants.REQUEST_CODE);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        User user = MyApplication.getInstance().getUser();
        showUser(user);
    }
    private void showUser(User user){
        if (user!=null){
            userNameTxt.setText(user.getUsername());
            Picasso.with(getContext()).load(user.getLogo_url()).into(mLogoImageView);
           // Log.d("myimage",user.getLogo_url());
        }else{
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
