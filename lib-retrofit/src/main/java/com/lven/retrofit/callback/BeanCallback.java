package com.lven.retrofit.callback;

import com.lven.retrofit.core.RestCreator;
import com.lven.retrofit.parse.ParameterTypeUtils;

import java.lang.reflect.Type;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 这里是通用解析
 *
 * 其实最好自己解析，根据不用的项目，不同解析方案
 */
public abstract class BeanCallback<T> extends OnCallback {
    @Override
    public final void onSuccess(final String response) {
        // 1. 获取泛型
        Type actualType = ParameterTypeUtils.parameterType(this);
        // 如果是要字符串，直接返回
        if (ParameterTypeUtils.isString(actualType)) {
            onSucceed((T) response);
            return;
        }
        Single.just(actualType)
                .map(new Function<Type, T>() {
                    @Override
                    public T apply(Type type) throws Exception {
                        return RestCreator.getGSon().fromJson(response, type);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<T>() {
                    @Override
                    public void accept(T data) throws Exception {
                        onSucceed(data);
                    }
                });
    }

    public abstract void onSucceed(T data);

    @Override
    public void onError(int code, String message) {

    }
}
