package com.lven.retrofit.api;

import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Tag;
import retrofit2.http.Url;

/**
 * restful风格：Retrofit基本网络请求
 */
public interface RestService {
    /**
     * GET 用于查询资源
     */
    @GET
    Call<ResponseBody> get(@Url String url, @HeaderMap Map<String, String> headers, @QueryMap Map<String, Object> params, @Tag String tag);

    /**
     * 用于删除服务端的资源
     */
    @DELETE
    Call<ResponseBody> delete(@Url String url, @HeaderMap Map<String, String> headers, @QueryMap Map<String, Object> params, @Tag String tag);


    /**
     * 表单形式
     * 用于创建、更新服务端的资源
     */
    @FormUrlEncoded
    @POST
    Call<ResponseBody> post(@Url String url, @HeaderMap Map<String, String> headers, @FieldMap Map<String, Object> params, @Tag String tag);

    /**
     * 表单形式
     * 用于更新服务端的资源
     */
    @FormUrlEncoded
    @PUT
    Call<ResponseBody> put(@Url String url, @HeaderMap Map<String, String> headers, @FieldMap Map<String, Object> params, @Tag String tag);

    /**
     * JSON形式
     * 用于创建、更新服务端的资源
     */
    @POST
    Call<ResponseBody> post(@Url String url, @HeaderMap Map<String, String> headers, @Body RequestBody requestBody, @Tag String tag);

    /**
     * JSON形式
     * 用于更新服务端的资源
     */
    @PUT
    Call<ResponseBody> put(@Url String url, @HeaderMap Map<String, String> headers, @Body RequestBody requestBody, @Tag String tag);

    /**
     * 下载
     * 下载是直接到内存,所以需要 @Streaming
     */
    @Streaming
    @GET
    Call<ResponseBody> download(@Url String url, @HeaderMap Map<String, String> headers, @QueryMap Map<String, Object> params, @Tag String tag);

    /**
     * 上传
     */
    @Multipart
    @POST
    Call<ResponseBody> upload(@Url String url, @HeaderMap Map<String, String> headers, @Body RequestBody multipartBody, @Tag String tag);


}
