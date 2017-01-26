package com.skye.lover.model;

import com.google.gson.annotations.Expose;

/**
 * 用户
 */
public class User {
    public static final int GENDER_MALE = 1;//男
    public static final int GENDER_FEMALE = 2;//女
    // 用户id，用户登录账号，密码，昵称，头像，生日，登录时间，注册时间，相爱关系的另一方,另一方的昵称、另一方的头像、另一方的性别
    @Expose
    private String id, name, password, nickname, avatar, birthday, loginTime, createTime,
            another, anotherNickname, anotherAvatar;

    @Expose
    private int gender, anotherGender;// 用户性别【0：保密；1：男；2：女】

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
