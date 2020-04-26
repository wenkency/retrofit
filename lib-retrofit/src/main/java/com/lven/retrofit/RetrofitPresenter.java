package com.lven.retrofit;

import android.app.Activity;

import com.lven.retrofit.api.RestMethod;
import com.lven.retrofit.callback.IOnCallback;
import com.lven.retrofit.core.RestClient;
import com.lven.retrofit.utils.RestUtils;

import java.util.Map;

public class RetrofitPresenter {
    /**
     * 所有请求
     */
    public static void request(Activity activity, RestMethod method, String url, Map<String, String> headers,
                               Map<String, Object> params, IOnCallback onCallback) {
        RestClient.create()
                .method(method)
                .url(url)
                .tag(activity)
                .headers(headers)
                .params(params)
                .build()
                .request(onCallback);
    }

    // ============== get ===============================================================================
    public static void get(Activity activity, String url, Map<String, Object> params, IOnCallback onCallback) {
        request(activity, RestMethod.GET, url, null, params, onCallback);
    }

    public static void get(Activity activity, String url, Object params, IOnCallback onCallback) {
        get(activity, url, RestUtils.getParams(params), onCallback);
    }

    public static void get(Activity activity, String url, String key, String value, IOnCallback onCallback) {
        get(activity, url, RestUtils.getParams(key, value), onCallback);
    }

    public static void get(Activity activity, String url, IOnCallback onCallback) {
        get(activity, url, null, onCallback);
    }

    public static void get(String url, IOnCallback onCallback) {
        get(null, url, onCallback);
    }

    // ============== post ===============================================================================
    public static void post(Activity activity, String url, Map<String, Object> params, IOnCallback onCallback) {
        request(activity, RestMethod.POST, url, null, params, onCallback);
    }

    public static void post(Activity activity, String url, Object params, IOnCallback onCallback) {
        post(activity, url, RestUtils.getParams(params), onCallback);
    }

    public static void post(Activity activity, String url, String key, String value, IOnCallback onCallback) {
        post(activity, url, RestUtils.getParams(key, value), onCallback);
    }

    public static void post(Activity activity, String url, IOnCallback onCallback) {
        post(activity, url, null, onCallback);
    }

    public static void post(String url, IOnCallback onCallback) {
        post(null, url, onCallback);
    }

    // ============== put ===============================================================================
    public static void put(Activity activity, String url, Map<String, Object> params, IOnCallback onCallback) {
        request(activity, RestMethod.PUT, url, null, params, onCallback);
    }

    public static void put(Activity activity, String url, Object params, IOnCallback onCallback) {
        put(activity, url, RestUtils.getParams(params), onCallback);
    }

    public static void put(Activity activity, String url, String key, String value, IOnCallback onCallback) {
        put(activity, url, RestUtils.getParams(key, value), onCallback);
    }

    public static void put(Activity activity, String url, IOnCallback onCallback) {
        put(activity, url, null, onCallback);
    }

    public static void put(String url, IOnCallback onCallback) {
        put(null, url, onCallback);
    }

    // ============== delete ===============================================================================
    public static void delete(Activity activity, String url, Map<String, Object> params, IOnCallback onCallback) {
        request(activity, RestMethod.DELETE, url, null, params, onCallback);
    }

    public static void delete(Activity activity, String url, Object params, IOnCallback onCallback) {
        delete(activity, url, RestUtils.getParams(params), onCallback);
    }

    public static void delete(Activity activity, String url, String key, String value, IOnCallback onCallback) {
        delete(activity, url, RestUtils.getParams(key, value), onCallback);
    }

    public static void delete(Activity activity, String url, IOnCallback onCallback) {
        delete(activity, url, null, onCallback);
    }

    public static void delete(String url, IOnCallback onCallback) {
        delete(null, url, onCallback);
    }

    // ============== postForm ===============================================================================
    public static void postForm(Activity activity, String url, Map<String, Object> params, IOnCallback onCallback) {
        request(activity, RestMethod.POST_FORM, url, null, params, onCallback);
    }

    public static void postForm(Activity activity, String url, Object params, IOnCallback onCallback) {
        postForm(activity, url, RestUtils.getParams(params), onCallback);
    }

    public static void postForm(Activity activity, String url, String key, String value, IOnCallback onCallback) {
        postForm(activity, url, RestUtils.getParams(key, value), onCallback);
    }

    public static void postForm(Activity activity, String url, IOnCallback onCallback) {
        postForm(activity, url, null, onCallback);
    }

    public static void postForm(String url, IOnCallback onCallback) {
        postForm(null, url, onCallback);
    }

    // ============== putForm ===============================================================================
    public static void putForm(Activity activity, String url, Map<String, Object> params, IOnCallback onCallback) {
        request(activity, RestMethod.PUT_FORM, url, null, params, onCallback);
    }

    public static void putForm(Activity activity, String url, Object params, IOnCallback onCallback) {
        putForm(activity, url, RestUtils.getParams(params), onCallback);
    }

    public static void putForm(Activity activity, String url, String key, String value, IOnCallback onCallback) {
        putForm(activity, url, RestUtils.getParams(key, value), onCallback);
    }

    public static void putForm(Activity activity, String url, IOnCallback onCallback) {
        putForm(activity, url, null, onCallback);
    }

    public static void putForm(String url, IOnCallback onCallback) {
        putForm(null, url, onCallback);
    }

    /**
     * 上传
     */
    public static void upload(Activity activity, String url, Map<String, Object> params, IOnCallback onCallback) {
        RestClient.create()
                .method(RestMethod.UPLOAD)
                .url(url)
                .tag(activity)
                .params(params)
                .build()
                .request(onCallback);
    }

    /**
     * 下载
     */
    public static void download(Activity activity, String url, Map<String, String> headers, Map<String, Object> params,
                                String dirName, String fileName, IOnCallback onCallback) {
        RestClient.create()
                .method(RestMethod.DOWNLOAD)
                .url(url)
                .tag(activity)
                .params(params)
                .headers(headers)
                .fileName(fileName)
                .dirName(dirName)
                .build()
                .request(onCallback);
    }

    /**
     * 下载
     */
    public static void download(Activity activity, String url, String dirName, String fileName, IOnCallback onCallback) {
        download(activity, url, null, null, dirName, fileName, onCallback);
    }
}
