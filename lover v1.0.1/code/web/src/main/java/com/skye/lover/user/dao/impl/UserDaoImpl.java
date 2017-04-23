package com.skye.lover.user.dao.impl;

import com.skye.lover.common.dao.BaseDao;
import com.skye.lover.user.dao.UserDao;
import com.skye.lover.user.model.resp.User;
import com.skye.lover.util.CommonUtil;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户Dao实现
 */
@Repository
public class UserDaoImpl extends BaseDao implements UserDao {
    /**
     * 填充用户实体类
     *
     * @param rs 数据库查询结果集
     * @return 用户实体
     */
    private User fillUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.id = rs.getString(User.ID);
        user.name = rs.getString(User.NAME);
        user.password = rs.getString(User.PASSWORD);
        user.nickname = rs.getString(User.NICKNAME);
        user.gender = rs.getInt(User.GENDER);
        user.avatar = rs.getString(User.AVATAR);
        user.birthday = rs.getString(User.BIRTHDAY);
        user.loginTime = CommonUtil.getTimestamp(rs.getString(User.LOGIN_TIME));
        user.createTime = CommonUtil.getTimestamp(rs.getString(User.CREATE_TIME));
        return user;
    }

    /**
     * 用户登录
     *
     * @param name     用户账号
     * @param password 用户密码
     * @return 用户实体类
     */
    @Override
    public User login(String name, String password) {
        String sql = "SELECT * FROM `user` WHERE name = :name AND password = :password";
        Map<String, Object> params = new HashMap<>();
        params.put(User.NAME, name);
        params.put(User.PASSWORD, password);
        List<User> list = jdbcTemplate.query(sql, params, (ResultSet rs, int i) -> fillUser(rs));
        return list == null || list.isEmpty() ? null : list.get(0);
    }

    /**
     * 更新用户登录时间
     *
     * @param userId    用户id
     * @param loginTime 用户登录时间
     * @return 数据是否更新成功
     */
    @Override
    public boolean updateLoginTime(String userId, long loginTime) {
        String sql = "UPDATE `user` SET login_time = :login_time WHERE id =:id";
        Map<String, Object> params = new HashMap<>();
        params.put(User.LOGIN_TIME, CommonUtil.getTime(loginTime));
        params.put(User.ID, userId);
        return jdbcTemplate.update(sql, params) > 0;
    }

    /**
     * 是否存在账号为name的用户
     *
     * @param name 账号
     * @return 存在时返回true
     */
    @Override
    public boolean isExist(String name) {
        String sql = "SELECT id FROM `user` WHERE `name` = :name";
        Map<String, Object> params = new HashMap<>();
        params.put(User.NAME, name);
        List<?> list = jdbcTemplate.queryForList(sql, params);
        return list != null && !list.isEmpty();
    }

    /**
     * 添加用户
     *
     * @param name     用户账号
     * @param password 密码
     * @param gender   性别
     * @return 数据是否添加成功
     */
    @Override
    public boolean insert(String name, String password, int gender) {
        String sql = "INSERT INTO `user` (name,password,nickname,gender) VALUES (:name,:password,:nickname,:gender)";
        Map<String, Object> params = new HashMap<>();
        params.put(User.NAME, name);
        params.put(User.PASSWORD, password);
        params.put(User.NICKNAME, name);
        params.put(User.GENDER, gender);
        return jdbcTemplate.update(sql, params) > 0;
    }

    /**
     * 查询用户信息
     *
     * @param userId 用户id
     * @return 用户信息实体
     */
    @Override
    public User query(String userId) {
        String sql = "SELECT * FROM `user` WHERE id =:id";
        Map<String, Object> params = new HashMap<>();
        params.put(User.ID, userId);
        List<User> list = jdbcTemplate.query(sql, params, (ResultSet rs, int i) -> fillUser(rs));
        return list == null || list.isEmpty() ? null : list.get(0);
    }

    /**
     * 更新用户信息
     *
     * @param userId 用户id
     * @param field  字段名
     * @param value  字段值
     * @return 数据是否更新成功
     */
    @Override
    public boolean update(String userId, String field, Object value) {
        String sql = "UPDATE `user` SET " + field + " = :" + field + " WHERE id =:id";
        Map<String, Object> params = new HashMap<>();
        params.put(field, value);
        params.put(User.ID, userId);
        return jdbcTemplate.update(sql, params) > 0;
    }

    /**
     * 更新用户密码
     *
     * @param userId      用户id
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return 数据是否更新成功
     */
    @Override
    public boolean updatePassword(String userId, String oldPassword, String newPassword) {
        String sql = "UPDATE `user` SET password = :newPassword WHERE id = :id AND password = :oldPassword";
        Map<String, Object> params = new HashMap<>();
        params.put("newPassword", newPassword);
        params.put(User.ID, userId);
        params.put("oldPassword", oldPassword);
        return jdbcTemplate.update(sql, params) > 0;
    }
}
