package com.skye.lover.activity.user;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jakewharton.rxbinding.view.RxView;
import com.skye.actionsheet.ActionSheet;
import com.skye.lover.ConstantUtil;
import com.skye.lover.R;
import com.skye.lover.activity.MainLoverActivity;
import com.skye.lover.activity.MainSingleActivity;
import com.skye.lover.activity.base.BaseActivity;
import com.skye.lover.activity.my.userinfo.AvatarActivity;
import com.skye.lover.activity.pillowtalk.OthersPillowTalkListActivity;
import com.skye.lover.activity.privatemessage.PrivateMessageActivity;
import com.skye.lover.model.AvatarTag;
import com.skye.lover.model.User;
import com.skye.lover.mvp.presenter.OtherInfoPresenter;
import com.skye.lover.mvp.view.OtherInfoView;
import com.skye.lover.util.CommonUtil;
import com.skye.lover.util.ShareDataUtil;
import com.skye.lover.util.URLConfig;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;

/**
 * 其他用户信息界面
 */
public class OtherInfoActivity extends BaseActivity<OtherInfoView, OtherInfoPresenter> implements OtherInfoView {
    public static final String ANOTHER = "another";
    public static final String ANOTHER_NICKNAME = "anotherNickname";
    @BindView(R.id.img_avatar)
    SimpleDraweeView avatar;//头像
    @BindView(R.id.ll_another)
    View llAnother;//对方
    @BindView(R.id.tv_status)
    TextView status;//情感状态
    @BindView(R.id.img_avatar_another)
    SimpleDraweeView avatarAnother;//对方的头像
    @BindView(R.id.mark)
    View mark;
    @BindView(R.id.tv_nickname)
    TextView nickname;//昵称
    @BindView(R.id.tv_gender)
    TextView gender;//性别
    @BindView(R.id.tv_birthday)
    TextView birthday;//生日
    private String another, anotherNickname, headerImg;

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_other_info;
    }

    @Override
    public OtherInfoPresenter createPresenter() {
        return new OtherInfoPresenter();
    }

    @Override
    public int getDescription() {
        return R.string.other_info_activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTopbar();
        topbar.left.setVisibility(View.VISIBLE);
        topbar.right.setText(R.string.chat);
        topbar.right.setVisibility(View.VISIBLE);
        Intent intent = getIntent();
        another = intent.getStringExtra(ANOTHER);
        anotherNickname = intent.getStringExtra(ANOTHER_NICKNAME);
        topbar.title.setText(anotherNickname);
        presenter.otherInfo(another);

        //聊聊
        RxView.clicks(topbar.right).throttleFirst(1, TimeUnit.SECONDS).subscribe((v) ->
                startActivity(new Intent(context, PrivateMessageActivity.class)
                        .putExtra(PrivateMessageActivity.ANOTHER, another)
                        .putExtra(PrivateMessageActivity.ANOTHER_NICKNAME, anotherNickname)));
        //头像
        RxView.clicks(findViewById(R.id.ll_avatar)).throttleFirst(1, TimeUnit.SECONDS).subscribe((v) -> {
            if (TextUtils.isEmpty(headerImg)) return;
            startActivity(new Intent(context, AvatarActivity.class).putExtra(AvatarActivity.AVATAR, headerImg));
        });
        //对方用户发表的悄悄话或世界广播
        RxView.clicks(findViewById(R.id.ll_pillow_talk)).throttleFirst(1, TimeUnit.SECONDS).subscribe((v) ->
                startActivity(new Intent(context, OthersPillowTalkListActivity.class)
                        .putExtra(OthersPillowTalkListActivity.USER_ID, another)
                        .putExtra(OthersPillowTalkListActivity.NICKNAME, anotherNickname))
        );
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        intent = getIntent();
        another = intent.getStringExtra(ANOTHER);
        anotherNickname = intent.getStringExtra(ANOTHER_NICKNAME);
        topbar.title.setText(anotherNickname);
        presenter.otherInfo(another);
    }

    @Override
    public void afterOtherInfo(User user) {
        headerImg = user.getAvatar();
        //头像
        avatar.getHierarchy().setFailureImage(CommonUtil.getFailureImage(user.getGender()));
        if (!TextUtils.isEmpty(headerImg)) {
            AvatarTag tag = new AvatarTag(headerImg, user.getGender());
            if (!tag.equals(avatar.getTag())) {
                avatar.setImageURI(URLConfig.SERVER_HOST + tag.avatar);
                avatar.setTag(tag);
            }
        } else if (avatar.getTag() == null || !ConstantUtil.APPNAME.equals(avatar.getTag())) {
            avatar.setImageURI("");
            avatar.setTag(ConstantUtil.APPNAME);
        }
        if (!TextUtils.isEmpty(user.getAnother())) {//已经有恋人
            status.setText(R.string.lover);
            avatarAnother.getHierarchy().setFailureImage(CommonUtil.getFailureImage(user.getAnotherGender()));
            if (!TextUtils.isEmpty(user.getAnotherAvatar())) {
                AvatarTag tag = new AvatarTag(user.getAnotherAvatar(), user.getAnotherGender());
                if (!tag.equals(avatarAnother.getTag())) {
                    avatarAnother.setImageURI(URLConfig.SERVER_HOST + tag.avatar);
                    avatarAnother.setTag(tag);
                }
            } else if (avatarAnother.getTag() == null || !ConstantUtil.APPNAME.equals(avatarAnother.getTag())) {
                avatarAnother.setImageURI("");
                avatarAnother.setTag(ConstantUtil.APPNAME);
            }
            avatarAnother.setVisibility(View.VISIBLE);
            if (ShareDataUtil.get(context, User.ID).equals(user.getAnother())) {//对方正是登录用户
                mark.setVisibility(View.VISIBLE);
                llAnother.setOnClickListener(new OnClickBreakUpListener(user.getId()));//单方面分手
            }
        } else {//单身
            status.setText(R.string.single);
            mark.setVisibility(View.VISIBLE);
            if (TextUtils.isEmpty(ShareDataUtil.get(context, User.ANOTHER)))//如果登录用户也是单身，则可以示爱对方
                llAnother.setOnClickListener(new OnClickFallInLoveListener(user.getId()));//示爱等待对方同意
        }
        //昵称
        nickname.setText(user.getNickname());
        //性别
        if (user.getGender() == 1)
            OtherInfoActivity.this.gender.setText(R.string.male);
        else if (user.getGender() == 2)
            OtherInfoActivity.this.gender.setText(R.string.female);
        else OtherInfoActivity.this.gender.setText(R.string.secret);
        //生日
        birthday.setText(user.getBirthday());
    }

    @Override
    public void afterBreakUp() {
        toast(R.string.break_up_success);
        ShareDataUtil.set(context, User.ANOTHER, "");
        ShareDataUtil.set(context, User.ANOTHER_NICKNAME, "");
        ShareDataUtil.set(context, User.ANOTHER_AVATAR, "");
        ShareDataUtil.set(context, User.ANOTHER_GENDER, "0");
        //重新成为单身贵族
        Intent intent = new Intent(context, MainSingleActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void afterFallInLove(User user) {
        if (user != null) {
            CommonUtil.toast(context, R.string.congratulation);
            ShareDataUtil.set(context, User.ANOTHER, user.getId());
            ShareDataUtil.set(context, User.ANOTHER_NICKNAME, user.getNickname());
            ShareDataUtil.set(context, User.ANOTHER_AVATAR, user.getAvatar());
            ShareDataUtil.set(context, User.ANOTHER_GENDER, user.getGender() + "");
            //恭喜你们成为恋人
            Intent intent = new Intent(context, MainLoverActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else CommonUtil.toast(context, R.string.courtshipdisplay_success);
    }

    /**
     * 分手点击事件监听
     */
    private class OnClickBreakUpListener implements View.OnClickListener {
        private String another;//对方用户id

        OnClickBreakUpListener(String another) {
            this.another = another;
        }

        @Override
        public void onClick(View v) {
            setTheme(R.style.ActionSheetStyleIOS7);
            ActionSheet menuView = new ActionSheet(context);
            menuView.setCancelButtonTitle(getString(R.string.cancel));
            menuView.addItems(getString(R.string.break_up));
            menuView.setItemClickListener((int itemPosition) -> {
                switch (itemPosition) {
                    case 0:
                        presenter.breakUp(another);
                        break;
                    default:
                        break;
                }
            });
            menuView.setCancelableOnTouchMenuOutside(true);
            menuView.showMenu();
        }
    }

    /**
     * 坠入爱河点击事件监听
     */
    private class OnClickFallInLoveListener implements View.OnClickListener {
        private String another;//对方用户id

        OnClickFallInLoveListener(String another) {
            this.another = another;
        }

        @Override
        public void onClick(View v) {
            setTheme(R.style.ActionSheetStyleIOS7);
            ActionSheet menuView = new ActionSheet(context);
            menuView.setCancelButtonTitle(getString(R.string.cancel));
            menuView.addItems(getString(R.string.courtshipdisplay));
            menuView.setItemClickListener((int itemPosition) -> {
                switch (itemPosition) {
                    case 0:
                        presenter.fallInLove(another);
                        break;
                    default:
                        break;
                }
            });
            menuView.setCancelableOnTouchMenuOutside(true);
            menuView.showMenu();
        }
    }
}
