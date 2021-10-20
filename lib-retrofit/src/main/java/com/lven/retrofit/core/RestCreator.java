package com.lven.retrofit.core;

import android.content.Context;

import com.google.gson.Gson;
import com.lven.retrofit.api.RestConfig;
import com.lven.retrofit.api.RestService;
import com.lven.retrofit.api.RxRestService;
import com.lven.retrofit.utils.RestSSLUtils;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * 产生Retrofit客户端
 */
public class RestCreator {
    /**
     * 产生一个全局的Retrofit客户端
     */
    private static final class RetrofitHolder {
        private static final String BASE_URL = RestConfig.getInstance().getBaseUrl();
        private static final Retrofit RETROFIT = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(HttpClientHolder.getOkHttpClient())
                .build();

    }

    /**
     * 这个是创建OkHttpClient
     */
    private static class HttpClientHolder {
        private static final int TIME_OUT = 60;
        private static OkHttpClient OK_HTTP_CLIENT;

        private static final OkHttpClient getOkHttpClient() {
            RestConfig config = RestConfig.getInstance();
            OkHttpClient client = config.getOkHttpClient();
            if (client != null) {
                return client;
            }
            if (OK_HTTP_CLIENT != null) {
                return OK_HTTP_CLIENT;
            }
            // 主机过滤
            HostnameVerifier hostnameVerifier = config.getHostnameVerifier();
            if (hostnameVerifier == null) {
                hostnameVerifier = new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                };
            }
            OkHttpClient.Builder builder = new OkHttpClient.Builder()
                    .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
                    .readTimeout(TIME_OUT, TimeUnit.SECONDS)
                    .writeTimeout(TIME_OUT, TimeUnit.SECONDS)
                    .hostnameVerifier(hostnameVerifier);
            // SSL配置
            try {
                SSLSocketFactory factory = config.getSslSocketFactory();
                X509TrustManager trustManager = config.getTrustManager();
                if (factory == null) {
                    factory = RestSSLUtils.initSSLSocketFactory();
                }
                if (trustManager == null) {
                    trustManager = RestSSLUtils.initTrustManager();
                }
                builder.sslSocketFactory(factory, trustManager);
            } catch (Throwable e) {
                e.printStackTrace();
            }

            // 添加请求前的拦截器：如取消网络的TAG
            List<Interceptor> interceptors = config.getInterceptors();
            if (interceptors != null) {
                for (Interceptor interceptor : interceptors) {
                    builder.addInterceptor(interceptor);
                }
            }

            // 添加请求成功后的拦截器：如缓存
            List<Interceptor> netInterceptors = config.getNetInterceptors();
            Context context = config.getContext();
            if (netInterceptors != null && netInterceptors.size() > 0) {
                File file = context.getCacheDir();
                if (!file.exists()) {
                    file.mkdirs();
                }
                Cache cache = new Cache(file, 100 * 1024 * 1024);
                builder.cache(cache);
                for (Interceptor interceptor : netInterceptors) {
                    builder.addNetworkInterceptor(interceptor);
                }
            }

            return OK_HTTP_CLIENT = builder.build();
        }
    }


    /**
     * 提供接口让调用者得到retrofit对象
     */
    private static final class RestServiceHolder {
        private static final RestService REST_SERVICE = RetrofitHolder.RETROFIT.create(RestService.class);
    }

    /**
     * 获取网络请求接口
     */
    public static RestService getRestService() {
        return RestServiceHolder.REST_SERVICE;
    }


    /**
     * 提供接口让调用者得到retrofit对象
     */
    private static final class RxRestServiceHolder {
        private static final RxRestService REST_SERVICE = RetrofitHolder.RETROFIT.create(RxRestService.class);
    }

    /**
     * 获取网络请求接口
     */
    public static RxRestService getRxRestService() {
        return RxRestServiceHolder.REST_SERVICE;
    }

    private static class GSonHolder {
        private static Gson GSON = new Gson();
    }

    public static Gson getGSon() {
        return GSonHolder.GSON;
    }

    public static OkHttpClient getOkHttpClient() {
        return HttpClientHolder.getOkHttpClient();
    }
}
