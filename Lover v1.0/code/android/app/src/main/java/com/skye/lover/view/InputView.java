package com.skye.lover.view;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.skye.lover.R;
import com.skye.lover.logic.chatinput.OnSendListener;
import com.skye.lover.logic.chatinput.OnSendSuccessListener;
import com.skye.lover.logic.chatinput.SendingContent;
import com.skye.lover.model.PrivateMessage;
import com.skye.lover.util.CommonUtil;

/**
 * 聊天输入
 */
public class InputView extends LinearLayout implements View.OnClickListener, View.OnTouchListener, FaceContainer.OnFaceOprateListener, OnSendSuccessListener {
    private Context context;
    private Activity activity;
    private InputMethodManager manager;//软键盘管理类
    private View body;//内容布局view,即除了表情布局或者软键盘布局以外的布局，用于固定bar的高度，防止跳闪
    private View switchSmileKeyboard;//切换表情和键盘
    private EditText content;//输入框
    private FaceContainer fc;//表情键盘
    private String another;//聊天中的另一方
    private OnSendListener listener;//发送事件监听

    public InputView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        manager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        LayoutInflater.from(context).inflate(R.layout.input_view, this, true);
        switchSmileKeyboard = findViewById(R.id.switch_smile_keyboard);
        switchSmileKeyboard.setOnClickListener(this);
        content = (EditText) findViewById(R.id.ed_content);
        content.setOnTouchListener(this);
        findViewById(R.id.tv_send).setOnClickListener(this);
        fc = (FaceContainer) findViewById(R.id.face_container);
        fc.setOnFaceOpreateListener(this);
    }

    public void setAnother(String another) {
        this.another = another;
    }

    public void setOnSendListener(OnSendListener listener) {
        this.listener = listener;
    }

    public void bind(Activity activity, View body) {
        this.activity = activity;
        this.body = body;
        content.requestFocus();
        //隐藏软键盘
//        hideSoftInput();
    }

    @Override
    public void onSendSuccess(PrivateMessage pm) {
        content.setText("");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.switch_smile_keyboard:
                if (fc.isShown()) {
                    lockBodyHeight();//显示软键盘时，锁定内容高度，防止跳闪。
                    hideSmileLayout(true);//隐藏表情布局，显示软键盘
                    unlockBodyHeightDelayed();//软键盘显示后，释放内容高度
                } else {
                    if (isSoftInputShown()) {//同上
                        lockBodyHeight();
                        showSmileLayout();
                        unlockBodyHeightDelayed();
                    } else {
                        showSmileLayout();//两者都没显示，直接显示表情布局
                    }
                }
                break;
            case R.id.tv_send://发送
                String content = CommonUtil.getEditTextString(this.content);
                if (TextUtils.isEmpty(content)) {
                    CommonUtil.toast(context, R.string.chat_content_can_not_empty);
                    return;
                }
                if (listener != null) {
                    SendingContent sc = new SendingContent();
                    sc.setAnother(another);
                    sc.setContent(content);
                    listener.onSend(sc);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP && fc.isShown()) {
            lockBodyHeight();//显示软键盘时，锁定内容高度，防止跳闪。
            hideSmileLayout(true);//隐藏表情布局，显示软键盘
            //软键盘显示后，释放内容高度
            content.postDelayed(new Runnable() {
                @Override
                public void run() {
                    unlockBodyHeightDelayed();
                }
            }, 200L);
        }
        return false;
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

    /**
     * 点击返回键时先隐藏表情布局
     */
    public boolean interceptBackPress() {
        if (fc.isShown()) {
            hideSmileLayout(false);
            return true;
        }
        return false;
    }

    private void showSmileLayout() {
        int softInputHeight = CommonUtil.getSupportSoftInputHeight(activity);
        if (softInputHeight == 0) {
            softInputHeight = CommonUtil.getKeyBoardHeight();
        }
        hideSoftInput();
        fc.getLayoutParams().height = softInputHeight;
        fc.setVisibility(View.VISIBLE);
    }

    /**
     * 隐藏表情布局
     *
     * @param showSoftInput 是否显示软键盘
     */
    private void hideSmileLayout(boolean showSoftInput) {
        if (fc.isShown()) {
            fc.setVisibility(View.GONE);
            if (showSoftInput) {
                showSoftInput();
            }
        }
    }

    /**
     * 锁定内容高度，防止跳闪
     */
    private void lockBodyHeight() {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) body.getLayoutParams();
        params.height = body.getHeight();
        params.weight = 0.0F;
    }

    /**
     * 释放被锁定的内容高度
     */
    private void unlockBodyHeightDelayed() {
        content.postDelayed(new Runnable() {
            @Override
            public void run() {
                ((LinearLayout.LayoutParams) body.getLayoutParams()).weight = 1.0F;
            }
        }, 200L);
    }

    /**
     * 编辑框获取焦点，并显示软键盘
     */
    private void showSoftInput() {
        content.requestFocus();
        content.post(new Runnable() {
            @Override
            public void run() {
                manager.showSoftInput(content, 0);
                switchSmileKeyboard.setBackgroundResource(R.drawable.shape_keyboard);
            }
        });
    }

    /**
     * 隐藏软键盘
     */
    private void hideSoftInput() {
        manager.hideSoftInputFromWindow(content.getWindowToken(), 0);
        switchSmileKeyboard.setBackgroundResource(R.drawable.shape_smile);
    }

    /**
     * 是否显示软键盘
     *
     * @return 软键盘是否正在显示
     */
    private boolean isSoftInputShown() {
        return CommonUtil.getSupportSoftInputHeight(activity) != 0;
    }
}
