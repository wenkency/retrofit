package com.lven.retrofit.adapter;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.lven.retrofit.core.RestCreator;
import com.lven.retrofit.utils.RestTagUtils;

import java.util.List;

import okhttp3.Call;

/**
 * 根据TAG，在Activity销毁时，自动关联取消网络
 */
public class ActivityCallbacks extends ActivityCallbacksAdapter {
    /**
     * 只关心销毁取消网络
     */
    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
        // 在这里取消当前Activity的网络请求
        String tag = RestTagUtils.getTag(activity);
        List<Call> calls = RestCreator.getOkHttpClient().dispatcher().runningCalls();
        if (calls == null || calls.isEmpty()) {
            return;
        }
        for (Call call : calls) {
            if (tag.equals(call.request().tag(String.class))) {
                call.cancel();
            }
        }
    }
}
