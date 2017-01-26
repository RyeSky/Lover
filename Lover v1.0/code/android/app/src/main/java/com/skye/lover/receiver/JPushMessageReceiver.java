package com.skye.lover.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.skye.lover.ConstantUtil;
import com.skye.lover.LoverApplication;
import com.skye.lover.R;
import com.skye.lover.activity.MainLoverActivity;
import com.skye.lover.activity.MainSingleActivity;
import com.skye.lover.activity.my.MessageListActivity;
import com.skye.lover.activity.pillowtalk.BasePillowTalkDetailActivity;
import com.skye.lover.activity.pillowtalk.BroadcastDetailActivity;
import com.skye.lover.activity.pillowtalk.PillowTalkDetailActivity;
import com.skye.lover.activity.privatemessage.PrivateMessageActivity;
import com.skye.lover.activity.user.OtherInfoActivity;
import com.skye.lover.model.Message;
import com.skye.lover.model.PillowTalk;
import com.skye.lover.model.PrivateMessage;
import com.skye.lover.util.CommonUtil;
import com.skye.lover.util.ShareData;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.data.JPushLocalNotification;

/**
 * 自定义接收器
 */
public class JPushMessageReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent data) {
        Intent intent;
        Bundle bundle = data.getExtras();
        Message message = null;
        try {
            message = CommonUtil.parseToObject(bundle.getString(JPushInterface.EXTRA_EXTRA), Message.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(data.getAction())) {// 收到通知
//            if (!ShareData.isLogined() || message == null) return;
//        } else
        if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(data.getAction())) {// 收到自定义消息
            message = null;
            try {
                message = CommonUtil.parseToObject(bundle.getString(JPushInterface.EXTRA_MESSAGE), Message.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (!ShareData.isLogined() || message == null) return;
            if (message.getType() == Message.TYPE_PERSONAL && message.getSubType() == Message.PRIVATE_MESSAGE) {//私信
                if (PrivateMessageActivity.isRunningInForeGround() && PrivateMessageActivity.isChattingUser(message.getAnotherId())) {
                } else {
                    JPushLocalNotification ln = new JPushLocalNotification();
                    ln.setBuilderId(0);
                    ln.setContent(message.getTitle());
                    ln.setTitle(context.getString(R.string.app_name));
                    ln.setNotificationId(11111);
                    ln.setBroadcastTime(System.currentTimeMillis() + 600);

                    ln.setExtras(CommonUtil.parseToJsonStr(message));
                    JPushInterface.addLocalNotification(LoverApplication.getInstance(), ln);
                }
                PrivateMessage pm = new PrivateMessage();
                pm.setId(message.getId());
                pm.setSender(message.getAnotherId());
                pm.setSenderNickname(message.getAnotherNickname());
                pm.setSenderAvatar(message.getAnotherAvatar());
                pm.setSenderGender(message.getAnotherGender());
                pm.setContent(message.getContent());
                pm.setCreateTime(message.getTime());
                context.sendBroadcast(new Intent(ConstantUtil.ACTION_RECEIVE_PRIVATE_MESSAGE)
                        .putExtra(PrivateMessageActivity.MESSAGE, pm));
            }
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(data.getAction())) {//用户点开通知
            if (!ShareData.isLogined() || message == null) return;
            switch (message.getType()) {
                case Message.TYPE_SYSTEM://系统消息
                    intent = new Intent(context, MessageListActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(intent);
                    break;
                case Message.TYPE_PERSONAL://个人消息
                    switch (message.getSubType()) {
                        case Message.COURTSHIPDISPLAY://示爱，跳转到对方主页
                            intent = new Intent(context, OtherInfoActivity.class).putExtra(OtherInfoActivity.ANOTHER, message.getAnotherId())
                                    .putExtra(OtherInfoActivity.ANOTHER_NICKNAME, message.getAnotherNickname());
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            context.startActivity(intent);
                            break;
                        case Message.COURTSHIPDISPLAY_BE_AGREED://示爱被同意
                            if (TextUtils.isEmpty(ShareData.getShareStringData(ShareData.ANOTHER))) {//登录用户当前还在单身模式，跳转到恋人模式
                                ShareData.setShareStringData(ShareData.ANOTHER, message.getAnotherId());
                                ShareData.setShareStringData(ShareData.ANOTHER_NICKNAME, message.getAnotherNickname());
                                ShareData.setShareStringData(ShareData.ANOTHER_AVATAR, message.getAnotherAvatar());
                                ShareData.setShareIntData(ShareData.ANOTHER_GENDER, message.getAnotherGender());
                                //恭喜你们成为恋人
                                intent = new Intent(context, MainLoverActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                context.startActivity(intent);
                            } else {
                                intent = new Intent(context, OtherInfoActivity.class).putExtra(OtherInfoActivity.ANOTHER, message.getAnotherId())
                                        .putExtra(OtherInfoActivity.ANOTHER_NICKNAME, message.getAnotherNickname());
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                context.startActivity(intent);
                            }
                            break;
                        case Message.BE_BROKE_UP://被分手
                            if (!TextUtils.isEmpty(ShareData.getShareStringData(ShareData.ANOTHER))) {//登录用户当前还在恋人模式，跳转到单身模式
                                ShareData.setShareStringData(ShareData.ANOTHER, "");
                                ShareData.setShareStringData(ShareData.ANOTHER_NICKNAME, "");
                                ShareData.setShareStringData(ShareData.ANOTHER_AVATAR, "");
                                ShareData.setShareIntData(ShareData.ANOTHER_GENDER, 0);
                                //重新成为单身贵族
                                intent = new Intent(context, MainSingleActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                context.startActivity(intent);
                            } else {
                                intent = new Intent(context, MessageListActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                context.startActivity(intent);
                            }
                            break;
                        case Message.REPLY://回复
                        case Message.COMMENT://评论
                            if (message.getPillowTalkType() == PillowTalk.PILLOW_TALK) {//悄悄话
                                intent = new Intent(context, PillowTalkDetailActivity.class)
                                        .putExtra(BasePillowTalkDetailActivity.PILLOW_TALK_ID, message.getPillowTalkId());
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                context.startActivity(intent);
                            } else if (message.getPillowTalkType() == PillowTalk.BROADCAST) {//世界广播
                                intent = new Intent(context, BroadcastDetailActivity.class)
                                        .putExtra(BasePillowTalkDetailActivity.PILLOW_TALK_ID, message.getPillowTalkId());
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                context.startActivity(intent);
                            }
                            break;
                        case Message.PRIVATE_MESSAGE://私信
                            intent = new Intent(context, PrivateMessageActivity.class)
                                    .putExtra(PrivateMessageActivity.ANOTHER, message.getAnotherId())
                                    .putExtra(PrivateMessageActivity.ANOTHER_NICKNAME, message.getAnotherNickname());
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            context.startActivity(intent);
                            break;
                        default:
                            break;
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
