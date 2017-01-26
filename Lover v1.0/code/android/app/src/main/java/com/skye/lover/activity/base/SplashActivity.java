package com.skye.lover.activity.base;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.skye.lover.R;
import com.skye.lover.activity.MainLoverActivity;
import com.skye.lover.activity.MainSingleActivity;
import com.skye.lover.activity.user.LoginActivity;
import com.skye.lover.util.ShareData;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 闪屏界面
 * </br>
 * 控制应用启动后应该跳转那个界面
 */
public class SplashActivity extends BaseActivity {

    @BindView(R.id.img_avatar)
    ImageView avatar;//头像

    @Override
    public void handleMsg(Message msg) {
        super.handleMsg(msg);
        switch (msg.what) {
            case 0:
                Class<?> clazz;
                if (ShareData.isLogined()) {//已经登录
                    if (TextUtils.isEmpty(ShareData.getShareStringData(ShareData.ANOTHER)))//单身
                        clazz = MainSingleActivity.class;
                    else clazz = MainLoverActivity.class;//有相爱的人
                } else clazz = LoginActivity.class;//没有登录
                startActivity(new Intent(context, clazz));
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_splash;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 添加透明度动画
        AlphaAnimation animation = new AlphaAnimation(0.3f, 1.0f);
        animation.setDuration(2000);
        if (ShareData.isLogined()) {
            avatar.setVisibility(View.VISIBLE);
            ImageLoader.getInstance().displayImage(ShareData.getShareStringData(ShareData.AVATAR), avatar);
            avatar.startAnimation(animation);
        }
        findViewById(R.id.tv_app_name).startAnimation(animation);
        findViewById(R.id.tv_app_tip).startAnimation(animation);
        handler.sendEmptyMessageDelayed(0, 3000);
    }

}
