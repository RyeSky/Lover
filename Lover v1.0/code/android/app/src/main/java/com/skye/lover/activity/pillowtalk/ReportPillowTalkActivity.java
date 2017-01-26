package com.skye.lover.activity.pillowtalk;

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
 * 举报悄悄话界面
 */
public class ReportPillowTalkActivity extends BaseActivity {
    public static final String PILLOW_TALK_ID = "pillow_talk_id";//悄悄话id
    @BindView(R.id.ed_report)
    EditText report;//举报内容
    private String pillowTalkId;//悄悄话id

    /**
     * 举报悄悄话接口回调
     */
    private Callback callbackReportPillowTalk = new Callback() {
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
                            CommonUtil.toast(context, R.string.report_success);
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
        return R.layout.activity_report_pillow_talk;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTopbar();
        topbar.title.setText(R.string.report);
        topbar.left.setVisibility(View.VISIBLE);
        pillowTalkId = getIntent().getStringExtra(PILLOW_TALK_ID);
    }

    @OnClick(R.id.btn_ok)
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ok://确定
                String report = CommonUtil.getEditTextString(this.report);
                if (TextUtils.isEmpty(report)) {
                    CommonUtil.toast(context, R.string.report_can_not_empty);
                    return;
                }
                Map<String, String> params = new HashMap<>();
                params.put("reporter", ShareData.getShareStringData(ShareData.ID));
                params.put("pillowTalkId", pillowTalkId);
                params.put("content", report);
                dialog = CommonUtil.showLoadingDialog(context, R.string.please_wait);
                OkHttpUtil.doPost(context, URLConfig.ACTION_REPORT_PILLOW_TALK, params, callbackReportPillowTalk);
                break;
            default:
                break;
        }
    }
}

