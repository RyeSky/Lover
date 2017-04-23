package com.skye.lover.activity.my.userinfo;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.jakewharton.rxbinding.view.RxView;
import com.skye.lover.R;
import com.skye.lover.activity.base.BaseActivity;
import com.skye.lover.model.User;
import com.skye.lover.mvp.presenter.UpdateNicknamePresenter;
import com.skye.lover.mvp.view.BaseView;
import com.skye.lover.util.CommonUtil;
import com.skye.lover.util.ShareDataUtil;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;

/**
 * 更新昵称界面
 */
public class UpdateNicknameActivity extends BaseActivity<BaseView, UpdateNicknamePresenter> implements BaseView {
    @BindView(R.id.ed_nickname)
    EditText nickname;//昵称

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_update_nickname;
    }

    @Override
    public UpdateNicknamePresenter createPresenter() {
        return new UpdateNicknamePresenter();
    }

    @Override
    public int getDescription() {
        return R.string.update_nickname_activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTopbar();
        topbar.title.setText(R.string.nickname);
        topbar.left.setVisibility(View.VISIBLE);
        String nickname = ShareDataUtil.get(context, User.NICKNAME);
        this.nickname.setText(nickname);
        if (!TextUtils.isEmpty(nickname))
            this.nickname.setSelection(nickname.length());

        //确定
        RxView.clicks(findViewById(R.id.btn_ok)).throttleFirst(1, TimeUnit.SECONDS).subscribe((v) -> {
            String name = CommonUtil.getEditTextString(this.nickname);
            if (TextUtils.isEmpty(name)) {
                CommonUtil.toast(context, R.string.nickname_can_not_empty);
                return;
            }
            presenter.updateNickname(name);
        });
    }
}
