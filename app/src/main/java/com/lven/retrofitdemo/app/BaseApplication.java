package com.lven.retrofitdemo.app;

import android.app.Application;

import com.lven.retrofit.api.RestConfig;
import com.lven.retrofit.interceptor.CacheInterceptor;

import okhttp3.logging.HttpLoggingInterceptor;

public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // 1. 初始化
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
        RestConfig.getInstance()
                .setBaseUrl("http://httpbin.org/")
                .setDebugUrl("http://httpbin.org/")
                .setDebug(true)
                .addInterceptor(interceptor)
                // 可设置10内再次请求，走缓存
                .addNetInterceptor(new CacheInterceptor(10))
                .register(this);
    }
}
