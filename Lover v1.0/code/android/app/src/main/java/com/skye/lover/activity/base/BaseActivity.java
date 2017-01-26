package com.skye.lover.activity.base;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.skye.lover.ConstantUtil;
import com.skye.lover.R;
import com.skye.lover.util.CommonUtil;
import com.skye.lover.view.Topbar;

import butterknife.ButterKnife;

/**
 * 界面基类
 */
public abstract class BaseActivity extends FragmentActivity {
    protected Context context;//上下文对象
    protected Topbar topbar;//标题栏
    protected Dialog dialog;//加载框
    @SuppressLint("HandlerLeak")
    protected Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            handleMsg(msg);
        }
    };

    /**
     * 布局ID
     */
    public abstract int getLayoutResourceId();

    /**
     * 处理Message
     */
    public void handleMsg(Message msg) {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = this;
        setContentView(getLayoutResourceId());
        ButterKnife.bind(this);
    }

    /**
     * 初始化标题栏
     */
    public void initTopbar() {
        topbar = (Topbar) findViewById(R.id.topbar);
        topbar.left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    /**
     * 显示软键盘
     */
    public void displaySoftKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            View localView = getCurrentFocus();
            if (localView != null && localView.getWindowToken() != null) {
                inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    /**
     * 隐藏软键盘
     */
    protected void hideSoftKeyboard() {
        int softKeyBoardHeight = CommonUtil.getKeyBoardHeight();
        if (softKeyBoardHeight == 0 || softKeyBoardHeight == ConstantUtil.DEFAULT_SOFT_INPUT_HEIGHT)
            CommonUtil.getSupportSoftInputHeight(this);
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            View localView = getCurrentFocus();
            if (localView != null && localView.getWindowToken() != null) {
                IBinder windowToken = localView.getWindowToken();
                inputMethodManager.hideSoftInputFromWindow(windowToken, 0);
            }
        }
    }
}
