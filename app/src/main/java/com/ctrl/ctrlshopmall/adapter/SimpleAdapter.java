package com.ctrl.ctrlshopmall.adapter;

import android.content.Context;

import java.util.List;

/**
 * Created by ctrlc on 2017/11/13.
 */

public abstract class SimpleAdapter<T> extends BaseAdapter<T, BaseViewHolder> {
    public SimpleAdapter(List<T> datas, int layoutId, Context context) {
        super(datas, layoutId, context);
    }
}
