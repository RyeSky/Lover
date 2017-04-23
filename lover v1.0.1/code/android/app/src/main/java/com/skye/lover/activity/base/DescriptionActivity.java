package com.skye.lover.activity.base;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.skye.lover.R;
import com.skye.lover.mvp.presenter.BasePresenterEmptyImpl;
import com.skye.lover.mvp.view.BaseView;

import butterknife.BindView;

/**
 * 描述界面
 */
public class DescriptionActivity extends BaseActivity<BaseView, BasePresenterEmptyImpl> implements BaseView {
    public static final String DESCRIPTION = "description";//描述
    @BindView(R.id.tv_descrption)
    TextView description;//描述

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_description;
    }

    @Override
    public BasePresenterEmptyImpl createPresenter() {
        return null;
    }

    @Override
    public int getDescription() {
        return 0;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTopbar();
        topbar.title.setText(R.string.description);
        topbar.left.setVisibility(View.VISIBLE);

        int desc = getIntent().getIntExtra(DESCRIPTION, 0);
        if (desc > 0) description.setText(desc);
    }
}
