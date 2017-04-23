package com.skye.lover.user.service;

import com.skye.lover.common.model.resp.BaseResponse;

/**
 * 用户业务层
 */
public interface UserService {
    /**
     * 用户登录
     *
     * @param br       返回数据基类
     * @param name     用户账号
     * @param password 用户密码
     */
    void login(BaseResponse br, String name, String password);

    /**
     * 其他用户信息
     *
     * @param br     返回数据基类
     * @param userId 用户id
     */
    void otherInfo(BaseResponse br, String userId);

    /**
     * 用户注册
     *
     * @param br       返回数据基类
     * @param name     用户账号
     * @param password 用户密码
     * @param gender   性别
     */
    void register(BaseResponse br, String name, String password, int gender);

    /**
     * 更新用户头像
     *
     * @param br     返回数据基类
     * @param userId 用户id
     * @param avatar 头像路径
     */
    void updateAvatar(BaseResponse br, String userId, String avatar);

    /**
     * 更新用户昵称
     *
     * @param br       返回数据基类
     * @param userId   用户id
     * @param nickname 昵称
     */
    void updateNickname(BaseResponse br, String userId, String nickname);

    /**
     * 更新用户性别
     *
     * @param br     返回数据基类
     * @param userId 用户id
     * @param gender 性别
     */
    void updateGender(BaseResponse br, String userId, int gender);

    /**
     * 更新用户生日
     *
     * @param br       返回数据基类
     * @param userId   用户id
     * @param birthday 生日
     */
    void updateBirthday(BaseResponse br, String userId, String birthday);

    /**
     * 更新用户密码
     *
     * @param br          返回数据基类
     * @param userId      用户id
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     */
    void updatePassword(BaseResponse br, String userId, String oldPassword, String newPassword);
}
