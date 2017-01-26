package com.skye.lover.activity.my.userinfo;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.RadioButton;

import com.skye.lover.R;
import com.skye.lover.activity.base.BaseActivity;
import com.skye.lover.exception.NoNetworkConnectException;
import com.skye.lover.model.BaseResponse;
import com.skye.lover.util.CommonUtil;
import com.skye.lover.util.OkHttpUtil;
import com.skye.lover.util.ShareData;
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

public class UpdateGenderActivity extends BaseActivity implements FlowRadioGroup.OnCheckedChangeListener {
    @BindView(R.id.rg)
    FlowRadioGroup rg;//性别
    private String gender;

    /**
     * 更新性别接口回调
     */
    private Callback callbackUpdateGender = new Callback() {
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
                            ShareData.setShareIntData(ShareData.GENDER, TextUtils.isEmpty(gender) ? 0 : Integer.parseInt(gender));//保存新性别
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
        return R.layout.activity_update_gender;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTopbar();
        topbar.title.setText(R.string.register);
        topbar.left.setVisibility(View.VISIBLE);
        rg.setOnCheckedChangeListener(this);
        int id, gender = ShareData.getShareIntData(ShareData.GENDER);
        if (gender == 1) id = R.id.rb_male;
        else if (gender == 2) id = R.id.rb_female;
        else id = R.id.rb_secret;
        ((RadioButton) findViewById(id)).setChecked(true);
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
            case R.id.btn_ok://确定
                Map<String, String> params = new HashMap<>();
                params.put("userId", ShareData.getShareStringData(ShareData.ID));
                params.put("gender", TextUtils.isEmpty(gender) ? "0" : gender);
                dialog = CommonUtil.showLoadingDialog(context, R.string.please_wait);
                OkHttpUtil.doPost(context, URLConfig.ACTION_UPDATE_GENDER, params, callbackUpdateGender);
                break;
            default:
                break;
        }
    }
}
