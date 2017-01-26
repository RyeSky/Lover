package com.skye.lover.model;

import com.google.gson.annotations.Expose;

/**
 *上传文件返回数据
 */
public class FilePaths {
    @Expose
    private String filePaths;//文件路径，多个路径用英文逗号分隔

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
