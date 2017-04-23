package com.skye.lover.activity.my.setting;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.skye.lover.R;
import com.skye.lover.activity.base.BaseActivity;
import com.skye.lover.mvp.presenter.BasePresenterEmptyImpl;
import com.skye.lover.mvp.view.BaseView;
import com.skye.lover.util.CommonUtil;

import butterknife.BindView;

/**
 * 关于界面
 */
public class AboutActivity extends BaseActivity<BaseView, BasePresenterEmptyImpl> {
    @BindView(R.id.tv_app_current_version)
    TextView appCurrentVersion;//app当前版本

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_about;
    }

    @Override
    public BasePresenterEmptyImpl createPresenter() {
        return null;
    }

    @Override
    public int getDescription() {
        return R.string.about_activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTopbar();
        topbar.title.setText(R.string.about);
        topbar.left.setVisibility(View.VISIBLE);
        appCurrentVersion.setText("版本：" + CommonUtil.getVersionName(context));
    }
}
