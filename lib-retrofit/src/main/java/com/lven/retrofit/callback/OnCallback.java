package com.lven.retrofit.callback;

import android.util.Log;

import com.lven.retrofit.api.RestConfig;

import java.util.Map;

/**
 * 默认回调
 */
public class OnCallback implements IOnCallback {

    public static final String TAG = "OnCallback";

    @Override
    public void onAfter() {
        if (RestConfig.getInstance().isDebug()) {
            Log.e(TAG, "onAfter");
        }
    }

    @Override
    public void onBefore(Map<String, String> headers) {
        if (RestConfig.getInstance().isDebug()) {
            Log.e(TAG, "onBefore:" + (headers == null ? 0 : headers.size()));
        }
    }

    @Override
    public void onError(int code, String message) {
        if (RestConfig.getInstance().isDebug()) {
            Log.e(TAG, code + ":" + message);
        }
    }

    @Override
    public void onProgress(float progress, float current, float total) {
        if (RestConfig.getInstance().isDebug()) {
            Log.e("TAG", progress + ":" + current + ":" + total);
        }
    }

    @Override
    public void onSuccess(String response) {
        if (RestConfig.getInstance().isDebug()) {
            Log.e(TAG, response);
        }

    }
}
