package com.skye.lover.activity.my.userinfo;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jakewharton.rxbinding.view.RxView;
import com.skye.lover.ConstantUtil;
import com.skye.lover.R;
import com.skye.lover.activity.base.BaseActivity;
import com.skye.lover.model.AvatarTag;
import com.skye.lover.model.User;
import com.skye.lover.mvp.presenter.BasePresenterEmptyImpl;
import com.skye.lover.mvp.view.BaseView;
import com.skye.lover.util.CommonUtil;
import com.skye.lover.util.ShareDataUtil;
import com.skye.lover.util.URLConfig;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;

/**
 * 用户信息界面
 */
public class UserInfoActivity extends BaseActivity<BaseView, BasePresenterEmptyImpl> {
    @BindView(R.id.img_avatar)
    SimpleDraweeView avatar;//头像
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
    public BasePresenterEmptyImpl createPresenter() {
        return null;
    }

    @Override
    public int getDescription() {
        return R.string.user_info_activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTopbar();
        topbar.title.setText(R.string.user_info);
        topbar.left.setVisibility(View.VISIBLE);

        //头像
        RxView.clicks(findViewById(R.id.ll_avatar)).throttleFirst(1, TimeUnit.SECONDS)
                .subscribe((v) -> startActivity(new Intent(context, AvatarActivity.class)));
        //昵称
        RxView.clicks(findViewById(R.id.ll_nickname)).throttleFirst(1, TimeUnit.SECONDS)
                .subscribe((v) -> startActivity(new Intent(context, UpdateNicknameActivity.class)));
        //性别
        RxView.clicks(findViewById(R.id.ll_gender)).throttleFirst(1, TimeUnit.SECONDS)
                .subscribe((v) -> startActivity(new Intent(context, UpdateGenderActivity.class)));
        //生日
        RxView.clicks(findViewById(R.id.ll_birthday)).throttleFirst(1, TimeUnit.SECONDS)
                .subscribe((v) -> startActivity(new Intent(context, UpdateBirthdayActivity.class)));
        //密码
        RxView.clicks(findViewById(R.id.tv_update_password)).throttleFirst(1, TimeUnit.SECONDS)
                .subscribe((v) -> startActivity(new Intent(context, UpdatePasswordActivity.class)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        String photo = ShareDataUtil.get(context, User.AVATAR);//头像
        int gender = User.GENDER_SECRET;//性别
        try {
            gender = Integer.parseInt(ShareDataUtil.get(context, User.GENDER));
        } catch (Exception e) {
            e.printStackTrace();
        }
        //头像
        avatar.getHierarchy().setFailureImage(CommonUtil.getFailureImage(gender));
        if (!TextUtils.isEmpty(photo)) {
            AvatarTag tag = new AvatarTag(photo, gender);
            if (!tag.equals(avatar.getTag())) {
                avatar.setImageURI(URLConfig.SERVER_HOST + photo);
                avatar.setTag(tag);
            }
        } else if (avatar.getTag() == null || !ConstantUtil.APPNAME.equals(avatar.getTag())) {
            avatar.setImageURI("");
            avatar.setTag(ConstantUtil.APPNAME);
        }
        //昵称
        nickname.setText(ShareDataUtil.get(context, User.NICKNAME));
        //性别
        if (gender == User.GENDER_MALE) this.gender.setText(R.string.male);
        else if (gender == User.GENDER_FEMALE) this.gender.setText(R.string.female);
        else this.gender.setText(R.string.secret);
        //生日
        birthday.setText(ShareDataUtil.get(context, User.BIRTHDAY));
    }
}
