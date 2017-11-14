package com.ctrl.ctrlshopmall.adapter;

import android.content.Context;

import com.ctrl.ctrlshopmall.R;
import com.ctrl.ctrlshopmall.bean.Category;

import java.util.List;

/**
 * Created by ctrlc on 2017/11/13.
 */

public class CategoryAdapter extends SimpleAdapter<Category> {
    public CategoryAdapter(List<Category> datas, Context context) {
        super(datas, R.layout.template_single_text, context);
    }

    @Override
    public void bindData(BaseViewHolder holder, Category item) {
        holder.getTextView(R.id.textView).setText(item.getName());
    }
}
