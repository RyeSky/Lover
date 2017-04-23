package com.skye.lover.listener;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import com.skye.lover.activity.user.OtherInfoActivity;
import com.skye.lover.model.User;
import com.skye.lover.util.ShareDataUtil;

/**
 * 点击头像事件监听
 */
public class OnClickAvatarListener implements View.OnClickListener {
    private Context context;
    private String another, anotherNickname;

    public OnClickAvatarListener(Context context, String another, String anotherNickname) {
        this.context = context;
        this.another = another;
        this.anotherNickname = anotherNickname;
    }

    @Override
    public void onClick(View v) {
        if (context == null || TextUtils.isEmpty(another) || TextUtils.isEmpty(anotherNickname))
            return;
        if (ShareDataUtil.get(context, User.ID).equals(another)) return;//对方就是登录用户，用户不能和自己聊天
        context.startActivity(new Intent(context, OtherInfoActivity.class)
                .putExtra(OtherInfoActivity.ANOTHER, another)
                .putExtra(OtherInfoActivity.ANOTHER_NICKNAME, anotherNickname));
    }
}
