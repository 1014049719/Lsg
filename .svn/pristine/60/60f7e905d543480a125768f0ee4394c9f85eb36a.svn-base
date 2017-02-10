package com.talenton.lsg.base.okhttp.cookie.store;

import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.cache.CookieCache;
import com.franmontiel.persistentcookiejar.persistence.CookiePersistor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.HttpUrl;

/**
 * Created by ttt on 2016/8/8.
 */
public class PersistentCookie implements ClearableCookieJar {
    public CookieCache cache;
    public CookiePersistor persistor;

    public PersistentCookie(CookieCache cache, CookiePersistor persistor) {
        this.cache = cache;
        this.persistor = persistor;
        this.cache.addAll(persistor.loadAll());
    }

    public synchronized void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        this.cache.addAll(cookies);
        this.persistor.saveAll(cookies);
    }

    public synchronized List<Cookie> loadForRequest(HttpUrl url) {
        ArrayList removedCookies = new ArrayList();
        ArrayList validCookies = new ArrayList();
        Iterator it = this.cache.iterator();

        while(it.hasNext()) {
            Cookie currentCookie = (Cookie)it.next();
            if(isCookieExpired(currentCookie)) {
                removedCookies.add(currentCookie);
                it.remove();
            } else if(currentCookie.matches(url)) {
                validCookies.add(currentCookie);
            }
        }

        this.persistor.removeAll(removedCookies);
        return validCookies;
    }

    private static boolean isCookieExpired(Cookie cookie) {
        return cookie.expiresAt() < System.currentTimeMillis();
    }

    public synchronized void clear() {
        this.cache.clear();
        this.persistor.clear();
    }
}