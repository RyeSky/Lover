package com.skye.lover.fragment;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.skye.actionsheet.ActionSheet;
import com.skye.lover.ConstantUtil;
import com.skye.lover.R;
import com.skye.lover.activity.privatemessage.PrivateMessageActivity;
import com.skye.lover.adapter.PrivateMessageSessionAdapter;
import com.skye.lover.model.PrivateMessageSession;
import com.skye.lover.mvp.presenter.ChatPresenter;
import com.skye.lover.mvp.view.ChatView;
import com.skye.lover.view.Topbar;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import butterknife.OnItemLongClick;

/**
 * 聊聊碎片
 */
public class ChatFragment extends BaseFragment<ChatView, ChatPresenter> implements ChatView, SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.topbar)
    Topbar topbar;//标题栏
    @BindView(R.id.empty_list_tip)
    TextView empty;//空列表提示
    @BindView(R.id.srl)
    SwipeRefreshLayout srl;//下拉刷新控件
    @BindView(R.id.listview)
    ListView listView;//列表
    private PrivateMessageSessionAdapter adapter;//私信会话列表适配器
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && ConstantUtil.ACTION_RECEIVE_PRIVATE_MESSAGE.equals(intent.getAction())) {
                onRefresh();
            }
        }
    };

    @Override
    public ChatPresenter createPresenter() {
        return new ChatPresenter();
    }

    @SuppressLint("InflateParams")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_chat, null);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ButterKnife.bind(this, view);
        topbar.title.setText(R.string.chat);
        srl.setOnRefreshListener(this);
        srl.setColorSchemeResources(R.color.dark_red_normal, R.color.light_red_normal, R.color.dark_yellow_normal,
                R.color.light_yellow_normal, R.color.light_green_normal, R.color.dark_green_normal, R.color.light_blue_normal,
                R.color.dark_blue_normal, R.color.light_purple_normal, R.color.dark_purple_normal);
        listView.setEmptyView(empty);
        adapter = new PrivateMessageSessionAdapter(activity);
        listView.setAdapter(adapter);

        RxView.clicks(topbar.right).throttleFirst(1, TimeUnit.SECONDS).subscribe((Void) -> {
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!hidden) onRefresh();
        activity.registerReceiver(receiver, new IntentFilter(ConstantUtil.ACTION_RECEIVE_PRIVATE_MESSAGE));
    }

    @OnItemClick(R.id.listview)
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        PrivateMessageSession pms = (PrivateMessageSession) adapterView.getItemAtPosition(position);
        startActivity(new Intent(activity, PrivateMessageActivity.class)
                .putExtra(PrivateMessageActivity.ANOTHER, pms.getAnother())
                .putExtra(PrivateMessageActivity.ANOTHER_NICKNAME, pms.getAnotherNickname()));
    }

    @OnItemLongClick(R.id.listview)
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        final PrivateMessageSession pms = (PrivateMessageSession) parent.getItemAtPosition(position);
        if (pms == null) return false;
        activity.setTheme(R.style.ActionSheetStyleIOS7);
        ActionSheet menuView = new ActionSheet(activity);
        menuView.setCancelButtonTitle("取消");// before add items
        menuView.addItems(getString(R.string.delete));
        menuView.setItemClickListener((int itemPosition) -> {
            switch (itemPosition) {
                case 0:
                    presenter.deleteByPrivateMessageSession(pms.getAnother(), position);
                    break;
                default:
                    break;
            }
        });
        menuView.setCancelableOnTouchMenuOutside(true);
        menuView.showMenu();
        return true;
    }

    @Override
    public void onPause() {
        activity.unregisterReceiver(receiver);
        super.onPause();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        this.hidden = hidden;
        if (!hidden) onRefresh();
    }

    @Override
    public void onRefresh() {
        srl.setEnabled(false);
        presenter.refresh();
    }

    @Override
    public void refreshData(List<PrivateMessageSession> list) {
        adapter.setList(list);
        if (adapter.isEmpty())
            empty.setText(R.string.empty_chat);
    }

    @Override
    public void hideRefresh() {
        if (srl != null) {
            srl.setRefreshing(false);// 停止刷新
            srl.setEnabled(true);
        }
    }

    @Override
    public void deleteByPrivateMessageSession(int position) {
        adapter.remove(position);
        if (adapter.isEmpty())
            empty.setText(R.string.empty_chat);
        toast(R.string.delete_success);
    }
}
