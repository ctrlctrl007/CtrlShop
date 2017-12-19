package com.ctrl.ctrlshopmall.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.ctrl.ctrlshopmall.R;
import com.ctrl.ctrlshopmall.WareDetailActiviy;
import com.ctrl.ctrlshopmall.adapter.BaseAdapter;
import com.ctrl.ctrlshopmall.adapter.CategoryAdapter;
import com.ctrl.ctrlshopmall.adapter.HotWareAdapter;
import com.ctrl.ctrlshopmall.adapter.decoration.DividerItemDecoration;
import com.ctrl.ctrlshopmall.bean.Banner;
import com.ctrl.ctrlshopmall.bean.Category;
import com.ctrl.ctrlshopmall.bean.Page;
import com.ctrl.ctrlshopmall.bean.Wares;
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
 * 商品分类及分类商品显示碎片
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

    private long categoryId = 0;

    private int currPage=1;
    private int totalPage=1;
    private int pageSize=10;


    private  static final int STATE_NORMAL=0;
    private  static final int STATE_REFREH=1;
    private  static final int STATE_MORE=2;

    private HotWareAdapter hotWareAdapter;

    private int state=STATE_NORMAL;

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
        initFreshLayout();

    }

    private void initFreshLayout(){

        refreshLayout.setLoadMore(true);
        refreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                refresh();
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                super.onRefreshLoadMore(materialRefreshLayout);
                if(currPage<=totalPage){
                    loadMore();
                }else{
                    Toast.makeText(getContext(),"没有更多数据了",Toast.LENGTH_SHORT).show();
                    refreshLayout.finishRefreshLoadMore();
                }

            }
        });

    }
    private void refresh(){
        currPage = 1;
        state = STATE_REFREH;
        requestWares();

    }
    private void loadMore(){
        currPage = currPage+1;
        state = STATE_MORE;
        requestWares();
    }

    private void requestBanners(){
        Map<String,Object> params = new HashMap<>();
        params.put("type",1);

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
                if(categories!=null&&categories.size()>0){
                    categoryId = categories.get(0).getId();
                    requestWares();
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }
    public void showCategory(final List<Category> categories){
        CategoryAdapter adapter = new CategoryAdapter(categories,getContext());
        adapter.setOnitemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                Category category = categories.get(position);
                categoryId = category.getId();
                currPage = 1;
                state = STATE_NORMAL;
                requestWares();

            }
        });
        categoryRecyclerView.setAdapter(adapter);
        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        categoryRecyclerView.setItemAnimator(new DefaultItemAnimator());
        categoryRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL_LIST));
    }
    private void requestWares(){
        Map<String,Object> params = new HashMap<>();
        params.put("categoryId",String.valueOf(categoryId));
        params.put("curPage",String.valueOf(currPage));
        params.put("pageSize",String.valueOf(pageSize));
        helper.post(Contants.API.WARES_LIST, params, new SpotsCallBack<Page<Wares>>(getContext()) {



            @Override
            public void onSuccess(Response response, Page<Wares> warePage) {
                List<Wares> wares = warePage.getList();
                showCategoryWare(wares);
                totalPage = warePage.getTotalCount()/pageSize+1;
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });


    }
    private void showCategoryWare(final List<Wares> wares){
        switch (state){
            case STATE_NORMAL:
                if(hotWareAdapter==null){
                hotWareAdapter = new HotWareAdapter(wares,R.layout.template_grid_wares,getContext());
                goodsRecyclerView.setAdapter(hotWareAdapter);
                hotWareAdapter.setOnitemClickListener(new BaseAdapter.OnItemClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        Intent intent = new Intent(getActivity(), WareDetailActiviy.class);
                        intent.putExtra(Contants.WARE,wares.get(position));
                        startActivity(intent);
                    }
                });
                goodsRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
                goodsRecyclerView.setItemAnimator(new DefaultItemAnimator());}
                else{
                    hotWareAdapter.clear();
                    hotWareAdapter.addData(wares);
                    goodsRecyclerView.scrollToPosition(0);

                }
                break;
            case STATE_REFREH:
                hotWareAdapter.clear();
                hotWareAdapter.addData(wares);

                goodsRecyclerView.scrollToPosition(0);
                refreshLayout.finishRefresh();
                break;
            case STATE_MORE:
                hotWareAdapter.addData(hotWareAdapter.getDatas().size(),wares);

                goodsRecyclerView.scrollToPosition(hotWareAdapter.getDatas().size());

                refreshLayout.finishRefreshLoadMore();
        }

    }
}
