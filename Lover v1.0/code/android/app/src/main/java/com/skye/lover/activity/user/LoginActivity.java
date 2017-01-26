package com.skye.lover.activity.user;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import com.skye.lover.R;
import com.skye.lover.activity.MainLoverActivity;
import com.skye.lover.activity.MainSingleActivity;
import com.skye.lover.activity.base.BaseActivity;
import com.skye.lover.exception.NoNetworkConnectException;
import com.skye.lover.model.BaseResponse;
import com.skye.lover.model.User;
import com.skye.lover.util.CommonUtil;
import com.skye.lover.util.OkHttpUtil;
import com.skye.lover.util.ShareData;
import com.skye.lover.util.URLConfig;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 登录界面
 */
public class LoginActivity extends BaseActivity {
    @BindView(R.id.ed_name)
    EditText edName;//用户名
    @BindView(R.id.ed_password)
    EditText edPassword;//密码
    // 双击退出
    long exitTime = 0L;
    /**
     * 登录接口回调
     */
    private Callback callbackLogin = new Callback() {
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
                        if (br.check()) {
                            User user = CommonUtil.parseToObject(br.result, User.class);//转化为用户类
                            Class<?> clazz;
                            if (TextUtils.isEmpty(user.getAnother()))//单身
                                clazz = MainSingleActivity.class;
                            else clazz = MainLoverActivity.class;//有相爱的人
                            ShareData.save(user);//保存用户信息
                            CommonUtil.toast(context, R.string.login_success);
                            startActivity(new Intent(context, clazz));
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
        return R.layout.activity_login;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTopbar();
        topbar.title.setText(R.string.login);
        topbar.right.setText(R.string.register);
        topbar.right.setVisibility(View.VISIBLE);
    }

    @OnClick({R.id.tv_right, R.id.btn_ok})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_right://注册
                startActivity(new Intent(context, RegisterActivity.class));
                break;
            case R.id.btn_ok://登录
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
                Map<String, String> params = new HashMap<>();
                params.put("name", name);
                params.put("password", CommonUtil.md5(password));
                dialog = CommonUtil.showLoadingDialog(context, R.string.logining);
                OkHttpUtil.doPost(context, URLConfig.ACTION_LOGIN, params, callbackLogin);
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            // 在两秒钟内,连续点击了2次退出程序
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                CommonUtil.toast(context, R.string.tip_again_to_exit);
                exitTime = System.currentTimeMillis();
            } else finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
