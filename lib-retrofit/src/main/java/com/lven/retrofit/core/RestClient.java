package com.lven.retrofit.core;

import com.lven.retrofit.api.RestConfig;
import com.lven.retrofit.api.RestMethod;
import com.lven.retrofit.api.RestService;
import com.lven.retrofit.callback.IOnCallback;
import com.lven.retrofit.callback.RestCallback;
import com.lven.retrofit.utils.RestParamsUtils;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;

public class RestClient {
    String url;
    Map<String, String> headers;
    Map<String, Object> params;
    RestMethod method;
    // 文件下载相关
    String dirName, fileName;
    String tag;

    public static RestBuilder create() {
        return new RestBuilder();
    }

    public void apply(RestBuilder builder) {
        this.url = builder.url;
        this.headers = builder.headers;
        this.params = builder.params;
        this.method = builder.method;
        this.dirName = builder.dirName;
        this.fileName = builder.fileName;
        this.tag = builder.tag;
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

    public void request(IOnCallback onCallback) {
        RestService service = RestCreator.getRestService();
        Call<ResponseBody> call = null;
        if (onCallback != null) {
            onCallback.onBefore(headers);
        }
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
                call = service.upload(url, headers, RestParamsUtils.multipartBody(params, onCallback), tag);
                break;
            case DOWNLOAD:
                new RestDownloadClient(tag, url, headers, params, dirName, fileName, onCallback).download();
                return;
        }
        if (call != null) {
            call.enqueue(new RestCallback(onCallback));
        }

    }


}
