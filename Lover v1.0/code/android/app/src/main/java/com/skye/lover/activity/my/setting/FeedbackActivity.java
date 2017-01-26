package com.skye.lover.activity.my.setting;

import android.os.Build;
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
 * 意见反馈界面
 */
public class FeedbackActivity extends BaseActivity {
    @BindView(R.id.ed_feedback)
    EditText feedback;//反馈内容

    /**
     * 意见反馈接口回调
     */
    private Callback callbackSubmitFeedback = new Callback() {
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
                            CommonUtil.toast(context, R.string.submit_success);
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
        return R.layout.activity_feedback;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTopbar();
        topbar.title.setText(R.string.feedback);
        topbar.left.setVisibility(View.VISIBLE);
    }

    @SuppressWarnings("deprecation")
    @OnClick(R.id.btn_ok)
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ok://确定
                String feedback = CommonUtil.getEditTextString(this.feedback);
                if (TextUtils.isEmpty(feedback)) {
                    CommonUtil.toast(context, R.string.feedback_can_not_empty);
                    return;
                }
                Map<String, String> params = new HashMap<>();
                params.put("publisher", ShareData.getShareStringData(ShareData.ID));
                params.put("content", feedback);
                params.put("platform", "0");
                params.put("platformVersion", Build.VERSION.SDK);
                params.put("appVersion", CommonUtil.getVersionName(context));
                dialog = CommonUtil.showLoadingDialog(context, R.string.please_wait);
                OkHttpUtil.doPost(context, URLConfig.ACTION_SUBMIT_FEEDBACK, params, callbackSubmitFeedback);
                break;
            default:
                break;
        }
    }
}
