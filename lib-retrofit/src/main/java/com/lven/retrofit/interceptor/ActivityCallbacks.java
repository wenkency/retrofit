package com.lven.retrofit.interceptor;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.lven.retrofit.core.RestCreator;
import com.lven.retrofit.utils.RestTagUtils;

import java.util.List;

import okhttp3.Call;

/**
 * 根据TAG，在Activity销毁时，自动关联取消网络
 */
public class ActivityCallbacks implements Application.ActivityLifecycleCallbacks {
    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
        // 在这里取消当前Activity的网络请求
        String tag = RestTagUtils.getTag(activity);
        List<Call> calls = RestCreator.getOkHttpClient().dispatcher().runningCalls();
        for (Call call : calls) {
            if (tag.equals(call.request().tag(String.class))) {
                call.cancel();
            }
        }
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {

    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {

    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {

    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

    }


}
