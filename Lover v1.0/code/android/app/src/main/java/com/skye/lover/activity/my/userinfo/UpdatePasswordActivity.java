package com.skye.lover.activity.my.userinfo;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.skye.lover.R;
import com.skye.lover.activity.base.BaseActivity;
import com.skye.lover.exception.NoNetworkConnectException;
import com.skye.lover.model.BaseResponse;
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
 * 修改密码界面
 */
public class UpdatePasswordActivity extends BaseActivity {
    @BindView(R.id.ed_old_password)
    EditText edOldPassword;//旧密码
    @BindView(R.id.ed_new_password)
    EditText edNewPassword;//新密码
    @BindView(R.id.ed_affirm_password)
    EditText edAffirmPassword;//确认密码
    private String password;

    /**
     * 更新密码接口回调
     */
    private Callback callbackUpdatePassword = new Callback() {
        @Override
        public void onFailure(Call call, final IOException e) {
            e.printStackTrace();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    CommonUtil.closeLoadingDialog(dialog);
                    if (!(e instanceof NoNetworkConnectException)) //不是没有网络
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
                            ShareData.setShareStringData(ShareData.PASSWORD, password);//保存新密码
                            CommonUtil.toast(context, R.string.update_success);
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
        return R.layout.activity_update_password;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTopbar();
        topbar.title.setText(R.string.update_password);
        topbar.left.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.btn_ok)
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ok:
                //密码
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
                password = CommonUtil.md5(newPassword);
                Map<String, String> params = new HashMap<>();
                params.put("userId", ShareData.getShareStringData(ShareData.ID));
                params.put("oldPassword", CommonUtil.md5(oldPassword));
                params.put("newPassword", CommonUtil.md5(newPassword));
                dialog = CommonUtil.showLoadingDialog(context, R.string.please_wait);
                OkHttpUtil.doPost(context, URLConfig.ACTION_UPDATE_PASSWORD, params, callbackUpdatePassword);
                break;
            default:
                break;
        }
    }
}
