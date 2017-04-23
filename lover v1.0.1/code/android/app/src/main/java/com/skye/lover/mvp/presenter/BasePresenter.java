package com.skye.lover.mvp.presenter;


import com.skye.lover.model.Cross;
import com.skye.lover.mvp.view.BaseView;
import com.skye.lover.util.CommonUtil;
import com.trello.rxlifecycle.LifecycleProvider;
import com.trello.rxlifecycle.android.ActivityEvent;
import com.trello.rxlifecycle.android.FragmentEvent;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * 主导器基类
 */
public abstract class BasePresenter<V extends BaseView> implements Presenter {
    protected List<Cross> crosses;
    protected CompositeSubscription subscription;
    private WeakReference<LifecycleProvider<ActivityEvent>> activityLifecycleproviderRef;
    private WeakReference<LifecycleProvider<FragmentEvent>> fragmentLifecycleproviderRef;
    private WeakReference<V> viewRef;//视图弱引用

    @Override
    public void attachView(BaseView view) {
        this.viewRef = new WeakReference<>((V) view);
        LifecycleProvider<ActivityEvent> activityLifecycleprovider;
        LifecycleProvider<FragmentEvent> fragmentLifecycleprovider = null;
        try {
            activityLifecycleprovider = (LifecycleProvider<ActivityEvent>) view;
            activityLifecycleprovider.bindUntilEvent(ActivityEvent.DESTROY);
        } catch (Exception e) {
            activityLifecycleprovider = null;
            e.printStackTrace();
            try {
                fragmentLifecycleprovider = (LifecycleProvider<FragmentEvent>) view;
                fragmentLifecycleprovider.bindUntilEvent(FragmentEvent.DESTROY);
            } catch (Exception e1) {
                fragmentLifecycleprovider = null;
                e1.printStackTrace();
            }
        }
        if (activityLifecycleprovider != null)
            activityLifecycleproviderRef = new WeakReference<>(activityLifecycleprovider);
        if (fragmentLifecycleprovider != null)
            fragmentLifecycleproviderRef = new WeakReference<>(fragmentLifecycleprovider);
        subscription = new CompositeSubscription();
        crosses = new ArrayList<>();
    }

    /**
     * 获得视图
     */
    public V getView() {
        return viewRef == null ? null : viewRef.get();
    }

    @Override
    public void cancelCalls() {
        unSubscribe();
        CommonUtil.log("size:" + subscription.toString());
        if (crosses != null && !crosses.isEmpty()) {
            for (Cross item : crosses)
                if (item.call != null) item.call.cancel();
            crosses.clear();
        }
    }

    @Override
    public void detachView() {
        cancelCalls();
        unSubscribe();
        subscription = null;
        if (viewRef != null) {
            viewRef.clear();
            viewRef = null;
        }
        crosses = null;
    }

    @Override
    public void add(Cross cross) {
        crosses.add(cross);
    }

    @Override
    public void remove(Cross cross) {
        crosses.remove(cross);
    }

    /**
     * 建立订阅关系
     *
     * @param observable 可观察者
     * @param subscriber 观察者
     */
    @SuppressWarnings("unchecked")
    public void addSubscribe(Observable observable, Subscriber subscriber) {
        if (subscription == null)
            subscription = new CompositeSubscription();
        LifecycleProvider<ActivityEvent> activityLifecycleprovider = activityLifecycleproviderRef == null ? null : activityLifecycleproviderRef.get();
        LifecycleProvider<FragmentEvent> fragmentLifecycleprovider = fragmentLifecycleproviderRef == null ? null : fragmentLifecycleproviderRef.get();
        if (activityLifecycleprovider != null)
            subscription.add(observable.subscribeOn(Schedulers.io())
                    .compose(activityLifecycleprovider.bindUntilEvent(ActivityEvent.PAUSE))
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(subscriber));
        else if (fragmentLifecycleprovider != null)
            subscription.add(observable.subscribeOn(Schedulers.io())
                    .compose(fragmentLifecycleprovider.bindUntilEvent(FragmentEvent.PAUSE))
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(subscriber));
        else
            subscription.add(observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(subscriber));
    }

    /**
     * 建立订阅关系
     *
     * @param observable 可观察者
     * @param action     观察者
     */
    @SuppressWarnings("unchecked")
    public <T> void addAction(Observable observable, Action1<T> action) {
        if (subscription == null)
            subscription = new CompositeSubscription();
        subscription.add(observable.subscribe(action));
    }

    /**
     * 解除订阅关系
     */
    private void unSubscribe() {
        if (subscription != null) subscription.clear();
    }
}
