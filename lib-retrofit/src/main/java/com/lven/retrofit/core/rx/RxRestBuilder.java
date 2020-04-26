package com.lven.retrofit.core.rx;

import android.app.Activity;

import androidx.collection.ArrayMap;

import com.lven.retrofit.api.RestMethod;
import com.lven.retrofit.callback.IOnProgress;
import com.lven.retrofit.utils.RestTagUtils;

import java.util.Map;


public class RxRestBuilder {
    String url;
    Map<String, String> headers;
    Map<String, Object> params;
    RestMethod method;
    String tag;
    IOnProgress onProgress;
    String dirName, fileName;
    public RxRestBuilder() {
        headers = new ArrayMap<>();
        params = new ArrayMap<>();
        method = RestMethod.POST;
    }

    public RxRestBuilder url(String url) {
        this.url = url;
        return this;
    }

    public RxRestBuilder onProgress(IOnProgress onProgress) {
        this.onProgress = onProgress;
        return this;
    }

    /**
     * 给网络请求打个TAG
     * 如果打了TAG，在Activity销毁时候自动取消
     */
    public RxRestBuilder tag(Activity activity) {
        tag = RestTagUtils.getTag(activity);
        return this;
    }
    public RxRestBuilder dirName(String dirName) {
        this.dirName = dirName;
        return this;
    }

    public RxRestBuilder fileName(String fileName) {
        this.fileName = fileName;
        return this;
    }
    public RxRestBuilder headers(Map<String, String> headers) {
        if (headers != null) {
            this.headers.putAll(headers);
        }
        return this;
    }

    public RxRestBuilder header(String key, String value) {
        this.headers.put(key, value);
        return this;
    }

    public RxRestBuilder params(Map<String, Object> params) {
        if (params != null) {
            this.params.putAll(params);
        }
        return this;
    }

    public RxRestBuilder params(String key, Object value) {
        this.params.put(key, value);
        return this;
    }

    public RxRestBuilder method(RestMethod method) {
        this.method = method;
        return this;
    }

    public RxRestClient build() {
        RxRestClient client = new RxRestClient();
        client.apply(this);
        return client;
    }
}
