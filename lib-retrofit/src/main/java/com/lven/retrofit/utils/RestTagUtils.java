package com.lven.retrofit.utils;

public class RestTagUtils {
    public static String getTag(Object object) {
        String tag = "Tag_";
        if (object != null) {
            tag += object.getClass().getSimpleName() + "_" + object.hashCode();
        }
        return tag;
    }
}
