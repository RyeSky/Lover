package com.skye.lover.activity.privatemessage;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.skye.lover.ConstantUtil;
import com.skye.lover.R;
import com.skye.lover.activity.base.BaseActivity;
import com.skye.lover.adapter.PrivateMessageAdapter;
import com.skye.lover.exception.NoNetworkConnectException;
import com.skye.lover.logic.chatinput.OnSendSuccessListener;
import com.skye.lover.logic.chatinput.SendUtil;
import com.skye.lover.model.BaseResponse;
import com.skye.lover.model.PrivateMessage;
import com.skye.lover.util.CommonUtil;
import com.skye.lover.util.OkHttpUtil;
import com.skye.lover.util.ShareData;
import com.skye.lover.util.URLConfig;
import com.skye.lover.view.InputView;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 私信聊天界面
 */
public class PrivateMessageActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, OnSendSuccessListener {
    public static final String ANOTHER = "another";
    public static final String ANOTHER_NICKNAME = "anotherNickname";
    public static final String MESSAGE = "message";
    private static boolean runningInForeGround = false;//是否正在前台运行
    private static String another;//聊天对方的id
    @BindView(R.id.empty_list_tip)
    TextView empty;//空列表提示
    @BindView(R.id.srl)
    SwipeRefreshLayout srl;//下拉刷新控件
    @BindView(R.id.listview)
    ListView listView;//列表
    @BindView(R.id.input)
    InputView input;
    private SendUtil su;
    private PrivateMessageAdapter adapter;
    private boolean isLastPage = false;//是否为最后一页
    /**
     * 加載更多接口回调
     */
    private Callback callbackLoadMore = new Callback() {
        @Override
        public void onFailure(Call call, final IOException e) {
            e.printStackTrace();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    CommonUtil.closeLoadingDialog(dialog);
                    if (!(e instanceof NoNetworkConnectException)) {//不是没有网络
                        CommonUtil.toast(context, R.string.bad_request);
                        empty.setText(R.string.bad_request);
                    } else
                        empty.setText(R.string.net_not_ok);
                    if (srl != null) {
                        srl.setRefreshing(false);// 停止刷新
                        srl.setEnabled(true);
                    }
                }
            });
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            final String body = response.body().string();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    CommonUtil.closeLoadingDialog(dialog);
                    try {
                        CommonUtil.log(body);
                        BaseResponse br = CommonUtil.parseToObject(body, BaseResponse.class);//转化为返回基类
                        if (br.check()) {
                            Data data = CommonUtil.parseToObject(br.result, Data.class);
                            adapter.add(data.list);
                            listView.post(new Runnable() {
                                @Override
                                public void run() {
                                    listView.setSelection(adapter.getCount() > 10 ? 0 : listView.getCount());
                                }
                            });
                            isLastPage = adapter.getFirstPrivateMessageId().equals(data.firstPrivateMessageId);
                            if (adapter.isEmpty())
                                empty.setText(R.string.empty_private_message);
                        } else {
                            CommonUtil.toast(context, br.message);
                            empty.setText(br.message);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        CommonUtil.toast(context, R.string.bad_request);
                        empty.setText(R.string.bad_request);
                    }
                    if (srl != null) {
                        srl.setRefreshing(false);// 停止刷新
                        srl.setEnabled(!isLastPage);
                    }
                }
            });
        }
    };

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && ConstantUtil.ACTION_RECEIVE_PRIVATE_MESSAGE.equals(intent.getAction())) {
                PrivateMessage pm = (PrivateMessage) intent.getSerializableExtra(MESSAGE);
                if (pm == null) return;
                onSendSuccess(pm);
            }
        }
    };

    /**
     * 是否正在前台运行
     */
    public static boolean isRunningInForeGround() {
        return runningInForeGround;
    }

    /**
     * 是否正是聊天对象
     */
    public static boolean isChattingUser(String userId) {
        return !TextUtils.isEmpty(another) && another.equals(userId);
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_private_message;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        another = intent.getStringExtra(ANOTHER);
        String anotherNickname = intent.getStringExtra(ANOTHER_NICKNAME);
        initTopbar();
        topbar.title.setText(anotherNickname);
        topbar.left.setVisibility(View.VISIBLE);
        srl.setOnRefreshListener(this);
        srl.setColorScheme(R.color.dark_red_normal, R.color.light_red_normal, R.color.dark_yellow_normal,
                R.color.light_yellow_normal, R.color.light_green_normal, R.color.dark_green_normal, R.color.light_blue_normal,
                R.color.dark_blue_normal, R.color.light_purple_normal, R.color.dark_purple_normal);
        listView.setEmptyView(empty);
        adapter = new PrivateMessageAdapter(context);
        listView.setAdapter(adapter);
        su = new SendUtil(context, input, this);
        input.setAnother(another);
        input.setOnSendListener(su);
        input.bind(this, srl);
        onRefresh();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        intent = getIntent();
        another = intent.getStringExtra(ANOTHER);
        String anotherNickname = intent.getStringExtra(ANOTHER_NICKNAME);
        topbar.title.setText(anotherNickname);
        adapter.clear();
        onRefresh();
    }

    @Override
    protected void onResume() {
        super.onResume();
        runningInForeGround = true;
        registerReceiver(receiver, new IntentFilter(ConstantUtil.ACTION_RECEIVE_PRIVATE_MESSAGE));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    @Override
    public void onRefresh() {
        srl.setEnabled(false);
        Map<String, String> params = new HashMap<>();
        params.put("userId", ShareData.getShareStringData(ShareData.ID));
        params.put("another", another);
        params.put("privateMessageId", adapter.getFirstPrivateMessageId());
        dialog = CommonUtil.showLoadingDialog(context, R.string.loading);
        OkHttpUtil.doPost(context, URLConfig.ACTION_PRIVATE_MESSAGES, params, callbackLoadMore);
    }

    @Override
    public void onSendSuccess(PrivateMessage pm) {
        adapter.add(pm);
        listView.post(new Runnable() {
            @Override
            public void run() {
                listView.setSelection(listView.getCount());
            }
        });
    }

    @Override
    public void onBackPressed() {//判断是否拦截返回键操作
        if (!input.interceptBackPress()) super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        su.clearOnSendSuccessListener();
        runningInForeGround = false;
        another = "";
    }

    private class Data {
        List<PrivateMessage> list;
        String firstPrivateMessageId;
    }
}
