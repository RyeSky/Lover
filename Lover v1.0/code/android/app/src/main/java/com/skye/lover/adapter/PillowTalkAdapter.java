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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.skye.lover.R;
import com.skye.lover.listener.OnClickAvatarListener;
import com.skye.lover.model.AvatarTag;
import com.skye.lover.model.PillowTalk;
import com.skye.lover.model.User;
import com.skye.lover.util.CommonUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 蜜语适配器
 */
public class PillowTalkAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<PillowTalk> list = new ArrayList<>();

    public PillowTalkAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void add(List<PillowTalk> list) {
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
            convertView = inflater.inflate(R.layout.item_pillow_talk, null);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else vh = (ViewHolder) convertView.getTag();
        PillowTalk pt = list.get(position);
        //头像
        if (!TextUtils.isEmpty(pt.getPublisherAvatar())) {
            AvatarTag tag = pt.getPublisherAvatarTag();
            if (!tag.equals(vh.avatarOne.getTag())) {
                ImageLoader.getInstance().displayImage(pt.getPublisherAvatar(), vh.avatarOne, CommonUtil.getDisplayOptions(pt.getPublisherGender()));
                vh.avatarOne.setTag(tag);
            }
        } else {
            vh.avatarOne.setImageResource(CommonUtil.getDefaultImageResource(pt.getPublisherGender()));
            vh.avatarOne.setTag(null);
        }
        vh.avatarOne.setOnClickListener(new OnClickAvatarListener(context, pt.getPublisherId(), pt.getPublisherNickname()));
        if (pt.getType() == PillowTalk.PILLOW_TALK) {//悄悄话
            if (!TextUtils.isEmpty(pt.getAnotherAvatar())) {
                AvatarTag tag = pt.getAnotherAvatarTag();
                if (!tag.equals(vh.avatarAnother.getTag())) {
                    ImageLoader.getInstance().displayImage(pt.getAnotherAvatar(), vh.avatarAnother, CommonUtil.getDisplayOptions(pt.getAnotherGender()));
                    vh.avatarAnother.setTag(tag);
                }
            } else {
                vh.avatarAnother.setImageResource(CommonUtil.getDefaultImageResource(pt.getAnotherGender()));
                vh.avatarAnother.setTag(null);
            }
            vh.avatarAnother.setOnClickListener(new OnClickAvatarListener(context, pt.getAnotherId(), pt.getAnotherNickname()));
            vh.avatarAnother.setVisibility(View.VISIBLE);
        } else vh.avatarAnother.setVisibility(View.INVISIBLE);//世界广播
        //昵称
        StringBuilder nickname = new StringBuilder();
        if (pt.getPublisherGender() == 1) nickname.append("<font color=\"#1993D2\">");
        else if (pt.getPublisherGender() == 2) nickname.append("<font color=\"#FB79A6\">");
        else nickname.append("<font color=\"#7F7F7F\">");
        nickname.append("【");
        nickname.append(pt.getPublisherNickname());
        nickname.append("】</font>");
        if (pt.getType() == PillowTalk.PILLOW_TALK) {//悄悄话
            nickname.append("<font color=\"#333333\">对</font>");
            if (pt.getAnotherGender() == User.GENDER_MALE)
                nickname.append("<font color=\"#1993D2\">");
            else if (pt.getAnotherGender() == User.GENDER_FEMALE)
                nickname.append("<font color=\"#FB79A6\">");
            else nickname.append("<font color=\"#7F7F7F\">");
            nickname.append("【");
            nickname.append(pt.getAnotherNickname());
            nickname.append("】</font>");
            nickname.append("<font color=\"#333333\">说</font>");
        }
        vh.nickname.setText(Html.fromHtml(nickname.toString()));
        //时间
        vh.time1.setText(pt.getCreateTime());
        vh.time2.setText(pt.getCreateTime());
        //赞数量
        vh.praiseCount1.setText(pt.getPraiseCount() + "");
        vh.praiseCount2.setText(pt.getPraiseCount() + "");
        //评论数量
        vh.commentCount1.setText(pt.getCommentCount() + "");
        vh.commentCount2.setText(pt.getCommentCount() + "");
        //图片
        String img = "";
        if (!TextUtils.isEmpty(pt.getImgs())) {
            String imgs[] = pt.getImgs().split(",");
            if (imgs.length > 0)
                img = imgs[0];
        }
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) vh.content.getLayoutParams();
        if (!TextUtils.isEmpty(img)) {
            vh.img.setVisibility(View.VISIBLE);
            if (!img.equals(vh.img.getTag())) {
                ImageLoader.getInstance().displayImage(img, vh.img);
                vh.img.setTag(img);
            }
            lp.height = CommonUtil.dp2px(context, 100);//有图片时，悄悄话内容是和图片等高
        } else {
            vh.img.setVisibility(View.GONE);
            vh.img.setTag(null);
            lp.height = RelativeLayout.LayoutParams.WRAP_CONTENT;//没有图片时，悄悄话内容包裹内容
        }
        vh.content.setLayoutParams(lp);
        //内容
        vh.content.setText(pt.getContent());
        if (TextUtils.isEmpty(pt.getContent())) {
            vh.content.setVisibility(View.GONE);
            vh.container1.setVisibility(View.VISIBLE);
            vh.container2.setVisibility(View.GONE);
            if (pt.isOpen() || pt.getType() == PillowTalk.BROADCAST) {//公开的悄悄话或世界广播
                vh.lpc1.setVisibility(View.VISIBLE);
                vh.lcc1.setVisibility(View.VISIBLE);
                vh.unopened1.setVisibility(View.GONE);
            } else {
                vh.lpc1.setVisibility(View.INVISIBLE);
                vh.lcc1.setVisibility(View.GONE);
                vh.unopened1.setVisibility(View.VISIBLE);
            }
        } else {
            vh.content.setVisibility(View.VISIBLE);
            vh.container1.setVisibility(View.GONE);
            vh.container2.setVisibility(View.VISIBLE);
            if (pt.isOpen() || pt.getType() == PillowTalk.BROADCAST) {//公开的悄悄话或世界广播
                vh.lpc2.setVisibility(View.VISIBLE);
                vh.lcc2.setVisibility(View.VISIBLE);
                vh.unopened2.setVisibility(View.GONE);
            } else {
                vh.lpc2.setVisibility(View.INVISIBLE);
                vh.lcc2.setVisibility(View.GONE);
                vh.unopened2.setVisibility(View.VISIBLE);
            }
        }
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.avatar_one)
        ImageView avatarOne;//相恋关系中的一方头像
        @BindView(R.id.avatar_another)
        ImageView avatarAnother;//相恋关系中的另一方头像
        @BindView(R.id.img)
        ImageView img;//悄悄话图片
        @BindView(R.id.nickname)
        TextView nickname;//昵称
        @BindView(R.id.content)
        TextView content;//悄悄话内容
        @BindView(R.id.time1)
        TextView time1;//时间
        @BindView(R.id.time2)
        TextView time2;//时间
        @BindView(R.id.praise_count1)
        TextView praiseCount1;//赞数量
        @BindView(R.id.praise_count2)
        TextView praiseCount2;//赞数量
        @BindView(R.id.comment_count1)
        TextView commentCount1;//评论数量
        @BindView(R.id.comment_count2)
        TextView commentCount2;//评论数量
        @BindView(R.id.time_praise_comment1)
        View container1;
        @BindView(R.id.time_praise_comment2)
        View container2;
        @BindView(R.id.ll_praise_count1)
        View lpc1;
        @BindView(R.id.ll_praise_count2)
        View lpc2;
        @BindView(R.id.ll_comment_count1)
        View lcc1;
        @BindView(R.id.ll_comment_count2)
        View lcc2;
        @BindView(R.id.unopened1)
        View unopened1;
        @BindView(R.id.unopened2)
        View unopened2;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
