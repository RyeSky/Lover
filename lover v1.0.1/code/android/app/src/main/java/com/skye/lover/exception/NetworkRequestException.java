package com.skye.lover.exception;

import com.skye.lover.model.Cross;

/**
 * 网路请求异常类
 */
public class NetworkRequestException extends Exception {
    /**
     * 穿越
     */
    public final Cross cross;

    public NetworkRequestException(Cross cross, String message) {
        super(message);
        this.cross = cross;
    }

    @Override
    public String toString() {
        return "NetworkRequestException{" +
                "cross=" + cross +
                '}';
    }
}
