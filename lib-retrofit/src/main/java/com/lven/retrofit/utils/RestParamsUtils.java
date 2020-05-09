package com.lven.retrofit.utils;


import android.text.TextUtils;

import com.lven.retrofit.callback.IOnProgress;
import com.lven.retrofit.core.RestCreator;
import com.lven.retrofit.core.RestMultipartBody;

import java.io.File;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class RestParamsUtils {
    /**
     * Json请求体
     */
    public static RequestBody requestBody(Map<String, Object> params) {
        return RequestBody.create(RestCreator.getGSon().toJson(params),
                MediaType.parse("application/json;charset=UTF-8"));
    }

    /**
     * 文件上传请求体
     */
    public static MultipartBody multipartBody(Map<String, Object> params, IOnProgress onCallback) {
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        for (String key : params.keySet()) {
            if (TextUtils.isEmpty(key)) {
                continue;
            }
            Object value = params.get(key);
            if (value == null) {
                continue;
            }
            if (value instanceof File) {
                File file = (File) value;
                fileToBody(builder, key, file, onCallback);
            } else if (value instanceof List) {
                List<File> files = (List<File>) value;
                for (File file : files) {
                    fileToBody(builder, key, file, onCallback);
                }
            } else {
                builder.addFormDataPart(key, String.valueOf(value));
            }
        }
        MultipartBody multipartBody = builder.build();
        return multipartBody;
    }

    /**
     * 创建文件体
     */
    private static void fileToBody(MultipartBody.Builder builder, String key, File file, IOnProgress onCallback) {
        RequestBody fileBody = RequestBody.create(file, MediaType.parse(guessFileType(file.getAbsolutePath())));
        builder.addFormDataPart(key, file.getName(), new RestMultipartBody(fileBody, onCallback));
    }

    /**
     * 猜测文件类型
     */
    private static String guessFileType(String path) {
        String contentType = URLConnection.getFileNameMap().getContentTypeFor(path);
        if (contentType == null) {
            contentType = "application/octet-stream";
        }
        return contentType;
    }
}
