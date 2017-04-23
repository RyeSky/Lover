package com.skye.lover.model;

import android.support.annotation.Keep;

import com.google.gson.annotations.Expose;

/**
 * 上传文件返回数据
 */
@Keep
public class FilePaths {
    /**
     * 文件路径，多个路径用英文逗号分隔
     */
    @Expose
    private String filePaths;

    public String getFilePaths() {
        return filePaths;
    }

    @Override
    public String toString() {
        return "FilePaths{" +
                "filePaths='" + filePaths + '\'' +
                '}';
    }
}
