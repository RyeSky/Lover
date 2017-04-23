package com.skye.lover.util;

import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

/**
 * handler内部类
 */
public abstract class CommonInnerHandler<T> extends Handler {
    private WeakReference<T> reference;

    public CommonInnerHandler(T t) {
        this.reference = new WeakReference<>(t);
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        T t = reference == null ? null : reference.get();
        handleMessage(t, msg);
    }

    public abstract void handleMessage(T t, Message msg);
}
