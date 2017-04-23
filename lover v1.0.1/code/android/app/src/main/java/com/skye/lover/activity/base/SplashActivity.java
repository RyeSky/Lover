package com.skye.lover.activity.base;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AlphaAnimation;

import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.view.SimpleDraweeView;
import com.skye.lover.R;
import com.skye.lover.activity.MainLoverActivity;
import com.skye.lover.activity.MainSingleActivity;
import com.skye.lover.activity.user.LoginActivity;
import com.skye.lover.model.User;
import com.skye.lover.mvp.presenter.BasePresenterEmptyImpl;
import com.skye.lover.mvp.view.BaseView;
import com.skye.lover.util.CommonUtil;
import com.skye.lover.util.ShareDataUtil;
import com.skye.lover.util.URLConfig;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.Observable;

/**
 * 启动界面
 */
public class SplashActivity extends BaseActivity<BaseView, BasePresenterEmptyImpl> implements BaseView {
    @BindView(R.id.img_avatar)
    SimpleDraweeView avatar;//头像

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_splash;
    }

    @Override
    public BasePresenterEmptyImpl createPresenter() {
        return new BasePresenterEmptyImpl();
    }

    @Override
    public int getDescription() {
        return R.string.splash_activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 添加透明度动画
        AlphaAnimation animation = new AlphaAnimation(0.3f, 1.0f);
        animation.setDuration(2000);
        if (User.isLogined(context)) {
            avatar.setVisibility(View.VISIBLE);

            int gender = User.GENDER_SECRET;//性别
            try {
                gender = Integer.parseInt(ShareDataUtil.get(context, User.GENDER));
            } catch (Exception e) {
                e.printStackTrace();
            }
            GenericDraweeHierarchy hierarchy = avatar.getHierarchy();
            hierarchy.setFailureImage(CommonUtil.getFailureImage(gender));
            avatar.setImageURI(URLConfig.SERVER_HOST + ShareDataUtil.get(context, User.AVATAR));

            avatar.startAnimation(animation);
        }
        findViewById(R.id.tv_app_name).startAnimation(animation);
        findViewById(R.id.tv_app_tip).startAnimation(animation);
        Observable.timer(3, TimeUnit.SECONDS).subscribe((v) -> {
            Class<?> clazz;
            if (User.isLogined(context)) {//已经登录
                if (TextUtils.isEmpty(ShareDataUtil.get(context, User.ANOTHER)))//单身
                    clazz = MainSingleActivity.class;
                else clazz = MainLoverActivity.class;//有相爱的人
            } else clazz = LoginActivity.class;//没有登录
            startActivity(new Intent(context, clazz));
            finish();
        });
    }
}
