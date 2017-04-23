package com.skye.lover.activity.common;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.interfaces.DraweeController;
import com.jakewharton.rxbinding.view.RxView;
import com.skye.actionsheet.ActionSheet;
import com.skye.lover.R;
import com.skye.lover.activity.base.BaseActivity;
import com.skye.lover.mvp.presenter.BasePresenterEmptyImpl;
import com.skye.lover.mvp.view.BaseView;
import com.skye.lover.util.CommonUtil;
import com.skye.lover.util.OkHttpUtil;
import com.skye.lover.util.URLConfig;
import com.skye.lover.view.fresco.zoomable.ZoomableDraweeView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;

/**
 * 显示图片界面
 */
public class ShowPictureActivity extends BaseActivity<BaseView, BasePresenterEmptyImpl> implements BaseView {
    public static final String PICTURES = "pictures";//图片路径集合
    public static final String POSITION = "position";//被点击图片的位置
    @BindView(R.id.spvp)
    ViewPager spvp;
    private List<View> views;
    private String url;
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
            url = URLConfig.SERVER_HOST + pictures.get(position);
            topbar.title.setText("图片" + (position + 1) + "/" + pictures.size());
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

    };
    private Handler handler = new Handler();

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_show_picture;
    }

    @Override
    public BasePresenterEmptyImpl createPresenter() {
        return null;
    }

    @Override
    public int getDescription() {
        return R.string.show_picture_activity;
    }

    @SuppressLint("InflateParams")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTopbar();
        topbar.right.setText(R.string.more);
        topbar.left.setVisibility(View.VISIBLE);
        topbar.right.setVisibility(View.VISIBLE);
        Intent intent = getIntent();
        pictures = intent.getStringArrayListExtra(PICTURES);
        if (pictures == null || pictures.isEmpty()) finish();
        int position = intent.getIntExtra(POSITION, 0);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view;
        ZoomableDraweeView img;
        DraweeController controller;
        GenericDraweeHierarchy hierarchy;
        views = new ArrayList<>();
        for (String item : pictures) {
            view = inflater.inflate(R.layout.item_picture, null);
            img = (ZoomableDraweeView) view.findViewById(R.id.img);
            controller = Fresco.newDraweeControllerBuilder().setUri(Uri.parse(URLConfig.SERVER_HOST + item)).build();
            hierarchy = new GenericDraweeHierarchyBuilder(getResources())
                    .setActualImageScaleType(ScalingUtils.ScaleType.FIT_CENTER)
                    .setFailureImage(CommonUtil.emptyFailureImage, ScalingUtils.ScaleType.FOCUS_CROP)
                    .setProgressBarImage(new ProgressBarDrawable())
                    .build();
            img.setController(controller);
            img.setHierarchy(hierarchy);
            views.add(view);
        }
        spvp.setAdapter(null);
        adapter.notifyDataSetChanged();
        spvp.setAdapter(adapter);
        spvp.setOnPageChangeListener(listener);
        spvp.setCurrentItem(position);
        url = URLConfig.SERVER_HOST + pictures.get(position);
        topbar.title.setText("图片" + (position + 1) + "/" + pictures.size());

        //更多
        RxView.clicks(topbar.right).throttleFirst(1, TimeUnit.SECONDS).subscribe((v) -> more());
    }

    /**
     * 更多
     */
    public void more() {
        if (TextUtils.isEmpty(url)) return;
        setTheme(R.style.ActionSheetStyleIOS7);
        ActionSheet menuView = new ActionSheet(context);
        menuView.setCancelButtonTitle("取消");// before add items
        menuView.addItems("保存图片");
        menuView.setItemClickListener((int itemPosition) -> {
            switch (itemPosition) {
                case 0:
                    OkHttpUtil.downloadFile(context, url, new OkHttpUtil.OnDownloadFileListener() {
                        @Override
                        public void onStart() {
                            showDialog(getString(R.string.please_wait));
                        }

                        @Override
                        public void onCompleted(final String message) {
                            handler.post(() -> {
                                dismissDialog();
                                toast(message);
                            });
                        }
                    });
                    break;
                default:
                    break;
            }
        });
        menuView.setCancelableOnTouchMenuOutside(true);
        menuView.showMenu();
    }
}
