package com.ctrl.ctrlshopmall.adapter;

import android.content.Context;
import android.view.View;
import android.widget.Button;

import com.ctrl.ctrlshopmall.R;
import com.ctrl.ctrlshopmall.bean.Wares;
import com.ctrl.ctrlshopmall.http.FrescoHelper;
import com.ctrl.ctrlshopmall.http.ShoppingCartUtil;
import com.ctrl.ctrlshopmall.utils.ToastUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * Created by ctrlc on 2017/11/13.
 */

public class HotWareAdapter extends SimpleAdapter<Wares> {

    ShoppingCartUtil cartUtil;

    public HotWareAdapter(List<Wares> datas, int layoutId, Context context) {
        super(datas, layoutId, context);
        cartUtil = new ShoppingCartUtil(context);
    }

    @Override
    public void bindData(BaseViewHolder holder, final Wares item) {
        SimpleDraweeView image = (SimpleDraweeView) holder.getView(R.id.drawee_view);
        FrescoHelper.getInstance().setImageUrlByGradual(image,item.getImgUrl());
        holder.getTextView(R.id.text_title).setText(item.getName());
        holder.getTextView(R.id.text_price).setText("￥"+item.getPrice());
        Button add = holder.getButton(R.id.btn_add);
        if(add!=null){
            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cartUtil.put(item);
                    ToastUtils.show(context,"添加到购物车");
                }
            });
        }
    }
    public void resetLayout(int layoutId){
        this.layoutId = layoutId;
        notifyItemRangeChanged(0,getDatas().size());
    }
}
