package com.lven.retrofitdemo;

import android.app.Activity;

import com.lven.retrofit.RetrofitPresenter;
import com.lven.retrofit.callback.IObjectCallback;
import com.lven.retrofit.callback.ObjectCallback;
import com.lven.retrofitdemo.callback.PostBean;

public class MainPresenter {
    /**
     * 多个请求回调到一个方法
     */
    public static void postA(Activity activity, IObjectCallback callback) {
        PostBean bean = new PostBean("1001");
        RetrofitPresenter.post(activity,
                "post", bean,
                new ObjectCallback(callback, String.class, 0));
    }

    /**
     * 多个请求回调到一个方法
     */
    public static void postB(Activity activity, IObjectCallback callback) {
        PostBean bean = new PostBean("1002");
        RetrofitPresenter.post(activity,
                "post", bean,
                new ObjectCallback(callback, String.class, 1));
    }
    /**
     * 多个请求回调到一个方法
     */
    public static void postC(Activity activity, ObjectCallback callback) {
        PostBean bean = new PostBean("1003");
        RetrofitPresenter.post(activity,
                "post", bean,
               callback);
    }
}
