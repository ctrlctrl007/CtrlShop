package com.ctrl.ctrlshopmall.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.ctrl.ctrlshopmall.R;
import com.ctrl.ctrlshopmall.bean.ShoppingCart;
import com.ctrl.ctrlshopmall.http.FrescoHelper;
import com.ctrl.ctrlshopmall.http.ShoppingCartUtil;
import com.ctrl.ctrlshopmall.utils.ToastUtils;
import com.ctrl.ctrlshopmall.widget.NumberAddSubView;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.Iterator;
import java.util.List;

/**
 * Created by ctrlc on 2017/11/15.
 */

public class CartAdapter extends SimpleAdapter<ShoppingCart> implements BaseAdapter.OnItemClickListener{
    private ShoppingCartUtil shoppingCartUtil;

    private TextView totalTxt;

    private CheckBox mCheckBox;
    public CartAdapter(List<ShoppingCart> datas, Context context, TextView totalTxt, final CheckBox checkBox) {
        super(datas, R.layout.template_cart, context);
        this.totalTxt = totalTxt;
        mCheckBox = checkBox;
        shoppingCartUtil = new ShoppingCartUtil(context);
        showTotalPrice();
        setOnitemClickListener(this);
        isAllchecked();
        mCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAllOrNone(checkBox.isChecked());
                showTotalPrice();
            }
        });
    }

    @Override
    public void bindData(BaseViewHolder holder, final ShoppingCart item) {
        CheckBox checkBox = (CheckBox) holder.getView(R.id.checkbox);
        checkBox.setChecked(item.isChecked());
        SimpleDraweeView simpleDraweeView = (SimpleDraweeView) holder.getView(R.id.drawee_view);
        FrescoHelper.getInstance().setImageUrlByGradual(simpleDraweeView,item.getImgUrl());
        holder.getTextView(R.id.text_title).setText(item.getName());
        holder.getTextView(R.id.text_price).setText(item.getPrice().toString());
        NumberAddSubView addSubView = (NumberAddSubView) holder.getView(R.id.num_control);
        addSubView.setValue(item.getCount());
        addSubView.setOnButtonClickListener(new NumberAddSubView.OnButtonClickListener() {
            @Override
            public void onButtonAddClick(View view, int value) {
                item.setCount(value);
                shoppingCartUtil.update(item);
                showTotalPrice();
            }

            @Override
            public void onButtonSubClick(View view, int value) {
                item.setCount(value);
                shoppingCartUtil.update(item);
                showTotalPrice();
            }
        });
    }
    public void showTotalPrice(){
        datas = shoppingCartUtil.getAll();
        Float totalPrice = new Float(0);
        if(datas!=null && datas.size()>0){
            for (ShoppingCart cart : datas){
                if(cart.isChecked()) {
                    totalPrice = totalPrice + cart.getPrice() * cart.getCount();
                }
            }
        }
        totalTxt.setText(totalPrice.toString());
    }

    @Override
    public void onClick(View view, int position) {
        ShoppingCart cart = datas.get(position);
        cart.setChecked(!cart.isChecked());
        shoppingCartUtil.update(cart);
        notifyItemChanged(position);
        isAllchecked();
        showTotalPrice();
    }
    public void isAllchecked(){
       // datas = shoppingCartUtil.getAll();
        int count = 0;
        int checkSize = 0;
        if(datas!=null&datas.size()>0){
            count = datas.size();
            for(ShoppingCart cart : datas){
                if (!cart.isChecked()){
                    mCheckBox.setChecked(false);
                }else{
                    checkSize = checkSize+1;

                }
            }
            if(count==checkSize){
                mCheckBox.setChecked(true);
            }
        }
    }
    public void checkAllOrNone(boolean checked){
        //datas = shoppingCartUtil.getAll();
        if(datas!=null&datas.size()>0) {
            int i = 0;
            for (ShoppingCart cart : datas) {
                cart.setChecked(checked);
                notifyItemChanged(i);
                shoppingCartUtil.update(cart);
                i++;
            }
        }

    }
    public void delet(){
        if(datas!=null&datas.size()>0) {
            Iterator<ShoppingCart> iterator = datas.iterator();
            while (iterator.hasNext()){
                ShoppingCart cart = iterator.next();
                if(cart.isChecked()){
                     int position = datas.indexOf(cart);
                     shoppingCartUtil.delete(cart);
                     iterator.remove();
                     notifyItemRemoved(position);

                }

            }

          /*  for (Iterator iterator = datas.iterator();iterator.hasNext();){*/
          /*      ShoppingCart cart = (ShoppingCart) iterator.next();*/
          /*      if (cart.isChecked()){*/
          /*          int position = datas.indexOf(cart);*/
          /*          shoppingCartUtil.delete(cart);*/
          /*          datas.remove(cart);*/
          /*          notifyItemRemoved(position);*/
          /*      }*/
          /*  }*/
        }
    }
}
