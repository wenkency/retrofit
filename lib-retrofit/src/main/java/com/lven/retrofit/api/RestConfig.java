package com.lven.retrofit.api;

import android.app.Application;
import android.content.Context;

import com.lven.retrofit.interceptor.ActivityCallbacks;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Interceptor;

/**
 * 网络请求统一配置
 */
public class RestConfig {
    /**
     * 配置BaseUrl
     */
    private String baseUrl;
    private String debugUrl;
    private Context context;
    private boolean isDebug;
    /**
     * 请求成功前拦截器
     */
    private List<Interceptor> interceptors;
    /**
     * 请求成功后的拦截器
     */
    private List<Interceptor> netInterceptors;

    private RestConfig() {
        interceptors = new ArrayList<>();
        netInterceptors = new ArrayList<>();
    }

    private static class Holder {
        private static RestConfig config = new RestConfig();
    }

    public static RestConfig getInstance() {
        return Holder.config;
    }

    /**
     * 如果是Debug就用debugUrl
     */
    public final String getBaseUrl() {
        if (isDebug) {
            return debugUrl;
        }
        return baseUrl;
    }

    public final RestConfig setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        return this;
    }

    public final Context getContext() {
        return context;
    }


    public final boolean isDebug() {
        return isDebug;
    }

    public final RestConfig setDebug(boolean debug) {
        isDebug = debug;
        return this;
    }

    public final RestConfig addInterceptor(Interceptor interceptor) {
        this.interceptors.add(interceptor);
        return this;
    }

    public final List<Interceptor> getInterceptors() {
        return interceptors;
    }

    public final RestConfig addNetInterceptor(Interceptor interceptor) {
        this.netInterceptors.add(interceptor);
        return this;
    }

    public List<Interceptor> getNetInterceptors() {
        return netInterceptors;
    }

    public final RestConfig setDebugUrl(String debugUrl) {
        this.debugUrl = debugUrl;
        return this;
    }

    public RestConfig setContext(Context context) {
        this.context = context;
        return this;
    }

    /**
     * 这里注册Activity回调监听，用于Activity销毁自动取消网络
     */
    public final RestConfig register(Application application) {
        this.context = application.getApplicationContext();
        application.registerActivityLifecycleCallbacks(new ActivityCallbacks());
        return this;
    }
}
