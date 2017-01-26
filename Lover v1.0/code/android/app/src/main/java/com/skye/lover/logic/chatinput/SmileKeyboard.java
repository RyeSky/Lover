package com.skye.lover.logic.chatinput;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.skye.lover.util.CommonUtil;
import com.skye.lover.util.ShareData;

/**
 * author : zejian
 * time : 2016年1月5日 上午11:14:27
 * email : shinezejian@163.com
 * description :源码来自开源项目https://github.com/dss886/Android-EmotionInputDetector
 * 本人仅做细微修改以及代码解析
 */
public class SmileKeyboard {
    private Activity activity;
    private InputMethodManager manager;//软键盘管理类
    private View fc;//表情布局faceContainer
    private EditText content;//
    private View body;//内容布局view,即除了表情布局或者软键盘布局以外的布局，用于固定bar的高度，防止跳闪

    private SmileKeyboard() {

    }

    /**
     * 外部静态调用
     *
     * @param activity 界面
     */
    public static SmileKeyboard with(Activity activity) {
        SmileKeyboard emotionInputDetector = new SmileKeyboard();
        emotionInputDetector.activity = activity;
        emotionInputDetector.manager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        return emotionInputDetector;
    }

    /**
     * 绑定内容view，此view用于固定bar的高度，防止跳闪
     *
     * @param contentView 主体内容区
     */
    public SmileKeyboard bindToContent(View contentView) {
        body = contentView;
        return this;
    }

    /**
     * 绑定编辑框
     *
     * @param editText 内容输入框
     */
    public SmileKeyboard bindToEditText(EditText editText) {
        content = editText;
        content.requestFocus();
        content.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP && fc.isShown()) {
                    lockBodyHeight();//显示软件盘时，锁定内容高度，防止跳闪。
                    hideSmileLayout(true);//隐藏表情布局，显示软件盘

                    //软件盘显示后，释放内容高度
                    content.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            unlockBodyHeightDelayed();
                        }
                    }, 200L);
                }
                return false;
            }
        });
        return this;
    }

    /**
     * 绑定表情按钮
     *
     * @param emotionButton 表情、键盘切换按钮
     */
    public SmileKeyboard bindToEmotionButton(View emotionButton) {
        emotionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fc.isShown()) {
                    lockBodyHeight();//显示软件盘时，锁定内容高度，防止跳闪。
                    hideSmileLayout(true);//隐藏表情布局，显示软件盘
                    unlockBodyHeightDelayed();//软件盘显示后，释放内容高度
                } else {
                    if (isSoftInputShown()) {//同上
                        lockBodyHeight();
                        showSmileLayout();
                        unlockBodyHeightDelayed();
                    } else {
                        showSmileLayout();//两者都没显示，直接显示表情布局
                    }
                }
            }
        });
        return this;
    }

    /**
     * 设置表情内容布局
     *
     * @param emotionView 表情容器
     */
    public SmileKeyboard setEmotionView(View emotionView) {
        fc = emotionView;
        return this;
    }

    public SmileKeyboard build() {
        //设置软件盘的模式：SOFT_INPUT_ADJUST_RESIZE  这个属性表示Activity的主窗口总是会被调整大小，从而保证软键盘显示空间。
        //从而方便我们计算软件盘的高度
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN |
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        //隐藏软件盘
        hideSoftInput();
        return this;
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
        int softInputHeight = getSupportSoftInputHeight();
        if (softInputHeight == 0) {
            softInputHeight = getKeyBoardHeight();
        }
        hideSoftInput();
        fc.getLayoutParams().height = softInputHeight;
        fc.setVisibility(View.VISIBLE);
    }

    /**
     * 隐藏表情布局
     *
     * @param showSoftInput 是否显示软件盘
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
     * 编辑框获取焦点，并显示软件盘
     */
    private void showSoftInput() {
        content.requestFocus();
        content.post(new Runnable() {
            @Override
            public void run() {
                manager.showSoftInput(content, 0);
            }
        });
    }

    /**
     * 隐藏软件盘
     */
    private void hideSoftInput() {
        manager.hideSoftInputFromWindow(content.getWindowToken(), 0);
    }

    /**
     * 是否显示软件盘
     *
     * @return 软键盘是否正在显示
     */
    private boolean isSoftInputShown() {
        return getSupportSoftInputHeight() != 0;
    }

    /**
     * 获取软键盘的高度
     *
     * @return 软键盘的高度
     */
    private int getSupportSoftInputHeight() {
        Rect r = new Rect();
        /**
         * decorView是window中的最顶层view，可以从window中通过getDecorView获取到decorView。
         * 通过decorView获取到程序显示的区域，包括标题栏，但不包括状态栏。
         */
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
        //获取屏幕的高度
        int screenHeight = activity.getWindow().getDecorView().getRootView().getHeight();
        //计算软件盘的高度
        int softInputHeight = screenHeight - r.bottom;

        /**
         * 某些Android版本下，没有显示软键盘时减出来的高度总是144，而不是零，
         * 这是因为高度是包括了虚拟按键栏的(例如华为系列)，所以在API Level高于20时，
         * 我们需要减去底部虚拟按键栏的高度（如果有的话）
         */
        if (Build.VERSION.SDK_INT >= 20) {
            // When SDK Level >= 20 (Android L), the softInputHeight will contain the height of softButtonsBar (if has)
            softInputHeight = softInputHeight - getSoftButtonsBarHeight();
        }

        if (softInputHeight < 0) {
            CommonUtil.log("SmileKeyboard--Warning: value of softInputHeight is below zero!");
        }
        //存一份到本地
        if (softInputHeight > 0) {
            ShareData.setShareIntData(ShareData.SOFT_INPUT_HEIGHT, softInputHeight);
        }
        return softInputHeight;
    }

    /**
     * 获取软键盘高度，由于第一次直接弹出表情时会出现小问题，787是一个均值，作为临时解决方案
     */
    private int getKeyBoardHeight() {
        int softInputHeight = ShareData.getShareIntData(ShareData.SOFT_INPUT_HEIGHT);
        if (softInputHeight == 0) softInputHeight = 787;
        return softInputHeight;
    }

    /**
     * 获取底部虚拟按键栏的高度
     *
     * @return 底部虚拟按键栏的高度
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private int getSoftButtonsBarHeight() {
        DisplayMetrics metrics = new DisplayMetrics();
        //这个方法获取可能不是真实屏幕的高度
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int usableHeight = metrics.heightPixels;
        //获取当前屏幕的真实高度
        activity.getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        int realHeight = metrics.heightPixels;
        if (realHeight > usableHeight) {
            return realHeight - usableHeight;
        } else {
            return 0;
        }
    }
}
