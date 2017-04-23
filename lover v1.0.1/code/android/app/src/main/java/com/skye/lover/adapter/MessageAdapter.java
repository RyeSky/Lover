package com.skye.lover.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.skye.lover.LoverApplication;
import com.skye.lover.R;
import com.skye.lover.model.Message;
import com.skye.lover.util.CommonUtil;
import com.skye.lover.util.URLConfig;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 消息列表适配器
 */
public class MessageAdapter extends BaseAdapter {
    private static final int IMG_HEIGHT = (int) ((CommonUtil.getWindowWidth(LoverApplication.getInstance()) - 50) * 0.618);
    private Context context;
    private LayoutInflater inflater;
    private List<Message> list = new ArrayList<>();

    public MessageAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    public void add(List<Message> list) {
        if (list != null) {
            this.list.addAll(list);
            notifyDataSetChanged();
        }
    }

    public void clear() {
        list.clear();
    }

    public void remove(int position) {
        if (position >= 0 && position < list.size()) {
            list.remove(position);
            notifyDataSetChanged();
        }
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
            convertView = inflater.inflate(R.layout.item_message, null);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else vh = (ViewHolder) convertView.getTag();
        Message message = list.get(position);
        vh.time.setText(message.getCreateTime());//时间
        vh.title.setText(message.getTitle());//标题
        vh.content.setText(message.getContent());//图片
        //没有图片时显示内容，有图片就显示图片
        String imgs = message.getImgs();
        if (!TextUtils.isEmpty(imgs)) {
            String fm = imgs.split(",")[0];
            if (!TextUtils.isEmpty(fm)) {
                vh.content.setVisibility(View.GONE);
                vh.img.setImageURI(URLConfig.SERVER_HOST + fm);
                vh.img.getHierarchy().setFailureImage(CommonUtil.emptyFailureImage);
                vh.img.setVisibility(View.VISIBLE);
            } else {
                vh.img.setVisibility(View.GONE);
                vh.content.setVisibility(View.VISIBLE);
            }
        } else {
            vh.img.setVisibility(View.GONE);
            vh.content.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.time)
        TextView time;//时间
        @BindView(R.id.title)
        TextView title;//标题
        @BindView(R.id.content)
        TextView content;//内容
        @BindView(R.id.img)
        SimpleDraweeView img;//图片

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) img.getLayoutParams();
            lp.height = IMG_HEIGHT;
            img.setLayoutParams(lp);
        }
    }
}
