package com.skye.lover.model;

import com.google.gson.reflect.TypeToken;
import com.skye.lover.util.CommonUtil;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import rx.Subscriber;

/**
 * 返回数据响应订阅者，没有吐司也没有在界面显示错误信息
 */
public abstract class QuietResponseSubscriber<T> extends Subscriber<Cross> {
    @Override
    public void onCompleted() {
    }

    @Override
    public void onError(Throwable e) {
        CommonUtil.handleNetworkRequestException(e);
    }

    @Override
    public void onNext(Cross cross) {
        BaseResponse<T> br = null;
        try {
            Type clazz = getClass().getGenericSuperclass();
            Type type = ((ParameterizedType) clazz).getActualTypeArguments()[0];
            br = CommonUtil.gson.fromJson(cross.body, TypeToken.getParameterized(BaseResponse.class, type).getType());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (br != null && br.check()) onSuccess(br.result);
        else CommonUtil.log(br == null ? "" : br.message);
    }

    /**
     * 解析成功
     *
     * @param obj 解析出的目标对象
     */
    public abstract void onSuccess(T obj);
}