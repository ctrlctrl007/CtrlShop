package com.ctrl.ctrlshopmall;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.cjj.MaterialRefreshLayout;
import com.ctrl.ctrlshopmall.adapter.BaseAdapter;
import com.ctrl.ctrlshopmall.adapter.HotWareAdapter;
import com.ctrl.ctrlshopmall.adapter.decoration.DividerItemDecoration;
import com.ctrl.ctrlshopmall.bean.Page;
import com.ctrl.ctrlshopmall.bean.Ware;
import com.ctrl.ctrlshopmall.utils.Contants;
import com.ctrl.ctrlshopmall.utils.PageUtil;
import com.ctrl.ctrlshopmall.widget.CNiaoToolBar;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;

public class WareListActivity extends BaseActivity implements PageUtil.OnPagerListener<Ware>, TabLayout.OnTabSelectedListener,View.OnClickListener{
    private static final int TAB_DEFAULT = 0;
    private static final int TAB_PRICE = 2;
    private static final int TAB_SALED = 1;

    public static final int ACTION_LIST=1;
    public static final int ACTION_GIRD=2;

    private long campaignId = 0;
    private int orderBy = 0;

    private HotWareAdapter adapter;

    @ViewInject(R.id.tool_bar)
    private CNiaoToolBar toolBar;

    @ViewInject(R.id.tab_layout)
    private TabLayout mTabLayout;

    @ViewInject(R.id.txt_summary)
    private TextView summaryTxt;

    @ViewInject(R.id.refresh_layout)
    private MaterialRefreshLayout mRefreshLayout;

    @ViewInject(R.id.recycler_view)
    private RecyclerView mRecyclerView;

    private PageUtil pageUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ware_list);
        ViewUtils.inject(this);

        campaignId = getIntent().getLongExtra(Contants.COMPAINGAIN_ID,0);
        initTab();
        getData();
        initToolBar();
    }
    private void getData(){
        pageUtil =PageUtil.newBulder()
                .setLoadMore(true)
                .setUrl(Contants.API.WARES_CAMPAIN_LIST)
                .putParam("campaignId",campaignId)
                .putParam("orderBy",orderBy)
                .putParam("orderBy",orderBy)
                .setmOnPagerListener(this)
                .setmRefreshLayout(mRefreshLayout)
                .build(this,new TypeToken<Page<Ware>>(){}.getType());
        pageUtil.request();
    }

    private void initToolBar(){
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WareListActivity.this.finish();
            }
        });

        toolBar.setRightButtonIcon(R.drawable.icon_grid_32);
        toolBar.getRightButton().setTag(ACTION_LIST);
        toolBar.setRightButtonOnClickListener(this);
    }
    private void initTab(){
        TabLayout.Tab tab= mTabLayout.newTab();
        tab.setText("默认");
        tab.setTag(TAB_DEFAULT);

        mTabLayout.addTab(tab);



        tab= mTabLayout.newTab();
        tab.setText("价格");
        tab.setTag(TAB_PRICE);

        mTabLayout.addTab(tab);

        tab= mTabLayout.newTab();
        tab.setText("销量");
        tab.setTag(TAB_SALED);

        mTabLayout.addTab(tab);
        mTabLayout.setOnTabSelectedListener(this);
    }

    @Override
    public void load(final List<Ware> datas, int totalPage, int totalCount) {
        summaryTxt.setText("共有"+totalCount+"件商品");
        adapter = new HotWareAdapter(datas,R.layout.template_hot_wares,this);
        adapter.setOnitemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(WareListActivity.this,WareDetailActiviy.class);

                intent.putExtra(Contants.WARE,datas.get(position));
                startActivity(intent);
            }
        });
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));

    }

    @Override
    public void loadMore(List<Ware> datas, int totalPage, int totalCount) {
        adapter.loadMoreData(datas);
        mRecyclerView.scrollToPosition(adapter.getDatas().size());

    }

    @Override
    public void refresh(List<Ware> datas, int totalPage, int totalCount) {
        adapter.refreshData(datas);
        mRecyclerView.scrollToPosition(0);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        orderBy = (int)tab.getTag();
        pageUtil.putParam("orderBy",orderBy);
        pageUtil.request();

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onClick(View view) {
        int action = (int)view.getTag();
        if(ACTION_LIST == action){
            toolBar.setRightButtonIcon(R.drawable.icon_list_32);
            toolBar.getRightButton().setTag(ACTION_GIRD);

            adapter.resetLayout(R.layout.template_grid_wares);

            mRecyclerView.setLayoutManager(new GridLayoutManager(this,2));
        }else if(ACTION_GIRD == action){
            toolBar.setRightButtonIcon(R.drawable.icon_grid_32);
            toolBar.getRightButton().setTag(ACTION_LIST);

            adapter.resetLayout(R.layout.template_hot_wares);

            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        }
    }
}
