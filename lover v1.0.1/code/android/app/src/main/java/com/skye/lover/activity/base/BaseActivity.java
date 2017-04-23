package com.skye.lover.activity.base;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.skye.lover.ConstantUtil;
import com.skye.lover.LoverApplication;
import com.skye.lover.R;
import com.skye.lover.model.Cross;
import com.skye.lover.mvp.presenter.BasePresenter;
import com.skye.lover.mvp.view.BaseView;
import com.skye.lover.util.CommonUtil;
import com.skye.lover.util.FixInputLeakUtil;
import com.skye.lover.view.Topbar;
import com.trello.rxlifecycle.LifecycleProvider;
import com.trello.rxlifecycle.LifecycleTransformer;
import com.trello.rxlifecycle.RxLifecycle;
import com.trello.rxlifecycle.android.ActivityEvent;

import javax.annotation.Nonnull;

import butterknife.ButterKnife;
import rx.Observable;
import rx.subjects.BehaviorSubject;

/**
 * 所有activity类的基类
 */
public abstract class BaseActivity<V extends BaseView, T extends BasePresenter<V>> extends FragmentActivity implements LifecycleProvider<ActivityEvent> {
    protected final BehaviorSubject<ActivityEvent> lifecycleSubject = BehaviorSubject.create();
    protected Context context;//上下文对象
    protected Topbar topbar;//标题栏
    protected Dialog dialog;//加载框
    protected T presenter;//主导器

    /**
     * 布局ID
     */
    public abstract int getLayoutResourceId();

    /**
     * 创建主导器
     */
    public abstract T createPresenter();

    /**
     * 界面描述文字
     */
    public int getDescription() {
        return 0;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = this;
        setContentView(getLayoutResourceId());
        ButterKnife.bind(this);
        presenter = createPresenter();
        if (presenter != null) presenter.attachView((V) this);
        lifecycleSubject.onNext(ActivityEvent.CREATE);
    }

    @Nonnull
    @Override
    public Observable<ActivityEvent> lifecycle() {
        return lifecycleSubject.asObservable();
    }

    @Nonnull
    @Override
    public <S> LifecycleTransformer<S> bindUntilEvent(@Nonnull ActivityEvent event) {
        return RxLifecycle.bindUntilEvent(lifecycleSubject, event);
    }

    @Nonnull
    @Override
    public <S> LifecycleTransformer<S> bindToLifecycle() {
        return RxLifecycle.bind(lifecycleSubject);
    }

    @Override
    protected void onStart() {
        super.onStart();
        lifecycleSubject.onNext(ActivityEvent.START);
    }

    @Override
    protected void onResume() {
        super.onResume();
        lifecycleSubject.onNext(ActivityEvent.RESUME);
    }

    @Override
    protected void onPause() {
        lifecycleSubject.onNext(ActivityEvent.PAUSE);
        if (presenter != null) presenter.cancelCalls();
        super.onPause();
    }

    @Override
    protected void onStop() {
        lifecycleSubject.onNext(ActivityEvent.STOP);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        lifecycleSubject.onNext(ActivityEvent.DESTROY);
        if (presenter != null) presenter.detachView();
        else presenter = null;
        FixInputLeakUtil.fixFocusedViewLeak(LoverApplication.getInstance());
        super.onDestroy();
    }

    /**
     * 初始化标题栏
     */
    public void initTopbar() {
        topbar = (Topbar) findViewById(R.id.topbar);
        topbar.left.setOnClickListener((view) -> finish());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        int description = getDescription();
        if (description > 0) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu, menu);
            return true;
        } else return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.descriptioin://描述
                startActivity(new Intent(context, DescriptionActivity.class)
                        .putExtra(DescriptionActivity.DESCRIPTION, getDescription()));
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
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
    public void hideSoftKeyboard() {
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

    /**
     * 返回上下文对象
     */
    public Context getContext() {
        return context;
    }

    /**
     * 显示加载对话框
     */
    public void showDialog() {
        dialog = CommonUtil.showLoadingDialog(context);
    }

    /**
     * 显示加载对话框
     *
     * @param text 加载框中的文字
     */
    public void showDialog(String text) {
        dialog = CommonUtil.showLoadingDialog(context, text);
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
        CommonUtil.toast(context, text);
    }

    /**
     * 显示吐司
     *
     * @param text 吐司内容更
     */
    public void toast(String text) {
        CommonUtil.toast(context, text);
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
