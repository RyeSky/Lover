package com.skye.lover.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 共用工具类
 */
public class CommonUtil {
    public static final Gson gson = new GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation().create();

    public static <T> T parseJsonToObject(String json, Class<T> classOfT) {
        T t = null;
        try {
            t = gson.fromJson(json, classOfT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }

    public static ApplicationContext getContext() {
        return new ClassPathXmlApplicationContext("application_context.xml");
    }

    /**
     * 判断字符串是否为空
     *
     * @param parameter 要判断的字符串
     * @return 如果parameter为空则返回true
     */
    public static boolean isBlank(CharSequence parameter) {
        return parameter == null || parameter.length() == 0;
    }

    /**
     * 打印日志
     *
     * @param message 日志信息
     */
    public static void log(String message) {
        if (ConstantUtil.DEBUG) {
            System.out.println((isBlank(message) ? "" : message));
        }
    }

    /**
     * 从字符串获取时间截
     *
     * @param parameter 带有时间格式的字符串
     * @return 时间截
     */
    public static String getTimestamp(String parameter) {
        if (isBlank(parameter))
            return "";
        long time = 0l;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            time = sdf.parse(parameter).getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return time == 0l ? "" : (time + "");
    }

    /**
     * 从时间截获取字符串
     *
     * @param timestamp 时间截
     * @return 时间格式字符串
     */
    public static String getTime(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date(timestamp));
    }

    /**
     * byte数组转String
     *
     * @param param byte数组
     * @return String
     */
    public static String getStringFromByteArray(byte[] param) {
        String content = "";
        try {
            content = new String(param, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content;
    }
}
