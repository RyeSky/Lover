package com.skye.lover.activity.pillowtalk;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.jakewharton.rxbinding.view.RxView;
import com.skye.lover.R;
import com.skye.lover.activity.base.BaseActivity;
import com.skye.lover.mvp.presenter.ReportPillowTalkPresenter;
import com.skye.lover.mvp.view.BaseView;
import com.skye.lover.util.CommonUtil;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;

/**
 * 举报悄悄话界面
 */
public class ReportPillowTalkActivity extends BaseActivity<BaseView, ReportPillowTalkPresenter> implements BaseView {
    public static final String PILLOW_TALK_ID = "pillow_talk_id";//悄悄话id
    @BindView(R.id.ed_report)
    EditText report;//举报内容
    private String pillowTalkId;//悄悄话id

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_report_pillow_talk;
    }

    @Override
    public ReportPillowTalkPresenter createPresenter() {
        return new ReportPillowTalkPresenter();
    }

    @Override
    public int getDescription() {
        return R.string.report_pillow_talk_activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTopbar();
        topbar.title.setText(R.string.report);
        topbar.left.setVisibility(View.VISIBLE);
        pillowTalkId = getIntent().getStringExtra(PILLOW_TALK_ID);

        //确定
        RxView.clicks(findViewById(R.id.btn_ok)).throttleFirst(1, TimeUnit.SECONDS).subscribe((v) -> {
            String report = CommonUtil.getEditTextString(this.report);
            if (TextUtils.isEmpty(report)) {
                CommonUtil.toast(context, R.string.report_can_not_empty);
                return;
            }
            presenter.reportPillowTalk(pillowTalkId, report);
        });
    }
}
