package com.skye.lover.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;

import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.view.SimpleDraweeView;
import com.skye.lover.LoverApplication;
import com.skye.lover.R;
import com.skye.lover.model.Photo;
import com.skye.lover.util.CommonUtil;
import com.skye.lover.util.ThumbnailsUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 图片适配器
 */
public class PhotoAdapter extends BaseAdapter {
    private static final int PHOTO_WIDTH_HEIGHT = (CommonUtil.getWindowWidth(LoverApplication.getInstance())
            - LoverApplication.getInstance().getResources().getDimensionPixelSize(R.dimen.horizontal) * 4) / 3;//图片宽高
    private LayoutInflater inflater;
    private List<Photo> list = new ArrayList<>();

    public PhotoAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    public void setList(List<Photo> list) {
        this.list = list;
        notifyDataSetChanged();
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
            convertView = inflater.inflate(R.layout.item_photo, null);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else vh = (ViewHolder) convertView.getTag();
        Photo photo = list.get(position);
        vh.img.setImageURI(ThumbnailsUtil.get(photo.getImageId(), photo.getDisplayPath()));
        vh.checked.setVisibility(photo.isChecked() ? View.VISIBLE : View.GONE);
        return convertView;
    }

    /**
     * 获取选中图片的张数
     */
    public int getSelectedPhotosSize() {
        int count = 0;
        for (Photo photo : list) {
            if (photo.isChecked()) count++;
        }
        return count;
    }

    /**
     * 获取选中的图片路径集合
     */
    public List<String> getSelectedPhotos() {
        List<Photo> temp = new ArrayList<>();
        for (Photo photo : list) {
            if (photo.isChecked()) {
                temp.add(photo);
            }
        }
        List<String> ls = new ArrayList<>();
        if (!temp.isEmpty()) {
            Collections.sort(temp);
            for (Photo photo : temp) {
                ls.add(photo.getAbsolutePath());
            }
        }
        return ls;
    }

    /**
     * 指定图片是否已经被选中
     */
    public boolean isSelected(Photo photo) {
        List<String> ls = getSelectedPhotos();
        return ls.contains(photo.getAbsolutePath());
    }

    static class ViewHolder {
        @BindView(R.id.img)
        SimpleDraweeView img;//图片
        @BindView(R.id.checked)
        View checked;//是否被选中

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) img.getLayoutParams();
            lp.width = PHOTO_WIDTH_HEIGHT;
            lp.height = PHOTO_WIDTH_HEIGHT;
            img.setLayoutParams(lp);
            GenericDraweeHierarchy hierarchy = img.getHierarchy();
            hierarchy.setFailureImage(CommonUtil.emptyFailureImage);
        }
    }
}
