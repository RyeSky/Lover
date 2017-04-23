package com.skye.lover.activity.pillowtalk.comment;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.jakewharton.rxbinding.view.RxView;
import com.skye.lover.R;
import com.skye.lover.activity.base.BaseActivity;
import com.skye.lover.mvp.presenter.PublishCommentPresenter;
import com.skye.lover.mvp.view.BaseView;
import com.skye.lover.util.CommonUtil;
import com.skye.lover.view.FaceContainer;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;

/**
 * 回复悄悄话界面
 */
public class PublishCommentActivity extends BaseActivity<BaseView, PublishCommentPresenter> implements BaseView, View.OnTouchListener, FaceContainer.OnFaceOprateListener {
    public static final String PILLOW_TALK_ID = "pillowTalkId";
    @BindView(R.id.ed_content)
    EditText content;//悄悄话内容
    @BindView(R.id.face_container)
    FaceContainer fc;
    private String pillowTalkId;//悄悄话id
    private Handler handler = new Handler();

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_publish_comment;
    }

    @Override
    public PublishCommentPresenter createPresenter() {
        return new PublishCommentPresenter();
    }

    @Override
    public int getDescription() {
        return R.string.publish_comment_activity;
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


        //确定
        RxView.clicks(findViewById(R.id.btn_ok)).throttleFirst(1, TimeUnit.SECONDS)
                .subscribe((v) -> {
                    String content = CommonUtil.getEditTextString(this.content);
                    if (TextUtils.isEmpty(content)) {
                        CommonUtil.toast(context, R.string.reply_can_not_empty);
                        return;
                    }
                    presenter.publishComment(pillowTalkId, content);
                });
        //调用表情键盘
        RxView.clicks(findViewById(R.id.call_face_container)).throttleFirst(1, TimeUnit.SECONDS)
                .subscribe((v) -> {
                    if (fc.getVisibility() == View.VISIBLE) {//正在显示表情面板
                        fc.setVisibility(View.GONE);
                        handler.postDelayed(this::displaySoftKeyboard, 300);
                    } else {
                        hideSoftKeyboard();
                        handler.postDelayed(() -> fc.setVisibility(View.VISIBLE), 300);
                    }
                });
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
}
