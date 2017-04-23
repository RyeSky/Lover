package com.skye.lover.util;

import android.util.SparseArray;

/**
 * 本地相册相片缩略图工具类
 */
public class ThumbnailsUtil {
    private static SparseArray<String> map = new SparseArray<>();

    /**
     * 获取指定key对应的value
     */
    public static String get(int key, String defaultValue) {
        if (map == null) return defaultValue;
        return map.get(key, defaultValue);
    }

    /**
     * 放入数据
     */
    public static void put(Integer key, String value) {
        map.put(key, value);
    }

    /**
     * 清空数据
     */
    public static void clear() {
        map.clear();
    }
}
