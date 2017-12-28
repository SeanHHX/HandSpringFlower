package com.cqupt.handspringflower.utils;

import com.cqupt.handspringflower.cookie.CookiesManager;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class HttpUtils {

    // get
    public static void sendOkHttpRequest(String address, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient.Builder().build();
        Request request = new Request.Builder()
                .url(address).get().build();
        client.newCall(request).enqueue(callback);
    }

    // post:登录/注册
    public static void sendOkHttpRequest(String address, String jsonData, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient.Builder().cookieJar(new CookiesManager()).build();
        RequestBody requestBody = new FormBody.Builder().add("params", jsonData).build();
        Request request = new Request.Builder().url(address).post(requestBody).build();
        client.newCall(request).enqueue(callback);
    }
}
