package com.skye.lover.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.skye.lover.LoverApplication;
import com.skye.lover.activity.base.BaseActivity;
import com.skye.lover.model.Cross;
import com.skye.lover.mvp.presenter.BasePresenter;
import com.skye.lover.mvp.view.BaseView;
import com.skye.lover.util.CommonUtil;
import com.squareup.leakcanary.RefWatcher;
import com.trello.rxlifecycle.LifecycleProvider;
import com.trello.rxlifecycle.LifecycleTransformer;
import com.trello.rxlifecycle.RxLifecycle;
import com.trello.rxlifecycle.android.FragmentEvent;

import javax.annotation.Nonnull;

import rx.Observable;
import rx.subjects.BehaviorSubject;

/**
 * 碎片基类
 */
public abstract class BaseFragment<V extends BaseView, T extends BasePresenter<V>> extends Fragment implements LifecycleProvider<FragmentEvent> {
    protected final BehaviorSubject<FragmentEvent> lifecycleSubject = BehaviorSubject.create();
    protected BaseActivity activity;//host activity
    protected View view;//根视图
    protected Dialog dialog;//加载框
    protected boolean hidden;//碎片是否隐藏
    protected T presenter;//主导器

    /**
     * 创建主导器
     */
    public abstract T createPresenter();

    @SuppressWarnings("unchecked")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = createPresenter();
        if (presenter != null) presenter.attachView((V) this);
        lifecycleSubject.onNext(FragmentEvent.CREATE);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (BaseActivity) activity;
        lifecycleSubject.onNext(FragmentEvent.ATTACH);
    }


    @Nonnull
    @Override
    public Observable<FragmentEvent> lifecycle() {
        return lifecycleSubject.asObservable();
    }

    @Nonnull
    @Override
    public <S> LifecycleTransformer<S> bindUntilEvent(@Nonnull FragmentEvent event) {
        return RxLifecycle.bindUntilEvent(lifecycleSubject, event);
    }

    @Nonnull
    @Override
    public <S> LifecycleTransformer<S> bindToLifecycle() {
        return RxLifecycle.bind(lifecycleSubject);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lifecycleSubject.onNext(FragmentEvent.CREATE_VIEW);
    }

    @Override
    public void onStart() {
        super.onStart();
        lifecycleSubject.onNext(FragmentEvent.START);
    }

    @Override
    public void onResume() {
        super.onResume();
        lifecycleSubject.onNext(FragmentEvent.RESUME);
    }

    @Override
    public void onPause() {
        lifecycleSubject.onNext(FragmentEvent.PAUSE);
        if (presenter != null) presenter.cancelCalls();
        super.onPause();
    }

    @Override
    public void onStop() {
        lifecycleSubject.onNext(FragmentEvent.STOP);
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        lifecycleSubject.onNext(FragmentEvent.DESTROY_VIEW);
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        lifecycleSubject.onNext(FragmentEvent.DESTROY);
        if (presenter != null) presenter.detachView();
        else presenter = null;
        RefWatcher refWatcher = LoverApplication.getRefWatcher();
        if (refWatcher != null) refWatcher.watch(this);
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        lifecycleSubject.onNext(FragmentEvent.DETACH);
        super.onDetach();
    }

    /**
     * 显示软键盘
     */
    public void displaySoftKeyboard() {
        if (activity == null || activity.isFinishing()) return;
        activity.displaySoftKeyboard();
    }

    /**
     * 隐藏软键盘
     */
    public void hideSoftKeyboard() {
        if (activity == null || activity.isFinishing()) return;
        activity.hideSoftKeyboard();
    }

    /**
     * 返回上一个界面
     *
     * @param resultCode 结果码
     */
    public void setResult(int resultCode) {
        if (activity == null || activity.isFinishing()) return;
        activity.setResult(resultCode);
    }

    /**
     * 返回上一个界面
     *
     * @param resultCode 结果码
     * @param data       返回数据载体
     */
    public void setResult(int resultCode, Intent data) {
        if (activity == null || activity.isFinishing()) return;
        activity.setResult(resultCode, data);
    }

    /**
     * 销毁界面
     */
    public void finish() {
        if (activity == null || activity.isFinishing()) return;
        activity.finish();
    }

    /**
     * 返回上下文对象
     */
    public Context getContext() {
        return activity;
    }

    /**
     * 显示加载对话框
     */
    public void showDialog() {
        dialog = CommonUtil.showLoadingDialog(activity);
    }

    /**
     * 显示加载对话框
     *
     * @param text 加载框中的文字
     */
    public void showDialog(String text) {
        dialog = CommonUtil.showLoadingDialog(activity, text);
    }

    /**
     * 关闭加载框
     */
    public void dismissDialog() {
        CommonUtil.closeLoadingDialog(dialog);
    }

    /**
     * 显示吐司
     *
     * @param text 吐司内容更
     */
    public void toast(int text) {
        CommonUtil.toast(activity, text);
    }

    /**
     * 显示吐司
     *
     * @param text 吐司内容更
     */
    public void toast(String text) {
        CommonUtil.toast(activity, text);
    }

    /**
     * 出错
     *
     * @param cross   穿越
     * @param message 出错描述
     */
    public void error(Cross cross, String message) {
    }
}
