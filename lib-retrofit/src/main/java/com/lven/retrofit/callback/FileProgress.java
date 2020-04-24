package com.lven.retrofit.callback;

import com.lven.retrofit.utils.RestFileUtils;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

/**
 * 文件下载进度回调
 */
public class FileProgress implements RestFileUtils.OnProgress {
    IOnCallback onCallback;
    long total;

    public FileProgress(IOnCallback onCallback, long total) {
        this.onCallback = onCallback;
        this.total = total;
    }

    @Override
    public void onProgress(long current) {
        if (onCallback != null) {
            // 回调到主线程
            Single.just(current)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<Long>() {
                        @Override
                        public void accept(Long current) throws Exception {
                            onCallback.onProgress(current * 100f / total, current, total);
                        }
                    });

        }
    }
}
