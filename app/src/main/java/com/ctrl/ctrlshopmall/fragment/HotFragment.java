package com.ctrl.ctrlshopmall.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.cjj.MaterialRefreshLayout;
import com.ctrl.ctrlshopmall.R;
import com.ctrl.ctrlshopmall.WareDetailActiviy;
import com.ctrl.ctrlshopmall.adapter.BaseAdapter;
import com.ctrl.ctrlshopmall.adapter.decoration.DividerItemDecoration;
import com.ctrl.ctrlshopmall.adapter.HotWareAdapter;
import com.ctrl.ctrlshopmall.bean.Page;
import com.ctrl.ctrlshopmall.bean.Ware;
import com.ctrl.ctrlshopmall.utils.Contants;
import com.ctrl.ctrlshopmall.utils.PageUtil;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.view.annotation.ViewInject;
import java.util.List;

/**
 * 热卖商品
 * Created by ctrlc on 2017/11/7.
 */

public class HotFragment extends BaseFragment implements PageUtil.OnPagerListener<Ware>{


    @ViewInject(R.id.hot_recycler_view)
    private RecyclerView mRecyclerView;

    @ViewInject(R.id.refresh_view)
    private MaterialRefreshLayout mRefreshLayout;

    private HotWareAdapter adapter;


    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_hot,container,false);
    }

    @Override
    public void init() {

        PageUtil pageUtil = PageUtil.newBulder().
                setUrl(Contants.API.WARES_HOT).
                setmRefreshLayout(mRefreshLayout).
                setPageSize(10).
                setLoadMore(true).
                setmOnPagerListener(this).build(getContext(),new TypeToken<Page<Ware>>(){}.getType());
        pageUtil.request();

    }

    @Override
    public void load(final List<Ware> datas, int totalPage, int totalCount) {
        adapter = new HotWareAdapter(datas,R.layout.template_hot_wares,getContext());
        mRecyclerView.setAdapter(adapter);
        adapter.setOnitemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(getActivity(), WareDetailActiviy.class);

                intent.putExtra(Contants.WARE,datas.get(position));
                startActivity(intent);
            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));
    }

    @Override
    public void loadMore(List<Ware> datas, int totalPage, int totalCount) {
        adapter.addData(adapter.getDatas().size(),datas);
        mRecyclerView.scrollToPosition(adapter.getDatas().size());
    }

    @Override
    public void refresh(List<Ware> datas, int totalPage, int totalCount) {
        adapter.clear();
        adapter.addData(datas);


        mRecyclerView.scrollToPosition(0);
    }
}
