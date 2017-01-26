package com.skye.lover.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.skye.lover.R;
import com.skye.lover.model.Emoji;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FaceAdapter extends BaseAdapter {

    private List<Emoji> data;

    private LayoutInflater inflater;

    public FaceAdapter(Context context, List<Emoji> list) {
        this.inflater = LayoutInflater.from(context);
        this.data = list;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("InflateParams")
    @SuppressWarnings("deprecation")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.face_item, null);
            vh=new ViewHolder(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        Emoji emoji = data.get(position);
        if (emoji.getId() == R.mipmap.face_delete) {
            convertView.setBackgroundDrawable(null);
            vh.img.setImageResource(emoji.getId());
            vh.face.setVisibility(View.GONE);
            vh.img.setVisibility(View.VISIBLE);
        } else if (TextUtils.isEmpty(emoji.getString())) {
            convertView.setBackgroundDrawable(null);
            vh.img.setImageDrawable(null);
            vh.face.setVisibility(View.GONE);
            vh.img.setVisibility(View.VISIBLE);
        } else {//表情
            vh.face.setText(emoji.getString());
            vh.face.setVisibility(View.VISIBLE);
            vh.img.setVisibility(View.GONE);
        }
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.face)
        TextView face;
        @BindView(R.id.img)
        ImageView img;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}