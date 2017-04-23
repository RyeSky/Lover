package com.skye.lover;

import android.os.Environment;

import com.skye.lover.util.CommonUtil;

import java.io.File;

/**
 * 常量类
 */
public class ConstantUtil {
    public static final String APPNAME = "lover";//应用名称
    public static final boolean DEBUG = true;//调试模式
    public static final int MAX_PHOTOS_SIZE = 9;//最大选中张数
    public static final String PARAMETER = "parameter";// 请求参数名
    public static final String BASE_PATH = Environment.getExternalStorageDirectory().getPath() + File.separator
            + APPNAME + File.separator;// 手机本地的文件存放位置
    public static final String PICTURE_PATH = BASE_PATH + "picture" + File.separator;// 存放图片
    public static final int NETWORK_REQUEST_CACHE_SIZE = 10 * 1024 * 1024;//网络请求缓存大小
    public static final String SOFT_INPUT_HEIGHT = "softInputHeight";//软键盘高度
    public static final int DEFAULT_SOFT_INPUT_HEIGHT = CommonUtil.getWindowHeight(LoverApplication.getInstance()) / 2;//默认的软键盘高度
    public static final String ACTION_RECEIVE_PRIVATE_MESSAGE = "com.skye.lover.action.receiver_private_message";//接收到私信消息

    private ConstantUtil() {
    }
}
