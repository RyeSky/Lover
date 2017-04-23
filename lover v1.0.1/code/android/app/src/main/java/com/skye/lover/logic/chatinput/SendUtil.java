package com.skye.lover.logic.chatinput;

import android.content.Context;

import com.skye.lover.model.Cross;
import com.skye.lover.model.PrivateMessage;
import com.skye.lover.model.QuietResponseSubscriber;
import com.skye.lover.model.User;
import com.skye.lover.util.OkHttpUtil;
import com.skye.lover.util.ShareDataUtil;
import com.skye.lover.util.URLConfig;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 私信发送工具类
 */
public class SendUtil implements OnSendListener {
    private Context context;
    private List<OnSendSuccessListener> listeners = new ArrayList<>();//私信发送成功事件监听

    public SendUtil(Context context, OnSendSuccessListener... listeners) {
        this.context = context;
        if (listeners != null && listeners.length > 0) {
            this.listeners.addAll(Arrays.asList(listeners));
        }
    }

    @Override
    public void onSend(SendingContent content) {
        if (content == null) return;
        sendText(content.getAnother(), content.getContent());
    }

    /**
     * 通知订阅者
     *
     * @param pm 私信记录实体
     */
    private void notifyListeners(PrivateMessage pm) {
        for (OnSendSuccessListener item : listeners) item.onSendSuccess(pm);
    }

    /**
     * 清空私信发送成功事件监听
     */
    public void clearOnSendSuccessListener() {
        listeners.clear();
        listeners = null;
    }

    /**
     * 发送文本消息
     *
     * @param another 接收者id
     * @param content 消息内容
     */
    private void sendText(String another, String content) {
        Map<String, String> params = new HashMap<>();
        params.put("userId", ShareDataUtil.get(context, User.ID));
        params.put("another", another);
        params.put("content", content);
        Cross cross = OkHttpUtil.buildPostCall(URLConfig.ACTION_PUBLISH_PRIVATE_MESSAGE, params, false, false);
        OkHttpUtil.executePostCall(cross).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new QuietResponseSubscriber<PrivateMessage>() {
                    @Override
                    public void onSuccess(PrivateMessage obj) {
                        if (obj != null) notifyListeners(obj);
                    }
                });
    }
}
