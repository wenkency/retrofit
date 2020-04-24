package com.lven.retrofit.core;


import com.lven.retrofit.callback.FileProgress;
import com.lven.retrofit.callback.FileSingleObserver;
import com.lven.retrofit.callback.IOnCallback;
import com.lven.retrofit.utils.RestFileUtils;

import java.io.File;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * 下载处理
 */
public class RxDownloadClient {
    String url;
    Map<String, String> headers;
    Map<String, Object> params;
    IOnCallback onCallback;
    // 文件下载相关
    String dirName, fileName;
    String tag;

    public RxDownloadClient(String url, Map<String, String> headers, Map<String, Object> params,
                            String dirName, String fileName, IOnCallback onCallback, String tag) {
        this.url = url;
        this.headers = headers;
        this.params = params;
        this.onCallback = onCallback;
        this.dirName = dirName;
        this.fileName = fileName;
        this.tag = tag;
    }

    public void download() {
        RestCreator.getRxRestService()
                .download(url, headers, params, tag)
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
