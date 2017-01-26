package com.skye.lover.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.skye.actionsheet.ActionSheet;
import com.skye.lover.ConstantUtil;
import com.skye.lover.R;
import com.skye.lover.activity.privatemessage.PrivateMessageActivity;
import com.skye.lover.adapter.PrivateMessageSessionAdapter;
import com.skye.lover.exception.NoNetworkConnectException;
import com.skye.lover.model.BaseResponse;
import com.skye.lover.model.PrivateMessageSession;
import com.skye.lover.util.CommonUtil;
import com.skye.lover.util.OkHttpUtil;
import com.skye.lover.util.ShareData;
import com.skye.lover.util.URLConfig;
import com.skye.lover.view.Topbar;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import butterknife.OnItemLongClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 聊聊碎片
 */
public class ChatFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.topbar)
    Topbar topbar;//标题栏
    @BindView(R.id.empty_list_tip)
    TextView empty;//空列表提示
    @BindView(R.id.srl)
    SwipeRefreshLayout srl;//下拉刷新控件
    @BindView(R.id.listview)
    ListView listView;//列表
    private Activity activity;//对Activity的引用
    private Handler handler = new Handler();
    private View view;//碎片根布局
    private PrivateMessageSessionAdapter adapter;
    // 是否正在刷新、是否为最后一页、是否正在加载更多
    /**
     * 下拉刷新接口回调
     */
    private Callback callbackRefresh = new Callback() {
        @Override
        public void onFailure(Call call, final IOException e) {
            e.printStackTrace();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (!(e instanceof NoNetworkConnectException)) {//不是没有网络
                        CommonUtil.toast(activity, R.string.bad_request);
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
                    try {
                        CommonUtil.log(body);
                        BaseResponse br = CommonUtil.parseToObject(body, BaseResponse.class);//转化为返回基类
                        if (br.check()) {
                            List<PrivateMessageSession> list = CommonUtil.gson.fromJson(br.result, new TypeToken<List<PrivateMessageSession>>() {
                            }.getType());
                            adapter.setList(list);
                            if (adapter.isEmpty())
                                empty.setText(R.string.empty_chat);
                        } else {
                            CommonUtil.toast(activity, br.message);
                            empty.setText(br.message);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        CommonUtil.toast(activity, R.string.bad_request);
                        empty.setText(R.string.bad_request);
                    }
                    if (srl != null) {
                        srl.setRefreshing(false);// 停止刷新
                        srl.setEnabled(true);
                    }
                }
            });
        }
    };

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && ConstantUtil.ACTION_RECEIVE_PRIVATE_MESSAGE.equals(intent.getAction())) {
                onRefresh();
            }
        }
    };

    private Dialog dialog;
    private boolean hidden;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    @SuppressLint("InflateParams")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_chat, null);
        return view;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ButterKnife.bind(this, view);
        topbar.title.setText(R.string.chat);
        srl.setOnRefreshListener(this);
        srl.setColorScheme(R.color.dark_red_normal, R.color.light_red_normal, R.color.dark_yellow_normal,
                R.color.light_yellow_normal, R.color.light_green_normal, R.color.dark_green_normal, R.color.light_blue_normal,
                R.color.dark_blue_normal, R.color.light_purple_normal, R.color.dark_purple_normal);
        listView.setEmptyView(empty);
        adapter = new PrivateMessageSessionAdapter(activity);
        listView.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!hidden) onRefresh();
        activity.registerReceiver(receiver, new IntentFilter(ConstantUtil.ACTION_RECEIVE_PRIVATE_MESSAGE));
    }

    @Override
    public void onPause() {
        super.onPause();
        activity.unregisterReceiver(receiver);
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
        Map<String, String> params = new HashMap<>();
        params.put("userId", ShareData.getShareStringData(ShareData.ID));
        OkHttpUtil.doPost(activity, URLConfig.ACTION_PRIVATE_MESSAGE_SESSIONS, params, callbackRefresh);
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
        menuView.setItemClickListener(new ActionSheet.MenuItemClickListener() {
            @Override
            public void onItemClick(int itemPosition) {
                switch (itemPosition) {
                    case 0:
                        Map<String, String> params = new HashMap<>();
                        params.put("userId", pms.getOne());
                        params.put("another", pms.getAnother());
                        dialog = CommonUtil.showLoadingDialog(activity, R.string.please_wait);
                        OkHttpUtil.doPost(activity, URLConfig.ACTION_DELETE_PRIVATE_MESSAGE_SESSION, params,
                                new CallbackDeleteByPrivateMessageSession(position));
                        break;
                    default:
                        break;
                }
            }

        });
        menuView.setCancelableOnTouchMenuOutside(true);
        menuView.showMenu();
        return true;
    }


    /**
     * 根据聊天的两个用户删除整个会话记录接口回调
     */
    private class CallbackDeleteByPrivateMessageSession implements Callback {
        private int position;

        CallbackDeleteByPrivateMessageSession(int position) {
            this.position = position;
        }

        @Override
        public void onFailure(Call call, final IOException e) {
            e.printStackTrace();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    CommonUtil.closeLoadingDialog(dialog);
                    if (!(e instanceof NoNetworkConnectException)) //不是没有网络
                        CommonUtil.toast(activity, R.string.bad_request);
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
                            adapter.remove(position);
                            if (adapter.isEmpty())
                                empty.setText(R.string.empty_chat);
                            CommonUtil.toast(activity, R.string.delete_success);
                        } else
                            CommonUtil.toast(activity, br.message);
                    } catch (Exception e) {
                        e.printStackTrace();
                        CommonUtil.toast(activity, R.string.bad_request);
                    }
                }
            });
        }
    }
}
