package com.lven.retrofit.api;

import android.app.Application;
import android.content.Context;

import com.lven.retrofit.adapter.ActivityCallbacks;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;

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
    private ActivityCallbacks callback;
    /**
     * 公共请求头
     */
    private Map<String, String> commHeaders;
    /**
     * 公共请求参数
     */
    private Map<String, Object> commParams;

    private SSLSocketFactory sslSocketFactory;
    private X509TrustManager trustManager;
    private HostnameVerifier hostnameVerifier;
    // 这个最优先
    private OkHttpClient okHttpClient;

    private RestConfig() {
    }

    public RestConfig setSslSocketFactory(SSLSocketFactory sslSocketFactory, X509TrustManager trustManager) {
        this.sslSocketFactory = sslSocketFactory;
        this.trustManager = trustManager;
        return this;
    }

    public RestConfig setHostnameVerifier(HostnameVerifier hostnameVerifier) {
        this.hostnameVerifier = hostnameVerifier;
        return this;
    }

    public HostnameVerifier getHostnameVerifier() {
        return hostnameVerifier;
    }

    public RestConfig setOkHttpClient(OkHttpClient okHttpClient) {
        this.okHttpClient = okHttpClient;
        return this;
    }

    public OkHttpClient getOkHttpClient() {
        return okHttpClient;
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
        initInterceptor();
        this.interceptors.add(interceptor);
        return this;
    }

    private void initInterceptor() {
        if (this.interceptors == null) {
            interceptors = new ArrayList<>();
        }
    }

    public final List<Interceptor> getInterceptors() {
        initInterceptor();
        return interceptors;
    }

    public final RestConfig addNetInterceptor(Interceptor interceptor) {
        initNetInterceptor();
        this.netInterceptors.add(interceptor);
        return this;
    }


    public List<Interceptor> getNetInterceptors() {
        initNetInterceptor();
        return netInterceptors;
    }

    private void initNetInterceptor() {
        if (this.netInterceptors == null) {
            netInterceptors = new ArrayList<>();
        }
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
    public final void register(Application application) {
        this.context = application.getApplicationContext();
        // 判断一下，用户可能多次调用
        if (callback == null) {
            callback = new ActivityCallbacks();
            application.registerActivityLifecycleCallbacks(callback);
        }
    }

    /**
     * 这里不注册Activity回调监听，一般不用管
     */
    public final void unregister(Application application) {
        if (callback != null) {
            application.unregisterActivityLifecycleCallbacks(callback);
            callback = null;
        }
    }

    public final Map<String, String> getCommHeaders() {
        return commHeaders;
    }

    public final RestConfig setCommHeaders(Map<String, String> commHeaders) {
        this.commHeaders = commHeaders;
        return this;
    }

    public final Map<String, Object> getCommParams() {
        return commParams;
    }

    public final RestConfig setCommParams(Map<String, Object> commParams) {
        this.commParams = commParams;
        return this;
    }

    public SSLSocketFactory getSslSocketFactory() {
        return sslSocketFactory;
    }

    public X509TrustManager getTrustManager() {
        return trustManager;
    }
}
