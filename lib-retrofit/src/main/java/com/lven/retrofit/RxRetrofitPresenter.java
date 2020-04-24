package com.lven.retrofit;

import android.app.Activity;

import com.lven.retrofit.api.RestCode;
import com.lven.retrofit.api.RestMethod;
import com.lven.retrofit.callback.IOnCallback;
import com.lven.retrofit.callback.IOnProgress;
import com.lven.retrofit.core.RestClient;
import com.lven.retrofit.core.RxRestClient;
import com.lven.retrofit.utils.RestUtils;

import java.io.IOException;
import java.util.Map;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class RxRetrofitPresenter {
    /**
     * 所有请求
     */
    public static Single<ResponseBody> request(Activity activity, RestMethod method, String url, final Map<String, String> headers, Map<String, Object> params, IOnProgress onProgress) {
        // 这里只是创建一个Request
        return RxRestClient.create()
                .method(method)
                .url(url)
                .tag(activity)
                .headers(headers)
                .onProgress(onProgress)
                .params(params)
                .build()
                .request();
    }

    /**
     * 所有请求
     */
    public static void request(Activity activity, RestMethod method, String url, final Map<String, String> headers, Map<String, Object> params, final IOnCallback onCallback) {
        // 没回调，下面就不走了
        if (onCallback == null) {
            return;
        }
        // 订阅
        request(activity, method, url, headers, params, (IOnProgress) onCallback)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<ResponseBody>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        if (onCallback != null) {
                            onCallback.onBefore(headers);
                        }
                    }

                    @Override
                    public void onSuccess(ResponseBody body) {
                        try {
                            if (onCallback != null) {
                                onCallback.onSuccess(body.string());
                            }
                        } catch (IOException e) {
                            if (onCallback != null) {
                                onCallback.onError(RestCode.PARSE_ERROR, e.getMessage());
                            }
                        }
                        if (onCallback != null) {
                            onCallback.onAfter();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (onCallback != null) {
                            onCallback.onError(RestCode.REST_ERROR, e.getMessage());
                        }
                    }
                });
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
    public void download(Activity activity, String url, Map<String, Object> params, String dirName, String fileName, IOnCallback onCallback) {
        RestClient.create()
                .method(RestMethod.DOWNLOAD)
                .url(url)
                .tag(activity)
                .params(params)
                .fileName(fileName)
                .dirName(dirName)
                .build()
                .request(onCallback);
    }
}
