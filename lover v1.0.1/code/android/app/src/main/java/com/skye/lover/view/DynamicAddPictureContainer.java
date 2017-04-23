package com.skye.lover.view;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.skye.lover.ConstantUtil;
import com.skye.lover.R;
import com.skye.lover.util.CommonUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 动态添加图片容器
 */
public class DynamicAddPictureContainer extends RelativeLayout implements View.OnClickListener {
    private List<String> pictures = new ArrayList<>();
    private SimpleDraweeView imgs[] = new SimpleDraweeView[12];
    private View anchors[] = new View[3], deletes[] = new View[12];
    private OnClickAddPictureListener listener;

    public DynamicAddPictureContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.dynamic_add_picture_container, this, true);
        setPadding(CommonUtil.dp2px(context, 10), 0, CommonUtil.dp2px(context, 10), 0);
        int screenWidth = CommonUtil.getWindowWidth(context);
        int margin = CommonUtil.dp2px(context, 3);
        int widthheight = (screenWidth - CommonUtil.dp2px(context, 44)) / 4;
        int imgIds[] = new int[]{R.id.img0, R.id.img1, R.id.img2, R.id.img3
                , R.id.img4, R.id.img5, R.id.img6, R.id.img7
                , R.id.img8, R.id.img9, R.id.img10, R.id.img11};
        LayoutParams lp;
        for (int i = 0; i < 12; i++) {
            imgs[i] = (SimpleDraweeView) findViewById(imgIds[i]);
            lp = (LayoutParams) imgs[i].getLayoutParams();
            lp.width = widthheight;
            lp.height = widthheight;
            lp.setMargins(margin, margin, margin, margin);
            imgs[i].setLayoutParams(lp);
            imgs[i].setOnClickListener(this);
        }
        int anchorIds[] = new int[]{R.id.anchor0, R.id.anchor1, R.id.anchor2};
        for (int i = 0; i < 3; i++) {
            anchors[i] = findViewById(anchorIds[i]);
            lp = (LayoutParams) anchors[i].getLayoutParams();
            lp.height = widthheight;
            lp.setMargins(0, margin, 0, margin);
            anchors[i].setLayoutParams(lp);
        }
        int deleteIds[] = new int[]{R.id.delete0, R.id.delete1, R.id.delete2, R.id.delete3
                , R.id.delete4, R.id.delete5, R.id.delete6, R.id.delete7
                , R.id.delete8, R.id.delete9, R.id.delete10, R.id.delete11};
        for (int i = 0; i < 12; i++) {
            deletes[i] = findViewById(deleteIds[i]);
            deletes[i].setOnClickListener(this);
        }
        notifyDataSetChanged();
    }

    public void setOnClickAddPictureListener(OnClickAddPictureListener listener) {
        this.listener = listener;
    }

    /**
     * 添加一张图片
     */
    public void add(String picture) {
        if (!TextUtils.isEmpty(picture)) {
            pictures.add(picture);
            notifyDataSetChanged();
        }
    }

    /**
     * 批量添加图片
     */
    public void add(List<String> pictures) {
        if (pictures != null && !pictures.isEmpty()) {
            this.pictures.addAll(pictures);
            notifyDataSetChanged();
        }
    }

    /**
     * 图片列表是否为空
     */
    public boolean isEmpty() {
        return pictures.isEmpty();
    }

    /**
     * 图片列表长度
     */
    public int size() {
        return pictures.size();
    }

    public List<String> getPictures() {
        return pictures;
    }

    private void notifyDataSetChanged() {
        int size = pictures.size();
        int total = size + 1;
        int row = total / 4;
        if (total % 4 != 0)
            row += 1;
        int rowEnd = row * 4;//结束行最后一个item的索引
        List<Image> pis = new ArrayList<>();
        Image image;
        if (pictures != null) {
            for (int i = 0; i < pictures.size(); i++) {
                image = new Image();
                image.type = 1;
                image.filePath = pictures.get(i);
                image.position = i;
                pis.add(image);
            }
        }
        if (pictures == null || pictures.size() < ConstantUtil.MAX_PHOTOS_SIZE) {
            image = new Image();
            image.type = 0;
            pis.add(image);
        }
        //显示隐藏锚
        for (int i = 0; i < 3; i++)
            anchors[i].setVisibility(i < row ? VISIBLE : GONE);
        //显示图片
        size = pis.size();
        for (int i = 0; i < 12; i++) {
            if (i < size) {//需要显示
                image = pis.get(i);
                if (image.type == 0) {//图标
                    imgs[i].getHierarchy().setActualImageScaleType(ScalingUtils.ScaleType.FIT_XY);
                    imgs[i].setImageURI("");
                    imgs[i].setTag(ConstantUtil.APPNAME);
                    deletes[i].setVisibility(GONE);
                } else {//图片
                    imgs[i].getHierarchy().setActualImageScaleType(ScalingUtils.ScaleType.FOCUS_CROP);
                    displayImage(image, imgs[i]);
                    imgs[i].setTag(image);
                    deletes[i].setVisibility(VISIBLE);
                }
                imgs[i].setVisibility(VISIBLE);
            } else if (i < rowEnd) {//需要显示但不能触发点击
                imgs[i].setTag(null);
                deletes[i].setVisibility(GONE);
                imgs[i].setVisibility(INVISIBLE);
            } else {//不显示
                imgs[i].setTag(null);
                deletes[i].setVisibility(GONE);
                imgs[i].setVisibility(GONE);
            }
        }
    }

    /**
     * 显示图片
     */
    private void displayImage(Image image, SimpleDraweeView img) {
        if (!TextUtils.isEmpty(image.filePath)) {
            if (!image.equals(img.getTag())) {
                img.setImageURI("file://" + image.filePath);
                img.setTag(image);
            }
        } else {
            img.setImageURI(Uri.parse("res://com.skye.lover/" + R.drawable.shape_white));
            img.setTag(null);
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img0:
            case R.id.img1:
            case R.id.img2:
            case R.id.img3:
            case R.id.img4:
            case R.id.img5:
            case R.id.img6:
            case R.id.img7:
            case R.id.img8:
                if (listener != null && v.getTag() instanceof String)
                    listener.onClickAddPicture();
                break;
            case R.id.delete0:
                pictures.remove(0);
                notifyDataSetChanged();
                break;
            case R.id.delete1:
                pictures.remove(1);
                notifyDataSetChanged();
                break;
            case R.id.delete2:
                pictures.remove(2);
                notifyDataSetChanged();
                break;
            case R.id.delete3:
                pictures.remove(3);
                notifyDataSetChanged();
                break;
            case R.id.delete4:
                pictures.remove(4);
                notifyDataSetChanged();
                break;
            case R.id.delete5:
                pictures.remove(5);
                notifyDataSetChanged();
                break;
            case R.id.delete6:
                pictures.remove(6);
                notifyDataSetChanged();
                break;
            case R.id.delete7:
                pictures.remove(7);
                notifyDataSetChanged();
                break;
            case R.id.delete8:
                pictures.remove(8);
                notifyDataSetChanged();
                break;
            default:
                break;
        }
    }

    /**
     * 添加图片点击事件监听
     */
    public interface OnClickAddPictureListener {
        void onClickAddPicture();
    }

    /**
     * 图片信息
     */
    private class Image {
        int type, position;// 【0：图标;1:图片】
        String filePath;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Image image = (Image) o;
            return filePath != null ? filePath.equals(image.filePath) : image.filePath == null;
        }

        @Override
        public int hashCode() {
            return filePath != null ? filePath.hashCode() : 0;
        }

        @Override
        public String toString() {
            return "Image{" +
                    "type=" + type +
                    ", position=" + position +
                    ", filePath='" + filePath + '\'' +
                    '}';
        }
    }
}
