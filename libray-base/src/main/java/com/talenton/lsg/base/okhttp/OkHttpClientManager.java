package com.talenton.lsg.base.okhttp;

import android.support.annotation.NonNull;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.talenton.lsg.base.AppConfig;
import com.talenton.lsg.base.R;
import com.talenton.lsg.base.XltApplication;
import com.talenton.lsg.base.okhttp.callback.FileCallBack;
import com.talenton.lsg.base.okhttp.callback.GsonCallback;
import com.talenton.lsg.base.okhttp.callback.StringCallback;
import com.talenton.lsg.base.okhttp.cookie.CookieJarImpl;
import com.talenton.lsg.base.okhttp.cookie.store.CookieStore;
import com.talenton.lsg.base.okhttp.cookie.store.PersistentCookie;
import com.talenton.lsg.base.server.IntegerSerializer;
import com.talenton.lsg.base.server.LongSerializer;
import com.talenton.lsg.base.server.UserServer;
import com.talenton.lsg.base.server.XLTResponseListener;
import com.talenton.lsg.base.util.AppLogger;
import com.talenton.lsg.base.util.Exceptions;
import com.talenton.lsg.base.util.FormatUtil;
import com.talenton.lsg.base.util.Checker;
import com.talenton.lsg.base.util.FileUtil;
import com.talenton.lsg.base.util.XLTToast;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;

import okhttp3.Call;
import okhttp3.Cookie;
import okhttp3.CookieJar;

/**
 * Created by ttt on 2016/3/25.
 */
public class OkHttpClientManager {

    public static int TYPE_NO_PREV = 0;
    public static int TYPE_SHOP = 1;

    private static OkHttpClientManager mInstance;
    private OkHttpUtils mOkHttp;
    private Gson mGson;

    private OkHttpClientManager(){

        GsonBuilder gb = new GsonBuilder();
        gb.registerTypeAdapter(Long.class, new LongSerializer()).registerTypeAdapter(Integer.class,
                new IntegerSerializer());
        mGson = gb.create();
    }

    public static OkHttpClientManager getInstance(){
        if (mInstance == null){
            synchronized (OkHttpClientManager.class){
                if (mInstance == null){
                    mInstance = new OkHttpClientManager();
                }
            }
        }
        return mInstance;
    }

    public void init(){
        mOkHttp = OkHttpUtils.getInstance();
    }

    public Gson getmGson(){
        return mGson;
    }

    public <T> void addGsonRequest(@NonNull String url, Class<T> responseClass, XLTResponseListener<T> successListener, String params){
        GsonCallback<T> request = new GsonCallback<T>(responseClass, successListener);
        //.mediaType(MediaType.parse("application/x-www-form-urlencoded; charset=utf-8"))
        String urls = String.format("%s%s&access_token=%s", AppConfig.HOME_URL, url, UserServer.getAccessToken());
        //AppLogger.d("http request url==>\n"+ urls);
        //AppLogger.d("http request params==>\n"+ FormatUtil.formatJson(params));
        mOkHttp .post()
                .url(urls)
                .addParams("para", params)
                .build()
                .execute(request);
    }

    //带超时时间
    public <T> void addGsonRequest(@NonNull String url, Class<T> responseClass, XLTResponseListener<T> successListener, String params, int timeout){
        GsonCallback<T> request = new GsonCallback<T>(responseClass, successListener);
        String urls = String.format("%s%s&access_token=%s", AppConfig.HOME_URL, url, UserServer.getAccessToken());
        //AppLogger.d("http request url==>\n"+ urls);
        //AppLogger.d("http request params==>\n"+ FormatUtil.formatJson(params));
        mOkHttp .post()
                .url(urls)
                .addParams("para", params)
                .build()
                .connTimeOut(timeout)
                .readTimeOut(timeout)
                .writeTimeOut(timeout)
                .execute(request);
    }

    public <T> void addGsonRequest(int type, @NonNull String url, Class<T> responseClass, XLTResponseListener<T> successListener, String params){
        String urls = url;
        if (type == TYPE_SHOP){
            urls = String.format("%s%s&access_token=%s", AppConfig.HOME_URL_SHOP, url, UserServer.getAccessToken());
        }
        GsonCallback<T> request = new GsonCallback<T>(responseClass, successListener);
        mOkHttp .post()
                .url(urls)
                .addParams("para", params)
                .build()
                .execute(request);
    }

    public <T> void addGsonRequest(int type, @NonNull String url, Class<T> responseClass, XLTResponseListener<T> successListener, String params, int timeout){
        String urls = url;
        if (type == TYPE_SHOP){
            urls = String.format("%s%s&access_token=%s", AppConfig.HOME_URL_SHOP, url, UserServer.getAccessToken());
        }
        GsonCallback<T> request = new GsonCallback<T>(responseClass, successListener);
        mOkHttp .post()
                .url(urls)
                .addParams("para", params)
                .build()
                .connTimeOut(timeout)
                .readTimeOut(timeout)
                .writeTimeOut(timeout)
                .execute(request);
    }

    public <T> void addGsonRequest1(@NonNull String url, Class<T> responseClass, XLTResponseListener<T> successListener, String params){
        addGsonRequest(TYPE_SHOP, url, responseClass, successListener, params);
    }

    public void GetHttpsRequest(@NonNull String url, StringCallback callback){
        if (callback == null) return;
        mOkHttp.get()
                .url(url)
                .build()
                .execute(callback);
    }

    public <T> void addFileReqeust(@NonNull String url, Class<T> responseClass, XLTResponseListener<T> successListener, Map<String, String> params, String filekey, File file){
        if (Checker.isEmpty(file)){
            XLTToast.makeText(XltApplication.getAppContext(), R.string.file_error).show();
            return;
        }
        GsonCallback<T> request = new GsonCallback<T>(responseClass, successListener);
        String urls = String.format("%s%s&access_token=%s", AppConfig.HOME_URL, url, UserServer.getAccessToken());

        mOkHttp.post()
                .addFile(filekey, FileUtil.getFileExtName(file), file)
                .url(urls)
                .params(params)
                .build()
                .execute(request);
    }

    public void addFileReqeust(@NonNull String url,FileCallBack fileCallBack,Map<String,String> params){
        mOkHttp.post()
                .url(url)
                .params(params)
                .build()
                .execute(fileCallBack);
    }

    public void syncCookie(CookieManager manager,String url){
        PersistentCookie cookieStore = mOkHttp.getCookieStore();
        if (cookieStore == null || cookieStore.cache == null) return;
        Iterator<Cookie> iters = cookieStore.cache.iterator();
        if (iters == null) return;
        while(iters.hasNext()){
            Cookie entry = iters.next();
            String key = entry.name();
            String val = entry.value();
            manager.setCookie(url, String.format("%s=%s;", key,val));
        }
    }

    public void removeCookie(){
        PersistentCookie cookieStore = mOkHttp.getCookieStore();
        if (cookieStore == null) return;
        cookieStore.clear();
        CookieSyncManager.createInstance(XltApplication.getAppContext());
        CookieManager manager = CookieManager.getInstance();
        if (manager != null)
            manager.removeAllCookie();
    }

    public void cancelTag(Object tag)
    {
        for (Call call : mOkHttp.getOkHttpClient().dispatcher().queuedCalls())
        {
            if (tag.equals(call.request().tag()))
            {
                call.cancel();
            }
        }
        for (Call call : mOkHttp.getOkHttpClient().dispatcher().runningCalls())
        {
            if (tag.equals(call.request().tag()))
            {
                call.cancel();
            }
        }
    }
}
