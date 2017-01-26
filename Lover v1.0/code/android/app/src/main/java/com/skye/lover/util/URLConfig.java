package com.skye.lover.util;

/**
 * 网络接口地址配置
 */
public class URLConfig {
    /**
     * 服务器IP
     */
    public static final String SERVER_HOST = "http://192.168.191.1:8080/Lover/servlet/";
//     public static final String SERVER_HOST = "http://10.0.2.2:8080/Lover/servlet/";
//    public static final String SERVER_HOST = "http://192.168.2.58:8080/Lover/servlet/";
    // public static final String SERVER_HOST = "http://192.168.1.104:8080/Lover/servlet/";
    // public static final String SERVER_HOST = "http://16167951js.imwork.net/Lover/servlet/";
    /**
     * 提交app bug日志
     */
    public static final String ACTION_SUBMIT_BUG = "common/SubmitBug";
    /**
     * 登录
     */
    public static final String ACTION_LOGIN = "user/Login";
    /**
     * 注册
     */
    public static final String ACTION_REGISTER = "user/Register";
    /**
     * 发现
     */
    public static final String ACTION_FIND = "pillowtalk/Find";
    /**
     * 蜜语
     */
    public static final String ACTION_HONEY_WORD = "pillowtalk/HoneyWord";
    /**
     * 上传文件
     */
    public static final String ACTION_UPLOAD_FILE = "common/FileUpload";
    /**
     * 更新用户头像
     */
    public static final String ACTION_UPDATE_AVATAR = "user/UpdateAvatar";
    /**
     * 更新用户昵称
     */
    public static final String ACTION_UPDATE_NICKNAME = "user/UpdateNickname";
    /**
     * 更新用户性别
     */
    public static final String ACTION_UPDATE_GENDER = "user/UpdateGender";
    /**
     * 更新用户生日
     */
    public static final String ACTION_UPDATE_BIRTHDAY = "user/UpdateBirthday";
    /**
     * 更新密码
     */
    public static final String ACTION_UPDATE_PASSWORD = "user/UpdatePassword";
    /**
     * 意见反馈
     */
    public static final String ACTION_SUBMIT_FEEDBACK = "user/SubmitFeedback";
    /**
     * 发表悄悄话
     */
    public static final String ACTION_PUBLISH_PILLOW_TALK = "pillowtalk/PublishPillowTalk";
    /**
     * 公开悄悄话
     */
    public static final String ACTION_OPEN_PILLOW_TALK = "pillowtalk/OpenPillowTalk";
    /**
     * 删除悄悄话
     */
    public static final String ACTION_DELETE_PILLOW_TALK = "pillowtalk/DeletePillowTalk";
    /**
     * 举报悄悄话
     */
    public static final String ACTION_REPORT_PILLOW_TALK = "pillowtalk/ReportPillowTalk";
    /**
     * 悄悄话详情
     */
    public static final String ACTION_PILLOW_TALK_DETAIL = "pillowtalk/PillowTalkDetail";
    /**
     * 跟随悄悄话列表
     */
    public static final String ACTION_FOLLOWS = "pillowtalk/followpillowtalk/Follows";
    /**
     * 发表跟随悄悄话
     */
    public static final String ACTION_PUBLISH_FOLLOW_PILLOW_TALK = "pillowtalk/followpillowtalk/PublishFollowPillowTalk";
    /**
     * 删除跟随悄悄话
     */
    public static final String ACTION_DELETE_FOLLOW_PILLOW_TALK = "pillowtalk/followpillowtalk/DeleteFollowPillowTalk";
    /**
     * 发表评论
     */
    public static final String ACTION_PUBLISH_COMMENT = "pillowtalk/comment/PublishComment";
    /**
     * 评论列表
     */
    public static final String ACTION_COMMENTS = "pillowtalk/comment/Comments";
    /**
     * 删除评论
     */
    public static final String ACTION_DELETE_COMMENT = "pillowtalk/comment/DeleteComment";
    /**
     * 悄悄话部分属性
     */
    public static final String ACTION_PILLOW_TALK_PROPERTIES = "pillowtalk/PillowTalkProperties";
    /**
     * 收藏悄悄话
     */
    public static final String ACTION_COLLET = "pillowtalk/collect/Collect";
    /**
     * 取消收藏悄悄话
     */
    public static final String ACTION_CANCEL_COLLET = "pillowtalk/collect/CancelCollect";
    /**
     * 用户收藏的悄悄话列表
     */
    public static final String ACTION_COLLETED_PILLOW_TALKS = "pillowtalk/collect/CollectedPillowTalks";
    /**
     * 赞悄悄话
     */
    public static final String ACTION_PRAISE = "pillowtalk/praise/Praise";
    /**
     * 取消赞悄悄话
     */
    public static final String ACTION_CANCEL_PRAISE = "pillowtalk/praise/CancelPraise";
    /**
     * 用户赞过的悄悄话列表
     */
    public static final String ACTION_PRAISED_PILLOW_TALKS = "pillowtalk/praise/PraisedPillowTalks";
    /**
     * 用户评论过的悄悄话列表
     */
    public static final String ACTION_COMMENTED_PILLOW_TALKS = "pillowtalk/comment/CommentedPillowTalks";
    /**
     * 私信会话列表
     */
    public static final String ACTION_PRIVATE_MESSAGE_SESSIONS = "privatemessage/PrivateMessageSessions";
    /**
     * 根据聊天的两个用户删除整个会话记录
     */
    public static final String ACTION_DELETE_PRIVATE_MESSAGE_SESSION = "privatemessage/DeleteByPrivateMessageSession";
    /**
     * 私信聊天记录
     */
    public static final String ACTION_PRIVATE_MESSAGES = "privatemessage/PrivateMessages";
    /**
     * 发送私信消息
     */
    public static final String ACTION_PUBLISH_PRIVATE_MESSAGE = "privatemessage/PublishPrivateMessage";
    /**
     * 根据私信记录id和聊天者id删除私信
     */
    public static final String ACTION_DELETE_BY_PRIVATE_MESSAGE_ID = "privatemessage/DeleteByPrivateMessageId";
    /**
     * 其他用户信息
     */
    public static final String ACTION_OTHER_INFO = "user/OtherInfo";
    /**
     * 分手
     */
    public static final String ACTION_BREAK_UP = "loverrelatiionship/BreakUp";
    /**
     * 坠入爱河
     */
    public static final String ACTION_FALL_IN_LOVE = "loverrelatiionship/FallInLove";
    /**
     * 指定用户发表的悄悄话列表
     */
    public static final String ACTION_OTHERS_PILLOW_TALKS = "pillowtalk/OthersPillowTalks";
    /**
     * 消息列表
     */
    public static final String ACTION_MESSAGES = "message/Messages";
    /**
     * 刪除消息
     */
    public static final String ACTION_DELETE_MESSAGE = "message/DeleteMessage";
    private URLConfig() {
    }
}
