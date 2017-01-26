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
import com.skye.lover.model.Comment;
import com.skye.lover.model.User;
import com.skye.lover.util.CommonUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 评论列表适配器
 */
public class CommentAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<Comment> list = new ArrayList<>();

    public CommentAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void add(List<Comment> list) {
        if (list != null)
            this.list.addAll(list);
        notifyDataSetChanged();
    }

    public void remove(int position) {
        if (position >= 0 && position < list.size()) {
            list.remove(position);
            notifyDataSetChanged();
        }
    }

    public void remove(Comment comment) {
        if (list == null || list.isEmpty()) return;
        list.remove(comment);
        notifyDataSetChanged();
    }

    public void clear() {
        this.list.clear();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
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
            convertView = inflater.inflate(R.layout.item_comment, null);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else vh = (ViewHolder) convertView.getTag();
        Comment comment = list.get(position);
        //头像
        if (!TextUtils.isEmpty(comment.getAvatar())) {
            if (!comment.getAvatar().equals(vh.avatar.getTag())) {
                ImageLoader.getInstance().displayImage(comment.getAvatar(), vh.avatar, CommonUtil.getDisplayOptions(comment.getGender()));
                vh.avatar.setTag(comment.getAvatar());
            }
        } else {
            vh.avatar.setImageResource(CommonUtil.getDefaultImageResource(comment.getGender()));
            vh.avatar.setTag(null);
        }
        vh.avatar.setOnClickListener(new OnClickAvatarListener(context, comment.getCommenter(), comment.getNickname()));
        //昵称
        StringBuilder nickname = new StringBuilder();
        if (comment.getGender() == User.GENDER_MALE) nickname.append("<font color=\"#1993D2\">");
        else if (comment.getGender() == User.GENDER_FEMALE)
            nickname.append("<font color=\"#FB79A6\">");
        else nickname.append("<font color=\"#7F7F7F\">");
        nickname.append(comment.getNickname());
        nickname.append("</font>");
        vh.nickname.setText(Html.fromHtml(nickname.toString()));
        //时间
        vh.time.setText(comment.getCreateTime());
        //内容
        vh.content.setText(comment.getContent());
        return convertView;
    }

    static class ViewHolder {
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
