package com.skye.lover.model;

import android.content.Context;
import android.support.annotation.Keep;
import android.text.TextUtils;

import com.google.gson.annotations.Expose;
import com.skye.lover.util.ShareDataUtil;

/**
 * 用户
 */
@Keep
public class User {
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
     * 恋爱关系中的另一方昵称
     */
    public static final String ANOTHER_NICKNAME = "another_nickname";
    /**
     * 恋爱关系中的另一方性别【0：保密；1：男；2：女】
     */
    public static final String ANOTHER_GENDER = "another_gender";
    /**
     * 恋爱关系中的另一方头像
     */
    public static final String ANOTHER_AVATAR = "another_avatar";
    /**
     * 记录id
     */
    @Expose
    private String id;
    /**
     * 登录账号
     */
    @Expose
    private String name;
    /**
     * md5加密后的密码
     */
    @Expose
    private String password;
    /**
     * 昵称
     */
    @Expose
    private String nickname;
    /**
     * 头像
     */
    @Expose
    private String avatar;
    /**
     * 性别【0：保密；1：男；2：女】
     */
    @Expose
    private int gender;
    /**
     * 生日
     */
    @Expose
    private String birthday;
    /**
     * 最后一次登录时间
     */
    @Expose
    private String loginTime;
    /**
     * 用户注册时间
     */
    @Expose
    private String createTime;
    /**
     * 相恋关系中的另一方id，anotherNickname、anotherAvatar、anotherGender类似
     */
    @Expose
    private String another;
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
     * 保存用户
     *
     * @param user 用户信息实体
     */
    public static void save(Context context, User user) {
        ShareDataUtil.set(context, ID, user.getId());
        ShareDataUtil.set(context, NAME, user.getName());
        ShareDataUtil.set(context, PASSWORD, user.getPassword());
        ShareDataUtil.set(context, NICKNAME, user.getNickname());
        ShareDataUtil.set(context, GENDER, user.getGender() + "");
        ShareDataUtil.set(context, AVATAR, user.getAvatar());
        ShareDataUtil.set(context, BIRTHDAY, user.getBirthday());
        ShareDataUtil.set(context, LOGIN_TIME, user.getLoginTime());
        ShareDataUtil.set(context, CREATE_TIME, user.getCreateTime());
        ShareDataUtil.set(context, ANOTHER, user.getAnother());
        ShareDataUtil.set(context, ANOTHER_NICKNAME, user.getAnotherNickname());
        ShareDataUtil.set(context, ANOTHER_GENDER, user.getAnotherGender() + "");
        ShareDataUtil.set(context, ANOTHER_AVATAR, user.getAnotherAvatar());
    }

    /**
     * 是否已经登录
     *
     * @return 如果已经登录则返回true, 否则返回false
     * @param context 上下文对象
     */
    public static boolean isLogined(Context context) {
        return !TextUtils.isEmpty(ShareDataUtil.get(context, ID)) &&
                !TextUtils.isEmpty(ShareDataUtil.get(context, NAME)) &&
                !TextUtils.isEmpty(ShareDataUtil.get(context, PASSWORD));
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(String loginTime) {
        this.loginTime = loginTime;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getAnother() {
        return another;
    }

    public void setAnother(String another) {
        this.another = another;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getAnotherNickname() {
        return anotherNickname;
    }

    public void setAnotherNickname(String anotherNickname) {
        this.anotherNickname = anotherNickname;
    }

    public String getAnotherAvatar() {
        return anotherAvatar;
    }

    public void setAnotherAvatar(String anotherAvatar) {
        this.anotherAvatar = anotherAvatar;
    }

    public int getAnotherGender() {
        return anotherGender;
    }

    public void setAnotherGender(int anotherGender) {
        this.anotherGender = anotherGender;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", nickname='" + nickname + '\'' +
                ", avatar='" + avatar + '\'' +
                ", birthday='" + birthday + '\'' +
                ", loginTime='" + loginTime + '\'' +
                ", createTime='" + createTime + '\'' +
                ", another='" + another + '\'' +
                ", anotherNickname='" + anotherNickname + '\'' +
                ", anotherAvatar='" + anotherAvatar + '\'' +
                ", anotherGender='" + anotherGender + '\'' +
                ", gender=" + gender +
                '}';
    }
}
