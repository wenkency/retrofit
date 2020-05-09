package com.lven.retrofit.core.rx;


import com.lven.retrofit.api.RestConfig;
import com.lven.retrofit.api.RestMethod;
import com.lven.retrofit.api.RxRestService;
import com.lven.retrofit.callback.IOnProgress;
import com.lven.retrofit.core.RestCreator;
import com.lven.retrofit.utils.RestParamsUtils;

import java.util.Map;

import io.reactivex.Single;
import okhttp3.ResponseBody;


public class RxRestClient {
    String url;
    Map<String, String> headers;
    Map<String, Object> params;
    RestMethod method;
    String dirName, fileName;
    String tag;
    IOnProgress onProgress;

    public static RxRestBuilder create() {
        return new RxRestBuilder();
    }

    public void apply(RxRestBuilder builder) {
        this.url = builder.url;
        this.headers = builder.headers;
        this.params = builder.params;
        this.method = builder.method;
        this.tag = builder.tag;
        this.dirName = builder.dirName;
        this.fileName = builder.fileName;
        this.onProgress = builder.onProgress;
        // 在这里添加公共请求头和请求参数
        Map<String, String> commHeaders = RestConfig.getInstance().getCommHeaders();
        if (this.headers != null && commHeaders != null) {
            this.headers.putAll(commHeaders);
        }
        Map<String, Object> commParams = RestConfig.getInstance().getCommParams();
        if (this.params != null && commParams != null) {
            this.params.putAll(commParams);
        }
    }

    public Single<ResponseBody> request() {
        RxRestService service = RestCreator.getRxRestService();
        Single<ResponseBody> call = null;
        switch (method) {
            case GET:
                call = service.get(url, headers, params, tag);
                break;
            case DELETE:
                call = service.delete(url, headers, params, tag);
                break;
            case PUT_FORM:
                call = service.put(url, headers, params, tag);
                break;
            case POST_FORM:
                call = service.post(url, headers, params, tag);
                break;
            case POST:
                call = service.post(url, headers, RestParamsUtils.requestBody(params), tag);
                break;
            case PUT:
                call = service.put(url, headers, RestParamsUtils.requestBody(params), tag);
                break;
            case UPLOAD:
                call = service.upload(url, headers, RestParamsUtils.multipartBody(params, onProgress), tag);
                break;
            case DOWNLOAD:
                call = service.download(url, headers, params, tag);
                break;
        }
        return call;
    }


}
