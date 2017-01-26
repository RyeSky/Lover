package com.skye.lover.activity.my.userinfo;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.skye.lover.R;
import com.skye.lover.activity.base.BaseActivity;
import com.skye.lover.model.AvatarTag;
import com.skye.lover.util.CommonUtil;
import com.skye.lover.util.ShareData;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 用戶信息界面
 */
public class UserInfoActivity extends BaseActivity {
    @BindView(R.id.img_avatar)
    ImageView avatar;//头像
    @BindView(R.id.tv_nickname)
    TextView nickname;//昵称
    @BindView(R.id.tv_gender)
    TextView gender;//性别
    @BindView(R.id.tv_birthday)
    TextView birthday;//生日

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_user_info;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTopbar();
        topbar.title.setText(R.string.user_info);
        topbar.left.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String photo = ShareData.getShareStringData(ShareData.AVATAR);//头像
        int gender = ShareData.getShareIntData(ShareData.GENDER);//性别
        //头像
        if (!TextUtils.isEmpty(photo)) {
            AvatarTag tag = new AvatarTag(photo, gender);
            if (!tag.equals(avatar.getTag())) {
                ImageLoader.getInstance().displayImage(photo, avatar, CommonUtil.getDisplayOptions(gender));
                avatar.setTag(tag);
            }
        } else {
            avatar.setImageResource(CommonUtil.getDefaultImageResource(gender));
            avatar.setTag(null);
        }
        //昵称
        nickname.setText(ShareData.getShareStringData(ShareData.NICKNAME));
        //性别
        if (gender == 1) this.gender.setText(R.string.male);
        else if (gender == 2) this.gender.setText(R.string.female);
        else this.gender.setText(R.string.secret);
        //生日
        birthday.setText(ShareData.getShareStringData(ShareData.BIRTHDAY));
    }

    @OnClick({R.id.ll_avatar, R.id.ll_nickname, R.id.ll_gender, R.id.ll_birthday, R.id.tv_update_password})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_avatar://头像
                startActivity(new Intent(context, AvatarActivity.class));
                break;
            case R.id.ll_nickname://更新昵称
                startActivity(new Intent(context, UpdateNicknameActivity.class));
                break;
            case R.id.ll_gender://更新性别
                startActivity(new Intent(context, UpdateGenderActivity.class));
                break;
            case R.id.ll_birthday://更新生日
                startActivity(new Intent(context, UpdateBirthdayActivity.class));
                break;
            case R.id.tv_update_password://修改密码
                startActivity(new Intent(context, UpdatePasswordActivity.class));
                break;
            default:
                break;
        }
    }
}
