package com.lven.retrofit.core;

import com.lven.retrofit.api.RestCode;
import com.lven.retrofit.callback.FileProgress;
import com.lven.retrofit.callback.FileSingleObserver;
import com.lven.retrofit.callback.IOnCallback;
import com.lven.retrofit.utils.RestFileUtils;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Map;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 下载处理
 */
public class RestDownloadClient {
    private final String tag;
    private final String url;
    private final Map<String, String> headers;
    private final Map<String, Object> params;
    private final IOnCallback onCallback;
    // 文件下载相关
    private final String dirName, fileName;

    public RestDownloadClient(String tag,String url, Map<String, String> headers, Map<String, Object> params,
                              String dirName, String fileName, IOnCallback onCallback) {
        this.url = url;
        this.headers = headers;
        this.params = params;
        this.onCallback = onCallback;
        this.dirName = dirName;
        this.fileName = fileName;
        this.tag = tag;
    }

    public void download() {
        RestCreator.getRestService().download(url, headers, params, tag)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        // 处理下载
                        if (response.isSuccessful()) {
                            // 处理下载结果
                            handleResult(response.body());
                        } else {
                            if (onCallback != null) {
                                onCallback.onError(response.code(), response.message());
                            }
                            if (onCallback != null) {
                                onCallback.onAfter();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, @NotNull Throwable e) {
                        if (onCallback != null) {
                            onCallback.onError(RestCode.DOWNLOAD_ERROR, e.getMessage());
                        }
                        if (onCallback != null) {
                            onCallback.onAfter();
                        }
                    }
                });
    }

    private void handleResult(ResponseBody body) {
        // 在子线程去处理下载任务
        Single.just(body)
                .map(new Function<ResponseBody, File>() {
                    @Override
                    public File apply(ResponseBody body) throws Exception {
                        return handleFile(body);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new FileSingleObserver(onCallback));
    }

    /**
     * 这方法是在子线程的
     */
    private File handleFile(ResponseBody body) {
        final long total = body.contentLength();
        File file = RestFileUtils.writeToDisk(body.byteStream(),
                dirName,
                fileName,
                new FileProgress(onCallback, total));
        return file;
    }


}
