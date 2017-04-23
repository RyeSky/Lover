package com.skye.lover.activity.my.userinfo;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.jakewharton.rxbinding.view.RxView;
import com.skye.lover.R;
import com.skye.lover.activity.base.BaseActivity;
import com.skye.lover.mvp.presenter.UpdatePasswordPresenter;
import com.skye.lover.mvp.view.BaseView;
import com.skye.lover.util.CommonUtil;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;

/**
 * 更新密码界面
 */
public class UpdatePasswordActivity extends BaseActivity<BaseView, UpdatePasswordPresenter> implements BaseView {
    @BindView(R.id.ed_old_password)
    EditText edOldPassword;//旧密码
    @BindView(R.id.ed_new_password)
    EditText edNewPassword;//新密码
    @BindView(R.id.ed_affirm_password)
    EditText edAffirmPassword;//确认密码

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_update_password;
    }

    @Override
    public UpdatePasswordPresenter createPresenter() {
        return new UpdatePasswordPresenter();
    }

    @Override
    public int getDescription() {
        return R.string.update_password_activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTopbar();
        topbar.title.setText(R.string.update_password);
        topbar.left.setVisibility(View.VISIBLE);

        //确定
        RxView.clicks(findViewById(R.id.btn_ok)).throttleFirst(1, TimeUnit.SECONDS)
                .subscribe((v) -> { //密码
                    String oldPassword = CommonUtil.getEditTextString(edOldPassword);
                    if (TextUtils.isEmpty(oldPassword)) {
                        CommonUtil.toast(context, R.string.old_password_can_not_empty);
                        return;
                    }
                    //新密码
                    String newPassword = CommonUtil.getEditTextString(edNewPassword);
                    if (TextUtils.isEmpty(newPassword)) {
                        CommonUtil.toast(context, R.string.new_password_can_not_empty);
                        return;
                    }
                    //确认密码
                    String affirmPassword = CommonUtil.getEditTextString(edAffirmPassword);
                    if (TextUtils.isEmpty(affirmPassword)) {
                        CommonUtil.toast(context, R.string.affirm_password_can_not_empty);
                        return;
                    }
                    //两次输入密码不一致
                    if (!affirmPassword.equals(newPassword)) {
                        CommonUtil.toast(context, R.string.affirm_password_not_equals_password);
                        return;
                    }
                    presenter.updatePassword(oldPassword, newPassword);
                });
    }
}
