package com.skye.lover.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.skye.lover.R;
import com.skye.lover.listener.OnClickAvatarListener;
import com.skye.lover.model.FollowPillowTalk;
import com.skye.lover.model.User;
import com.skye.lover.util.CommonUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 跟随悄悄话列表适配器
 */
public class FollowPillowTalkAdapter extends BaseAdapter {
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
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return list == null ? null : list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_follow_pillow_talk, null);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else vh = (ViewHolder) convertView.getTag();
        FollowPillowTalk fpt = list.get(position);
        vh.divider.setVisibility(position == 0 ? View.INVISIBLE : View.VISIBLE);
        //头像
        if (!TextUtils.isEmpty(fpt.getAvatar())) {
            if (!fpt.getAvatar().equals(vh.avatar.getTag())) {
                ImageLoader.getInstance().displayImage(fpt.getAvatar(), vh.avatar, CommonUtil.getDisplayOptions(fpt.getGender()));
                vh.avatar.setTag(fpt.getAvatar());
            }
        } else {
            vh.avatar.setImageResource(CommonUtil.getDefaultImageResource(fpt.getGender()));
            vh.avatar.setTag(null);
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
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.divider)
        View divider;//分隔线
        @BindView(R.id.avatar)
        ImageView avatar;//头像
        @BindView(R.id.nickname)
        TextView nickname;//昵称
        @BindView(R.id.time)
        TextView time;//发表时间
        @BindView(R.id.content)
        TextView content;//悄悄话内容

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
