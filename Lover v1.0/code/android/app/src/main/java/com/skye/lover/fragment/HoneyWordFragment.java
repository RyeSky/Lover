package com.skye.lover.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.skye.lover.R;
import com.skye.lover.activity.MainLoverActivity;
import com.skye.lover.activity.pillowtalk.BasePillowTalkDetailActivity;
import com.skye.lover.activity.pillowtalk.PublishPillowTalkActivity;
import com.skye.lover.adapter.PillowTalkAdapter;
import com.skye.lover.exception.NoNetworkConnectException;
import com.skye.lover.model.BaseResponse;
import com.skye.lover.model.ListResponse;
import com.skye.lover.model.PillowTalk;
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
import butterknife.OnClick;
import butterknife.OnItemClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 蜜语碎片
 */
public class HoneyWordFragment extends Fragment implements AbsListView.OnScrollListener, SwipeRefreshLayout.OnRefreshListener {
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
                    if (!(e instanceof NoNetworkConnectException)) {//不是没有网络
                        CommonUtil.toast(activity, R.string.bad_request);
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
                                empty.setText(R.string.empty_honey_word);
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
                    if (!(e instanceof NoNetworkConnectException)) {//不是没有网络
                        CommonUtil.toast(activity, R.string.bad_request);
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
                    try {
                        CommonUtil.log(body);
                        BaseResponse br = CommonUtil.parseToObject(body, BaseResponse.class);//转化为返回基类
                        if (br.check()) {
                            Data data = CommonUtil.parseToObject(br.result, Data.class);
                            adapter.add(data.list);
                            page = data.page;
                            isLastPage = data.page > data.pageCount;
                            if (adapter.isEmpty())
                                empty.setText(R.string.empty_honey_word);
                        } else {
                            CommonUtil.toast(activity, br.message);
                            empty.setText(br.message);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        CommonUtil.toast(activity, R.string.bad_request);
                        empty.setText(R.string.bad_request);
                    }
                    requestingMore = false;
                }
            });
        }
    };

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    @SuppressLint("InflateParams")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_honey_word, null);
        return view;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ButterKnife.bind(this, view);
        topbar.title.setText(R.string.honey_word);
        topbar.right.setText(R.string.publish);
        topbar.right.setVisibility(View.VISIBLE);
        srl.setOnRefreshListener(this);
        srl.setColorScheme(R.color.dark_red_normal, R.color.light_red_normal, R.color.dark_yellow_normal,
                R.color.light_yellow_normal, R.color.light_green_normal, R.color.dark_green_normal, R.color.light_blue_normal,
                R.color.dark_blue_normal, R.color.light_purple_normal, R.color.dark_purple_normal);
        listView.setEmptyView(empty);
        adapter = new PillowTalkAdapter(activity);
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
        params.put("another", ShareData.getShareStringData(ShareData.ANOTHER));
        params.put("page", page + "");
        OkHttpUtil.doPost(activity, URLConfig.ACTION_HONEY_WORD, params, callbackRefresh);
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
                params.put("another", ShareData.getShareStringData(ShareData.ANOTHER));
                params.put("page", page + "");
                OkHttpUtil.doPost(activity, URLConfig.ACTION_HONEY_WORD, params, callbackLoadMore);
            }
        }
    }

    @OnItemClick(R.id.listview)
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        PillowTalk pt = (PillowTalk) adapterView.getItemAtPosition(position);
        if (pt == null) return;
        BasePillowTalkDetailActivity.launch(activity, pt, MainLoverActivity.HONEY_WORD_PILLOW_TALK_ITEM);
    }

    @OnClick(R.id.tv_right)
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_right://发起
                activity.startActivityForResult(new Intent(activity, PublishPillowTalkActivity.class)
                                .putExtra(PublishPillowTalkActivity.TYPE, PublishPillowTalkActivity.PILLOW_TALK)
                        , MainLoverActivity.PUBLISH_PILLOW_TALK);
                break;
            default:
                break;
        }
    }

    private class Data extends ListResponse {
        public List<PillowTalk> list;
    }
}

