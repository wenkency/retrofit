package com.lven.retrofit.utils;



import com.lven.retrofit.core.RestCreator;

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
        return RequestBody.create(RestCreator.getGson().toJson(params),
                MediaType.parse("application/json;charset=UTF-8"));
    }

    /**
     * 文件上传请求体
     */
    public static MultipartBody multipartBody(Map<String, Object> params) {
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        for (String key : params.keySet()) {
            Object value = params.get(key);
            if (value == null) {
                continue;
            }
            if (value instanceof File) {
                File file = (File) value;
                fileToBody(builder, key, file);
            } else if (value instanceof List) {
                List<File> files = (List<File>) value;
                for (File file : files) {
                    fileToBody(builder, key, file);
                }
            } else {
                builder.addFormDataPart(key, String.valueOf(value));
            }
        }
        return builder.build();
    }

    /**
     * 创建文件体
     */
    private static void fileToBody(MultipartBody.Builder builder, String key, File file) {
        RequestBody fileBody = RequestBody.create(file, MediaType.parse(guessFileType(file.getAbsolutePath())));
        builder.addFormDataPart(key, file.getName(), fileBody);
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
