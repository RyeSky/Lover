package com.skye.lover.activity.my.setting;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.skye.lover.ConstantUtil;
import com.skye.lover.R;
import com.skye.lover.activity.base.BaseActivity;
import com.skye.lover.activity.user.LoginActivity;
import com.skye.lover.model.AppVersion;
import com.skye.lover.mvp.presenter.SettingPresenter;
import com.skye.lover.mvp.view.SettingView;
import com.skye.lover.util.CommonUtil;
import com.skye.lover.util.ShareDataUtil;

import java.io.File;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.Observable;
import rx.Subscriber;

/**
 * 设置界面
 */
public class SettingActivity extends BaseActivity<SettingView, SettingPresenter> implements SettingView {
    @BindView(R.id.tv_clear_cache)
    TextView clearCache;//清理缓存
    @BindView(R.id.tv_app_current_version)
    TextView appCurrentVersion;//app当前版本

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_setting;
    }

    @Override
    public SettingPresenter createPresenter() {
        return new SettingPresenter();
    }

    @Override
    public int getDescription() {
        return R.string.setting_activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTopbar();
        topbar.title.setText(R.string.setting);
        topbar.left.setVisibility(View.VISIBLE);
        appCurrentVersion.setText("当前版本：" + CommonUtil.getVersionName(context));
        clearCache.setText(CommonUtil.getAutoFileOrFilesSize(ConstantUtil.BASE_PATH));

        //版本更新
        RxView.clicks(findViewById(R.id.ll_app_update)).throttleFirst(1, TimeUnit.SECONDS).subscribe((v) -> presenter.lastAppVersion());

        //清理缓存
        RxView.clicks(findViewById(R.id.ll_clear_cache)).throttleFirst(1, TimeUnit.SECONDS).subscribe((v0) -> {
            showDialog(getString(R.string.please_wait));
            presenter.addSubscribe(Observable.unsafeCreate((subscriber) ->
                            new Thread(() -> {
                                CommonUtil.RecursionDeleteFile(new File(ConstantUtil.BASE_PATH));
                                subscriber.onNext(null);
                                subscriber.onCompleted();
                            }).start()
                    ), new Subscriber() {
                        @Override
                        public void onCompleted() {
                            dismissDialog();
                        }

                        @Override
                        public void onError(Throwable e) {
                            dismissDialog();
                        }

                        @Override
                        public void onNext(Object o) {
                            toast(R.string.clear_cache_success);
                            clearCache.setText(CommonUtil.getAutoFileOrFilesSize(ConstantUtil.BASE_PATH));
                        }
                    }
            );
        });

        //意见反馈
        RxView.clicks(findViewById(R.id.ll_feedback)).throttleFirst(1, TimeUnit.SECONDS).subscribe((v) -> startActivity(new Intent(context, FeedbackActivity.class)));

        //关于
        RxView.clicks(findViewById(R.id.ll_about)).throttleFirst(1, TimeUnit.SECONDS).subscribe((v) -> startActivity(new Intent(context, AboutActivity.class)));

        //退出登录
        RxView.clicks(findViewById(R.id.tv_exit)).throttleFirst(1, TimeUnit.SECONDS).subscribe((v) -> {
            ShareDataUtil.clear(context);
            toast(R.string.exit_success);
            CommonUtil.updateJpushTag();
            //跳到登录界面
            Intent intent = new Intent(context, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });
    }

    @Override
    public void afterLastAppVersion(AppVersion version) {
        if (!CommonUtil.foundNewAppVersion(this, version)) toast(R.string.is_last_app_version);
    }
}
