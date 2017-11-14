package com.ctrl.ctrlshopmall.fragment;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cjj.MaterialRefreshLayout;
import com.ctrl.ctrlshopmall.R;
import com.ctrl.ctrlshopmall.adapter.CategoryAdapter;
import com.ctrl.ctrlshopmall.adapter.decoration.DividerItemDecoration;
import com.ctrl.ctrlshopmall.bean.Banner;
import com.ctrl.ctrlshopmall.bean.Category;
import com.ctrl.ctrlshopmall.http.OkHttpHelper;
import com.ctrl.ctrlshopmall.http.SpotsCallBack;
import com.ctrl.ctrlshopmall.utils.Contants;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.squareup.okhttp.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ctrlc on 2017/11/7.
 */

public class CategoryFragment extends BaseFragment {

    @ViewInject(R.id.category_recycler_view)
    private RecyclerView categoryRecyclerView;

    @ViewInject(R.id.category_goods_recycler_view)
    private RecyclerView goodsRecyclerView;

    @ViewInject(R.id.category_refresh)
    private MaterialRefreshLayout refreshLayout;

    @ViewInject(R.id.category_banner)
    private SliderLayout mSliderLayout;

    private int categoryId;



    private OkHttpHelper helper;
    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_category,container,false);
    }

    @Override
    public void init() {
        helper = OkHttpHelper.getInstance();
        requestCategoryDatas();
        requestBanners();

    }
    private void requestBanners(){
        Map<String,String> params = new HashMap<String,String>();
        params.put("type","1");

        helper.post(Contants.API.BANNER,params,new SpotsCallBack<List<Banner>>(getContext()) {

            @Override
            public void onSuccess(Response response, List<Banner> banners) {
                showBanners(banners);
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }
    private void showBanners(List<Banner> banners){
        if(banners!=null){
            for(int i = 0;i<banners.size();i++){
                TextSliderView textSliderView = new TextSliderView(getActivity());
                textSliderView.description(banners.get(i).getName()).image(banners.get(i).getImgUrl());
                mSliderLayout.addSlider(textSliderView);
            }


        }
    }
    private void requestCategoryDatas(){
        helper.get(Contants.API.CATEGORY_LIST, new SpotsCallBack<List<Category>>(getContext()) {


            @Override
            public void onSuccess(Response response, List<Category> categories) {
                showCategory(categories);
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }
    public void showCategory(List<Category> categories){
        CategoryAdapter adapter = new CategoryAdapter(categories,getContext());
        categoryRecyclerView.setAdapter(adapter);
        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        categoryRecyclerView.setItemAnimator(new DefaultItemAnimator());
        categoryRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL_LIST));
    }
}
