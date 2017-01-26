package com.skye.lover.activity.user;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.skye.actionsheet.ActionSheet;
import com.skye.lover.R;
import com.skye.lover.activity.MainLoverActivity;
import com.skye.lover.activity.MainSingleActivity;
import com.skye.lover.activity.base.BaseActivity;
import com.skye.lover.activity.my.userinfo.AvatarActivity;
import com.skye.lover.activity.pillowtalk.OthersPillowTalkListActivity;
import com.skye.lover.activity.privatemessage.PrivateMessageActivity;
import com.skye.lover.exception.NoNetworkConnectException;
import com.skye.lover.model.AvatarTag;
import com.skye.lover.model.BaseResponse;
import com.skye.lover.model.User;
import com.skye.lover.util.CommonUtil;
import com.skye.lover.util.OkHttpUtil;
import com.skye.lover.util.ShareData;
import com.skye.lover.util.URLConfig;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 其他用户信息界面
 */
public class OtherInfoActivity extends BaseActivity {
    public static final String ANOTHER = "another";
    public static final String ANOTHER_NICKNAME = "anotherNickname";
    @BindView(R.id.img_avatar)
    ImageView avatar;//头像
    @BindView(R.id.ll_another)
    View llAnother;//对方
    @BindView(R.id.tv_status)
    TextView status;//情感状态
    @BindView(R.id.img_avatar_another)
    ImageView avatarAnother;//对方的头像
    @BindView(R.id.mark)
    View mark;
    @BindView(R.id.tv_nickname)
    TextView nickname;//昵称
    @BindView(R.id.tv_gender)
    TextView gender;//性别
    @BindView(R.id.tv_birthday)
    TextView birthday;//生日
    private String another, anotherNickname, headerImg;

