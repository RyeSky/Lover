package com.skye.lover.model;

import android.support.annotation.Keep;

import com.google.gson.annotations.Expose;
import com.skye.lover.util.CommonUtil;

/**
 * 消息实体类
 * <h2 style='{text-align:center;}'>关于消息的说明</h2>
 * <table border="2" width="100%" style='{text-align:center;}' frame="hsides"
 * rules="all" summary='关于消息的说明'>
 * <tr>
 * <td>参数名</td>
 * <td>描述</td>
 * <td>参数类型</td>
 * </tr>
 * <tr>
 * <td>id</td>
 * <td>消息记录id</td>
 * <td>String</td>
 * </tr>
 * <tr>
 * <td>type</td>
 * <td>消息类型【0：系统消息；1：个人消息】</td>
 * <td>int</td>
 * </tr>
 * <tr>
 * <td>subType</td>
 * <td>消息子类型【type=0时暂无；type=1时【0：示爱；1:示爱被同意；2：被对方分手；3：回复；4：评论；5：私信自定义消息】】</td>
 * <td>int</td>
 * </tr>
 * <tr>
 * <td>title</td>
 * <td>消息标题</td>
 * <td>String</td>
 * </tr>
 * <tr>
 * <td>content</td>
 * <td>消息内容。如果是私信自定义消息，则为聊天内容</td>
 * <td>String</td>
 * </tr>
 * <tr>
 * <td>anotherId</td>
 * <td>当type=1,subType=0,1,2时，为操作主动方的id。例如有人向我示爱，anotherId就是那个人的id，anotherNickname、anotherAvatar、anotherGender类似</td>
 * <td>String</td>
 * </tr>
 * <tr>
 * <td>anotherNickname</td>
 * <td>昵称</td>
 * <td>String</td>
 * </tr>
 * <tr>
 * <td>anotherAvatar</td>
 * <td>头像</td>
 * <td>String</td>
 * </tr>
 * <tr>
 * <td>anotherGender</td>
 * <td>性别【0：保密；1：男；2：女】</td>
 * <td>int</td>
 * </tr>
 * <tr>
 * <td>pillowTalkId</td>
 * <td>当type=1,subType=0,1,2时，pillowTalkId被回复或评论的悄悄话id，pillowTalkType、imgs类似</td>
 * <td>String</td>
 * </tr>
 * <tr>
 * <td>pillowTalkType</td>
 * <td>悄悄话的类型</td>
 * <td>int</td>
 * </tr>
 * <tr>
 * <td>imgs</td>
 * <td>悄悄话里的图片url</td>
 * <td>String</td>
 * </tr>
 * <tr>
 * <td>clickAble</td>
 * <td>消息是否可以点击【0：不可点击;1可点击】</td>
 * <td>int</td>
 * </tr>
 * <tr>
 * <td>createTime</td>
 * <td>消息创建时间时间截</td>
 * <td>String</td>
 * </tr>
 * </table>
 */
@Keep
public class Message {
    public static final int TYPE_SYSTEM = 0;//系统消息
    public static final int TYPE_PERSONAL = 1;//个人消息
    public static final int COURTSHIPDISPLAY = 0;//示爱
    public static final int COURTSHIPDISPLAY_BE_AGREED = 1;//示爱被同意
    public static final int BE_BROKE_UP = 2;//被对方分手
    public static final int REPLY = 3;//回复
    public static final int COMMENT = 4;//评论
    public static final int PRIVATE_MESSAGE = 5;//私信自定义消息
    /**
     * 消息是否可以点击【0：不可点击;1可点击】
     */
    @Expose
    public int clickAble;
    /**
     * 记录id
     */
    @Expose
    private String id;
    /**
     * 消息所属用户id
     */
    @Expose
    private String userId;
    /**
     * 消息类型【0：系统消息；1：个人消息】
     */
    @Expose
    private int type;
    /**
     * 消息子类型【type=0时暂无；type=1时【0：示爱；1:示爱被同意；2：被对方分手；3：回复；4：评论；5：私信自定义消息】】
     */
    @Expose
    private int subType;
    /**
     * 消息标题
     */
    @Expose
    private String title;
    /**
     * 消息内容
     */
    @Expose
    private String content;
    /**
     * 当type=1,subType=0,1,2时，为操作主动方的id。例如有人向我示爱，anotherId就是那个人的id，anotherNickname、anotherAvatar、anotherGender类似
     */
    @Expose
    private String anotherId;
    /**
     * 昵称
     */
    @Expose
    private String anotherNickname;
    /**
     * 头像
     */
    @Expose
    private String anotherAvatar;
    /**
     * 性别【0：保密；1：男；2：女】
     */
    @Expose
    private int anotherGender;
    /**
     * 当type=1,subType=0,1,2时，pillowTalkId被回复或评论的悄悄话id，pillowTalkType、imgs类似
     */
    @Expose
    private String pillowTalkId;
    /**
     * 悄悄话的类型【0:悄悄话；1:广播】
     */
    @Expose
    private int pillowTalkType;
    /**
     * 悄悄话里的图片url
     */
    @Expose
    private String imgs;
    /**
     * 消息创建时间
     */
    @Expose
    private String createTime;

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getAnotherId() {
        return anotherId;
    }

    public String getAnotherNickname() {
        return anotherNickname;
    }

    public String getAnotherAvatar() {
        return anotherAvatar;
    }

    public String getPillowTalkId() {
        return pillowTalkId;
    }

    public String getImgs() {
        return imgs;
    }

    public int getType() {
        return type;
    }

    public int getSubType() {
        return subType;
    }

    public int getAnotherGender() {
        return anotherGender;
    }

    public int getPillowTalkType() {
        return pillowTalkType;
    }

    public String getTime() {
        return createTime;
    }

    public boolean isClickAble() {
        return clickAble == 1;
    }

    public String getCreateTime() {
        String time = "";
        try {
            time = CommonUtil.getCostDate(Long.parseLong(createTime));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return time;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", type=" + type +
                ", subType=" + subType +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", anotherId='" + anotherId + '\'' +
                ", anotherNickname='" + anotherNickname + '\'' +
                ", anotherAvatar='" + anotherAvatar + '\'' +
                ", anotherGender=" + anotherGender +
                ", pillowTalkId='" + pillowTalkId + '\'' +
                ", pillowTalkType=" + pillowTalkType +
                ", imgs='" + imgs + '\'' +
                ", clickAble=" + clickAble +
                ", createTime='" + createTime + '\'' +
                '}';
    }
}
