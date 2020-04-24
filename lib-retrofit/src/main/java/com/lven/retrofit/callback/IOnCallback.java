package com.lven.retrofit.callback;

import java.util.Map;

public interface IOnCallback extends IOnProgress {
    void onBefore(Map<String, String> headers);

    void onAfter();


    void onError(int code, String message);

    void onSuccess(String response);
}
