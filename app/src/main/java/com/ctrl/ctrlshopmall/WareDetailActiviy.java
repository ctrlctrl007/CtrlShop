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

import cn.sharesdk.onekeyshare.OnekeyShare;
import dmax.dialog.SpotsDialog;

public class WareDetailActiviy extends AppCompatActivity implements View.OnClickListener{
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
        mToolBar.setRightButtonText("分享");
        mToolBar.setRightButtonOnClickListener(this);
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

    @Override
    public void onClick(View view) {
        showShare();

    }
    private void showShare() {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // 分享时Notification的图标和文字  2.5.9以后的版本不     调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle("Ctrl分享");
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText(ware.getName());
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        oks.setImageUrl(ware.getImgUrl());
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("买东西啦");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");

        // 启动分享GUI
        oks.show(this);
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

        private Context context;
        public WebAppInterface(Context context){
            this.context = context;

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
            ToastUtils.show(context,"已添加到购物车");
        }
        @JavascriptInterface
        public void buy(long id){
            shoppingCartUtil.put(ware);
            ToastUtils.show(context,"已添加到购物车");
        }
    }
}
