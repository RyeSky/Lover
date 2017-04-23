package com.skye.lover.model;

/**
 * 头像tag
 */
public class AvatarTag {
    public String avatar;//头像url
    public int gender;//头像主人性别

    public AvatarTag(String avatar, int gender) {
        this.avatar = avatar;
        this.gender = gender;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AvatarTag avatarTag = (AvatarTag) o;
        if (gender != avatarTag.gender) return false;
        return avatar != null ? avatar.equals(avatarTag.avatar) : avatarTag.avatar == null;
    }

    @Override
    public int hashCode() {
        int result = avatar != null ? avatar.hashCode() : 0;
        result = 31 * result + gender;
        return result;
    }

    @Override
    public String toString() {
        return "AvatarTag{" +
                "avatar='" + avatar + '\'' +
                ", gender=" + gender +
                '}';
    }
}
