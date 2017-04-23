package com.skye.lover.activity.my.setting;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.jakewharton.rxbinding.view.RxView;
import com.skye.lover.R;
import com.skye.lover.activity.base.BaseActivity;
import com.skye.lover.mvp.presenter.FeedbackPresenter;
import com.skye.lover.mvp.view.BaseView;
import com.skye.lover.util.CommonUtil;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;

/**
 * 意见反馈界面
 */
public class FeedbackActivity extends BaseActivity<BaseView, FeedbackPresenter> implements BaseView {
    @BindView(R.id.ed_feedback)
    EditText feedback;//反馈内容

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_feedback;
    }

    @Override
    public FeedbackPresenter createPresenter() {
        return new FeedbackPresenter();
    }

    @Override
    public int getDescription() {
        return R.string.feedback_activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTopbar();
        topbar.title.setText(R.string.feedback);
        topbar.left.setVisibility(View.VISIBLE);

        //意见反馈
        RxView.clicks(findViewById(R.id.btn_ok)).throttleFirst(1, TimeUnit.SECONDS).subscribe((v) -> {
            String feedback = CommonUtil.getEditTextString(this.feedback);
            if (TextUtils.isEmpty(feedback)) {
                CommonUtil.toast(context, R.string.feedback_can_not_empty);
                return;
            }
            presenter.submitFeedback(feedback);
        });
    }
}
