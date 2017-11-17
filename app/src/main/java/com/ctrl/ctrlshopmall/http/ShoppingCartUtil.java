package com.ctrl.ctrlshopmall.http;

import android.content.Context;
import android.util.SparseArray;

import com.ctrl.ctrlshopmall.bean.ShoppingCart;
import com.ctrl.ctrlshopmall.bean.Ware;
import com.ctrl.ctrlshopmall.utils.JSONUtil;
import com.ctrl.ctrlshopmall.utils.PreferencesUtils;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ctrlc on 2017/11/14.
 */

public class ShoppingCartUtil {

    private static final String CART_JSON = "cart_json";
    private Context mContext;
    private SparseArray<ShoppingCart> datas;

    public ShoppingCartUtil( Context mContext) {
        datas = new SparseArray<>();
        this.mContext = mContext;
        listToSparse();
    }
    public void put(Ware ware){
        put(convertData(ware));
    }

    public void put(ShoppingCart shoppingCart){
       ShoppingCart item = datas.get(shoppingCart.getId().intValue());
        if (item != null){
            item.setCount(item.getCount()+1);
        }else{
            item = shoppingCart;
            item.setCount(1);
        }
        datas.put(item.getId().intValue(),item);
        commit();
    }
    public void update(ShoppingCart shoppingCart){
        datas.put(shoppingCart.getId().intValue(),shoppingCart);
        commit();

    }
    public void updateAll(List<ShoppingCart> datas){
        PreferencesUtils.putString(mContext,CART_JSON, JSONUtil.toJson(datas));
    }
    public List<ShoppingCart> getAll(){
        return getFromLocal();
    }
    public void delete(ShoppingCart cart){
        datas.delete(cart.getId().intValue());
        commit();
    }
    private void commit(){
        List<ShoppingCart> shoppingCarts = sparseToList(datas);
        PreferencesUtils.putString(mContext,CART_JSON, JSONUtil.toJson(shoppingCarts));
    }

    public ShoppingCart convertData(Ware item){

        ShoppingCart cart = new ShoppingCart();

        cart.setId(item.getId());
        cart.setDescription(item.getDescription());
        cart.setImgUrl(item.getImgUrl());
        cart.setName(item.getName());
        cart.setPrice(item.getPrice());

        return cart;
    }
    public List<ShoppingCart> sparseToList(SparseArray<ShoppingCart> datas){
        List<ShoppingCart> shoppingCarts = new ArrayList<>();
        for (int i=0;i<datas.size();i++){
            shoppingCarts.add(datas.valueAt(i));
        }
        return shoppingCarts;

    }
    public void listToSparse(){
        List<ShoppingCart> datas = getFromLocal();
        if (datas!=null&& datas.size()>0)
        for (ShoppingCart shoppingCart: datas){
            this.datas.put(shoppingCart.getId().intValue(),shoppingCart);
        }


    }
    public List<ShoppingCart> getFromLocal(){
        String json = PreferencesUtils.getString(mContext,CART_JSON);
        List<ShoppingCart> carts = null;
        if (json!=null) {
            carts = JSONUtil.fromGson(json, new TypeToken<List<ShoppingCart>>() {
            }.getType());
        }
        return carts;
    }


}
