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
import com.skye.lover.logic.chatinput.OnSendSuccessListener;
import com.skye.lover.logic.chatinput.SendUtil;
import com.skye.lover.model.PrivateMessage;
import com.skye.lover.model.PrivateMessageWrapper;
import com.skye.lover.mvp.presenter.PrivateMessagePresenter;
import com.skye.lover.mvp.view.PrivateMessageView;
import com.skye.lover.view.InputView;

import butterknife.BindView;

/**
 * 私信聊天界面
 */
public class PrivateMessageActivity extends BaseActivity<PrivateMessageView, PrivateMessagePresenter>
        implements PrivateMessageView, SwipeRefreshLayout.OnRefreshListener, OnSendSuccessListener, PrivateMessageAdapter.OnDeletePrivateMessageListener {
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

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && ConstantUtil.ACTION_RECEIVE_PRIVATE_MESSAGE.equals(intent.getAction())) {
                PrivateMessage pm = intent.getParcelableExtra(MESSAGE);
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

    @Override
    public PrivateMessagePresenter createPresenter() {
        return new PrivateMessagePresenter();
    }

    @Override
    public int getDescription() {
        return R.string.private_message_activity;
    }

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
        srl.setColorSchemeResources(R.color.dark_red_normal, R.color.light_red_normal, R.color.dark_yellow_normal,
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
    public void refreshData(PrivateMessageWrapper wrapper) {
        adapter.add(wrapper.list);
        listView.post(() -> listView.setSelection(adapter.getCount() > 10 ? 0 : listView.getCount()));
        isLastPage = adapter.getFirstPrivateMessageId().equals(wrapper.firstPrivateMessageId);
        if (adapter.isEmpty()) empty.setText(R.string.empty_private_message);
    }

    @Override
    public void hideRefresh() {
        if (srl != null) {
            srl.setRefreshing(false);// 停止刷新
            srl.setEnabled(!isLastPage);
        }
    }

    @Override
    public void deleteSuccess(int position) {
        adapter.deleteSuccess(position);
    }

    @Override
    public void onDeletePrivateMessage(String privateMessageId, int position) {
        presenter.deletePrivateMessage(privateMessageId, position);
    }

    @Override
    protected void onPause() {
        unregisterReceiver(receiver);
        super.onPause();
    }

    @Override
    public void onRefresh() {
        srl.setEnabled(false);
        presenter.refresh(another, adapter.getFirstPrivateMessageId());
    }

    @Override
    public void onSendSuccess(PrivateMessage pm) {
        adapter.add(pm);
        listView.post(() -> listView.setSelection(listView.getCount()));
    }

    @Override
    public void onBackPressed() {//判断是否拦截返回键操作
        if (!input.interceptBackPress()) super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        su.clearOnSendSuccessListener();
        runningInForeGround = false;
        another = "";
        super.onDestroy();
    }
}
