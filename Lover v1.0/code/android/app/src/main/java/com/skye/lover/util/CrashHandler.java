package com.skye.lover.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.text.TextUtils;

import com.skye.lover.ConstantUtil;
import com.skye.lover.R;
import com.skye.lover.model.BaseResponse;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 程序崩溃后，收集崩溃信息
 */
public class CrashHandler implements UncaughtExceptionHandler {
    private final String CRASHFILENAME = "crash.txt";
    private UncaughtExceptionHandler mDefaultHandler;   // 系统默认的UncaughtException处理类
    private static CrashHandler INSTANCE = new CrashHandler(); // CrashHandler实例
    private Context context; // 程序的Context对象
    // 用于格式化日期,作为日志文件名的一部分
    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final boolean SAVE_CRASH_INFO_2_FILE = true;

    private final String SP_NAME = ConstantUtil.APPNAME + "_crashdata";
    private SharedPreferences sp;
    private final String MOBILE_BRAND = "mobileBrand";
    private final String MOBILE_VERSION = "mobileVersion";
    private final String PLATFORM = "platform";
    private final String PLATFORM_VERSION = "platformVersion";
    private final String APP_VERSION = "appVersion";
    private final String BUG_DETAILS = "bugDetails";


    /**
     * 提交bug接口回调
     */
    private Callback callbackSubmitBug = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            e.printStackTrace();
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            try {
                BaseResponse br = CommonUtil.parseToObject(response.body().string(), BaseResponse.class);
                if (br.check())//提交成功
                    clear();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    /**
     * 清除数据
     */
    private void clear() {
        if (sp == null) return;
        sp.edit().clear().commit();
    }

    /**
     * 保证只有一个CrashHandler实例
     */
    private CrashHandler() {
    }

    /**
     * 获取CrashHandler实例 ,单例模式
     */
    public static CrashHandler getInstance() {
        return INSTANCE;
    }


    /**
     * 判断之前是否崩溃过
     */
    private boolean isCrashed() {
        return !TextUtils.isEmpty(getShareStringData(BUG_DETAILS));
    }


    /**
     * 初始化
     *
     * @param context
     */
    public void init(Context context) {
        this.context = context;
        if (sp == null) {
            sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        }
        if (isCrashed()) {
            Map<String, String> params = new HashMap<String, String>();
            params.put(MOBILE_BRAND, Build.BRAND);
            params.put(MOBILE_VERSION, Build.PRODUCT);
            params.put(PLATFORM, "0");
            params.put(PLATFORM_VERSION, getShareStringData(PLATFORM_VERSION));
            params.put(APP_VERSION, getShareStringData(APP_VERSION));
            params.put(BUG_DETAILS, getShareStringData(BUG_DETAILS));
            OkHttpUtil.doPost(context, URLConfig.ACTION_SUBMIT_BUG, params, callbackSubmitBug);
        }
        // 获取系统默认的UncaughtException处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        // 设置该CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * 当UncaughtException发生时会转入该函数来处理
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && mDefaultHandler != null) {
            // 如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            try {
                Thread.currentThread();
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // 退出程序
            CommonUtil.exit();
        }
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     *
     * @param ex
     * @return true:如果处理了该异常信息;否则返回false.
     */
    private boolean handleException(Throwable ex) {
        if (ex == null)
            return false;
        // 使用Toast来显示异常信息
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                CommonUtil.toast(context, R.string.crash);
                Looper.loop();
            }
        }.start();
        save(ex);
        return true;
    }

    /**
     * 保存错误信息到文件中
     *
     * @param ex
     */
    private void save(Throwable ex) {
        String bug = "";
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String exs = writer.toString();
        if (exs.length() >= 8000) {
            exs = exs.substring(0, 7999);
        }
        bug = formatter.format(new Date()) + "\n" + exs;
        CommonUtil.log(bug);
        try {// 保存bug成文件
            if (SAVE_CRASH_INFO_2_FILE && CommonUtil.isExistSdcard()) {
                String path = Environment.getExternalStorageDirectory() + "/crash/";
                File dir = new File(path);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                FileOutputStream fos = new FileOutputStream(path + CRASHFILENAME);
                fos.write(bug.getBytes());
                fos.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        bug = bug.replace("\"", "\\").replace("\n", "---");
        //保存崩溃日志和崩溃当次所处的app版本与系统版本
        setShareStringData(PLATFORM_VERSION, Build.VERSION.SDK);
        setShareStringData(APP_VERSION, CommonUtil.getVersionName(context));
        setShareStringData(BUG_DETAILS, bug);
    }

    private String getShareStringData(String key) {
        if (sp == null) return "";
        return sp.getString(key, "");
    }

    private void setShareStringData(String key, String value) {
        if (sp == null) return;
        sp.edit().putString(key, value).commit();
    }
}
