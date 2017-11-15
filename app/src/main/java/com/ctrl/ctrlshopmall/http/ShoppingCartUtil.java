package com.ctrl.ctrlshopmall.http;

import android.content.Context;
import android.util.SparseArray;

import com.ctrl.ctrlshopmall.bean.ShoppingCart;

import java.util.List;

/**
 * Created by ctrlc on 2017/11/14.
 */

public class ShoppingCartUtil {

    private static final String CART_JSON = "cart_json";
    private Context mContext;
    private SparseArray<ShoppingCart> datas;

    public ShoppingCartUtil( Context mContext) {

        this.mContext = mContext;
    }



}
