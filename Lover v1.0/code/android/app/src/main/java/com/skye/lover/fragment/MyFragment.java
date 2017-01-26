package com.skye.lover.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.skye.lover.R;
import com.skye.lover.activity.my.CollectedPillowTalkListActivity;
import com.skye.lover.activity.my.CommentedPillowTalkListActivity;
import com.skye.lover.activity.my.MessageListActivity;
import com.skye.lover.activity.my.PraisedPillowTalkListActivity;
import com.skye.lover.activity.my.setting.SettingActivity;
import com.skye.lover.activity.my.userinfo.UserInfoActivity;
import com.skye.lover.activity.user.OtherInfoActivity;
import com.skye.lover.model.AvatarTag;
import com.skye.lover.util.CommonUtil;
import com.skye.lover.util.ShareData;
import com.skye.lover.view.Topbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * “我的”碎片
 */
public class MyFragment extends Fragment {
    @BindView(R.id.topbar)
    Topbar topbar;//标题栏
    @BindView(R.id.img_avatar)
    ImageView avatar;//头像
    @BindView(R.id.tv_nickname)
    TextView nickname;//昵称
    @BindView(R.id.img_avatar_another)
    ImageView anotherAvatar;//相恋关系中的另一方头像
    @BindView(R.id.tv_nickname_another)
    TextView anotherNickname;//相恋关系中的另一方昵称
    @BindView(R.id.split_line_another_top)
    View splitLineAnotherTop;
    @BindView(R.id.split_line_another_bottom)
    View getSplitLineAnotherBottom;
    @BindView(R.id.ll_another)
    View another;
    private Activity activity;//对Activity的引用
    private View view;//碎片根布局
    private boolean hidden;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
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
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!hidden)
            setViewData();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        this.hidden = hidden;
        if (!hidden)
            setViewData();
    }

    /**
     * 设置界面数据
     */
    private void setViewData() {
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
        nickname.setTextColor(activity.getResources().getColor(CommonUtil.getTextColor(gender)));
        nickname.setText(ShareData.getShareStringData(ShareData.NICKNAME));
        if (!TextUtils.isEmpty(ShareData.getShareStringData(ShareData.ANOTHER))) {//有恋人
            splitLineAnotherTop.setVisibility(View.VISIBLE);
            another.setVisibility(View.VISIBLE);
            getSplitLineAnotherBottom.setVisibility(View.VISIBLE);
            photo = ShareData.getShareStringData(ShareData.ANOTHER_AVATAR);//相恋关系中的另一方头像
            gender = ShareData.getShareIntData(ShareData.ANOTHER_GENDER);//相恋关系中的另一方性别
            //相恋关系中的另一方头像
            if (!TextUtils.isEmpty(photo)) {
                if (!photo.equals(anotherAvatar.getTag())) {
                    ImageLoader.getInstance().displayImage(photo, anotherAvatar, CommonUtil.getDisplayOptions(gender));
                    anotherAvatar.setTag(photo);
                }
            } else {
                anotherAvatar.setImageResource(CommonUtil.getDefaultImageResource(gender));
                anotherAvatar.setTag(null);
            }
            //相恋关系中的另一方昵称
            anotherNickname.setTextColor(activity.getResources().getColor(CommonUtil.getTextColor(gender)));
            anotherNickname.setText(ShareData.getShareStringData(ShareData.ANOTHER_NICKNAME));
        } else {//单身
            splitLineAnotherTop.setVisibility(View.GONE);
            another.setVisibility(View.GONE);
            getSplitLineAnotherBottom.setVisibility(View.GONE);
        }
    }

    @OnClick({R.id.tv_right, R.id.rl_user_info, R.id.ll_another, R.id.ll_collection, R.id.ll_praise, R.id.ll_comment, R.id.ll_message})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_right://设置
                startActivity(new Intent(activity, SettingActivity.class));
                break;
            case R.id.rl_user_info://用户信息
                startActivity(new Intent(activity, UserInfoActivity.class));
                break;
            case R.id.ll_another://恋爱关系中的另一方
                startActivity(new Intent(activity, OtherInfoActivity.class)
                        .putExtra(OtherInfoActivity.ANOTHER, ShareData.getShareStringData(ShareData.ANOTHER))
                        .putExtra(OtherInfoActivity.ANOTHER_NICKNAME, ShareData.getShareStringData(ShareData.ANOTHER_NICKNAME)));
                break;
            case R.id.ll_collection://收藏列表
                startActivity(new Intent(activity, CollectedPillowTalkListActivity.class));
                break;
            case R.id.ll_praise://赞列表
                startActivity(new Intent(activity, PraisedPillowTalkListActivity.class));
                break;
            case R.id.ll_comment://评论列表
                startActivity(new Intent(activity, CommentedPillowTalkListActivity.class));
                break;
            case R.id.ll_message://消息列表
                startActivity(new Intent(activity, MessageListActivity.class));
                break;
            default:
                break;
        }
    }
}
