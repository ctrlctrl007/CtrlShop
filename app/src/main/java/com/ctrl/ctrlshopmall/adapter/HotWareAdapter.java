package com.ctrl.ctrlshopmall.adapter;

import android.content.Context;

import com.ctrl.ctrlshopmall.R;
import com.ctrl.ctrlshopmall.adapter.BaseViewHolder;
import com.ctrl.ctrlshopmall.adapter.SimpleAdapter;
import com.ctrl.ctrlshopmall.bean.Ware;
import com.ctrl.ctrlshopmall.http.FrescoHelper;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * Created by ctrlc on 2017/11/13.
 */

public class HotWareAdapter extends SimpleAdapter<Ware> {

    public HotWareAdapter(List<Ware> datas,int layoutId, Context context) {
        super(datas, layoutId, context);
    }

    @Override
    public void bindData(BaseViewHolder holder, Ware item) {
        SimpleDraweeView image = (SimpleDraweeView) holder.getView(R.id.drawee_view);
        FrescoHelper.getInstance().setImageUrlByGradual(image,item.getImgUrl());
        holder.getTextView(R.id.text_title).setText(item.getName());
        holder.getTextView(R.id.text_price).setText("￥"+item.getPrice());
    }
}
