package com.skye.lover.user.model.resp;

import com.google.gson.annotations.Expose;

/**
 * 用户
 */
public class User {
    /**
     * 表名称
     */
    public static final String TABLE_NAME = "user";
    /**
     * 记录id
     */
    public static final String ID = "id";
    /**
     * 登录账号
     */
    public static final String NAME = "name";
    /**
     * md5加密后的密码
     */
    public static final String PASSWORD = "password";
    /**
     * 昵称
     */
    public static final String NICKNAME = "nickname";
    /**
     * 头像
     */
    public static final String AVATAR = "avatar";
    /**
     * 性别【0：保密；1：男；2：女】
     */
    public static final String GENDER = "gender";
    /**
     * 性别保密
     */
    public static final int GENDER_SECRET = 0;
    /**
     * 男
     */
    public static final int GENDER_MALE = 1;
    /**
     * 女
     */
    public static final int GENDER_FEMALE = 2;
    /**
     * 生日
     */
    public static final String BIRTHDAY = "birthday";
    /**
     * 最后一次登录时间
     */
    public static final String LOGIN_TIME = "login_time";
    /**
     * 用户注册时间
     */
    public static final String CREATE_TIME = "create_time";
    /**
     * 恋爱关系中的另一方id
     */
    public static final String ANOTHER = "another";
    /**
     * 记录id
     */
    @Expose
    public String id;
    /**
     * 登录账号
     */
    @Expose
    public String name;
    /**
     * md5加密后的密码
     */
    @Expose
    public String password;
    /**
     * 昵称
     */
    @Expose
    public String nickname;
    /**
     * 头像
     */
    @Expose
    public String avatar;
    /**
     * 性别【0：保密；1：男；2：女】
     */
    @Expose
    public int gender;
    /**
     * 生日
     */
    @Expose
    public String birthday;
    /**
     * 最后一次登录时间
     */
    @Expose
    public String loginTime;
    /**
     * 用户注册时间
     */
    @Expose
    public String createTime;
    /**
     * 相恋关系中的另一方id，anotherNickname、anotherAvatar、anotherGender类似
     */
    @Expose
    public String another;
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

    @Override
    public String toString() {
        return "User [id=" + id + ", name=" + name + ", password=" + password
                + ", nickname=" + nickname + ", avatar=" + avatar
                + ", birthday=" + birthday + ", loginTime=" + loginTime
                + ", createTime=" + createTime + ", another=" + another
                + ", anotherNickname=" + anotherNickname + ", anotherAvatar="
                + anotherAvatar + ", gender=" + gender + ", anotherGender="
                + anotherGender + "]";
    }

}
