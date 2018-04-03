package com.common.utils;

import java.lang.reflect.Array;
import java.util.List;

/**
 * @author wulinpeng
 * @datetime: 18/2/17 下午3:22
 * @description:
 */
public class CollectionsUtil {

    /**
     * 泛型list转换为数组
     * @param src List<T> 原List
     * @return T[] 转换后的数组
     */
    public static <T> T[] listToArray(List<T> src, Class<T> type) {
        if (src == null || src.isEmpty()) {
            return null;
        }
        // 初始化泛型数组
        // JAVA中不允许这样初始化泛型数组： T[] dest = new T[src.size()];
        T[] dest = (T[]) Array.newInstance(type, src.size());
        for (int i = 0; i < src.size(); i++) {
            dest[i] = src.get(i);
        }
        return (T[]) dest;
    }
}
