package com.skye.lover.pillowtalk.pillowtalk.model.resp;

import com.google.gson.annotations.Expose;

/**
 * 悄悄话实体类
 */
public class PillowTalk {
    /**
     * 悄悄话
     */
    public static final int TYPE_PILLOW_TALK = 0;
    /**
     * 广播
     */
    public static final int TYPE_BROADCAST = 1;
    /**
     * 表名称
     */
    public static final String TABLE_NAME = "pillow_talk";
    /**
     * 记录
     */
    public static final String ID = "id";
    /**
     * 登录用户id
     */
    public static final String USER_ID = "user_id";
    /**
     * 发表者id
     */
    public static final String PUBLISHER = "publisher";
    /**
     * 发表者昵称
     */
    public static final String PUBLISHER_NICKNAME = "publisher_nickname";
    /**
     * 发表者头像
     */
    public static final String PUBLISHER_AVATAR = "publisher_avatar";
    /**
     * 发表者性别【0：保密；1：男；2：女】
     */
    public static final String PUBLISHER_GENDER = "publisher_gender";
    /**
     * 悄悄话的对方id
     */
    public static final String ANOTHER = "another";
    /**
     * 悄悄话的对方昵称
     */
    public static final String ANOTHER_NICKNAME = "another_nickname";
    /**
     * 悄悄话的对方头像
     */
    public static final String ANOTHER_AVATAR = "another_avatar";
    /**
     * 悄悄话的对方性别【0：保密；1：男；2：女】
     */
    public static final String ANOTHER_GENDER = "another_gender";
    /**
     * 内容
     */
    public static final String CONTENT = "content";
    /**
     * 图片路径，多张图片用逗号分隔
     */
    public static final String IMGS = "imgs";
    /**
     * 类型【0:悄悄话；1:广播】
     */
    public static final String TYPE = "type";
    /**
     * 悄悄话发布者愿意公开悄悄话内容
     */
    public static final String PUBLISHER_OPEN = "publisher_open";
    /**
     * 悄悄话另一方同意公开悄悄话内容
     */
    public static final String ANOTHER_OPEN = "another_open";
    /**
     * 赞数量
     */
    public static final String PRAISE_COUNT = "praise_count";
    /**
     * 评论数量
     */
    public static final String COMMENT_COUNT = "comment_count";
    /**
     * 登录用户赞这条悄悄话产生的赞记录id
     */
    public static final String PRAISE_ID = "praise_id";
    /**
     * 登录用户收藏这条悄悄话产生的收藏记录id
     */
    public static final String COLLECT_ID = "collect_id";
    /**
     * 是否被删除【0：没有被删除；1：已经被删除】
     */
    public static final String DELETED = "deleted";
    /**
     * 悄悄话创建时间
     */
    public static final String CREATE_TIME = "create_time";

    /**
     * 记录
     */
    @Expose
    public String id;
    /**
     * 悄悄话的类型【0:悄悄话；1:广播】
     */
    @Expose
    public int type;
    /**
     * 发表者id
     */
    @Expose
    public String publisherId;
    /**
     * 发表者昵称
     */
    @Expose
    public String publisherNickname;
    /**
     * 发表者头像
     */
    @Expose
    public String publisherAvatar;
    /**
     * 发表者性别【0：保密；1：男；2：女】
     */
    @Expose
    public int publisherGender;
    /**
     * 悄悄话的对方id
     */
    @Expose
    public String anotherId;
    /**
     * 悄悄话的对方昵称
     */
    @Expose
    public String anotherNickname;
    /**
     * 悄悄话的对方头像
     */
    @Expose
    public String anotherAvatar;
    /**
     * 悄悄话的对方性别【0：保密；1：男；2：女】
     */
    @Expose
    public int anotherGender;
    /**
     * 内容
     */
    @Expose
    public String content;
    /**
     * 图片路径，多张图片用逗号分隔
     */
    @Expose
    public String imgs;
    /**
     * 悄悄话发布者愿意公开悄悄话内容
     */
    @Expose
    public int publisherOpen;
    /**
     * 悄悄话另一方同意公开悄悄话内容
     */
    @Expose
    public int anotherOpen;
    /**
     * 赞数量
     */
    @Expose
    public int praiseCount;
    /**
     * 评论数量
     */
    @Expose
    public int commentCount;
    /**
     * 登录用户赞这条悄悄话产生的赞记录id
     */
    @Expose
    public String praiseId;
    /**
     * 登录用户收藏这条悄悄话产生的收藏记录id
     */
    @Expose
    public String collectId;
    /**
     * 悄悄话创建时间
     */
    @Expose
    public String createTime;

    @Override
    public String toString() {
        return "PillowTalk{" +
                "id='" + id + '\'' +
                ", publisherId='" + publisherId + '\'' +
                ", publisherNickname='" + publisherNickname + '\'' +
                ", publisherAvatar='" + publisherAvatar + '\'' +
                ", anotherId='" + anotherId + '\'' +
                ", anotherNickname='" + anotherNickname + '\'' +
                ", anotherAvatar='" + anotherAvatar + '\'' +
                ", content='" + content + '\'' +
                ", imgs='" + imgs + '\'' +
                ", praiseId='" + praiseId + '\'' +
                ", collectId='" + collectId + '\'' +
                ", createTime='" + createTime + '\'' +
                ", publisherGender=" + publisherGender +
                ", anotherGender=" + anotherGender +
                ", praiseCount=" + praiseCount +
                ", commentCount=" + commentCount +
                ", publisherOpen=" + publisherOpen +
                ", anotherOpen=" + anotherOpen +
                ", type=" + type +
                '}';
    }
}
