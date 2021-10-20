package com.lven.retrofit.callback;

/**
 * ================================================================
 * <p>
 * 作者：Lven （61128910@qq.com）
 * <p>
 * 时间: 2019-11-15 13:22
 * <p>
 * 描述：统一回调
 * ================================================================
 */
public interface IObjectCallback {
    /**
     * 请求成功回调事件处理
     *
     * @param json 返回的Json
     * @param data 默认返回空，要自己解析
     */
    void onSuccess(String json, Object data, Class clazz, int requestCode);

    /**
     * 请求失败回调事件处理
     */
    void onError(int code, String message, int requestCode);

}  
