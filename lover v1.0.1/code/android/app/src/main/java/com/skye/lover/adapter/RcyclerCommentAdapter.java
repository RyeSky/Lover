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
import com.skye.lover.model.Comment;
import com.skye.lover.model.User;
import com.skye.lover.util.CommonUtil;
import com.skye.lover.util.URLConfig;
import com.skye.lover.view.exrecyclerview.ExRcyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 评论列表适配器
 */
public class RcyclerCommentAdapter extends ExRcyclerViewAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<Comment> list = new ArrayList<>();

    public RcyclerCommentAdapter(Context context) {
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
    public int getItemCount() {
        return list.size() + getHeaderCount() + getFooterCount();
    }

    @Override
    public Object get(int position) {
        return list.get(position);
    }

    @SuppressLint("InflateParams")
    @Override
    public android.support.v7.widget.RecyclerView.ViewHolder createViewHolder() {
        View view = inflater.inflate(R.layout.item_comment, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindCustomViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder vh = (ViewHolder) holder;
        Comment comment = list.get(position);
        //头像
        vh.avatar.getHierarchy().setFailureImage(CommonUtil.getFailureImage(comment.getGender()));
        if (!TextUtils.isEmpty(comment.getAvatar())) {
            if (!comment.getAvatar().equals(vh.avatar.getTag())) {
                vh.avatar.setImageURI(URLConfig.SERVER_HOST + comment.getAvatar());
                vh.avatar.setTag(comment.getAvatar());
            }
        } else if (vh.avatar.getTag() == null || !ConstantUtil.APPNAME.equals(vh.avatar.getTag())) {
            vh.avatar.setImageURI("");
            vh.avatar.setTag(ConstantUtil.APPNAME);
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
    }

    static class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder {
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
