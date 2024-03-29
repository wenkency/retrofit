# retrofit

1. retrofit网络请求封装库， 支持get post put delete download upload 。
2. 支持表单(postForm、putForm)请求。 文件上传、下载,支持进度回调。
3. 支持在Activity销毁时，自动取消网络。
4. 支持一个页面，处理多个接口。

### 引入

```
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}


implementation 'com.github.wenkency:retrofit:2.2.0'
// 网络请求相关
// retrofit + okhttp + rxjava2
implementation 'com.squareup.retrofit2:retrofit:2.9.0'
implementation 'com.squareup.retrofit2:adapter-rxjava2:2.9.0'
implementation "com.squareup.okhttp3:okhttp:4.9.1"
implementation "com.squareup.okio:okio:2.10.0"
implementation 'io.reactivex.rxjava2:rxandroid:2.1.1'
implementation 'io.reactivex.rxjava2:rxjava:2.2.21'
// gson
implementation 'com.google.code.gson:gson:2.8.8'
// 请求打印日志可选
// implementation 'com.squareup.okhttp3:logging-interceptor:4.9.1'


```

### Application初始化

```
public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // 1. 初始化
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        // 日志拦截器(可选)，自己依赖，参考Demo
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

### 通用解析，不同项目自己解析一次

```
/**
 * 参考解析（适合后台返回数据，直接生成的Bean），实际根据不用的项目，不同解析方案
 */
public abstract class BeanCallback<T> extends OnCallback {
    @Override
    public final void onSuccess(final String response) {
        // 1. 获取泛型
        Type actualType = ParameterTypeUtils.parameterType(this);
        // 如果是要字符串，直接返回
        if (ParameterTypeUtils.isString(actualType)) {
            onSucceed((T) response);
            return;
        }
        Single.just(actualType)
                .map(new Function<Type, T>() {
                    @Override
                    public T apply(Type type) throws Exception {
                        return RestCreator.getGSon().fromJson(response, type);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<T>() {
                    @Override
                    public void accept(T data) throws Exception {
                        onSucceed(data);
                    }
                });
    }

    public abstract void onSucceed(T data);

    @Override
    public void onError(int code, String message) {
        Log.e("onError",code+":"+message);
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
            MainPresenter.postB(this,this);
        } else if (requestCode == 1) { // B 接口回调
            tv.setText(data.toString());
            // B回调后，调C接口
            MainPresenter.postC(this,
                    new ObjectCallback(this,String.class,2));
        }else if (requestCode == 2){// C 接口回调
            tv.setText(data.toString());
        }
    }

    @Override
    public void onError(int code, String message, int requestCode) {
        Log.e("TAG", code + ":" + message);
    }

}
```

### 复杂数据JavaBean写法
```
/**
 * JavaBean写法
 */
public class PostBean {
    private String id;
    // 要转成Json的集合
    @FieldToJson
    private List<String> list;
    // 要转成Json的对象
    @FieldToJson
    private ObjectBean bean;


    public PostBean(String id) {
        this.id = id;
        bean = new ObjectBean("object bean");
        list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add("test:" + i);
        }
    }
}

```