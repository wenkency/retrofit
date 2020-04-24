package com.lven.retrofit.callback;

import com.lven.retrofit.api.RestCode;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 回调
 */
public class RestCallback implements Callback<ResponseBody> {
    IOnCallback onCallback;

    public RestCallback(IOnCallback onCallback) {
        this.onCallback = onCallback;
    }

    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        if (response.isSuccessful() && call.isExecuted()) {
            if (onCallback != null) {
                try {
                    onCallback.onSuccess(response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            if (onCallback != null) {
                onCallback.onError(response.code(), response.message());
            }
        }
        if (onCallback != null) {
            onCallback.onAfter();
        }
    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable e) {
        if (onCallback != null) {
            onCallback.onError(RestCode.REST_ERROR, e.getMessage());
        }
        if (onCallback != null) {
            onCallback.onAfter();
        }
    }
}
