package com.ctrl.ctrlshopmall.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by ctrlc on 2017/11/13.
 */

public class BaseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    protected SparseArray<View> views;
    protected BaseAdapter.OnItemClickListener listener;

    public BaseViewHolder(View itemView, BaseAdapter.OnItemClickListener listener) {
        super(itemView);
        this.listener = listener;

        views = new SparseArray<>();
        itemView.setOnClickListener(this);
    }

    public View getView(int viewId){
        return retrieveView(viewId);
    }

    public TextView getTextView(int viewId){
        return retrieveView(viewId);
    }

    public Button getButton(int viewId){
        return retrieveView(viewId);
    }
    public ImageView getImageView(int viewId){
        return retrieveView(viewId);
    }

    public <T extends View> T retrieveView(int viewId){
        View view = views.get(viewId);
        if(view == null){
            view = itemView.findViewById(viewId);

            views.put(viewId,view);
        }
        return (T)view;


    }

    @Override
    public void onClick(View view) {
        if(listener!=null) {
            listener.onClick(view, getLayoutPosition());
        }
    }
}
