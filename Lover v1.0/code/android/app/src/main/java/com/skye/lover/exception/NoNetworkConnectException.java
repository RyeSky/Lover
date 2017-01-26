package com.skye.lover.exception;

import com.skye.lover.LoverApplication;
import com.skye.lover.R;

import java.io.IOException;

/**
 * 没有网络链接异常
 */
public class NoNetworkConnectException extends IOException {
    public NoNetworkConnectException() {
        super(LoverApplication.getInstance().getString(R.string.net_not_ok));
    }
}
