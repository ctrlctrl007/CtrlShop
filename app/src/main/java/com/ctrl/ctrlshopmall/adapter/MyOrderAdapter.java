package com.ctrl.ctrlshopmall.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ctrl.ctrlshopmall.R;
import com.ctrl.ctrlshopmall.bean.Order;
import com.ctrl.ctrlshopmall.bean.OrderItem;
import com.squareup.picasso.Picasso;
import com.w4lle.library.NineGridAdapter;
import com.w4lle.library.NineGridlayout;

import java.util.List;

/**
 * Created by ctrlc on 2017/12/12.
 */

public class MyOrderAdapter extends SimpleAdapter<Order> {
    public MyOrderAdapter(List<Order> datas, Context context) {
        super(datas, R.layout.template_my_orders, context);
    }

    @Override
    public void bindData(BaseViewHolder holder, Order item) {
        holder.getTextView(R.id.txt_order_num).setText("订单号: "+item.getOrderNum());
        holder.getTextView(R.id.txt_order_money).setText("实付金额： ￥："+item.getAmount());

        TextView statusTxt = holder.getTextView(R.id.txt_status);
        switch (item.getStatus()){
            case Order.STATUS_SUCCESS:
                statusTxt.setText("支付成功");
                break;
            case Order.STATUS_PAY_WAIT:
                statusTxt.setText("待支付");
                break;
            case Order.STATUS_PAY_FAIL:
                statusTxt.setText("支付失败");
                break;
        }
        NineGridlayout nineGridlayout = (NineGridlayout) holder.getView(R.id.iv_ngrid_layout);
        ItemAdapter adapter = new ItemAdapter(context,item.getItems());
        nineGridlayout.setGap(5);
        nineGridlayout.setDefaultHeight(50);
        nineGridlayout.setDefaultWidth(50);
        nineGridlayout.setAdapter(adapter);


    }

    class ItemAdapter extends NineGridAdapter{

        private List<OrderItem> items;
        public ItemAdapter(Context context, List<OrderItem> items) {
            super(context, items);
            this.items = items;
        }

        @Override
        public int getCount() {
            return (items == null) ? 0 : items.size();
        }

        @Override
        public String getUrl(int position) {
            OrderItem item = getItem(position);
            return item.getWares().getImgUrl();
        }

        @Override
        public OrderItem getItem(int position) {
            return (items == null) ? null : items.get(position);
        }

        @Override
        public long getItemId(int position) {
            OrderItem item = getItem(position);
            return item==null?0:item.getId();
        }

        @Override
        public View getView(int i, View view) {
            ImageView iv = new ImageView(context);
            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
            iv.setBackgroundColor(Color.parseColor("#f5f5f5"));
            Picasso.with(context).load(getUrl(i)).placeholder(new ColorDrawable(Color.parseColor("#f5f5f5"))).into(iv);
            return iv;
        }
    }
}
