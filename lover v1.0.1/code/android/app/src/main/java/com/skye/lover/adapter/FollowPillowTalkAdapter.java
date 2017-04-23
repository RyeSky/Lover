package com.skye.lover.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.skye.lover.ConstantUtil;
import com.skye.lover.R;
import com.skye.lover.listener.OnClickAvatarListener;
import com.skye.lover.model.FollowPillowTalk;
import com.skye.lover.model.User;
import com.skye.lover.util.CommonUtil;
import com.skye.lover.util.URLConfig;
import com.skye.lover.view.exrecyclerview.ExRcyclerViewAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 跟随悄悄话列表适配器
 */
public class FollowPillowTalkAdapter extends ExRcyclerViewAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<FollowPillowTalk> list;

    public FollowPillowTalkAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setList(List<FollowPillowTalk> list) {
        if (list == null || list.isEmpty()) return;
        this.list = list;
        notifyDataSetChanged();
    }

    public void remove(FollowPillowTalk fpt) {
        if (list == null || list.isEmpty()) return;
        list.remove(fpt);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return (list == null ? 0 : list.size()) + getHeaderCount() + getFooterCount();
    }

    @Override
    public Object get(int position) {
        return list.get(position);
    }

    @SuppressLint("InflateParams")
    @Override
    public android.support.v7.widget.RecyclerView.ViewHolder createViewHolder() {
        View view = inflater.inflate(R.layout.item_follow_pillow_talk, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindCustomViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder vh = (ViewHolder) holder;
        FollowPillowTalk fpt = list.get(position);
        vh.divider.setVisibility(position == 0 ? View.INVISIBLE : View.VISIBLE);
        //头像
        vh.avatar.getHierarchy().setFailureImage(CommonUtil.getFailureImage(fpt.getGender()));
        if (!TextUtils.isEmpty(fpt.getAvatar())) {
            if (!fpt.getAvatar().equals(vh.avatar.getTag())) {
                vh.avatar.setImageURI(URLConfig.SERVER_HOST + fpt.getAvatar());
                vh.avatar.setTag(fpt.getAvatar());
            }
        } else if (vh.avatar.getTag() == null || !ConstantUtil.APPNAME.equals(vh.avatar.getTag())) {
            vh.avatar.setImageURI("");
            vh.avatar.setTag(ConstantUtil.APPNAME);
        }
        vh.avatar.setOnClickListener(new OnClickAvatarListener(context, fpt.getPublisher(), fpt.getNickname()));
        //昵称
        StringBuilder nickname = new StringBuilder();
        if (fpt.getGender() == User.GENDER_MALE) nickname.append("<font color=\"#1993D2\">");
        else if (fpt.getGender() == User.GENDER_FEMALE) nickname.append("<font color=\"#FB79A6\">");
        else nickname.append("<font color=\"#7F7F7F\">");
        nickname.append(fpt.getNickname());
        nickname.append("</font>");
        vh.nickname.setText(Html.fromHtml(nickname.toString()));
        //时间
        vh.time.setText(fpt.getCreateTime());
        //内容
        vh.content.setText(fpt.getContent());
    }

    static class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder {
        @BindView(R.id.divider)
        View divider;//分隔线
        @BindView(R.id.avatar)
        SimpleDraweeView avatar;//头像
        @BindView(R.id.nickname)
        TextView nickname;//昵称
        @BindView(R.id.time)
        TextView time;//发表时间
        @BindView(R.id.content)
        TextView content;//悄悄话内容

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
