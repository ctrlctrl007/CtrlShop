package com.ctrl.ctrlshopmall.fragment;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.ctrl.ctrlshopmall.R;
import com.ctrl.ctrlshopmall.adapter.decoration.DividerItemDecoration;
import com.ctrl.ctrlshopmall.adapter.HotWareAdapter;
import com.ctrl.ctrlshopmall.bean.Page;
import com.ctrl.ctrlshopmall.bean.Ware;
import com.ctrl.ctrlshopmall.http.OkHttpHelper;
import com.ctrl.ctrlshopmall.http.SpotsCallBack;
import com.ctrl.ctrlshopmall.utils.Contants;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.squareup.okhttp.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 热卖商品
 * Created by ctrlc on 2017/11/7.
 */

public class HotFragment extends BaseFragment {


    @ViewInject(R.id.hot_recycler_view)
    private RecyclerView mRecyclerView;

    @ViewInject(R.id.refresh_view)
    private MaterialRefreshLayout mRefreshLayout;
    private List<Ware> wares;

    private OkHttpHelper httpHelper = OkHttpHelper.getInstance();

    private int currentPage = 1;

    private int totalPage = 1;
    private int pageSize = 10;

    private HotWareAdapter adapter;
    private  static final int STATE_NORMAL=0;
    private  static final int STATE_REFRESH = 1;
    private  static final int STATE_MORE=2;

    private int state=STATE_NORMAL;

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_hot,container,false);
    }

    @Override
    public void init() {
        initData();
        mRefreshLayout.setLoadMore(true);
        mRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                refresh();
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {


                if(currentPage <=totalPage)
                    loadMore();
                else{
                    Toast.makeText(getContext(),"没有更多数据了",Toast.LENGTH_SHORT).show();
                    mRefreshLayout.finishRefreshLoadMore();
                    mRefreshLayout.setLoadMore(false);
                }
            }
        });
    }
    private void initData(){
        final Map<String,String> params = new HashMap<>();
        params.put("curPage",String.valueOf(currentPage));
        params.put("pageSize",String.valueOf(pageSize));

        httpHelper.post(Contants.API.WARES_HOT,params, new SpotsCallBack<Page<Ware>>(getContext()) {


            @Override
            public void onSuccess(Response response, Page<Ware> warePage) {
                wares = warePage.getList();
                currentPage = warePage.getCurrentPage();
                totalPage = warePage.getTotalCount()/pageSize+1;
                initReycler();

            }


            @Override
            public void onError(Response response, int code, Exception e) {

                    Toast.makeText(getContext(),"加载失败",Toast.LENGTH_LONG).show();
            }
        });

    }
    private void initReycler(){


        switch (state) {

            case STATE_NORMAL:
                adapter = new HotWareAdapter(wares,R.layout.template_hot_wares,getContext());

                mRecyclerView.setAdapter(adapter);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));

                break;

            case STATE_MORE:
                adapter.addData(adapter.getDatas().size(),wares);
                mRecyclerView.scrollToPosition(adapter.getDatas().size());
                mRefreshLayout.finishRefreshLoadMore();

                break;

            case STATE_REFRESH:
                adapter.clear();
                adapter.addData(wares);


                mRecyclerView.scrollToPosition(0);
                mRefreshLayout.finishRefresh();
                break;


        }



    }

    private void refresh(){
        currentPage = 1;
        state = STATE_REFRESH;
        initData();
    }

    private void loadMore(){
        currentPage = ++currentPage;
        state = STATE_MORE;

        initData();
    }
}
