package com.lven.retrofit.core;

import com.lven.retrofit.callback.IOnProgress;

import java.io.IOException;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;

/**
 * 文件上传，进度监听的代理类
 */

public class RestMultipartBody extends RequestBody {
    private MultipartBody mRequestBody;
    private IOnProgress mCallback;
    private int mCurrentLength;

    public RestMultipartBody(MultipartBody requestBody, IOnProgress callback) {
        mRequestBody = requestBody;
        mCallback = callback;
    }

    @Override
    public MediaType contentType() {
        return mRequestBody.contentType();
    }

    @Override
    public long contentLength() throws IOException {
        return mRequestBody.contentLength();
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        final long totalLength = contentLength();
        // 用OKIO的代理类
        ForwardingSink forwardingSink = new ForwardingSink(sink) {
            @Override
            public void write(Buffer source, long byteCount) throws IOException {
                super.write(source, byteCount);
                mCurrentLength += byteCount;
                // 上传进度回调
                if (mCallback != null) {
                    float progress = mCurrentLength * 1.0f / totalLength;
                    Single.just(progress)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Consumer<Float>() {
                                @Override
                                public void accept(Float progress) throws Exception {
                                    mCallback.onProgress(progress, mCurrentLength, totalLength);
                                }
                            });
                }
            }
        };
        BufferedSink buffer = Okio.buffer(forwardingSink);
        mRequestBody.writeTo(buffer);
        buffer.flush();
    }
}