    /**
     * 其他用户信息接口回调
     */
    private Callback callbackOtherInfo = new Callback() {
        @Override
        public void onFailure(Call call, final IOException e) {
            e.printStackTrace();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    CommonUtil.closeLoadingDialog(dialog);
                    if (!(e instanceof NoNetworkConnectException)) //不是没有网络
                        CommonUtil.toast(context, R.string.bad_request);
                }
            });
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            final String body = response.body().string();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    CommonUtil.closeLoadingDialog(dialog);
                    try {
                        CommonUtil.log(body);
                        BaseResponse br = CommonUtil.parseToObject(body, BaseResponse.class);//转化为返回基类
                        if (br.check()) {
                            User user = CommonUtil.parseToObject(br.result, User.class);
                            headerImg = user.getAvatar();
                            //头像
                            if (!TextUtils.isEmpty(user.getAvatar())) {
                                AvatarTag tag = new AvatarTag(user.getAvatar(), user.getGender());
                                if (!tag.equals(avatar.getTag())) {
                                    ImageLoader.getInstance().displayImage(user.getAvatar(), avatar, CommonUtil.getDisplayOptions(user.getGender()));
                                    avatar.setTag(tag);
                                }
                            } else {
                                avatar.setImageResource(CommonUtil.getDefaultImageResource(user.getGender()));
                                avatar.setTag(null);
                            }
                            if (!TextUtils.isEmpty(user.getAnother())) {//已经有恋人
                                status.setText(R.string.lover);
                                if (!TextUtils.isEmpty(user.getAnotherAvatar())) {
                                    AvatarTag tag = new AvatarTag(user.getAnotherAvatar(), user.getAnotherGender());
                                    if (!tag.equals(avatarAnother.getTag())) {
                                        ImageLoader.getInstance().displayImage(user.getAnotherAvatar(), avatarAnother, CommonUtil.getDisplayOptions(user.getAnotherGender()));
                                        avatarAnother.setTag(tag);
                                    }
                                } else {
                                    avatarAnother.setImageResource(CommonUtil.getDefaultImageResource(user.getAnotherGender()));
                                    avatarAnother.setTag(null);
                                }
                                avatarAnother.setVisibility(View.VISIBLE);
                                if (ShareData.getShareStringData(ShareData.ID).equals(user.getAnother())) {//对方正是登录用户
                                    mark.setVisibility(View.VISIBLE);
                                    llAnother.setOnClickListener(new OnClickBreakUpListener(user.getId()));//单方面分手
                                }
                            } else {//单身
                                status.setText(R.string.single);
                                mark.setVisibility(View.VISIBLE);
                                if (TextUtils.isEmpty(ShareData.getShareStringData(ShareData.ANOTHER)))//如果登录用户也是单身，则可以示爱对方
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
                        } else
                            CommonUtil.toast(context, br.message);
                    } catch (Exception e) {
                        e.printStackTrace();
                        CommonUtil.toast(context, R.string.bad_request);
                    }
                }
            });
        }
    };


    /**
     * 分手接口回调
     */
    private Callback callbackBreakUp = new Callback() {
        @Override
        public void onFailure(Call call, final IOException e) {
            e.printStackTrace();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    CommonUtil.closeLoadingDialog(dialog);
                    if (!(e instanceof NoNetworkConnectException)) //不是没有网络
                        CommonUtil.toast(context, R.string.bad_request);
                }
            });
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            final String body = response.body().string();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    CommonUtil.closeLoadingDialog(dialog);
                    try {
                        CommonUtil.log(body);
                        BaseResponse br = CommonUtil.parseToObject(body, BaseResponse.class);//转化为返回基类
                        if (br.check()) {
                            CommonUtil.toast(context, R.string.break_up_success);
                            ShareData.setShareStringData(ShareData.ANOTHER, "");
                            ShareData.setShareStringData(ShareData.ANOTHER_NICKNAME, "");
                            ShareData.setShareStringData(ShareData.ANOTHER_AVATAR, "");
                            ShareData.setShareIntData(ShareData.ANOTHER_GENDER, 0);
                            //重新成为单身贵族
                            Intent intent = new Intent(context, MainSingleActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        } else
                            CommonUtil.toast(context, br.message);
                    } catch (Exception e) {
                        e.printStackTrace();
                        CommonUtil.toast(context, R.string.bad_request);
                    }
                }
            });
        }
    };

    /**
     * 坠入爱河接口回调
     */
    private Callback callbackFallInLove = new Callback() {
        @Override
        public void onFailure(Call call, final IOException e) {
            e.printStackTrace();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    CommonUtil.closeLoadingDialog(dialog);
                    if (!(e instanceof NoNetworkConnectException)) //不是没有网络
                        CommonUtil.toast(context, R.string.bad_request);
                }
            });
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            final String body = response.body().string();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        CommonUtil.closeLoadingDialog(dialog);
                        CommonUtil.log(body);
                        BaseResponse br = CommonUtil.parseToObject(body, BaseResponse.class);//转化为返回基类
                        if (br.check()) {
                            User user = CommonUtil.parseToObject(br.result, User.class);
                            if (user != null) {
                                CommonUtil.toast(context, R.string.congratulation);
                                ShareData.setShareStringData(ShareData.ANOTHER, user.getId());
                                ShareData.setShareStringData(ShareData.ANOTHER_NICKNAME, user.getNickname());
                                ShareData.setShareStringData(ShareData.ANOTHER_AVATAR, user.getAvatar());
                                ShareData.setShareIntData(ShareData.ANOTHER_GENDER, user.getGender());
                                //恭喜你们成为恋人
                                Intent intent = new Intent(context, MainLoverActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            } else CommonUtil.toast(context, R.string.courtshipdisplay_success);
                        } else
                            CommonUtil.toast(context, br.message);
                    } catch (Exception e) {
                        e.printStackTrace();
                        CommonUtil.toast(context, R.string.bad_request);
                    }
                }
            });
        }
    };

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_other_info;
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
        getOtherInfo();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        intent = getIntent();
        another = intent.getStringExtra(ANOTHER);
        anotherNickname = intent.getStringExtra(ANOTHER_NICKNAME);
        topbar.title.setText(anotherNickname);
        getOtherInfo();
    }

    /**
     * 其他用户信息
     */
    private void getOtherInfo() {
        Map<String, String> params = new HashMap<>();
        params.put("userId", another);
        dialog = CommonUtil.showLoadingDialog(context, R.string.loading);
        OkHttpUtil.doPost(context, URLConfig.ACTION_OTHER_INFO, params, callbackOtherInfo);
    }

    @OnClick({R.id.tv_right, R.id.ll_avatar, R.id.ll_pillow_talk})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_right://聊聊
                startActivity(new Intent(context, PrivateMessageActivity.class)
                        .putExtra(PrivateMessageActivity.ANOTHER, another)
                        .putExtra(PrivateMessageActivity.ANOTHER_NICKNAME, anotherNickname));
                break;
            case R.id.ll_avatar://头像
                if (TextUtils.isEmpty(headerImg)) return;
                startActivity(new Intent(context, AvatarActivity.class).putExtra(AvatarActivity.AVATAR, headerImg));
                break;
            case R.id.ll_pillow_talk://对方用户发表的悄悄话或世界广播
                startActivity(new Intent(context, OthersPillowTalkListActivity.class)
                        .putExtra(OthersPillowTalkListActivity.USER_ID, another)
                        .putExtra(OthersPillowTalkListActivity.NICKNAME, anotherNickname));
                break;
            default:
                break;
        }
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
            menuView.setItemClickListener(new ActionSheet.MenuItemClickListener() {

                @Override
                public void onItemClick(int itemPosition) {
                    switch (itemPosition) {
                        case 0:
                            Map<String, String> params = new HashMap<>();
                            params.put("userId", ShareData.getShareStringData(ShareData.ID));
                            params.put("another", another);
                            dialog = CommonUtil.showLoadingDialog(context, R.string.please_wait);
                            OkHttpUtil.doPost(context, URLConfig.ACTION_BREAK_UP, params, callbackBreakUp);
                            break;
                        default:
                            break;
                    }
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
            menuView.setItemClickListener(new ActionSheet.MenuItemClickListener() {

                @Override
                public void onItemClick(int itemPosition) {
                    switch (itemPosition) {
                        case 0:
                            Map<String, String> params = new HashMap<>();
                            params.put("userId", ShareData.getShareStringData(ShareData.ID));
                            params.put("another", another);
                            dialog = CommonUtil.showLoadingDialog(context, R.string.please_wait);
                            OkHttpUtil.doPost(context, URLConfig.ACTION_FALL_IN_LOVE, params, callbackFallInLove);
                            break;
                        default:
                            break;
                    }
                }

            });
            menuView.setCancelableOnTouchMenuOutside(true);
            menuView.showMenu();
        }
    }
}
