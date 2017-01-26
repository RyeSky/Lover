package com.skye.lover.logic.chatinput;

import android.content.Context;
import android.os.Handler;

import com.skye.lover.R;
import com.skye.lover.exception.NoNetworkConnectException;
import com.skye.lover.model.BaseResponse;
import com.skye.lover.model.PrivateMessage;
import com.skye.lover.util.CommonUtil;
import com.skye.lover.util.OkHttpUtil;
import com.skye.lover.util.ShareData;
import com.skye.lover.util.URLConfig;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 私信发送工具类
 */
public class SendUtil implements OnSendListener {
    private Context context;
    private Handler handler = new Handler();
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
        params.put("userId", ShareData.getShareStringData(ShareData.ID));
        params.put("another", another);
        params.put("content", content);
        OkHttpUtil.doPost(context, URLConfig.ACTION_PUBLISH_PRIVATE_MESSAGE, params, new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                e.printStackTrace();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (!(e instanceof NoNetworkConnectException))//不是没有网络
                            CommonUtil.toast(context, R.string.bad_request);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String body = response.body().string();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            CommonUtil.log(body);
                            BaseResponse br = CommonUtil.parseToObject(body, BaseResponse.class);//转化为返回基类
                            if (br.check()) {
                                PrivateMessage pm = CommonUtil.parseToObject(br.result, PrivateMessage.class);
                                if (pm != null) notifyListeners(pm);
                            } else
                                CommonUtil.toast(context, br.message);
                        } catch (Exception e) {
                            e.printStackTrace();
                            CommonUtil.toast(context, R.string.bad_request);
                        }
                    }
                });
            }
        });
    }
}
