package com.talenton.lsg;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshWebView;
import com.talenton.lsg.base.okhttp.OkHttpClientManager;
import com.talenton.lsg.base.util.XLTToast;
import com.talenton.lsg.ui.JavaScriptLocalObj;
import com.talenton.lsg.ui.WebViewActivity;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class BaseWebViewActivity extends BaseCompatActivity {

    protected PullToRefreshWebView pullToRefreshWebView;
    protected WebView webView;
    protected ProgressBar wb_loading;

    protected String url;
    protected boolean isNeedPrefix;
    protected boolean toReload = false;
    protected JavaScriptLocalObj mJSObj;

    @Override
    public void setContentView(@LayoutRes int layoutResID){
        super.setContentView(layoutResID);

        pullToRefreshWebView = (PullToRefreshWebView) findViewById(R.id.webview);
        pullToRefreshWebView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        pullToRefreshWebView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<WebView>() {
            @Override
            public void onRefresh(PullToRefreshBase<WebView> refreshView) {
                pullToRefreshWebView.onRefreshComplete();
                webView.reload();
            }
        });
        webView = pullToRefreshWebView.getRefreshableView();
        wb_loading = (ProgressBar) findViewById(R.id.wb_loading);
        wb_loading.setMax(100);

        initVebView();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("SetJavaScriptEnabled")
    protected void initVebView() {
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                wb_loading.setVisibility(View.VISIBLE);
                wb_loading.setProgress(newProgress);
                if (newProgress == 100) {
                    wb_loading.setVisibility(View.GONE);
                }
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                if (mActionBarTitle != null)
                    mActionBarTitle.setText(title);
                super.onReceivedTitle(view, title);
            }
        });

        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.indexOf("yk://localhost/ACT_LOGIN") >= 0) // 20150814登录按钮
                {
                    //reLogin();
                    //finish();
                } else {
                    if (url.indexOf("_self=1") >= 0) {
                        view.loadUrl(url);
                    } else {
                        //toReload = true;
                        WebViewActivity.startWebViewActivity(BaseWebViewActivity.this, url, false);
                    }
                }
                return true;
            }

            //
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                XLTToast.makeText(LsgApplication.getAppContext(), errorCode + "/" + description, Toast.LENGTH_LONG).show();
            }

            @TargetApi(android.os.Build.VERSION_CODES.M)
            @Override
            public void onReceivedError(WebView view, WebResourceRequest req, WebResourceError rerr) {
                // Redirect to deprecated method, so you can use it in all SDK versions
                onReceivedError(view, rerr.getErrorCode(), rerr.getDescription().toString(), req.getUrl().toString());
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                // TODO Auto-generated method stub
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                // TODO Auto-generated method stub
                try {
                    String theTitle = view.getTitle().toString();
                    if (theTitle.indexOf("找不到网页") >= 0) // 20150408用提示代替出错网页
                    {
                        view.loadData(
                                "<html xmlns=\"http://www.w3.org/1999/xhtml\">\r\n<head>\r\n<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\r\n<title>温馨提示</title>\r\n</head>\r\n<body>\r\n<div align=\"center\">无法连接到服务器，<br/>请返回并检查您的网络！\r\n</div>\r\n</body>\r\n</html>",
                                "text/html; charset=UTF-8", null);
                    } else {
                        view.loadUrl("javascript:window.client.show('<head>'+"
                                + "document.getElementsByTagName('html')[0].innerHTML+'</head>');");// 20150122
                        // y.m.j.
                    }
                } catch (Exception e) {
                    // Log.d("20150120WebOpen","setText err e="+e.toString());
                }
                super.onPageFinished(view, url);
            }
        });
        WebSettings webSetting = webView.getSettings();
        //设置编码
        webSetting.setDefaultTextEncodingName("utf-8");
        // LOAD_DEFAULT//LOAD_NO_CACHE
        webSetting.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSetting.setPluginState(WebSettings.PluginState.ON);
        //支持js
        webSetting.setJavaScriptEnabled(true);
        // 设置可以支持缩放
        webSetting.setSupportZoom(true);
        // 设置出现缩放工具
        webSetting.setBuiltInZoomControls(true);
        webSetting.setDomStorageEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        addJS();
    }

    protected void addJS(){
        mJSObj = new JavaScriptLocalObj(this);
        webView.addJavascriptInterface(mJSObj, "client");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack();// 返回前一个页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onResume() {
        super.onResume();
        webView.resumeTimers();
        if (toReload) {
            webView.reload();
        }
        toReload = false;
        callHiddenWebViewMethod("onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        webView.pauseTimers();
        callHiddenWebViewMethod("onPause");
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        //webView.setVisibility(View.GONE);
        releaseAllWebViewCallback();
    }

    private void releaseAllWebViewCallback(){
        try {
            if (android.os.Build.VERSION.SDK_INT < 16){
                Field field = WebView.class.getDeclaredField("mWebViewCore");
                field = field.getType().getDeclaredField("mBrowserFrame");
                field = field.getType().getDeclaredField("sConfigCallback");
                field.setAccessible(true);
                field.set(null, null);
            }else {
                Field sConfigCallback = Class.forName("android.webkit.BrowserFrame").getDeclaredField("sConfigCallback");
                if (sConfigCallback != null) {
                    sConfigCallback.setAccessible(true);
                    sConfigCallback.set(null, null);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     *
     * 调用隐藏的WebView方法 <br />
     *
     * 说明：WebView完全退出swf的方法，停止声音的播放。
     *
     * @param name
     */

    private void callHiddenWebViewMethod(String name) {
        if (webView != null) {
            try {
                Method method = WebView.class.getMethod(name);
                method.invoke(webView); // 调用
            } catch (NoSuchMethodException e) { // 没有这样的方法
                Log.i("No such method: " + name, e.toString());
            } catch (IllegalAccessException e) { // 非法访问
                Log.i("Illegal Access: " + name, e.toString());
            } catch (InvocationTargetException e) { // 调用的目标异常
                Log.d("InvocationTarget:" + name, e.toString());
            }
        }
    }


    /**
     * 同步一下cookie
     */
    public static void synCookies(Context context, String url) {
        CookieSyncManager.createInstance(context);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        // cookieManager.removeSessionCookie();//移除
        OkHttpClientManager.getInstance().syncCookie(cookieManager, url);
        CookieSyncManager.getInstance().sync();
    }
}
