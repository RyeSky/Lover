package com.skye.lover.message.model.resp;

import com.google.gson.annotations.Expose;
import com.skye.lover.util.CommonUtil;

import java.util.HashMap;
import java.util.Map;

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
 * <td>消息子类型【type=0时暂无；type=1时【0：示爱；1:示爱被同意；2：被对方分手；3：回复；4：评论】】</td>
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
 * <td>悄悄话的类型【0:悄悄话；1:广播】</td>
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
public class Message {
    /**
     * 系统消息
     */
    public static final int TYPE_SYSTEM = 0;
    /**
     * 个人消息
     */
    public static final int TYPE_PERSONAL = 1;
    /**
     * 示爱
     */
    public static final int COURTSHIPDISPLAY = 0;
    /**
     * 示爱被同意
     */
    public static final int COURTSHIPDISPLAY_BE_AGREED = 1;
    /**
     * 被对方分手
     */
    public static final int BE_BROKE_UP = 2;
    /**
     * 回复
     */
    public static final int REPLY = 3;
    /**
     * 评论
     */
    public static final int COMMENT = 4;
    /**
     * 私信自定义消息
     */
    public static final int PRIVATE_MESSAGE = 5;
    /**
     * 表名称
     */
    public static final String TABLE_NAME = "message";
    /**
     * 记录id
     */
    public static final String ID = "id";
    /**
     * 消息所属用户id
     */
    public static final String USER_ID = "user_id";
    /**
     * 消息类型【0：系统消息；1：个人消息】
     */
    public static final String TYPE = "type";
    /**
     * 消息子类型【type=0时暂无；type=1时【0：示爱；1:示爱被同意；2：被对方分手；3：回复；4：评论】】
     */
    public static final String SUB_TYPE = "sub_type";
    /**
     * 消息标题
     */
    public static final String TITLE = "title";
    /**
     * 消息内容
     */
    public static final String CONTENT = "content";
    /**
     * 当type=1,subType=0,1,2时，为操作主动方的id。例如有人向我示爱，anotherId就是那个人的id，anotherNickname、anotherAvatar、anotherGender类似
     */
    public static final String ANOTHER = "another";
    /**
     * 昵称
     */
    public static final String ANOTHER_NICKNAME = "another_nickname";
    /**
     * 头像
     */
    public static final String ANOTHER_AVATAR = "another_avatar";
    /**
     * 性别【0：保密；1：男；2：女】
     */
    public static final String ANOTHER_GENDER = "another_gender";
    /**
     * 当type=1,subType=0,1,2时，pillowTalkId被回复或评论的悄悄话id，pillowTalkType、imgs类似
     */
    public static final String PILLOW_TALK_ID = "pillow_talk_id";
    /**
     * 悄悄话的类型【0:悄悄话；1:广播】
     */
    public static final String PILLOW_TALK_TYPE = "pillow_talk_type";
    /**
     * 消息是否可以点击【0：不可点击;1可点击】
     */
    public static final String CLICK_ABLE = "click_able";
    /**
     * 消息是否被删除【0：没有被删除；1：已经被删除】
     */
    public static final String DELETED = "deleted";
    /**
     * 消息创建时间
     */
    public static final String CREATE_TIME = "create_time";
    /**
     * 记录id
     */
    @Expose
    public String id;
    /**
     * 消息所属用户id
     */
    @Expose
    public String userId;
    /**
     * 消息类型【0：系统消息；1：个人消息】
     */
    @Expose
    public int type;
    /**
     * 消息子类型【type=0时暂无；type=1时【0：示爱；1:示爱被同意；2：被对方分手；3：回复；4：评论；5：私信自定义消息】】
     */
    @Expose
    public int subType;
    /**
     * 消息标题
     */
    @Expose
    public String title;
    /**
     * 消息内容
     */
    @Expose
    public String content;
    /**
     * 当type=1,subType=0,1,2时，为操作主动方的id。例如有人向我示爱，anotherId就是那个人的id，anotherNickname、anotherAvatar、anotherGender类似
     */
    @Expose
    public String anotherId;
    /**
     * 昵称
     */
    @Expose
    public String anotherNickname;
    /**
     * 头像
     */
    @Expose
    public String anotherAvatar;
    /**
     * 性别【0：保密；1：男；2：女】
     */
    @Expose
    public int anotherGender;
    /**
     * 当type=1,subType=0,1,2时，pillowTalkId被回复或评论的悄悄话id，pillowTalkType、imgs类似
     */
    @Expose
    public String pillowTalkId;
    /**
     * 悄悄话的类型【0:悄悄话；1:广播】
     */
    @Expose
    public int pillowTalkType;
    /**
     * 悄悄话里的图片url
     */
    @Expose
    public String imgs;
    /**
     * 消息是否可以点击【0：不可点击;1可点击】
     */
    @Expose
    public int clickAble;
    /**
     * 消息创建时间
     */
    @Expose
    public String createTime;

    /**
     * 把消息实体类转Map
     *
     * @return Map(String, String)
     */
    public Map<String, String> parseMessageToMap() {
        Map<String, String> map = new HashMap<>();
        map.put("id", CommonUtil.isBlank(id) ? "" : id);
        map.put("userId", CommonUtil.isBlank(userId) ? "" : userId);
        map.put("type", String.valueOf(type));
        map.put("subType", String.valueOf(subType));
        map.put("title", CommonUtil.isBlank(title) ? "" : title);
        map.put("content", CommonUtil.isBlank(content) ? "" : content);
        map.put("anotherId", CommonUtil.isBlank(anotherId) ? "" : anotherId);
        map.put("anotherNickname", CommonUtil.isBlank(anotherNickname) ? "" : anotherNickname);
        map.put("anotherAvatar", CommonUtil.isBlank(anotherAvatar) ? "" : anotherAvatar);
        map.put("anotherGender", String.valueOf(anotherGender));
        map.put("pillowTalkId", CommonUtil.isBlank(pillowTalkId) ? "" : pillowTalkId);
        map.put("pillowTalkType", String.valueOf(pillowTalkType));
        map.put("imgs", CommonUtil.isBlank(imgs) ? "" : imgs);
        map.put("clickAble", String.valueOf(clickAble));
        map.put("createTime", CommonUtil.isBlank(createTime) ? "" : createTime);
        return map;
    }

    @Override
    public String toString() {
        return CommonUtil.gson.toJson(this);
    }
}
