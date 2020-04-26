package com.lven.retrofit.api;

public interface RestCode {
    /**
     * 下载错误
     */
    int DOWNLOAD_ERROR = -1000;
    /**
     * 上传错误
     */
    int UPLOAD_ERROR = -1001;
    /**
     * 请求失败
     */
    int REST_ERROR = -1002;
    /**
     * 解析失败
     */
    int PARSE_ERROR = -1003;
}
