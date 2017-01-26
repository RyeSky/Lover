package com.skye.lover.activity.my;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.skye.actionsheet.ActionSheet;
import com.skye.lover.R;
import com.skye.lover.activity.base.BaseActivity;
import com.skye.lover.activity.pillowtalk.BasePillowTalkDetailActivity;
import com.skye.lover.adapter.PillowTalkAdapter;
import com.skye.lover.exception.NoNetworkConnectException;
import com.skye.lover.model.BaseResponse;
import com.skye.lover.model.ListResponse;
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
 * 用户收藏的悄悄话列表
 */
public class CollectedPillowTalkListActivity extends BaseActivity implements AbsListView.OnScrollListener, SwipeRefreshLayout.OnRefreshListener {
    private final int PILLOW_TALK_ITEM = 0;//点击悄悄话item
    @BindView(R.id.empty_list_tip)
    TextView empty;//空列表提示
    @BindView(R.id.srl)
    SwipeRefreshLayout srl;//下拉刷新控件
    @BindView(R.id.listview)
    ListView listView;//列表
    private PillowTalkAdapter adapter;
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
                                empty.setText(R.string.empty_collection);
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
                                empty.setText(R.string.empty_collection);
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
        return R.layout.activity_collected_pillow_talk_list;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTopbar();
        topbar.title.setText(R.string.collect_list);
        topbar.left.setVisibility(View.VISIBLE);
        srl.setOnRefreshListener(this);
        srl.setColorScheme(R.color.dark_red_normal, R.color.light_red_normal, R.color.dark_yellow_normal,
                R.color.light_yellow_normal, R.color.light_green_normal, R.color.dark_green_normal, R.color.light_blue_normal,
                R.color.dark_blue_normal, R.color.light_purple_normal, R.color.dark_purple_normal);
        listView.setEmptyView(empty);
        adapter = new PillowTalkAdapter(context);
        listView.setAdapter(adapter);
        listView.setOnScrollListener(this);
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
        OkHttpUtil.doPost(context, URLConfig.ACTION_COLLETED_PILLOW_TALKS, params, callbackRefresh);
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
                OkHttpUtil.doPost(context, URLConfig.ACTION_COLLETED_PILLOW_TALKS, params, callbackLoadMore);
            }
        }
    }

    @OnItemClick(R.id.listview)
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        PillowTalk pt = (PillowTalk) adapterView.getItemAtPosition(position);
        if (pt == null) return;
        BasePillowTalkDetailActivity.launch(this, pt, PILLOW_TALK_ITEM);
    }

    @OnItemLongClick(R.id.listview)
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        final PillowTalk pt = (PillowTalk) parent.getItemAtPosition(position);
        if (pt == null) return false;
        setTheme(R.style.ActionSheetStyleIOS7);
        ActionSheet menuView = new ActionSheet(context);
        menuView.setCancelButtonTitle("取消");// before add items
        menuView.addItems(getString(R.string.cancel_collect));
        menuView.setItemClickListener(new ActionSheet.MenuItemClickListener() {
            @Override
            public void onItemClick(int itemPosition) {
                switch (itemPosition) {
                    case 0:
                        Map<String, String> params = new HashMap<>();
                        params.put("userId", ShareData.getShareStringData(ShareData.ID));
                        params.put("pillowTalkId", pt.getId());
                        dialog = CommonUtil.showLoadingDialog(context, R.string.please_wait);
                        OkHttpUtil.doPost(context, URLConfig.ACTION_CANCEL_COLLET, params, new CallbackCancelCollect(position));
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        switch (requestCode) {
            case PILLOW_TALK_ITEM:
                if (data == null) return;
                String backReason = data.getStringExtra(BasePillowTalkDetailActivity.BACK_REASON);
                if (BasePillowTalkDetailActivity.OPEN.equals(backReason) || BasePillowTalkDetailActivity.DELETE.equals(backReason) ||
                        BasePillowTalkDetailActivity.CANCEL_PRAISE.equals(backReason) || BasePillowTalkDetailActivity.CANCEL_COLLECT.equals(backReason))
                    onRefresh();
                break;
            default:
                break;
        }
    }

    /**
     * 取消收藏悄悄话接口回调
     */
    private class CallbackCancelCollect implements Callback {
        private int position;

        CallbackCancelCollect(int position) {
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
                                empty.setText(R.string.empty_collection);
                            CommonUtil.toast(context, R.string.cancel_collect_success);
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
        public List<PillowTalk> list;
    }
}
