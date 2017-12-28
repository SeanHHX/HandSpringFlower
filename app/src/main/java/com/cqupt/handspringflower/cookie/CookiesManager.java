package com.cqupt.handspringflower.cookie;

import android.util.Log;

import com.cqupt.handspringflower.MyApplication;
import com.cqupt.handspringflower.utils.LogUtil;

import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

public class CookiesManager implements CookieJar {
    private static final PersistentCookieStore cookieStore = new PersistentCookieStore(MyApplication.getContext());
    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        if (cookies != null && cookies.size() > 0) {
            for (Cookie item : cookies) {
                cookieStore.add(url, item);
                LogUtil.e("+++++++++++++++", item.toString());
            }
        }
    }
    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        List<Cookie> cookies = cookieStore.get(url);
        LogUtil.e("cookies_size", cookies.size() + "");
        for(int i=0; i<cookies.size(); i++) {
            LogUtil.e("cookies", cookies.get(i).toString());
        }
        return cookies;
    }
}
