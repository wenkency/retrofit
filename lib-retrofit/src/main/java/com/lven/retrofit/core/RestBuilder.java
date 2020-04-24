package com.lven.retrofit.core;

import android.app.Activity;

import androidx.collection.ArrayMap;

import com.lven.retrofit.api.RestMethod;
import com.lven.retrofit.utils.RestTagUtils;

import java.util.Map;

public class RestBuilder {
    String url;
    Map<String, String> headers;
    Map<String, Object> params;
    RestMethod method;
    // 文件下载相关
    String dirName, fileName;
    String tag;

    public RestBuilder() {
        headers = new ArrayMap<>();
        params = new ArrayMap<>();
        method = RestMethod.POST;
    }

    public RestBuilder url(String url) {
        this.url = url;
        return this;
    }

    public RestBuilder dirName(String dirName) {
        this.dirName = dirName;
        return this;
    }

    public RestBuilder fileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    /**
     * 给网络请求打个TAG
     * 如果打了TAG，在Activity销毁时候自动取消
     */
    public RestBuilder tag(Activity activity) {
        this.tag = RestTagUtils.getTag(activity);
        return this;
    }

    public RestBuilder headers(Map<String, String> headers) {
        if (headers != null) {
            this.headers.putAll(headers);
        }
        return this;
    }

    public RestBuilder header(String key, String value) {
        this.headers.put(key, value);
        return this;
    }

    public RestBuilder params(Map<String, Object> params) {
        if (params != null) {
            this.params.putAll(params);
        }
        return this;
    }

    public RestBuilder params(String key, Object value) {
        this.params.put(key, value);
        return this;
    }

    public RestBuilder method(RestMethod method) {
        this.method = method;
        return this;
    }

    public RestClient build() {
        RestClient client = new RestClient();
        client.apply(this);
        return client;
    }
}
