package com.skye.lover.activity.my;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.skye.actionsheet.ActionSheet;
import com.skye.lover.R;
import com.skye.lover.activity.MainLoverActivity;
import com.skye.lover.activity.MainSingleActivity;
import com.skye.lover.activity.base.BaseActivity;
import com.skye.lover.activity.pillowtalk.BasePillowTalkDetailActivity;
import com.skye.lover.activity.pillowtalk.BroadcastDetailActivity;
import com.skye.lover.activity.pillowtalk.PillowTalkDetailActivity;
import com.skye.lover.activity.user.OtherInfoActivity;
import com.skye.lover.adapter.MessageAdapter;
import com.skye.lover.exception.NoNetworkConnectException;
import com.skye.lover.model.BaseResponse;
import com.skye.lover.model.ListResponse;
import com.skye.lover.model.Message;
import com.skye.lover.model.PillowTalk;
import com.skye.lover.util.CommonUtil;
import com.skye.lover.util.OkHttpUtil;
import com.skye.lover.util.ShareData;
import com.skye.lover.util.URLConfig;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnItemClick;
import butterknife.OnItemLongClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 消息列表界面
 */
public class MessageListActivity extends BaseActivity implements AbsListView.OnScrollListener, SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.empty_list_tip)
    TextView empty;//空列表提示
    @BindView(R.id.srl)
    SwipeRefreshLayout srl;//下拉刷新控件
    @BindView(R.id.listview)
    ListView listView;//列表
    private MessageAdapter adapter;
    private int page = 1;//页数
    private boolean refreshing = false, isLastPage = false, requestingMore = false;
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
                    refreshing = false;
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
                            adapter.clear();
                            adapter.add(data.list);
                            page = data.page;
                            isLastPage = data.page > data.pageCount;
                            if (adapter.isEmpty())
                                empty.setText(R.string.empty_message_list);
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
                        srl.setEnabled(true);
                    }
                    refreshing = false;
                }
            });
        }
    };
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
                    requestingMore = false;
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
                            page = data.page;
                            isLastPage = data.page > data.pageCount;
                            if (adapter.isEmpty())
                                empty.setText(R.string.empty_message_list);
                        } else {
                            CommonUtil.toast(context, br.message);
                            empty.setText(br.message);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        CommonUtil.toast(context, R.string.bad_request);
                        empty.setText(R.string.bad_request);
                    }
                    requestingMore = false;
                }
            });
        }
    };

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_message_list;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTopbar();
        topbar.title.setText(R.string.message_list);
        topbar.left.setVisibility(View.VISIBLE);
        srl.setOnRefreshListener(this);
        srl.setColorScheme(R.color.dark_red_normal, R.color.light_red_normal, R.color.dark_yellow_normal,
                R.color.light_yellow_normal, R.color.light_green_normal, R.color.dark_green_normal, R.color.light_blue_normal,
                R.color.dark_blue_normal, R.color.light_purple_normal, R.color.dark_purple_normal);
        listView.setEmptyView(empty);
        adapter = new MessageAdapter(context);
        listView.setAdapter(adapter);
        listView.setOnScrollListener(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        onRefresh();
    }

    @Override
    public void onRefresh() {
        srl.setEnabled(false);
        refreshing = true;
        page = 1;
        Map<String, String> params = new HashMap<>();
        params.put("userId", ShareData.getShareStringData(ShareData.ID));
        params.put("page", page + "");
        dialog = CommonUtil.showLoadingDialog(context, R.string.loading);
        OkHttpUtil.doPost(context, URLConfig.ACTION_MESSAGES, params, callbackRefresh);
    }

    @Override
    public void onScrollStateChanged(final AbsListView view, final int scrollState) {
    }

    @Override
    public void onScroll(final AbsListView view, final int firstVisibleItem, final int visibleItemCount,
                         final int totalItemCount) {
        // 滑动到底部，加载更多数据，且没有正在加载更多没有正在刷新且当前不是最后一页数据
        if (!refreshing && !requestingMore && !isLastPage) {
            int lastInScreen = firstVisibleItem + visibleItemCount;
            if (lastInScreen >= totalItemCount) {
                requestingMore = true;
                Map<String, String> params = new HashMap<>();
                params.put("userId", ShareData.getShareStringData(ShareData.ID));
                params.put("page", page + "");
                dialog = CommonUtil.showLoadingDialog(context, R.string.loading);
                OkHttpUtil.doPost(context, URLConfig.ACTION_MESSAGES, params, callbackLoadMore);
            }
        }
    }

    @OnItemClick(R.id.listview)
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Message message = (Message) adapterView.getItemAtPosition(position);
        if (message == null) return;
        if (message.getType() == Message.TYPE_PERSONAL) {//个人消息
            switch (message.getSubType()) {
                case Message.COURTSHIPDISPLAY://示爱，跳转到对方主页
                    startActivity(new Intent(context, OtherInfoActivity.class).putExtra(OtherInfoActivity.ANOTHER, message.getAnotherId())
                            .putExtra(OtherInfoActivity.ANOTHER_NICKNAME, message.getAnotherNickname()));
                    break;
                case Message.COURTSHIPDISPLAY_BE_AGREED://示爱被同意
                    if (TextUtils.isEmpty(ShareData.getShareStringData(ShareData.ANOTHER))) {//登录用户当前还在单身模式，跳转到恋人模式
                        ShareData.setShareStringData(ShareData.ANOTHER, message.getAnotherId());
                        ShareData.setShareStringData(ShareData.ANOTHER_NICKNAME, message.getAnotherNickname());
                        ShareData.setShareStringData(ShareData.ANOTHER_AVATAR, message.getAnotherAvatar());
                        ShareData.setShareIntData(ShareData.ANOTHER_GENDER, message.getAnotherGender());
                        //恭喜你们成为恋人
                        Intent intent = new Intent(context, MainLoverActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    } else {//已经在恋人模式
                        startActivity(new Intent(context, OtherInfoActivity.class).putExtra(OtherInfoActivity.ANOTHER, message.getAnotherId())
                                .putExtra(OtherInfoActivity.ANOTHER_NICKNAME, message.getAnotherNickname()));
                    }
                    break;
                case Message.BE_BROKE_UP://被分手
                    if (!TextUtils.isEmpty(ShareData.getShareStringData(ShareData.ANOTHER))) {//登录用户当前还在恋人模式，跳转到单身模式
                        ShareData.setShareStringData(ShareData.ANOTHER, "");
                        ShareData.setShareStringData(ShareData.ANOTHER_NICKNAME, "");
                        ShareData.setShareStringData(ShareData.ANOTHER_AVATAR, "");
                        ShareData.setShareIntData(ShareData.ANOTHER_GENDER, 0);
                        //重新成为单身贵族
                        Intent intent = new Intent(context, MainSingleActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                    break;
                case Message.REPLY://回复
                case Message.COMMENT://评论
                    if (message.getPillowTalkType() == PillowTalk.PILLOW_TALK)
                        startActivity(new Intent(context, PillowTalkDetailActivity.class)
                                .putExtra(BasePillowTalkDetailActivity.PILLOW_TALK_ID, message.getPillowTalkId()));
                    else if (message.getPillowTalkType() == PillowTalk.BROADCAST)
                        startActivity(new Intent(context, BroadcastDetailActivity.class)
                                .putExtra(BasePillowTalkDetailActivity.PILLOW_TALK_ID, message.getPillowTalkId()));
                    break;
                default:
                    break;
            }
        }
    }

    @OnItemLongClick(R.id.listview)
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        final Message message = (Message) parent.getItemAtPosition(position);
        if (message == null) return false;
        setTheme(R.style.ActionSheetStyleIOS7);
        ActionSheet menuView = new ActionSheet(context);
        menuView.setCancelButtonTitle("取消");// before add items
        menuView.addItems(getString(R.string.delete));
        menuView.setItemClickListener(new ActionSheet.MenuItemClickListener() {
            @Override
            public void onItemClick(int itemPosition) {
                switch (itemPosition) {
                    case 0:
                        Map<String, String> params = new HashMap<>();
                        params.put("userId", ShareData.getShareStringData(ShareData.ID));
                        params.put("messageId", message.getId());
                        dialog = CommonUtil.showLoadingDialog(context, R.string.please_wait);
                        OkHttpUtil.doPost(context, URLConfig.ACTION_DELETE_MESSAGE, params, new CallbackDeleteMessage(position));
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
     * 刪除消息接口回调
     */
    private class CallbackDeleteMessage implements Callback {
        private int position;

        CallbackDeleteMessage(int position) {
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
                    CommonUtil.closeLoadingDialog(dialog);
                    try {
                        CommonUtil.log(body);
                        BaseResponse br = CommonUtil.parseToObject(body, BaseResponse.class);//转化为返回基类
                        if (br.check()) {
                            adapter.remove(position);
                            if (adapter.isEmpty())
                                empty.setText(R.string.empty_message_list);
                            CommonUtil.toast(context, R.string.delete_success);
                        } else
                            CommonUtil.toast(context, br.message);
                    } catch (Exception e) {
                        e.printStackTrace();
                        CommonUtil.toast(context, R.string.bad_request);
                    }
                }
            });
        }
    }

    private class Data extends ListResponse {
        public List<Message> list;
    }
}
