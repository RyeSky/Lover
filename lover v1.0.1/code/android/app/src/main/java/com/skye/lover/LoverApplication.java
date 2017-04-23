package com.skye.lover;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.StrictMode;
import android.text.TextUtils;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.skye.lover.util.CrashHandler;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import java.util.List;

import butterknife.ButterKnife;
import cn.jpush.android.api.JPushInterface;

/**
 * 成双全局
 */
public class LoverApplication extends Application {
    private static LoverApplication instance;
    private static RefWatcher refWatcher;

    /**
     * 获取全局上下文
     */
    public static LoverApplication getInstance() {
        return instance;
    }

    /**
     * 引用观察者
     */
    public static RefWatcher getRefWatcher() {
        return refWatcher;
    }

    /**
     * 初始化LeakCanary
     */
    private static void initLeakCanary(LoverApplication application) {
        if (LeakCanary.isInAnalyzerProcess(application)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        if (ConstantUtil.DEBUG)
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectAll().penaltyLog().penaltyDeath().build());
        refWatcher = LeakCanary.install(application);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        if (!isInited()) {
            ButterKnife.setDebug(ConstantUtil.DEBUG);
            Fresco.initialize(this);
            CrashHandler.getInstance().init(this);
            JPushInterface.setDebugMode(ConstantUtil.DEBUG);
            JPushInterface.init(this);
            initLeakCanary(this);
        }
    }

    /**
     * 判断应用数据是否已经被设置过，Application的onCreate会执行多次，避免全局数据设置多次（每次启动服务会执行一次onCreate）
     *
     * @return boolean 如果因为启动服务而执行onCreate返回true说明之前已经初始化,否则返回false
     */
    private synchronized boolean isInited() {
        int pid = android.os.Process.myPid();
        String processName = "";
        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> ls = am.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo info : ls) {
            if (pid == info.pid) {
                processName = info.processName;
                break;
            }
        }
        return TextUtils.isEmpty(processName) || !processName.equalsIgnoreCase(getPackageName());
    }
}
