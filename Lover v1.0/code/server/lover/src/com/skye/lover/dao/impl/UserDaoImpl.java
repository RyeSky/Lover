package com.skye.lover.dao.impl;

import com.skye.lover.dao.BaseDao;
import com.skye.lover.dao.interf.UserDao;
import com.skye.lover.model.User;
import com.skye.lover.util.CommonUtil;

import java.sql.SQLException;

/**
 * 用户DAO实现
 */
public class UserDaoImpl extends BaseDao implements UserDao {

    /**
     * 用户登录
     *
     * @param name     用户账号
     * @param password 用户密码
     * @return 用户实体类
     */
    @Override
    public User login(String name, String password) {
        String sql = "select * from " + User.TABLE_NAME + " where " + User.NAME + " = ? and " + User.PASSWORD + " = ?";
        User user = null;
        try {
            conn = BaseDao.getConnection();
            pst = conn.prepareStatement(sql);
            pst.setString(1, name);
            pst.setString(2, password);
            rs = pst.executeQuery();
            while (rs.next()) {
                user = new User();
                user.id = rs.getString(User.ID);
                user.name = rs.getString(User.NAME);
                user.password = rs.getString(User.PASSWORD);
                user.nickname = rs.getString(User.NICKNAME);
                user.gender = rs.getInt(User.GENDER);
                user.avatar = rs.getString(User.AVATAR);
                user.birthday = rs.getString(User.BIRTHDAY);
                user.loginTime = CommonUtil.getTimestamp(rs
                        .getString(User.LOGIN_TIME));
                user.createTime = CommonUtil.getTimestamp(rs
                        .getString(User.CREATE_TIME));
            }
            close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
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
        String sql = "update " + User.TABLE_NAME + " set " + User.LOGIN_TIME + " = ? where " + User.ID + " = ?";
        int update = 0;// sql语句执行后影响的行数
        try {
            conn = BaseDao.getConnection();
            pst = conn.prepareStatement(sql);
            pst.setString(1, CommonUtil.getTime(loginTime));
            pst.setString(2, userId);
            update = pst.executeUpdate();
            close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return update > 0;
    }

    /**
     * 是否存在账号为name的用户
     *
     * @param name 账号
     * @return 存在时返回true
     */
    @Override
    public boolean isExist(String name) {
        String sql = "select * from " + User.TABLE_NAME + " where " + User.NAME + " = ?";
        boolean exist = false;
        try {
            conn = BaseDao.getConnection();
            pst = conn.prepareStatement(sql);
            pst.setString(1, name);
            rs = pst.executeQuery();
            if (rs.next())
                exist = true;
            close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exist;
    }

    /**
     * 添加用户
     *
     * @param name     用户账号
     * @param password 密码
     * @param gender   性别
     * @return 是否添加成功
     */
    @Override
    public boolean insert(String name, String password, int gender) {
        String sql = "insert into " + User.TABLE_NAME + " (" + User.NAME + ","
                + User.PASSWORD + "," + User.NICKNAME + "," + User.GENDER + ") values (?,?,?,?)";
        int update = 0;
        try {
            conn = BaseDao.getConnection();
            pst = conn.prepareStatement(sql);
            pst.setString(1, name);
            pst.setString(2, password);
            pst.setString(3, name);
            pst.setInt(4, gender);
            update = pst.executeUpdate();
            close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return update > 0;
    }

    /**
     * 查询用户信息
     *
     * @param userId 用户id
     * @return 用户信息实体
     */
    @Override
    public User query(String userId) {
        String sql = "select * from " + User.TABLE_NAME + " where " + User.ID + " = ?";
        User user = null;
        try {
            conn = BaseDao.getConnection();
            pst = conn.prepareStatement(sql);
            pst.setString(1, userId);
            rs = pst.executeQuery();
            while (rs.next()) {
                user = new User();
                user.id = rs.getString(User.ID);
                user.name = rs.getString(User.NAME);
                user.password = rs.getString(User.PASSWORD);
                user.nickname = rs.getString(User.NICKNAME);
                user.gender = rs.getInt(User.GENDER);
                user.avatar = rs.getString(User.AVATAR);
                user.birthday = rs.getString(User.BIRTHDAY);
                user.loginTime = CommonUtil.getTimestamp(rs
                        .getString(User.LOGIN_TIME));
                user.createTime = CommonUtil.getTimestamp(rs
                        .getString(User.CREATE_TIME));
            }
            close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
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
    public boolean update(String userId, String field, String value) {
        String sql = "update " + User.TABLE_NAME + " set " + field + " = ? where " + User.ID + " = ?";
        int update = 0;// sql语句执行后影响的行数
        try {
            conn = BaseDao.getConnection();
            pst = conn.prepareStatement(sql);
            pst.setString(1, value);
            pst.setString(2, userId);
            update = pst.executeUpdate();
            close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return update > 0;
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
    public boolean update(String userId, String field, int value) {
        String sql = "update " + User.TABLE_NAME + " set " + field + " = ? where " + User.ID + " = ?";
        int update = 0;// sql语句执行后影响的行数
        try {
            conn = BaseDao.getConnection();
            pst = conn.prepareStatement(sql);
            pst.setInt(1, value);
            pst.setString(2, userId);
            update = pst.executeUpdate();
            close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return update > 0;
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
    public boolean updatePassword(String userId, String oldPassword,
                                  String newPassword) {
        String sql = "update " + User.TABLE_NAME + " set " + User.PASSWORD
                + " = ? where " + User.ID + " = ? and " + User.PASSWORD + " = ?";
        int update = 0;// sql语句执行后影响的行数
        try {
            conn = BaseDao.getConnection();
            pst = conn.prepareStatement(sql);
            pst.setString(1, newPassword);
            pst.setString(2, userId);
            pst.setString(3, oldPassword);
            update = pst.executeUpdate();
            close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return update > 0;
    }
}
