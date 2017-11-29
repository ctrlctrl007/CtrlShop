package com.ctrl.ctrlshopmall.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ctrl.ctrlshopmall.R;
import com.ctrl.ctrlshopmall.WareListActivity;
import com.ctrl.ctrlshopmall.adapter.HomeCategoryAdapter;
import com.ctrl.ctrlshopmall.adapter.decoration.CardViewtemDecortion;
import com.ctrl.ctrlshopmall.bean.Banner;

import com.ctrl.ctrlshopmall.bean.Campaign;
import com.ctrl.ctrlshopmall.bean.HomeCampaign;
import com.ctrl.ctrlshopmall.http.BaseCallBack;
import com.ctrl.ctrlshopmall.http.OkHttpHelper;
import com.ctrl.ctrlshopmall.http.SpotsCallBack;
import com.ctrl.ctrlshopmall.utils.Contants;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.TextSliderView;


import com.lidroid.xutils.view.annotation.ViewInject;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 主页碎片
 * Created by ctrlc on 2017/11/7.
 */

public class HomeFragment extends BaseFragment {

    @ViewInject(R.id.slider)
    private SliderLayout mSliderLayout;

    @ViewInject(R.id.recycler_view)
    private RecyclerView mRecyclerView;

    private List<Banner> mBannerList;

    private OkHttpHelper mHttpHelper;

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home,container,false);
    }

    @Override
    public void init() {

        requestBanner();
        initRecyclerView();

    }
    private void initRecyclerView(){
        mHttpHelper.get(Contants.API.CAMPAIGN_HOME, new BaseCallBack<List<HomeCampaign>>() {
            @Override
            public void onBeforeRequest(Request request) {

            }

            @Override
            public void onFailure(Request request, Exception e) {

            }

            @Override
            public void onResponse(Response response) {

            }

            @Override
            public void onSuccess(Response response, List<HomeCampaign> homeCampaigns) {
                initData(homeCampaigns);
            }


            @Override
            public void onError(Response response, int code, Exception e) {

            }

            @Override
            public void onTokenError(Response response, int code) {

            }
        });

    }
    private void initData(List<HomeCampaign> campaigns){
        HomeCategoryAdapter adapter = new HomeCategoryAdapter(campaigns, getContext());
        adapter.setHomeCampaignsListener(new HomeCategoryAdapter.OnHomeCategoryOnClickListener() {
            @Override
            public void onClick(View view, Campaign campaign) {
                Toast.makeText(getContext(),campaign.getTitle(),Toast.LENGTH_SHORT).show();
            }
        });
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addItemDecoration(new CardViewtemDecortion());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        adapter.setHomeCampaignsListener(new HomeCategoryAdapter.OnHomeCategoryOnClickListener() {
            @Override
            public void onClick(View view, Campaign campaign) {
                Intent intent = new Intent(getActivity(), WareListActivity.class);
                intent.putExtra(Contants.COMPAINGAIN_ID,campaign.getId());

                startActivity(intent);
            }
        });
    }

    private void requestBanner(){
        mHttpHelper = OkHttpHelper.getInstance();
        Map<String,String> params = new HashMap<>();
        params.put("type","1");

        mHttpHelper.post(Contants.API.BANNER,params,new SpotsCallBack<List<Banner>>(getContext()) {

            @Override
            public void onSuccess(Response response, List<Banner> banners) {
                mBannerList = banners;
                initSlider();
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });

    }

    private void initSlider(){

        if(mBannerList!=null){
            for(int i = 0;i<mBannerList.size();i++){
                TextSliderView textSliderView = new TextSliderView(getActivity());
                textSliderView.description(mBannerList.get(i).getName()).image(mBannerList.get(i).getImgUrl());
                mSliderLayout.addSlider(textSliderView);
            }


        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSliderLayout.stopAutoCycle();
    }
}
