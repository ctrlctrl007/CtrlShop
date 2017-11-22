package com.ctrl.ctrlshopmall;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.ctrl.ctrlshopmall.bean.Ware;
import com.ctrl.ctrlshopmall.http.ShoppingCartUtil;
import com.ctrl.ctrlshopmall.utils.Contants;
import com.ctrl.ctrlshopmall.utils.ToastUtils;
import com.ctrl.ctrlshopmall.widget.CNiaoToolBar;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import dmax.dialog.SpotsDialog;

public class WareDetailActiviy extends AppCompatActivity {
    @ViewInject(R.id.tool_bar)
    private CNiaoToolBar mToolBar;

    @ViewInject(R.id.web_view)
    private WebView mWebView;


    private Ware ware;

    private WebAppInterface appInterface;

    private ShoppingCartUtil shoppingCartUtil;

    private SpotsDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ware_detail_activiy);
        ViewUtils.inject(this);
        ware = (Ware) getIntent().getSerializableExtra(Contants.WARE);
        if(ware ==null){
            finish();
        }
        mDialog = new SpotsDialog(this,"Loading.....");
        mDialog.show();
        shoppingCartUtil = new ShoppingCartUtil(this);
        initToolbar();
        initWebView();

    }
    private void initToolbar(){
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @SuppressLint("JavascriptInterface")
    private void initWebView(){
        WebSettings settings = mWebView.getSettings();

        settings.setJavaScriptEnabled(true);
        settings.setBlockNetworkImage(false);
        settings.setAppCacheEnabled(true);

        mWebView.loadUrl(Contants.API.WARES_DETAIL);
        appInterface = new WebAppInterface(this);
        mWebView.addJavascriptInterface(appInterface,"appInterface");

        //appInterface.showDetail();
        mWebView.setWebViewClient(new MyWebClient());

    }

    class MyWebClient extends WebViewClient{
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (mDialog!=null&&mDialog.isShowing()){
                mDialog.dismiss();
            }
            appInterface.showDetail();

        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            //appInterface.showDetail();
        }
    }


    class WebAppInterface {


        public WebAppInterface(Context context){

        }
        @JavascriptInterface
        public void showDetail(){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mWebView.loadUrl("javascript:showDetail("+ware.getId() +")");
                }
            });
        }
        @JavascriptInterface
        public void addToCart(long id){
            shoppingCartUtil.put(ware);
            ToastUtils.show(WareDetailActiviy.this,"已添加到购物车");
        }
        @JavascriptInterface
        public void buy(long id){
            shoppingCartUtil.put(ware);
            ToastUtils.show(WareDetailActiviy.this,"已添加到购物车");
        }
    }
}
