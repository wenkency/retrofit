package com.lven.retrofitdemo.callback;

import com.lven.retrofit.api.FieldToJson;

import java.util.ArrayList;
import java.util.List;

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
