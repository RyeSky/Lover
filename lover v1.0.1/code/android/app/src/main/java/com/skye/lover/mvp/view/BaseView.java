package com.skye.lover.mvp.view;

import android.content.Context;
import android.content.Intent;

import com.skye.lover.model.Cross;

/**
 * 视图基类
 */
public interface BaseView {
    /**
     * 返回上下文对象
     */
    Context getContext();

    /**
     * 显示加载对话框
     */
    void showDialog();

    /**
     * 显示加载对话框
     *
     * @param text 加载框中的文字
     */
    void showDialog(String text);

    /**
     * 关闭加载框
     */
    void dismissDialog();

    /**
     * 显示吐司
     *
     * @param text 吐司内容更
     */
    void toast(int text);

    /**
     * 显示吐司
     *
     * @param text 吐司内容更
     */
    void toast(String text);

    /**
     * 出错
     *
     * @param cross   穿越
     * @param message 出错描述
     */
    void error(Cross cross, String message);

    /**
     * 显示软键盘
     */
    void displaySoftKeyboard();

    /**
     * 隐藏软键盘
     */
    void hideSoftKeyboard();

    /**
     * 返回上一个界面
     *
     * @param resultCode 结果码
     */
    void setResult(int resultCode);

    /**
     * 返回上一个界面
     *
     * @param resultCode 结果码
     * @param data       返回数据载体
     */
    void setResult(int resultCode, Intent data);

    /**
     * 销毁界面
     */
    void finish();
}
