package com.lven.retrofit.callback;

import com.lven.retrofit.api.RestCode;

import java.io.File;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

public class FileSingleObserver implements SingleObserver<File> {
    IOnCallback onCallback;

    public FileSingleObserver(IOnCallback onCallback) {
        this.onCallback = onCallback;
    }

    @Override
    public void onSubscribe(Disposable d) {
    }

    @Override
    public void onSuccess(File value) {
        if (onCallback != null) {
            onCallback.onSuccess(value.getAbsolutePath());
        }
        if (onCallback != null) {
            onCallback.onAfter();
        }
    }

    @Override
    public void onError(Throwable e) {
        if (onCallback != null) {
            onCallback.onError(RestCode.DOWNLOAD_ERROR, e.getMessage());
        }
        if (onCallback != null) {
            onCallback.onAfter();
        }
    }
}
