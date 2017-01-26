package com.skye.lover.activity.user;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

import com.skye.lover.R;
import com.skye.lover.activity.base.BaseActivity;
import com.skye.lover.exception.NoNetworkConnectException;
import com.skye.lover.model.BaseResponse;
import com.skye.lover.util.CommonUtil;
import com.skye.lover.util.OkHttpUtil;
import com.skye.lover.util.URLConfig;
import com.skye.lover.view.FlowRadioGroup;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 注册界面
 */
public class RegisterActivity extends BaseActivity implements FlowRadioGroup.OnCheckedChangeListener {
    @BindView(R.id.ed_name)
    EditText edName;//用户名
    @BindView(R.id.ed_password)
    EditText edPassword;//密码
    @BindView(R.id.ed_affirm_password)
    EditText edAffirmPassword;//确认密码
    @BindView(R.id.rg)
    FlowRadioGroup rg;//性别
    private String gender;

    /**
     * 注册接口回调
     */
    private Callback callbackRegister = new Callback() {
        @Override
        public void onFailure(Call call, final IOException e) {
            e.printStackTrace();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    CommonUtil.closeLoadingDialog(dialog);
                    if (!(e instanceof NoNetworkConnectException))//不是没有网络
                        CommonUtil.toast(context, R.string.bad_request);
                }
            });
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            final String body = response.body().string();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    CommonUtil.closeLoadingDialog(dialog);
                    try {
                        CommonUtil.log(body);
                        BaseResponse br = CommonUtil.parseToObject(body, BaseResponse.class);//转化为返回基类
                        if (br.check()) {//注册成功
                            CommonUtil.toast(context, R.string.register_success);
                            finish();
                        } else
                            CommonUtil.toast(context, br.message);
                    } catch (Exception e) {
                        e.printStackTrace();
                        CommonUtil.toast(context, R.string.bad_request);
                    }
                }
            });
        }
    };

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_register;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTopbar();
        topbar.title.setText(R.string.register);
        topbar.left.setVisibility(View.VISIBLE);
        rg.setOnCheckedChangeListener(this);
        ((RadioButton) findViewById(R.id.rb_secret)).setChecked(true);
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

    @OnClick(R.id.btn_ok)
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ok://注册
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
                Map<String, String> params = new HashMap<>();
                params.put("name", name);
                params.put("password", CommonUtil.md5(password));
                params.put("gender", TextUtils.isEmpty(gender) ? "0" : gender);
                dialog = CommonUtil.showLoadingDialog(context, R.string.please_wait);
                OkHttpUtil.doPost(context, URLConfig.ACTION_REGISTER, params, callbackRegister);
                break;
            default:
                break;
        }
    }

}
