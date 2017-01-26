package com.skye.lover.activity.pillowtalk;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.MotionEvent;
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
import com.skye.lover.view.FaceContainer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 回复悄悄话界面
 */
public class ReplyActivity extends BaseActivity implements View.OnTouchListener, FaceContainer.OnFaceOprateListener {
    public static final String PILLOW_TALK_ID = "pillowTalkId";
    @BindView(R.id.ed_content)
    EditText content;//悄悄话内容
    @BindView(R.id.face_container)
    FaceContainer fc;
    private String pillowTalkId;//悄悄话id
    /**
     * 发表跟随悄悄话接口回调
     */
    private Callback callbackPublishFollowPillowTalk = new Callback() {
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
                            CommonUtil.toast(context, R.string.reply_success);
                            setResult(RESULT_OK);
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
        return R.layout.activity_reply;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTopbar();
        topbar.title.setText(R.string.reply);
        topbar.left.setVisibility(View.VISIBLE);
        pillowTalkId = getIntent().getStringExtra(PILLOW_TALK_ID);
        content.setOnTouchListener(this);
        fc.setOnFaceOpreateListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        fc.setVisibility(View.GONE);
        return true;
    }

    @Override
    public void onFaceSelected(String emoji, String character) {
        int index = content.getSelectionStart();
        Editable edit = content.getEditableText();
        if (index < 0 || index >= edit.length()) {
            edit.append(emoji);
        } else {
            edit.insert(index, emoji);
        }
    }

    @Override
    public void onFaceDeleted() {
        int selection = content.getSelectionStart();
        String text = content.getText().toString();
        if (selection >= 1) {
            try {
                String text2 = text.substring(selection - 2, selection);
                if (!TextUtils.isEmpty(text2) && fc.contain(text2)) {// 双字节表情
                    content.getText().delete(selection - 2, selection);
                    return;
                }
                String text4 = text.substring(selection - 4, selection);
                if (!TextUtils.isEmpty(text4) && fc.contain(text4)) {// 四字节表情
                    content.getText().delete(selection - 4, selection);
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            content.getText().delete(selection - 1, selection);
        }
    }

    @OnClick({R.id.btn_ok, R.id.call_face_container})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ok://确定
                String content = CommonUtil.getEditTextString(this.content);
                if (TextUtils.isEmpty(content)) {
                    CommonUtil.toast(context, R.string.reply_can_not_empty);
                    return;
                }
                Map<String, String> params = new HashMap<>();
                params.put("pillowTalkId", pillowTalkId);
                params.put("userId", ShareData.getShareStringData(ShareData.ID));
                params.put("content", content);
                dialog = CommonUtil.showLoadingDialog(context, R.string.please_wait);
                OkHttpUtil.doPost(context, URLConfig.ACTION_PUBLISH_FOLLOW_PILLOW_TALK, params, callbackPublishFollowPillowTalk);
                break;
            case R.id.call_face_container://调用表情键盘
                if (fc.getVisibility() == View.VISIBLE) {//正在显示表情面板
                    fc.setVisibility(View.GONE);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            displaySoftKeyboard();
                        }
                    }, 300);
                } else {
                    hideSoftKeyboard();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            fc.setVisibility(View.VISIBLE);
                        }
                    }, 300);
                }
                break;
            default:
                break;
        }
    }
}
