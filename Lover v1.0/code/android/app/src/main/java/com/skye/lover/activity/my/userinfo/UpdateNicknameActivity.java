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
 * 更新昵称界面
 */
public class UpdateNicknameActivity extends BaseActivity {
    @BindView(R.id.ed_nickname)
    EditText nickname;//昵称

    /**
     * 更新昵称接口回调
     */
    private Callback callbackUpdateNickname = new Callback() {
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
                            ShareData.setShareStringData(ShareData.NICKNAME, CommonUtil.getEditTextString(nickname));//保存新昵称
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
        return R.layout.activity_updata_nickname;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTopbar();
        topbar.title.setText(R.string.nickname);
        topbar.left.setVisibility(View.VISIBLE);
        String nickname = ShareData.getShareStringData(ShareData.NICKNAME);
        this.nickname.setText(nickname);
        if (!TextUtils.isEmpty(nickname))
            this.nickname.setSelection(nickname.length());
    }

    @OnClick(R.id.btn_ok)
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ok://确定
                String nickname = CommonUtil.getEditTextString(this.nickname);
                if (TextUtils.isEmpty(nickname)) {
                    CommonUtil.toast(context, R.string.nickname_can_not_empty);
                    return;
                }
                Map<String, String> params = new HashMap<>();
                params.put("userId", ShareData.getShareStringData(ShareData.ID));
                params.put("nickname", nickname);
                dialog = CommonUtil.showLoadingDialog(context, R.string.please_wait);
                OkHttpUtil.doPost(context, URLConfig.ACTION_UPDATE_NICKNAME, params, callbackUpdateNickname);
                break;
            default:
                break;
        }
    }
}
