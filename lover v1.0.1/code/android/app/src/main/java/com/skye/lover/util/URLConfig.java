package com.skye.lover.util;

/**
 * 网络接口地址配置
 */
public class URLConfig {
    /**
     * 服务器IP
     */
 public static final String SERVER_HOST = "http://192.168.191.1:8080/Lover-1.0.1/";
// public static final String SERVER_HOST ="http://10.0.2.2:8080/Lover-1.0.1/";
// public static final String SERVER_HOST ="http://192.168.2.58:8080/Lover-1.0.1/";
// public static final String SERVER_HOST ="http://192.168.1.104:8080/Lover-1.0.1/";
// public static final String SERVER_HOST ="http://16167951js.imwork.net/Lover-1.0.1/";
// public static final String SERVER_HOST = "http://192.168.0.109:8080/Lover-1.0.1/";
    /**
     * 根据手机平台查询最新版本
     */
    public static final String ACTION_LAST_APP_VERSION = "mobile/common/LastAppVersion";
    /**
     * 登录
     */
    public static final String ACTION_LOGIN = "mobile/user/Login";
    /**
     * 注册
     */
    public static final String ACTION_REGISTER = "mobile/user/Register";
    /**
     * 发现
     */
    public static final String ACTION_FIND = "mobile/pillowtalk/Find";
    /**
     * 蜜语
     */
    public static final String ACTION_HONEY_WORD = "mobile/pillowtalk/HoneyWord";
    /**
     * 上传文件
     */
    public static final String ACTION_UPLOAD_FILE = "mobile/common/FileUpload";
    /**
     * 更新用户头像
     */
    public static final String ACTION_UPDATE_AVATAR = "mobile/user/UpdateAvatar";
    /**
     * 更新用户昵称
     */
    public static final String ACTION_UPDATE_NICKNAME = "mobile/user/UpdateNickname";
    /**
     * 更新用户性别
     */
    public static final String ACTION_UPDATE_GENDER = "mobile/user/UpdateGender";
    /**
     * 更新用户生日
     */
    public static final String ACTION_UPDATE_BIRTHDAY = "mobile/user/UpdateBirthday";
    /**
     * 更新密码
     */
    public static final String ACTION_UPDATE_PASSWORD = "mobile/user/UpdatePassword";
    /**
     * 意见反馈
     */
    public static final String ACTION_SUBMIT_FEEDBACK = "mobile/user/SubmitFeedback";
    /**
     * 发表悄悄话
     */
    public static final String ACTION_PUBLISH_PILLOW_TALK = "mobile/pillowtalk/PublishPillowTalk";
    /**
     * 公开悄悄话
     */
    public static final String ACTION_OPEN_PILLOW_TALK = "mobile/pillowtalk/OpenPillowTalk";
    /**
     * 删除悄悄话
     */
    public static final String ACTION_DELETE_PILLOW_TALK = "mobile/pillowtalk/DeletePillowTalk";
    /**
     * 举报悄悄话
     */
    public static final String ACTION_REPORT_PILLOW_TALK = "mobile/pillowtalk/ReportPillowTalk";
    /**
     * 悄悄话详情
     */
    public static final String ACTION_PILLOW_TALK_DETAIL = "mobile/pillowtalk/PillowTalkDetail";
    /**
     * 跟随悄悄话列表
     */
    public static final String ACTION_FOLLOWS = "mobile/pillowtalk/followpillowtalk/Follows";
    /**
     * 发表跟随悄悄话
     */
    public static final String ACTION_PUBLISH_FOLLOW_PILLOW_TALK = "mobile/pillowtalk/followpillowtalk/PublishFollowPillowTalk";
    /**
     * 删除跟随悄悄话
     */
    public static final String ACTION_DELETE_FOLLOW_PILLOW_TALK = "mobile/pillowtalk/followpillowtalk/DeleteFollowPillowTalk";
    /**
     * 发表评论
     */
    public static final String ACTION_PUBLISH_COMMENT = "mobile/pillowtalk/comment/PublishComment";
    /**
     * 评论列表
     */
    public static final String ACTION_COMMENTS = "mobile/pillowtalk/comment/Comments";
    /**
     * 删除评论
     */
    public static final String ACTION_DELETE_COMMENT = "mobile/pillowtalk/comment/DeleteComment";
    /**
     * 悄悄话部分属性
     */
    public static final String ACTION_PILLOW_TALK_PROPERTIES = "mobile/pillowtalk/PillowTalkProperties";
    /**
     * 收藏悄悄话
     */
    public static final String ACTION_COLLET = "mobile/pillowtalk/collect/Collect";
    /**
     * 取消收藏悄悄话
     */
    public static final String ACTION_CANCEL_COLLECT = "mobile/pillowtalk/collect/CancelCollect";
    /**
     * 用户收藏的悄悄话列表
     */
    public static final String ACTION_COLLETED_PILLOW_TALKS = "mobile/pillowtalk/collect/CollectedPillowTalks";
    /**
     * 赞悄悄话
     */
    public static final String ACTION_PRAISE = "mobile/pillowtalk/praise/Praise";
    /**
     * 取消赞悄悄话
     */
    public static final String ACTION_CANCEL_PRAISE = "mobile/pillowtalk/praise/CancelPraise";
    /**
     * 用户赞过的悄悄话列表
     */
    public static final String ACTION_PRAISED_PILLOW_TALKS = "mobile/pillowtalk/praise/PraisedPillowTalks";
    /**
     * 用户评论过的悄悄话列表
     */
    public static final String ACTION_COMMENTED_PILLOW_TALKS = "mobile/pillowtalk/comment/CommentedPillowTalks";
    /**
     * 私信会话列表
     */
    public static final String ACTION_PRIVATE_MESSAGE_SESSIONS = "mobile/privatemessage/PrivateMessageSessions";
    /**
     * 根据聊天的两个用户删除整个会话记录
     */
    public static final String ACTION_DELETE_PRIVATE_MESSAGE_SESSION = "mobile/privatemessage/DeleteByPrivateMessageSession";
    /**
     * 私信聊天记录
     */
    public static final String ACTION_PRIVATE_MESSAGES = "mobile/privatemessage/PrivateMessages";
    /**
     * 发送私信消息
     */
    public static final String ACTION_PUBLISH_PRIVATE_MESSAGE = "mobile/privatemessage/PublishPrivateMessage";
    /**
     * 根据私信记录id和聊天者id删除私信
     */
    public static final String ACTION_DELETE_BY_PRIVATE_MESSAGE_ID = "mobile/privatemessage/DeleteByPrivateMessageId";
    /**
     * 其他用户信息
     */
    public static final String ACTION_OTHER_INFO = "mobile/user/OtherInfo";
    /**
     * 分手
     */
    public static final String ACTION_BREAK_UP = "mobile/loverrelatiionship/BreakUp";
    /**
     * 坠入爱河
     */
    public static final String ACTION_FALL_IN_LOVE = "mobile/loverrelatiionship/FallInLove";
    /**
     * 指定用户发表的悄悄话列表
     */
    public static final String ACTION_OTHERS_PILLOW_TALKS = "mobile/pillowtalk/OthersPillowTalks";
    /**
     * 消息列表
     */
    public static final String ACTION_MESSAGES = "mobile/message/Messages";
    /**
     * 刪除消息
     */
    public static final String ACTION_DELETE_MESSAGE = "mobile/message/DeleteMessage";
    /**
     * 提交app bug日志
     */
    static final String ACTION_SUBMIT_BUG = "mobile/common/SubmitBug";

    private URLConfig() {
    }
}
