package com.lven.retrofit.interceptor;

import android.text.TextUtils;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 这个是缓存
 */
public class CacheInterceptor implements Interceptor {
    private final int maxAge;

    public CacheInterceptor(int maxAge) {
        this.maxAge = maxAge;
    }

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        Request request = chain.request();
        String cacheControl = request.cacheControl().toString();
        if (TextUtils.isEmpty(cacheControl)) {
            cacheControl = "public, max-age=" + maxAge;
        }
        Response response = chain.proceed(request);
        return response.newBuilder()
                .header("Cache-Control", cacheControl)
                .removeHeader("Pragma")
                .build();
    }
}
