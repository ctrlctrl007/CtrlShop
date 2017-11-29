package com.ctrl.ctrlshopmall.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.ctrl.ctrlshopmall.bean.Page;
import com.ctrl.ctrlshopmall.bean.Ware;
import com.ctrl.ctrlshopmall.http.OkHttpHelper;
import com.ctrl.ctrlshopmall.http.SpotsCallBack;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ctrlc on 2017/11/17.
 */

public class PageUtil {

    private static Builder builder;
    private  static final int STATE_NORMAL=0;
    private  static final int STATE_REFRESH = 1;
    private  static final int STATE_MORE=2;
    private OkHttpHelper httpHelper;


    private int state=STATE_NORMAL;

    private PageUtil(){
        httpHelper = OkHttpHelper.getInstance();
        initRefreshLayout();
    }
    public static Builder newBulder(){
        builder = new Builder();
        return builder;
    }
    public void request(){
        requestData();
    }
    private void requestData(){
        httpHelper.get(buildUrl(), new PagerAdapter(builder.mContext));



    }
    private String buildUrl(){
        return builder.url+"?"+buildUrlParams();
    }
    private String buildUrlParams(){
        HashMap<String,Object> map = builder.params;
        map.put("curPage",builder.currentPage);
        map.put("pageSize",builder.pageSize);
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, Object> entry : map.entrySet()){
            sb.append(entry.getKey()+"="+entry.getValue());
            sb.append("&");
        }
        String s = sb.toString();
        if (s.endsWith("&")){
            s = s.substring(0,s.length()-1);
        }
        return s;
    }

    public void putParam(String key,Object o){
        builder.params.put(key,o);
    }


    private void initRefreshLayout(){
        builder.mRefreshLayout.setLoadMore(builder.canLoadMore);
        builder. mRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                refresh();
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {


                if(builder.currentPage <=builder.totalPage)
                    loadMore();
                else{
                    Toast.makeText(builder.mContext,"没有更多数据了",Toast.LENGTH_SHORT).show();
                    builder.mRefreshLayout.finishRefreshLoadMore();
                    builder.mRefreshLayout.setLoadMore(false);
                }
            }
        });

    }
    private void refresh(){
        builder.currentPage = 1;
        state = STATE_REFRESH;
        requestData();
    }

    private void loadMore(){
        builder.currentPage = ++builder.currentPage;
        state = STATE_MORE;

        requestData();
    }




    public static class Builder{
        private MaterialRefreshLayout mRefreshLayout;
        private int currentPage = 1;
        private int totalPage = 1;
        private int pageSize = 10;
        private String url;
        private Context mContext;
        private Type mType;
        private OnPagerListener mOnPagerListener;
        private boolean canLoadMore;
        public Builder setUrl(String url) {
            this.url = url;
            return builder;
        }
        public Builder setLoadMore(boolean loadMore){
            this.canLoadMore = loadMore;
            return builder;
        }

        private HashMap<String,Object> params = new HashMap<>(5);

        public Builder setmRefreshLayout(MaterialRefreshLayout mRefreshLayout) {
            this.mRefreshLayout = mRefreshLayout;
            return builder;
        }

        public Builder setmOnPagerListener(OnPagerListener mOnPagerListener) {
            this.mOnPagerListener = mOnPagerListener;
            return builder;
        }

        public Builder putParam(String string, Object object){
            params.put(string,object);
            return builder;
        }

        public Builder setPageSize(int pageSize) {
            this.pageSize = pageSize;
            return builder;
        }



        public PageUtil build(Context context,Type type)
        {
            mContext = context;
            mType = type;
            valid();
            return new PageUtil();
        }
        private void valid(){


            if(this.mContext==null)
                throw  new RuntimeException("content can't be null");

            if(this.url==null || "".equals(this.url))
                throw  new RuntimeException("url can't be  null");

            if(this.mRefreshLayout==null)
                throw  new RuntimeException("MaterialRefreshLayout can't be  null");
        }

    }
    class PagerAdapter<T> extends SpotsCallBack<Page<T>>{

        public PagerAdapter(Context context) {
            super(context);
            super.mType = builder.mType;

        }

        @Override
        public void onSuccess(Response response, Page<T> warePage) {
            builder.currentPage = warePage.getCurrentPage();
            builder.totalPage = warePage.getTotalCount()/builder.pageSize+1;
            builder.pageSize = warePage.getPageSize();
            showData(warePage.getList(),builder.totalPage,warePage.getTotalCount());
        }

        @Override
        public void onError(Response response, int code, Exception e) {
            Toast.makeText(builder.mContext,"加载失败"+response,Toast.LENGTH_LONG).show();
            if(STATE_REFRESH==state){
                builder.mRefreshLayout.finishRefresh();
            }else if(STATE_MORE == state){
                builder.mRefreshLayout.finishRefreshLoadMore();
            }

        }

        @Override
        public void onFailure(Request request, Exception e) {
            super.onFailure(request, e);
            ToastUtils.show(builder.mContext,"获取服务器数据失败");

            if(STATE_REFRESH==state){
                builder.mRefreshLayout.finishRefresh();
            }else if(STATE_MORE == state){
                builder.mRefreshLayout.finishRefreshLoadMore();
            }
        }
    }

    private <T>void showData(List<T> datas,int totalPage,int totalCount) {
        if(datas==null||datas.size()<0){
            ToastUtils.show(builder.mContext,"加载不到数据");
            return;
        }

        switch (state) {

            case STATE_NORMAL:
              if(builder.mOnPagerListener!=null){
                  builder.mOnPagerListener.load(datas,totalPage,totalCount);
              }

                break;

            case STATE_MORE:

                builder.mRefreshLayout.finishRefreshLoadMore();
                if(builder.mOnPagerListener!=null){
                    builder.mOnPagerListener.loadMore(datas,totalPage,totalCount);
                }

                break;

            case STATE_REFRESH:

                builder.mRefreshLayout.finishRefresh();
                if(builder.mOnPagerListener!=null){
                    builder.mOnPagerListener.refresh(datas,totalPage,totalCount);
                }
                break;


        }
    }
    public interface OnPagerListener<T>{
        void load(List<T> datas,int totalPage,int totalCount);
        void loadMore(List<T> datas,int totalPage,int totalCount);
        void refresh(List<T> datas,int totalPage,int totalCount);



    }
}
