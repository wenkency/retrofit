package com.lven.retrofitdemo;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.collection.ArrayMap;

import com.boardour.permission.OnPermissionCallbackAdapter;
import com.boardour.permission.Permission;
import com.boardour.permission.XPermission;
import com.lven.retrofit.RetrofitPresenter;
import com.lven.retrofit.RxRetrofitPresenter;
import com.lven.retrofit.api.RxRestService;
import com.lven.retrofit.callback.IObjectCallback;
import com.lven.retrofit.callback.ObjectCallback;
import com.lven.retrofit.callback.OnCallback;
import com.lven.retrofit.core.RestCreator;
import com.lven.retrofit.utils.RestUtils;

import java.io.File;
import java.util.Map;

import io.reactivex.Single;
import okhttp3.ResponseBody;

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

        XPermission.with(this)
                .permission(Permission.MANAGE_EXTERNAL_STORAGE)
                .request(new OnPermissionCallbackAdapter() {

                });
    }

    public synchronized void test() {
        RxRestService service = RestCreator.getRxRestService();
        Single<ResponseBody> get = service.get("", null, null, null);


    }

    public void request(View view) {
        // post();
        postObject();
    }

    public void multiGet() {
        RetrofitPresenter.get(this, "https://www.baidu.com", new OnCallback() {
            @Override
            public void onSuccess(String response) {
                Log.e("TAG", response);
                tv.setText(response);
            }

            @Override
            public void onError(int code, String message) {
                Log.e("TAG", code + ":" + message);
            }
        });
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
        // 请求写法一
       /* PostBean bean = new PostBean("1001");
        RetrofitPresenter.post(this, "post", bean, new OnCallback() {
            @Override
            public void onSuccess(String response) {
                tv.setText(response);
            }
        });*/
        // 请求写法二
        Map<String, Object> map = RestUtils.getParams();
        map.put("id", "1001");
        map.put("amount", 666);

        RetrofitPresenter.post(this, "post", map, new OnCallback() {
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
        RxRetrofitPresenter.download(this, url, "image", "image.jpg", new OnCallback() {
            @Override
            public void onProgress(float progress, float current, float total) {
                Log.e(TAG, progress + ":" + current + ":" + total);
            }

            @Override
            public void onError(int code, String message) {
                Log.e("TAG", code + ":" + message);
            }

            @Override
            public void onSuccess(String path) {
                tv.setText(path);
            }
        });
    }

    public void upload1() {
        File avatarFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                , "image.jpg");
        Map<String, Object> params = new ArrayMap<>();
        params.put("file", avatarFile);
        params.put("type", "project");
        RetrofitPresenter.upload(this,
                "https://api.zuihaoxiangmu.com/upload/file",
                params,
                new OnCallback() {
                    @Override
                    public void onProgress(float progress, float current, float total) {
                        super.onProgress(progress, current, total);
                        tv.setText(progress + ":" + current + ":" + total);
                    }

                    @Override
                    public void onError(int code, String message) {
                        Log.e("TAG", code + ":" + message);
                    }

                    @Override
                    public void onSuccess(String response) {
                        Log.e("TAG", response);
                        tv.setText(response);
                    }

                });
    }

    public void upload() {
        File avatarFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                , "image.jpg");
        Map<String, Object> params = new ArrayMap<>();
        params.put("goodsImg", avatarFile);
        RetrofitPresenter.upload(this,
                "https://api.car-house.cn/mapi/businessgoods/uploadGoodsImg/businessId_147102.json",
                params,
                new OnCallback() {
                    @Override
                    public void onProgress(float progress, float current, float total) {
                        super.onProgress(progress, current, total);
                        tv.setText(progress + ":" + current + ":" + total);
                    }

                    @Override
                    public void onSuccess(String response) {
                        tv.setText(response);
                    }

                    @Override
                    public void onError(int code, String message) {
                        Log.e("TAG", code + ":" + message);
                    }
                });
    }

    /**
     * post请求测试
     * 回调在当前页面
     * 调用顺序：A->B->C
     */
    public void postObject() {
        // A ->B ->C
        // 调A接口
        MainPresenter.postA(this, this);
    }

    @Override
    public void onSuccess(String json, Object data, Class clazz, int requestCode) {
        if (requestCode == 0) {// A 接口回调
            tv.setText(data.toString());
            // A回调后，调B接口
            MainPresenter.postB(this, this);
        } else if (requestCode == 1) { // B 接口回调
            tv.setText(data.toString());
            // B回调后，调C接口
            MainPresenter.getC(this,
                    new ObjectCallback(this, String.class, 2));
        } else if (requestCode == 2) {// C 接口回调
            tv.setText(data.toString());
            MainPresenter.getD(this, new ObjectCallback(this,
                    String.class, 3));
        } else if (requestCode == 3) {// D 接口回调
            tv.setText(data.toString());
        }
    }

    @Override
    public void onError(int code, String message, int requestCode) {
        Log.e("TAG", code + ":" + message);
    }

}
