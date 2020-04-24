package com.lven.retrofit.callback;

public interface IOnProgress {
    /**
     * 文件下载的进度
     */
    void onProgress(float progress, float current, float total);
}
