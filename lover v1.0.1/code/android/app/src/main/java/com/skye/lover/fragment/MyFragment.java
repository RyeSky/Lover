package com.skye.lover.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jakewharton.rxbinding.view.RxView;
import com.skye.lover.ConstantUtil;
import com.skye.lover.R;
import com.skye.lover.activity.my.CollectedPillowTalkListActivity;
import com.skye.lover.activity.my.CommentedPillowTalkListActivity;
import com.skye.lover.activity.my.MessageListActivity;
import com.skye.lover.activity.my.PraisedPillowTalkListActivity;
import com.skye.lover.activity.my.setting.SettingActivity;
import com.skye.lover.activity.my.userinfo.UserInfoActivity;
import com.skye.lover.activity.user.OtherInfoActivity;
import com.skye.lover.model.AvatarTag;
import com.skye.lover.model.User;
import com.skye.lover.mvp.presenter.BasePresenterEmptyImpl;
import com.skye.lover.mvp.view.BaseView;
import com.skye.lover.util.CommonUtil;
import com.skye.lover.util.ShareDataUtil;
import com.skye.lover.util.URLConfig;
import com.skye.lover.view.Topbar;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * “我的”碎片
 */
public class MyFragment extends BaseFragment<BaseView, BasePresenterEmptyImpl> {
    @BindView(R.id.topbar)
    Topbar topbar;//标题栏
    @BindView(R.id.img_avatar)
    SimpleDraweeView avatar;//头像
    @BindView(R.id.tv_nickname)
    TextView nickname;//昵称
    @BindView(R.id.img_avatar_another)
    SimpleDraweeView anotherAvatar;//相恋关系中的另一方头像
    @BindView(R.id.tv_nickname_another)
    TextView anotherNickname;//相恋关系中的另一方昵称
    @BindView(R.id.split_line_another_top)
    View splitLineAnotherTop;
    @BindView(R.id.split_line_another_bottom)
    View getSplitLineAnotherBottom;
    @BindView(R.id.ll_another)
    View another;

    @Override
    public BasePresenterEmptyImpl createPresenter() {
        return null;
    }

    @SuppressLint("InflateParams")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_my, null);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ButterKnife.bind(this, view);
        topbar.title.setText(R.string.my);
        topbar.right.setText(R.string.setting);
        topbar.right.setVisibility(View.VISIBLE);

        //设置
        RxView.clicks(topbar.right).throttleFirst(1, TimeUnit.SECONDS).subscribe((v) -> startActivity(new Intent(activity, SettingActivity.class)));
        //用户信息
        RxView.clicks(view.findViewById(R.id.rl_user_info)).throttleFirst(1, TimeUnit.SECONDS)
                .subscribe((v) -> startActivity(new Intent(activity, UserInfoActivity.class)));
        //恋爱关系中的另一方
        RxView.clicks(view.findViewById(R.id.ll_another)).throttleFirst(1, TimeUnit.SECONDS)
                .subscribe((v) -> startActivity(new Intent(activity, OtherInfoActivity.class)
                        .putExtra(OtherInfoActivity.ANOTHER, ShareDataUtil.get(activity, User.ANOTHER))
                        .putExtra(OtherInfoActivity.ANOTHER_NICKNAME, ShareDataUtil.get(activity, User.ANOTHER_NICKNAME))));
        //收藏列表
        RxView.clicks(view.findViewById(R.id.ll_collection)).throttleFirst(1, TimeUnit.SECONDS)
                .subscribe((v) -> startActivity(new Intent(activity, CollectedPillowTalkListActivity.class)));
        //赞列表
        RxView.clicks(view.findViewById(R.id.ll_praise)).throttleFirst(1, TimeUnit.SECONDS)
                .subscribe((v) -> startActivity(new Intent(activity, PraisedPillowTalkListActivity.class)));
        //评论列表
        RxView.clicks(view.findViewById(R.id.ll_comment)).throttleFirst(1, TimeUnit.SECONDS)
                .subscribe((v) -> startActivity(new Intent(activity, CommentedPillowTalkListActivity.class)));
        //消息列表
        RxView.clicks(view.findViewById(R.id.ll_message)).throttleFirst(1, TimeUnit.SECONDS)
                .subscribe((v) -> startActivity(new Intent(activity, MessageListActivity.class)));
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!hidden) setViewData();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        this.hidden = hidden;
        if (!hidden) setViewData();
    }

    /**
     * 设置界面数据
     */
    private void setViewData() {
        String photo = ShareDataUtil.get(activity, User.AVATAR);//头像
        int gender = User.GENDER_SECRET;//性别
        try {
            gender = Integer.parseInt(ShareDataUtil.get(activity, User.GENDER));
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
        nickname.setTextColor(activity.getResources().getColor(CommonUtil.getTextColor(gender)));
        nickname.setText(ShareDataUtil.get(activity, User.NICKNAME));
        if (!TextUtils.isEmpty(ShareDataUtil.get(activity, User.ANOTHER))) {//有恋人
            splitLineAnotherTop.setVisibility(View.VISIBLE);
            another.setVisibility(View.VISIBLE);
            getSplitLineAnotherBottom.setVisibility(View.VISIBLE);
            photo = ShareDataUtil.get(activity, User.ANOTHER_AVATAR);//相恋关系中的另一方头像
            gender = User.GENDER_SECRET;//相恋关系中的另一方性别
            try {
                gender = Integer.parseInt(ShareDataUtil.get(activity, User.ANOTHER_GENDER));
            } catch (Exception e) {
                e.printStackTrace();
            }
            //相恋关系中的另一方头像
            anotherAvatar.getHierarchy().setFailureImage(CommonUtil.getFailureImage(gender));
            if (!TextUtils.isEmpty(photo)) {
                if (!photo.equals(anotherAvatar.getTag())) {
                    anotherAvatar.setImageURI(URLConfig.SERVER_HOST + photo);
                    anotherAvatar.setTag(photo);
                }
            } else if (anotherAvatar.getTag() == null || !ConstantUtil.APPNAME.equals(anotherAvatar.getTag())) {
                anotherAvatar.setImageURI("");
                anotherAvatar.setTag(ConstantUtil.APPNAME);
            }
            //相恋关系中的另一方昵称
            anotherNickname.setTextColor(activity.getResources().getColor(CommonUtil.getTextColor(gender)));
            anotherNickname.setText(ShareDataUtil.get(activity, User.ANOTHER_NICKNAME));
        } else {//单身
            splitLineAnotherTop.setVisibility(View.GONE);
            another.setVisibility(View.GONE);
            getSplitLineAnotherBottom.setVisibility(View.GONE);
        }
    }
}
