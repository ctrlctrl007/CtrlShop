package com.ctrl.ctrlshopmall;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.ctrl.ctrlshopmall.bean.Tab;
import com.ctrl.ctrlshopmall.fragment.CartFragment;
import com.ctrl.ctrlshopmall.fragment.CategoryFragment;
import com.ctrl.ctrlshopmall.fragment.HomeFragment;
import com.ctrl.ctrlshopmall.fragment.HotFragment;
import com.ctrl.ctrlshopmall.fragment.MineFragment;
import com.ctrl.ctrlshopmall.widget.FragmentTabHost;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FragmentTabHost mTabHost;
    private LayoutInflater mInflater;

    private List<Tab> tabList = new ArrayList<>(5);

    private CartFragment cartFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        intiTab();
    }

    private void intiTab(){
        Tab home = new Tab(R.string.home,R.drawable.selector_icon_home,HomeFragment.class);
        Tab cart = new Tab(R.string.cart,R.drawable.selector_icon_cart,CartFragment.class);
        Tab category = new Tab(R.string.catagory,R.drawable.selector_icon_category,CategoryFragment.class);
        Tab hot = new Tab(R.string.hot,R.drawable.selector_icon_hot,HotFragment.class);
        Tab mine = new Tab(R.string.mine,R.drawable.selector_icon_mine,MineFragment.class);

        tabList.add(home);
        tabList.add(category);
        tabList.add(hot);
        tabList.add(cart);
        tabList.add(mine);

        mInflater  = LayoutInflater.from(this);
        mTabHost = (FragmentTabHost) this.findViewById(android.R.id.tabhost);
        mTabHost.setup(this,getSupportFragmentManager(),R.id.realtabcontent);
        for(Tab tab: tabList){
            TabHost.TabSpec tabSpec = mTabHost.newTabSpec(getString(tab.getTitle()));

            tabSpec.setIndicator(biuldIndicator(tab));
            mTabHost.addTab(tabSpec, tab.getFragment(),null);
        }

        mTabHost.getTabWidget().setShowDividers(LinearLayout.SHOW_DIVIDER_NONE);
        mTabHost.setCurrentTab(0);
        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String s) {
                if(s.equals(getString(R.string.cart))){
                    refreshCart();
                }
            }
        });

    }
    private View biuldIndicator(Tab tab){
        View view = mInflater.inflate(R.layout.tab_indicator,null);
        ImageView img = (ImageView) view.findViewById(R.id.icon_tab);
        TextView txt = (TextView) view.findViewById(R.id.txt_indicator);
        img.setImageResource(tab.getIcon());
        txt.setText(tab.getTitle());
        return view;
    }
    private void refreshCart(){
        if (cartFragment==null){
            cartFragment = (CartFragment) getSupportFragmentManager().findFragmentByTag(getString(R.string.cart));
            if (cartFragment!=null){
                cartFragment.refreshData();
            }

        }else{
            cartFragment.refreshData();
        }
    }

}
