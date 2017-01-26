package com.skye.lover.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.skye.lover.R;

import java.util.List;

/**
 * 自定义gridview3列;用于在动态列表中显示一组图片
 */
public class CustomGridView3Column extends LinearLayout {
    private Context context;
    private LinearLayout lls[];// 行
    private ImageView imgs[];// 图片控件数组
    private View ss[];// 行分割线
    private List<String> pictures;
    private OnItemClickListener listener;

    public CustomGridView3Column(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    /**
     * 设置点击监听
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    /**
     * 填充布局，获取组件引用
     */
    private void init(Context context) {
        this.context = context;
        LayoutInflater.from(this.context).inflate(R.layout.custom_gridview_3_column, this, true);

        setOrientation(VERTICAL);

        lls = new LinearLayout[3];
        lls[0] = (LinearLayout) findViewById(R.id.ll0);
        lls[1] = (LinearLayout) findViewById(R.id.ll1);
        lls[2] = (LinearLayout) findViewById(R.id.ll2);

        ss = new View[2];
        ss[0] = findViewById(R.id.s0);
        ss[1] = findViewById(R.id.s1);

        imgs = new ImageView[9];
        imgs[0] = (ImageView) findViewById(R.id.img0);
        imgs[1] = (ImageView) findViewById(R.id.img1);
        imgs[2] = (ImageView) findViewById(R.id.img2);
        imgs[3] = (ImageView) findViewById(R.id.img3);
        imgs[4] = (ImageView) findViewById(R.id.img4);
        imgs[5] = (ImageView) findViewById(R.id.img5);
        imgs[6] = (ImageView) findViewById(R.id.img6);
        imgs[7] = (ImageView) findViewById(R.id.img7);
        imgs[8] = (ImageView) findViewById(R.id.img8);
    }

    /**
     * 将各个组件设为不可见
     */
    public void reset() {
        lls[0].setVisibility(View.GONE);
        lls[1].setVisibility(View.GONE);
        lls[2].setVisibility(View.GONE);

        ss[0].setVisibility(View.GONE);
        ss[1].setVisibility(View.GONE);

        for (int i = 0; i < 9; i++)
            imgs[i].setVisibility(View.INVISIBLE);

        pictures = null;
    }

    /**
     * 设置图片
     */
    public void setPictures(List<String> pictures) {
        if (pictures == null || pictures.isEmpty()) return;
        this.pictures = pictures;
        // 根据图片张数显示组件
        int size = this.pictures.size();
        if (size <= 3)
            lls[0].setVisibility(View.VISIBLE);
        else if (size >= 4 && size <= 6) {
            lls[0].setVisibility(View.VISIBLE);
            lls[1].setVisibility(View.VISIBLE);
            ss[0].setVisibility(View.VISIBLE);
        } else if (size >= 7 && size <= 9) {
            lls[0].setVisibility(View.VISIBLE);
            lls[1].setVisibility(View.VISIBLE);
            lls[2].setVisibility(View.VISIBLE);
            ss[0].setVisibility(View.VISIBLE);
            ss[1].setVisibility(View.VISIBLE);
        }
        // 显示图片
        for (int i = 0; i < size; i++) {
            ImageLoader.getInstance().displayImage(this.pictures.get(i), imgs[i]);
            imgs[i].setOnClickListener(new OnImgClickListener(i));
            imgs[i].setVisibility(View.VISIBLE);
        }
    }

    /**
     * item点击监听
     */
    public interface OnItemClickListener {
        /**
         * item点击事件回调
         *
         * @param pictures 图片url
         * @param position 点击图片的位置
         */
        void onItemClick(List<String> pictures, int position);
    }

    /**
     * 图片点击事件监听
     */
    private class OnImgClickListener implements OnClickListener {
        private int position;

        OnImgClickListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            if (listener != null)
                listener.onItemClick(pictures, position);
        }

    }

}
