package com.ctrl.ctrlshopmall.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ctrl.ctrlshopmall.bean.Ware;

import java.util.List;

/**
 * Created by ctrlc on 2017/11/13.
 */

public abstract class BaseAdapter<T, H extends BaseViewHolder>  extends RecyclerView.Adapter<BaseViewHolder>{
    protected List<T> datas;
    protected final int layoutId;
    protected final Context context;
    protected OnItemClickListener listener;

    public BaseAdapter(List<T> datas,int layoutId,Context context){
        this.layoutId = layoutId;
        this.datas = datas;
        this.context = context;
    }


    public void setOnitemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }
    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutId,parent,false);


        return new BaseViewHolder(view,listener);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        T item = datas.get(position);
        bindData((H)holder,item);

    }

    public abstract void bindData(H holder,T item);

    @Override
    public int getItemCount() {
        if(datas!=null&&datas.size()>0){
            return datas.size();
        }
        return 0;
    }




    public void clear(){
        int itemCount = datas.size();
        datas.clear();
        this.notifyItemRangeRemoved(0,itemCount);
    }

    public List<T> getDatas(){

        return  datas;
    }
    public void addData(List<T> datas){

        addData(0,datas);
    }

    public void addData(int position,List<T> datas){
        if(datas !=null && datas.size()>0) {

            this.datas.addAll(datas);
            this.notifyItemRangeChanged(position, datas.size());
        }
    }
    public interface OnItemClickListener{
        void onClick(View view,int position);
    }
}
