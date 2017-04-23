package com.skye.lover.activity.user;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.jakewharton.rxbinding.view.RxView;
import com.skye.lover.R;
import com.skye.lover.activity.MainLoverActivity;
import com.skye.lover.activity.MainSingleActivity;
import com.skye.lover.activity.base.BaseActivity;
import com.skye.lover.mvp.presenter.LoginPresenter;
import com.skye.lover.mvp.view.LoginView;
import com.skye.lover.util.CommonUtil;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;

/**
 * 登录界面
 */
public class LoginActivity extends BaseActivity<LoginView, LoginPresenter> implements LoginView {
    @BindView(R.id.ed_name)
    EditText name;//用户名
    @BindView(R.id.ed_password)
    EditText password;//密码
    @BindView(R.id.btn_ok)
    Button btnOk;//确定
    // 双击退出
    private long exitTime = 0L;

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_login;
    }

    @Override
    public LoginPresenter createPresenter() {
        return new LoginPresenter();
    }

    @Override
    public int getDescription() {
        return R.string.login_activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTopbar();
        topbar.title.setText(R.string.login);
        topbar.right.setText(R.string.register);
        topbar.right.setVisibility(View.VISIBLE);

        //登录
        RxView.clicks(btnOk).throttleFirst(1, TimeUnit.SECONDS).subscribe((v) -> {
            //用户名
            String name = CommonUtil.getEditTextString(LoginActivity.this.name);
            if (TextUtils.isEmpty(name)) {
                CommonUtil.toast(context, R.string.name_can_not_empty);
                return;
            }
            //密码
            String password = CommonUtil.getEditTextString(LoginActivity.this.password);
            if (TextUtils.isEmpty(password)) {
                CommonUtil.toast(context, R.string.password_can_not_empty);
                return;
            }
            presenter.login(name, password);
                });
        //注册
        RxView.clicks(topbar.right).throttleFirst(1, TimeUnit.SECONDS).subscribe((v) ->
                startActivity(new Intent(context, RegisterActivity.class)));
    }

    /**
     * 跳转到主界面
     *
     * @param isSingle 是否是单身贵族
     */
    public void jumpToMainActivity(boolean isSingle) {
        Class<?> clazz;
        if (isSingle) clazz = MainSingleActivity.class;//单身贵族
        else clazz = MainLoverActivity.class;//有相爱的人
        startActivity(new Intent(context, clazz));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            // 在两秒钟内,连续点击了2次退出程序
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                CommonUtil.toast(context, R.string.tip_again_to_exit);
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                CommonUtil.exit();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
