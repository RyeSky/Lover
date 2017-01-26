package com.skye.lover.activity.common.localphotoalbum;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.skye.actionsheet.ActionSheet;
import com.skye.lover.R;
import com.skye.lover.activity.base.BaseActivity;
import com.skye.lover.util.CommonUtil;
import com.skye.lover.util.OkHttpUtil;
import com.skye.lover.view.photoview.PhotoView;
import com.skye.lover.view.photoview.PhotoViewAttacher;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 显示图片界面
 */
public class ShowPictureActivity extends BaseActivity implements PhotoViewAttacher.OnPhotoTapListener {
    public static final String PICTURES = "pictures";
    public static final String POSITION = "position";
    @BindView(R.id.spvp)
    ViewPager spvp;
    private List<View> views;
    private PagerAdapter adapter = new PagerAdapter() {

        @Override
        public int getCount() {
            return views.size(); // 个数
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return (arg0 == arg1); // 判断
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View obj;
            try {
                obj = views.get(position);
                container.addView(obj); // 添加到容器
            } catch (Exception e) {
                e.printStackTrace();
                obj = new View(context);
            }
            return obj;
        }

        @Override
        public void destroyItem(View container, int position, Object object) {
            try {
                ((ViewPager) container).removeView(views.get(position)); // 销毁
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    };
    private List<String> pictures;//图片路径集合
    private ViewPager.OnPageChangeListener listener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            topbar.title.setText("图片" + (position + 1) + "/" + pictures.size());
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

    };

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_show_picture;
    }

    @SuppressLint("InflateParams")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTopbar();
        topbar.left.setVisibility(View.VISIBLE);
        Intent intent = getIntent();
        pictures = intent.getStringArrayListExtra(PICTURES);
        if (pictures == null || pictures.isEmpty()) finish();
        int position = intent.getIntExtra(POSITION, 0);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view;
        PhotoView img;
        views = new ArrayList<>();
        for (String item : pictures) {
            view = inflater.inflate(R.layout.item_picture, null);
            img = (PhotoView) view.findViewById(R.id.img);
            ImageLoader.getInstance().displayImage(item, img);
            img.setOnLongClickListener(new OnLongClickImgListener(item));
            img.setOnPhotoTapListener(this);
            views.add(view);
        }
        spvp.setAdapter(null);
        adapter.notifyDataSetChanged();
        spvp.setAdapter(adapter);
        spvp.setOnPageChangeListener(listener);
        spvp.setCurrentItem(position);
        topbar.title.setText("图片" + (position + 1) + "/" + pictures.size());
    }

    @Override
    public void onPhotoTap(View view, float x, float y) {
        finish();
    }

    /**
     * 图片长按点击事件
     */
    private class OnLongClickImgListener implements View.OnLongClickListener {
        private String url;

        OnLongClickImgListener(String url) {
            this.url = url;
        }

        @Override
        public boolean onLongClick(View v) {
            if (TextUtils.isEmpty(url)) return true;
            setTheme(R.style.ActionSheetStyleIOS7);
            ActionSheet menuView = new ActionSheet(context);
            menuView.setCancelButtonTitle("取消");// before add items
            menuView.addItems("保存图片");
            menuView.setItemClickListener(new ActionSheet.MenuItemClickListener() {
                @Override
                public void onItemClick(int itemPosition) {
                    switch (itemPosition) {
                        case 0:
                            OkHttpUtil.downloadFile(context, url, new OkHttpUtil.OnDownloadFileListener() {
                                @Override
                                public void onStart() {
                                    dialog = CommonUtil.showLoadingDialog(context, R.string.please_wait);
                                }

                                @Override
                                public void onCompleted(final String message) {
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            CommonUtil.closeLoadingDialog(dialog);
                                            CommonUtil.toast(context, message);
                                        }
                                    });
                                }
                            });
                            break;
                        default:
                            break;
                    }
                }

            });
            menuView.setCancelableOnTouchMenuOutside(true);
            menuView.showMenu();
            return true;
        }
    }
}