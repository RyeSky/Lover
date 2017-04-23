package com.skye.lover.activity.my.userinfo;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

import com.jakewharton.rxbinding.view.RxView;
import com.skye.lover.R;
import com.skye.lover.activity.base.BaseActivity;
import com.skye.lover.model.User;
import com.skye.lover.mvp.presenter.UpdateGenderPresenter;
import com.skye.lover.mvp.view.BaseView;
import com.skye.lover.util.ShareDataUtil;
import com.skye.lover.view.FlowRadioGroup;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;

/**
 * 更新性别界面
 */
public class UpdateGenderActivity extends BaseActivity<BaseView, UpdateGenderPresenter> implements BaseView, FlowRadioGroup.OnCheckedChangeListener {
    @BindView(R.id.rg)
    FlowRadioGroup rg;//性别
    private String gender;

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_update_gender;
    }

    @Override
    public UpdateGenderPresenter createPresenter() {
        return new UpdateGenderPresenter();
    }

    @Override
    public int getDescription() {
        return R.string.update_gender_activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTopbar();
        topbar.title.setText(R.string.register);
        topbar.left.setVisibility(View.VISIBLE);
        rg.setOnCheckedChangeListener(this);
        int id, gender = User.GENDER_SECRET;
        try {
            gender = Integer.parseInt(ShareDataUtil.get(context, User.GENDER));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (gender == 1) id = R.id.rb_male;
        else if (gender == 2) id = R.id.rb_female;
        else id = R.id.rb_secret;
        ((RadioButton) findViewById(id)).setChecked(true);

        //确定
        RxView.clicks(findViewById(R.id.btn_ok)).throttleFirst(1, TimeUnit.SECONDS)
                .subscribe((v) -> presenter.updateGender(this.gender));
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
