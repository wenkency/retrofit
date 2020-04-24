package com.lven.retrofit.parse;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * 泛型参数获取实际类型工具类
 */
public class ParameterTypeUtils {
    /**
     * 获取对象泛型的实际类型
     */
    public static Type parameterType(Object object) {
        Type[] params = ((ParameterizedType) object.getClass().getGenericSuperclass())
                .getActualTypeArguments();
        Type actualType = params[0];
        // 如果是List类型
        if (!(actualType instanceof Class)) {
            Type typeOfList = ((ParameterizedType) actualType).getActualTypeArguments()[0];
            // 创建List类型
            actualType = new ParameterizedTypeImpl(List.class, new Type[]{typeOfList});
        }
        return actualType;
    }

    public static boolean isString(Type type) {
        if (type instanceof Class) {
            return type.toString().contains("java.lang.String");
        }
        return false;
    }
}
