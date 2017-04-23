package com.skye.lover.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.skye.lover.ConstantUtil;
import com.skye.lover.R;
import com.skye.lover.listener.OnClickAvatarListener;
import com.skye.lover.model.AvatarTag;
import com.skye.lover.model.PrivateMessageSession;
import com.skye.lover.model.User;
import com.skye.lover.util.CommonUtil;
import com.skye.lover.util.URLConfig;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 私信会话列表适配器
 */
public class PrivateMessageSessionAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<PrivateMessageSession> list;

    public PrivateMessageSessionAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setList(List<PrivateMessageSession> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void remove(int position) {
        if (list == null) return;
        list.remove(position);
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
            convertView = inflater.inflate(R.layout.item_private_message_session, null);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else vh = (ViewHolder) convertView.getTag();
        PrivateMessageSession pms = list.get(position);
        //头像
        vh.avatar.getHierarchy().setFailureImage(CommonUtil.getFailureImage(pms.getAnotherGender()));
        if (!TextUtils.isEmpty(pms.getAnotherAvatar())) {
            AvatarTag tag = pms.getAnotherAvatarTag();
            if (!tag.equals(vh.avatar.getTag())) {
                vh.avatar.setImageURI(URLConfig.SERVER_HOST + tag.avatar);
                vh.avatar.setTag(tag);
            }
        } else if (vh.avatar.getTag() == null || !ConstantUtil.APPNAME.equals(vh.avatar.getTag())) {
            vh.avatar.setImageURI("");
            vh.avatar.setTag(ConstantUtil.APPNAME);
        }
        vh.avatar.setOnClickListener(new OnClickAvatarListener(context, pms.getAnother(), pms.getAnotherNickname()));
        //昵称
        StringBuilder nickname = new StringBuilder();
        if (pms.getAnotherGender() == User.GENDER_MALE) nickname.append("<font color=\"#1993D2\">");
        else if (pms.getAnotherGender() == User.GENDER_FEMALE)
            nickname.append("<font color=\"#FB79A6\">");
        else nickname.append("<font color=\"#7F7F7F\">");
        nickname.append(pms.getAnotherNickname());
        nickname.append("</font>");
        vh.nickname.setText(Html.fromHtml(nickname.toString()));
        //时间
        vh.time.setText(pms.getLastPrivateMessageCreateTime());
        //内容
        vh.content.setText(pms.getLastPrivateMessageContent());
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.avatar)
        SimpleDraweeView avatar;//头像
        @BindView(R.id.nickname)
        TextView nickname;//昵称
        @BindView(R.id.time)
        TextView time;//最后一条私信发表时间
        @BindView(R.id.content)
        TextView content;//最后一条私信聊天内容

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
