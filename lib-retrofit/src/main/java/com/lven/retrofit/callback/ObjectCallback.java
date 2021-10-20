package com.lven.retrofit.callback;

import com.lven.retrofit.core.RestCreator;

/**
 * 接口形式回调，一个页面多个请求统一回调，有人喜欢这样用
 * 如果clazz指定，就帮忙解析
 */
public class ObjectCallback extends OnCallback {
    /**
     * 回调
     */
    private IObjectCallback callback;
    /**
     * 如果clazz指定，就帮忙解析
     */
    private Class clazz;
    /**
     * 请求码，用来区分不同的请求
     */
    private int requestCode;

    public ObjectCallback(IObjectCallback callback) {
        this(callback, null);
    }

    public ObjectCallback(IObjectCallback callback, Class clazz) {
        this(callback, clazz, -1);
    }


    public ObjectCallback(IObjectCallback callback, Class clazz, int requestCode) {
        this.callback = callback;
        this.clazz = clazz;
        this.requestCode = requestCode;
    }

    @Override
    public void onSuccess(String response) {
        if (callback == null) {
            return;
        }
        try {
            Object data = null;
            if (clazz != null) {
                if (clazz == String.class) {
                    data = response;
                } else {
                    data = RestCreator.getGSon().fromJson(response, clazz);
                }
            }
            callback.onSuccess(response, data, clazz, requestCode);
        } catch (Throwable e) {
            // 解析失败，原封不动返回
            callback.onSuccess(response, null, clazz, requestCode);
        }

    }

    @Override
    public void onError(int code, String message) {
        if (callback == null) {
            return;
        }
        callback.onError(code, message, requestCode);
    }
}
