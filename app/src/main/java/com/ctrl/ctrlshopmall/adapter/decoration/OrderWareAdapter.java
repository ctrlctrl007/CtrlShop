package com.ctrl.ctrlshopmall.adapter.decoration;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.ctrl.ctrlshopmall.R;
import com.ctrl.ctrlshopmall.adapter.BaseViewHolder;
import com.ctrl.ctrlshopmall.adapter.SimpleAdapter;
import com.ctrl.ctrlshopmall.bean.ShoppingCart;
import com.ctrl.ctrlshopmall.http.FrescoHelper;
import com.ctrl.ctrlshopmall.http.ShoppingCartUtil;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * Created by ctrlc on 2017/12/3.
 */

public class OrderWareAdapter extends SimpleAdapter<ShoppingCart> {

    //private ShoppingCartUtil shoppingCartUtil;
    public OrderWareAdapter(List<ShoppingCart> datas, Context context) {
        super(datas, R.layout.template_order_wares, context);
        //shoppingCartUtil = new ShoppingCartUtil(context);
    }

    @Override
    public void bindData(BaseViewHolder holder, ShoppingCart item) {
        SimpleDraweeView image = (SimpleDraweeView) holder.getView(R.id.drawee_view);
        FrescoHelper.getInstance().setImageUrlByGradual(image,item.getImgUrl());
    }
}
