package com.lven.retrofit.callback;

import java.util.Map;

/**
 * 默认回调
 */
public class OnCallback implements IOnCallback {
    @Override
    public void onAfter() {

    }

    @Override
    public void onBefore(Map<String, String> headers) {

    }

    @Override
    public void onError(int code, String message) {

    }

    @Override
    public void onProgress(float progress, float current, float total) {

    }

    @Override
    public void onSuccess(String response) {

    }
}
