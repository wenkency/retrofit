# retrofit
retrofit网络请求封装库，支持get post put delete download upload 表单(postForm、putForm)请求.
支持文件上传、下载支持进度回调。

### 引入

```
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}


implementation 'com.github.wenkency:retrofit:1.1.0'


```
### Application初始化
```
public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // 1. 初始化
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        // 日志拦截器，自己依赖，参考Demo
        interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);

        RestConfig.getInstance()
                .setBaseUrl("http://httpbin.org/")
                .setDebugUrl("http://httpbin.org/")
                .setDebugUrl("https://api.car-house.cn/")
                .setCommHeaders(null) // 添加公共请求头，根据项目自己添加
                .setCommParams(null)// 添加公共请求参数，根据项目自己添加
                .setDebug(true)
                .addInterceptor(interceptor)
                // 可设置10内再次请求，走缓存
                .addNetInterceptor(new CacheInterceptor(10))
                .register(this);
    }
}
```

### 使用方式
```
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
```