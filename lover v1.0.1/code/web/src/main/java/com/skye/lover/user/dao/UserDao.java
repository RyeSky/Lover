package com.skye.lover.user.dao;

import com.skye.lover.user.model.resp.User;

/**
 * 用户DAO
 */
public interface UserDao {
    /**
     * 用户登录
     *
     * @param name     用户账号
     * @param password 用户密码
     * @return 用户实体类
     */
    User login(String name, String password);

    /**
     * 更新用户登录时间
     *
     * @param userId    用户id
     * @param loginTime 用户登录时间
     * @return 数据是否更新成功
     */
    boolean updateLoginTime(String userId, long loginTime);

    /**
     * 是否存在账号为name的用户
     *
     * @param name 账号
     * @return 存在时返回true
     */
    boolean isExist(String name);

    /**
     * 添加用户
     *
     * @param name     用户账号
     * @param password 密码
     * @param gender   性别
     * @return 数据是否添加成功
     */
    boolean insert(String name, String password, int gender);

    /**
     * 查询用户信息
     *
     * @param userId 用户id
     * @return 用户信息实体
     */
    User query(String userId);

    /**
     * 更新用户信息
     *
     * @param userId 用户id
     * @param field  字段名
     * @param value  字段值
     * @return 数据是否更新成功
     */
    boolean update(String userId, String field, Object value);

    /**
     * 更新用户密码
     *
     * @param userId      用户id
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return 数据是否更新成功
     */
    boolean updatePassword(String userId, String oldPassword,
                           String newPassword);
}
