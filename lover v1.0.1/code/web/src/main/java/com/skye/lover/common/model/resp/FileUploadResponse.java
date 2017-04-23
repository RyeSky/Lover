package com.skye.lover.common.model.resp;

import com.google.gson.annotations.Expose;

/**
 * 文件上传响应
 */
public class FileUploadResponse {
    /**
     * 文件相对路径，多个路径用英文逗号分隔
     */
    @Expose
    public String filePaths;

    public FileUploadResponse(String filePaths) {
        this.filePaths = filePaths;
    }

    @Override
    public String toString() {
        return "FileUploadResponse{" +
                "filePaths='" + filePaths + '\'' +
                '}';
    }
}
