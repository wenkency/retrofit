package com.lven.retrofitdemo;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.lven.retrofit.RetrofitPresenter;
import com.lven.retrofit.callback.IObjectCallback;
import com.lven.retrofit.callback.ObjectCallback;
import com.lven.retrofit.callback.OnCallback;
import com.lven.retrofitdemo.callback.PostBean;

/**
 * Retrofit网络请求测试
 */
public class MainActivity extends AppCompatActivity implements IObjectCallback {
    public static final String TAG = "TAG";
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = findViewById(R.id.tv);
    }

    public void request(View view) {
        postObject();
    }

    /**
     * get请求测试
     */
    public void get() {
        // http://httpbin.org/get?id=1001
        RetrofitPresenter.get(this, "get", "id", "1001", new OnCallback() {
            @Override
            public void onSuccess(String response) {
                tv.setText(response);
            }
        });
    }

    /**
     * post请求测试
     */
    public void post() {
        // http://httpbin.org/post?id=1001
        PostBean bean = new PostBean("1001");
        RetrofitPresenter.post(this, "post", bean, new OnCallback() {
            @Override
            public void onSuccess(String response) {
                tv.setText(response);
            }
        });
    }

    /**
     * 下载测试
     */
    public void download() {
        String url = "https://img.car-house.cn/Upload/activity/20200424/J3GEiBhpAMfkesHCm7EWaQGwxDDwNbMc.png";
        // 下载后文件：/storage/emulated/0/Download/image/image.jpg
        RetrofitPresenter.download(this, url, "image", "image.jpg", new OnCallback() {
            @Override
            public void onProgress(float progress, float current, float total) {
                Log.e(TAG, progress + ":" + current + ":" + total);
            }

            @Override
            public void onSuccess(String path) {
                Log.e(TAG, path);
            }
        });
    }

    /**
     * post请求测试
     * 回调在当前页面
     * 其它也一样
     */
    public void postObject() {
        // http://httpbin.org/post?id=1001
        PostBean bean = new PostBean("1001");
        RetrofitPresenter.post(this, "post", bean, new ObjectCallback(this, String.class));
    }

    @Override
    public void onSuccess(String json, Object data, Class clazz) {
        tv.setText(data.toString());
    }

    @Override
    public void onError(int code, String message) {
        Log.e("TAG", code + ":" + message);
    }

}
