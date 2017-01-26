package com.skye.lover.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.skye.lover.ConstantUtil;
import com.skye.lover.model.User;

/**
 * 全局SharedPreferences管理
 */
public class ShareData {
    // 用户id，用户登录账号，密码，昵称，头像，登录时间，注册时间，相爱关系的另一方
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String PASSWORD = "password";
    public static final String NICKNAME = "nickname";
    public static final String GENDER = "gender";
    public static final String AVATAR = "avatar";
    public static final String BIRTHDAY = "birthday";
    public static final String LOGIN_TIME = "login_time";
    public static final String CREATE_TIME = "create_time";
    public static final String ANOTHER = "another";
    public static final String ANOTHER_NICKNAME = "another_nickname";
    public static final String ANOTHER_GENDER = "another_gender";
    public static final String ANOTHER_AVATAR = "another_avatar";
    public static final String SOFT_INPUT_HEIGHT = "soft_input_height";
    public static final String SP_NAME = ConstantUtil.APPNAME + "_sharedata";
    private static Context context;
    private static SharedPreferences sp;

    public static void init(Context context) {
        if (sp == null) {
            ShareData.context = context;
            sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        }
    }

    /**
     * 是否已经登录
     */
    public static boolean isLogined() {
        return !TextUtils.isEmpty(getShareStringData(ID)) && !TextUtils.isEmpty(getShareStringData(NAME)) && !TextUtils.isEmpty(getShareStringData(PASSWORD));
    }

    /**
     * 保存用户信息
     */
    public static void save(User user) {
        setShareStringData(ID, user.getId());
        setShareStringData(NAME, user.getName());
        setShareStringData(PASSWORD, user.getPassword());
        setShareStringData(NICKNAME, user.getNickname());
        setShareIntData(GENDER, user.getGender());
        setShareStringData(AVATAR, user.getAvatar());
        setShareStringData(BIRTHDAY, user.getBirthday());
        setShareStringData(LOGIN_TIME, user.getLoginTime());
        setShareStringData(CREATE_TIME, user.getCreateTime());
        setShareStringData(ANOTHER, user.getAnother());
        setShareStringData(ANOTHER_NICKNAME, user.getAnotherNickname());
        setShareIntData(ANOTHER_GENDER, user.getAnotherGender());
        setShareStringData(ANOTHER_AVATAR, user.getAnotherAvatar());
    }

    public static int getShareIntData(String key) {
        if (sp == null)
            return 0;
        return sp.getInt(key, 0);
    }

    public static void setShareIntData(String key, int value) {
        if (sp == null)
            return;
        sp.edit().putInt(key, value).commit();
    }

    public static String getShareStringData(String key) {
        if (sp == null)
            return "";
        return sp.getString(key, "");
    }

    public static void setShareStringData(String key, String value) {
        if (sp == null)
            return;
        sp.edit().putString(key, value).commit();
    }

    public static boolean getShareBooleanData(String key) {
        if (sp == null)
            return false;
        return sp.getBoolean(key, false);
    }

    public static void setShareBooleanData(String key, boolean value) {
        if (sp == null)
            return;
        sp.edit().putBoolean(key, value).commit();
    }

    public static float getShareFloatData(String key) {
        if (sp == null)
            return 0f;
        return sp.getFloat(key, 0f);
    }

    public static void setShareFloatData(String key, float value) {
        if (sp == null)
            return;
        sp.edit().putFloat(key, value).commit();
    }

    public static long getShareLongData(String key) {
        if (sp == null)
            return 0l;
        return sp.getLong(key, 0l);
    }

    public static void setShareLongData(String key, long value) {
        if (sp == null)
            return;
        sp.edit().putLong(key, value).commit();
    }

    public static void remove(String key) {
        if (sp == null)
            return;
        sp.edit().remove(key).commit();
    }

    public static void clear() {
        sp.edit().clear().commit();
    }

}
