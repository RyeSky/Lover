package com.skye.lover.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.skye.lover.LoverApplication;
import com.skye.lover.R;
import com.skye.lover.model.Photo;
import com.skye.lover.model.PhotoFolder;
import com.skye.lover.util.CommonUtil;
import com.skye.lover.util.ThumbnailsUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 本地相册文件夹适配器
 */
public class PhotoFolderAdapter extends BaseAdapter {
    private static final int PHOTO_WIDTH_HEIGHT = (CommonUtil.getWindowWidth(LoverApplication.getInstance())
            - LoverApplication.getInstance().getResources().getDimensionPixelSize(R.dimen.horizontal) * 4) / 3;//图片宽高
    private Context context;
    private LayoutInflater inflater;
    private List<PhotoFolder> list = new ArrayList<>();//相册文件夹信息集合

    public PhotoFolderAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setList(List<PhotoFolder> list) {
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
            convertView = inflater.inflate(R.layout.item_photo_folder, null);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else vh = (ViewHolder) convertView.getTag();
        PhotoFolder pf = list.get(position);
        Photo firstPhoto = pf.getPhotos().get(0);
        ImageLoader.getInstance().displayImage(ThumbnailsUtil.get(firstPhoto.getImageId(), firstPhoto.getDisplayPath()), vh.img);
        vh.txt.setText(pf.getDisplayName() + "(" + pf.size() + "张)");
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.img)
        ImageView img;//相册文件夹缩略图
        @BindView(R.id.txt)
        TextView txt;//相册文件夹信息

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) img.getLayoutParams();
            lp.width = PHOTO_WIDTH_HEIGHT;
            lp.height = PHOTO_WIDTH_HEIGHT;
            img.setLayoutParams(lp);
        }
    }
}
