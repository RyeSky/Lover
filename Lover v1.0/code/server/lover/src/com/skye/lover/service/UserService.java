package com.skye.lover.service;

import com.skye.lover.dao.impl.LoverRelationshipDaoImpl;
import com.skye.lover.dao.impl.UserDaoImpl;
import com.skye.lover.dao.interf.LoverRelationshipDao;
import com.skye.lover.dao.interf.UserDao;
import com.skye.lover.model.BaseResponse;
import com.skye.lover.model.User;
import com.skye.lover.util.CommonUtil;

/**
 * 用户业务层
 */
public class UserService {
    private UserDao ud = new UserDaoImpl();
    private LoverRelationshipDao lrd = new LoverRelationshipDaoImpl();

    /**
     * 用户登录
     *
     * @param br       返回数据基类
     * @param name     用户账号
     * @param password 用户密码
     */
    public void login(BaseResponse br, String name, String password) {
        User user = ud.login(name, password);
        if (user != null) {// 用户存在时，更新用户的登录时间
            user.another = lrd.queryAnother(user.id);
            if (!CommonUtil.isBlank(user.another)) {// 有恋人
                User another = ud.query(user.another);
                if (another != null) {// 相恋关系中另一方的信息
                    user.anotherNickname = another.nickname;
                    user.anotherAvatar = another.avatar;
                    user.anotherGender = another.gender;
                }
            }
            long loginTime = System.currentTimeMillis();
            user.loginTime = loginTime + "";
            ud.updateLoginTime(user.id, loginTime);
            br.result = user;
        } else {// 用户不存在
            br.code = BaseResponse.CODE_FAIL;
            br.message = "账号或密码错误，请确认后重试";
        }
    }

    /**
     * 其他用户信息
     *
     * @param br     返回数据基类
     * @param userId 用户id
     */
    public void otherInfo(BaseResponse br, String userId) {
        User user = ud.query(userId);
        if (user != null) {// 用户存在时，更新用户的登录时间
            user.another = lrd.queryAnother(user.id);
            if (!CommonUtil.isBlank(user.another)) {// 有恋人
                User another = ud.query(user.another);
                if (another != null) {// 相恋关系中另一方的信息
                    user.anotherNickname = another.nickname;
                    user.anotherAvatar = another.avatar;
                    user.anotherGender = another.gender;
                }
            }
            br.result = user;
        } else {// 用户不存在
            br.code = BaseResponse.CODE_FAIL;
            br.message = "账号或密码错误，请确认后重试";
        }
    }

    /**
     * 用户注册
     *
     * @param br       返回数据基类
     * @param name     用户账号
     * @param password 用户密码
     * @param gender   性别
     */
    public void register(BaseResponse br, String name, String password, int gender) {
        if (!ud.isExist(name)) {// 账号不存在
            if (!ud.insert(name, password, gender)) {// 插入数据出错
                br.code = BaseResponse.CODE_FAIL;
                br.message = BaseResponse.MESSAGE_FAIL;
            }
        } else {// 账号已经存在
            br.code = BaseResponse.CODE_FAIL;
            br.message = "账号已被占用";
        }
    }

    /**
     * 更新用户头像
     *
     * @param br     返回数据基类
     * @param userId 用户id
     * @param avatar 头像路径
     */
    public void updateAvatar(BaseResponse br, String userId, String avatar) {
        if (!ud.update(userId, User.AVATAR, avatar)) {
            br.code = BaseResponse.CODE_FAIL;
            br.message = BaseResponse.MESSAGE_FAIL;
        }
    }

    /**
     * 更新用户昵称
     *
     * @param br       返回数据基类
     * @param userId   用户id
     * @param nickname 昵称
     */
    public void updateNickname(BaseResponse br, String userId, String nickname) {
        if (!ud.update(userId, User.NICKNAME, nickname)) {
            br.code = BaseResponse.CODE_FAIL;
            br.message = BaseResponse.MESSAGE_FAIL;
        }
    }

    /**
     * 更新用户性别
     *
     * @param br     返回数据基类
     * @param userId 用户id
     * @param gender 性别
     */
    public void updateGender(BaseResponse br, String userId, int gender) {
        if (!ud.update(userId, User.GENDER, gender)) {
            br.code = BaseResponse.CODE_FAIL;
            br.message = BaseResponse.MESSAGE_FAIL;
        }
    }

    /**
     * 更新用户生日
     *
     * @param br       返回数据基类
     * @param userId   用户id
     * @param birthday 生日
     */
    public void updateBirthday(BaseResponse br, String userId, String birthday) {
        if (!ud.update(userId, User.BIRTHDAY, birthday)) {
            br.code = BaseResponse.CODE_FAIL;
            br.message = BaseResponse.MESSAGE_FAIL;
        }
    }

    /**
     * 更新用户密码
     *
     * @param br          返回数据基类
     * @param userId      用户id
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     */
    public void updatePassword(BaseResponse br, String userId, String oldPassword, String newPassword) {
        if (!ud.updatePassword(userId, oldPassword, newPassword)) {
            br.code = BaseResponse.CODE_FAIL;
            br.message = "修改失败";
        }
    }
}
