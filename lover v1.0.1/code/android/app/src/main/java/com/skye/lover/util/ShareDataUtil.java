package com.skye.lover.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.skye.lover.ConstantUtil;

/**
 * 全局SharedPreferences管理
 */
public class ShareDataUtil {
    /**
     * SharedPreferences文件名称
     */
    private static final String SP_NAME = ConstantUtil.APPNAME + "_sharedata";
    /**
     * SharedPreferences
     */
    private static SharedPreferences sp;

    /**
     * 初始化
     *
     * @param context 上下文对象
     */
    private static void init(Context context) {
        if (sp == null) sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
    }

    /**
     * 获取key指定的String
     *
     * @param context 上下文对象
     * @param key     键值
     * @return 键值对应的String，如果没有对应，则返回""
     */
    public static String get(Context context, String key) {
        if (sp == null) init(context);
        if (sp == null) return "";
        return sp.getString(key, "");
    }

    /**
     * 把key指定的String设置为value
     *
     * @param context 上下文对象
     * @param key     键值
     * @param value   要设置的值
     */
    public static void set(Context context, String key, String value) {
        if (sp == null) init(context);
        if (sp == null) return;
        sp.edit().putString(key, value).apply();
    }

    /**
     * 移除key指定的String
     *
     * @param context 上下文对象
     * @param key     键值
     */
    public static void remove(Context context, String key) {
        if (sp == null) init(context);
        if (sp == null) return;
        sp.edit().remove(key).apply();
    }

    /**
     * 清空数据
     *
     * @param context 上下文对象
     */
    public static void clear(Context context) {
        if (sp == null) init(context);
        if (sp == null) return;
        sp.edit().clear().apply();
    }

}
