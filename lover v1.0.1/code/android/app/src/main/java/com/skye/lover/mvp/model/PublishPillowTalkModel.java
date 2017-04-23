package com.skye.lover.mvp.model;

import android.content.Context;

import com.skye.lover.model.Cross;

import java.io.File;
import java.util.List;

/**
 * 发表悄悄话模型
 */
public interface PublishPillowTalkModel {
    /**
     * 上传文件
     *
     * @param files 文件集合
     * @return 穿越
     */
    Cross uploadFiles(List<File> files);

    /**
     * 发表悄悄话
     *
     * @param context 上下文对象
     * @param type    类型【0:悄悄话；1:广播】
     * @param content 悄悄话内容
     * @param imgs    悄悄话中附带的图片url
     * @return 穿越
     */
    Cross publishPillowTalk(Context context, String type, String content, String imgs);
}
