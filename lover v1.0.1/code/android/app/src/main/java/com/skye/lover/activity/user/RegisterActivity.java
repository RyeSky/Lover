package com.skye.lover.activity.user;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

import com.jakewharton.rxbinding.view.RxView;
import com.skye.lover.R;
import com.skye.lover.activity.base.BaseActivity;
import com.skye.lover.mvp.presenter.RegisterPresenter;
import com.skye.lover.mvp.view.BaseView;
import com.skye.lover.util.CommonUtil;
import com.skye.lover.view.FlowRadioGroup;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;

/**
 * 注册界面
 */
public class RegisterActivity extends BaseActivity<BaseView, RegisterPresenter> implements BaseView, FlowRadioGroup.OnCheckedChangeListener {
    @BindView(R.id.ed_name)
    EditText edName;//用户名
    @BindView(R.id.ed_password)
    EditText edPassword;//密码
    @BindView(R.id.ed_affirm_password)
    EditText edAffirmPassword;//确认密码
    @BindView(R.id.rg)
    FlowRadioGroup rg;//性别
    private String gender;

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_register;
    }

    @Override
    public RegisterPresenter createPresenter() {
        return new RegisterPresenter();
    }

    @Override
    public int getDescription() {
        return R.string.register_activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTopbar();
        topbar.title.setText(R.string.register);
        topbar.left.setVisibility(View.VISIBLE);
        rg.setOnCheckedChangeListener(this);
        ((RadioButton) findViewById(R.id.rb_secret)).setChecked(true);

        RxView.clicks(findViewById(R.id.btn_ok)).throttleFirst(1, TimeUnit.SECONDS).subscribe((v) -> {
            //用户名
            String name = CommonUtil.getEditTextString(edName);
            if (TextUtils.isEmpty(name)) {
                CommonUtil.toast(context, R.string.name_can_not_empty);
                return;
            }
            //密码
            String password = CommonUtil.getEditTextString(edPassword);
            if (TextUtils.isEmpty(password)) {
                CommonUtil.toast(context, R.string.password_can_not_empty);
                return;
            }
            //确认密码
            String affirmPassword = CommonUtil.getEditTextString(edAffirmPassword);
            if (TextUtils.isEmpty(affirmPassword)) {
                CommonUtil.toast(context, R.string.affirm_password_can_not_empty);
                return;
            }
            //两次输入密码不一致
            if (!affirmPassword.equals(password)) {
                CommonUtil.toast(context, R.string.affirm_password_not_equals_password);
                return;
            }
            presenter.register(name, password, gender);
        });
    }

    @Override
    public void onCheckedChanged(FlowRadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_secret://保密
                gender = "0";
                break;
            case R.id.rb_male://男
                gender = "1";
                break;
            case R.id.rb_female://女
                gender = "2";
                break;
            default:
                break;
        }
    }
}
