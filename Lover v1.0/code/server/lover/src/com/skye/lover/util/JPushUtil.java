package com.skye.lover.util;

//import cn.jiguang.common.resp.APIConnectionException;
//import cn.jiguang.common.resp.APIRequestException;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import com.skye.lover.model.Message;

/**
 * 极光推送工具类
 */
public class JPushUtil {
    private static final Logger log = LoggerFactory.getLogger(JPushUtil.class);

    /**
     * 执行推送动作
     *
     * @param payloads 推送负载
     */
    private static void push(PushPayload... payloads) {
        if (payloads == null || payloads.length == 0) return;
        JPushClient client = new JPushClient(Const.JPUSH_SECRET, Const.JPUSH_APP_KEY);
        for (PushPayload item : payloads) {
            try {
                PushResult result = client.sendPush(item);
                log.info("Got result - " + result);
            } catch (APIConnectionException e) {
                log.error("Connection error. Should retry later. ", e);
                log.error("Sendno: " + item.getSendno());
            } catch (APIRequestException e) {
                log.error("Error response from JPush server. Should review and fix it. ", e);
                log.info("HTTP Status: " + e.getStatus());
                log.info("Error Code: " + e.getErrorCode());
                log.info("Error Message: " + e.getErrorMessage());
                log.info("Msg ID: " + e.getMsgId());
                log.error("Sendno: " + item.getSendno());
            } catch (Exception e) {
                log.error("jpush error", e);
            }
        }
    }

    /**
     * 发送全局广播消息
     *
     * @param message 推送消息实体
     */
    public static void sendPushMessage(Message message) {
        String title = message.title;
        Map<String, String> extraParams = message.parseMessageToMap();
        PushPayload androidPayload = Builder.buildAndroidPushPayload(title, extraParams);
        PushPayload iosPayload = Builder.buildIosPushPayload(title, extraParams);
        push(androidPayload, iosPayload);
    }


    /**
     * 推送消息 - 单个别名 - 支持设置ios角标
     *
     * @param message 推送消息实体
     */
    public static void sendPushMessageByAliasByAlias(Message message) {
        String title = message.content;
        Map<String, String> extraParams = message.parseMessageToMap();
        String alias = message.userId;
        PushPayload androidPayload = Builder.buildAndroidPushPayloadByAlias(title, extraParams, alias);
        PushPayload iosPayload = Builder.builIOSPushPayloadByAlias(title, extraParams, alias);
        push(androidPayload, iosPayload);
    }

    /**
     * 推送自定义消息 - 不会在通知栏显示
     *
     * @param message 推送消息实体
     */
    public static void sendCustomMessage(Message message) {
        String alias = message.userId;
        String json = message.toString();
        push(Builder.buildPushPayloadForCustomMessage(alias, json));
    }


    /**
     * 负责构建各种推送消息负载
     */
    private static class Builder {

        /**
         * 构建android推送消息负载
         *
         * @param title       标题
         * @param extraParams 拓展参数,为json格式,例如extraParams.put("message",message);
         * @return 推送消息负载
         */
        private static PushPayload buildAndroidPushPayload(String title, Map<String, String> extraParams) {
            return PushPayload.newBuilder()
                    .setPlatform(Platform.android())
                    .setAudience(Audience.all())
                    .setNotification(Notification.android(title, Const.PROJECT_NAME, extraParams))
                    .build();
        }

        /**
         * 构建ios推送消息负载
         *
         * @param title       标题
         * @param extraParams 拓展参数,为json格式,例如extraParams.put("message",message);
         * @return 推送消息负载
         */
        private static PushPayload buildIosPushPayload(String title, Map<String, String> extraParams) {
            Options op = Options.sendno();
            op.setApnsProduction(true);
            return PushPayload.newBuilder()
                    .setPlatform(Platform.ios())
                    .setAudience(Audience.all())
                    .setNotification(Notification.newBuilder()
                            .addPlatformNotification(
                                    IosNotification.newBuilder().setAlert(title).addExtras(extraParams).build())
                            .build())
                    .setOptions(op)
                    .build();
        }

        /**
         * 通过别名构建Android推送消息负载
         *
         * @param title       标题
         * @param extraParams 拓展参数,为json格式,例如extraParams.put("message",message);
         * @param alias       指定用户的别名
         * @return 推送消息负载
         */
        private static PushPayload buildAndroidPushPayloadByAlias(String title, Map<String, String> extraParams, String alias) {
            return PushPayload.newBuilder()
                    .setPlatform(Platform.android())
                    .setAudience(Audience.alias(alias))
                    .setNotification(Notification.android(title, Const.PROJECT_NAME, extraParams))
                    .build();
        }

        /**
         * 通过别名构建IOS推送消息负载
         *
         * @param title       标题
         * @param extraParams 拓展参数,为json格式,例如extraParams.put("message",message);
         * @param alias       指定用户的别名
         * @return 推送消息负载
         */
        private static PushPayload builIOSPushPayloadByAlias(String title, Map<String, String> extraParams, String alias) {
            Options op = Options.sendno();
            op.setApnsProduction(true);
            return PushPayload.newBuilder()
                    .setPlatform(Platform.ios())
                    .setAudience(Audience.alias(alias))
                    .setNotification(Notification.newBuilder()
                            .addPlatformNotification(
                                    IosNotification.newBuilder().setAlert(title).addExtras(extraParams).build())
                            .build())
                    .setOptions(op)
                    .build();
        }

        /**
         * 构建自定义消息
         *
         * @param alias 指定用户的别名
         * @param json  json字符串
         * @return 推送消息负载
         */
        private static PushPayload buildPushPayloadForCustomMessage(String alias, String json) {
            return PushPayload.newBuilder()
                    .setPlatform(Platform.android_ios())
                    .setAudience(Audience.alias(alias))
                    .setMessage(cn.jpush.api.push.model.Message.newBuilder().setMsgContent(json).addExtra("from", "JPush").build())
                    .build();
        }
    }
}
