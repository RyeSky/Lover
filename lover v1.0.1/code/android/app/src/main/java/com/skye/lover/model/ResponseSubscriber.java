package com.skye.lover.model;

import com.google.gson.reflect.TypeToken;
import com.skye.lover.R;
import com.skye.lover.exception.NetworkRequestException;
import com.skye.lover.mvp.presenter.Presenter;
import com.skye.lover.mvp.view.BaseView;
import com.skye.lover.util.CommonUtil;

import java.lang.ref.WeakReference;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import rx.Subscriber;

/**
 * 返回数据响应订阅者
 */
public abstract class ResponseSubscriber<T> extends Subscriber<Cross> {
    //使用弱引用，是为了内存优化
    private WeakReference<Presenter> presenterRef;//主导器引用
    private WeakReference<BaseView> viewRef;//视图引用

    public ResponseSubscriber(Presenter presenter, BaseView view) {
        if (presenter == null) throw new NullPointerException("Presenter must not be null!");
        if (view == null) throw new NullPointerException("BaseView must not be null!");
        presenterRef = new WeakReference<>(presenter);
        viewRef = new WeakReference<>(view);
    }

    @Override
    public void onCompleted() {
        BaseView view = viewRef == null ? null : viewRef.get();
        if (view == null) return;
        view.dismissDialog();
    }

    @Override
    public void onError(Throwable e) {
        Presenter presenter = presenterRef == null ? null : presenterRef.get();
        BaseView view = viewRef == null ? null : viewRef.get();
        if (presenter == null || view == null) return;
        view.dismissDialog();
        e.printStackTrace();
        String message = view.getContext().getString(R.string.bad_request);
        if (e instanceof NetworkRequestException) {
            NetworkRequestException nre = (NetworkRequestException) e;
            Throwable oe = nre.getCause();
            if (oe != null) oe.printStackTrace();
            if (oe instanceof ConnectException) //连接不到服务器
                message = view.getContext().getString(R.string.can_not_connect_to_server);
            else if (oe instanceof SocketException || oe instanceof SocketTimeoutException) //连接超时
                message = view.getContext().getString(R.string.disappointing_network_environment);
            else message = view.getContext().getString(R.string.net_not_ok);
            if (nre.cross.isNeedShowErrorMessage) view.error(nre.cross, message);
            presenter.remove(nre.cross);
        }
        view.toast(message);
    }

    @Override
    public void onNext(Cross cross) {
        Presenter presenter = presenterRef == null ? null : presenterRef.get();
        BaseView view = viewRef == null ? null : viewRef.get();
        if (presenter == null || view == null) return;
        BaseResponse<T> br = null;
        try {
            Type clazz = getClass().getGenericSuperclass();
            Type type = ((ParameterizedType) clazz).getActualTypeArguments()[0];
            br = CommonUtil.gson.fromJson(cross.body, TypeToken.getParameterized(BaseResponse.class, type).getType());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (br != null && br.check()) onSuccess(br.result, cross.extra);
        else {
            String message = br == null ? view.getContext().getString(R.string.bad_request) : br.message;
            view.toast(message);
            if (cross.isNeedShowErrorMessage) view.error(cross, message);
        }
        presenter.remove(cross);
    }

    /**
     * 解析成功
     *
     * @param result 解析出的目标对象
     * @param extra  附带信息
     */
    public abstract void onSuccess(T result, Object extra);
}
