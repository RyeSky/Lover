package com.skye.lover.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.skye.lover.R;
import com.skye.lover.activity.MainLoverActivity;
import com.skye.lover.activity.MainSingleActivity;
import com.skye.lover.activity.pillowtalk.BasePillowTalkDetailActivity;
import com.skye.lover.activity.pillowtalk.PillowTalkDetailActivity;
import com.skye.lover.activity.pillowtalk.PublishPillowTalkActivity;
import com.skye.lover.adapter.PillowTalkAdapter;
import com.skye.lover.model.PillowTalk;
import com.skye.lover.mvp.model.impl.FindModelImpl;
import com.skye.lover.mvp.presenter.PillowTalkListPresenter;
import com.skye.lover.mvp.view.PillowTalkListView;
import com.skye.lover.view.Topbar;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

/**
 * 发现碎片
 */
public class FindFragment extends BaseFragment<PillowTalkListView, PillowTalkListPresenter> implements PillowTalkListView, AbsListView.OnScrollListener, SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.topbar)
    Topbar topbar;//标题栏
    @BindView(R.id.empty_list_tip)
    TextView empty;//空列表提示
    @BindView(R.id.srl)
    SwipeRefreshLayout srl;//下拉刷新控件
    @BindView(R.id.listview)
    ListView listView;//列表
    private PillowTalkAdapter adapter;//蜜语适配器
    private boolean refreshing = false, isLastPage = false, requestingMore = false;
    // 是否正在刷新、是否为最后一页、是否正在加载更多

    @Override
    public PillowTalkListPresenter createPresenter() {
        return new PillowTalkListPresenter(new FindModelImpl());
    }

    @SuppressLint("InflateParams")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_find, null);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ButterKnife.bind(this, view);
        topbar.title.setText(R.string.find);
        topbar.right.setText(R.string.publish);
        topbar.right.setVisibility(View.VISIBLE);
        srl.setOnRefreshListener(this);
        srl.setColorSchemeResources(R.color.dark_red_normal, R.color.light_red_normal, R.color.dark_yellow_normal,
                R.color.light_yellow_normal, R.color.light_green_normal, R.color.dark_green_normal, R.color.light_blue_normal,
                R.color.dark_blue_normal, R.color.light_purple_normal, R.color.dark_purple_normal);
        listView.setEmptyView(empty);
        adapter = new PillowTalkAdapter(activity);
        listView.setAdapter(adapter);
        listView.setOnScrollListener(this);

        //发表
        RxView.clicks(topbar.right).throttleFirst(1, TimeUnit.SECONDS)
                .subscribe((v) -> {
                    int requestCode = -1;
                    if (activity instanceof MainLoverActivity)
                        requestCode = MainLoverActivity.PUBLISH_BROADCAST;
                    else if (activity instanceof MainSingleActivity)
                        requestCode = MainSingleActivity.PUBLISH_BROADCAST;
                    if (requestCode != -1)
                        activity.startActivityForResult(new Intent(activity, PublishPillowTalkActivity.class)
                                .putExtra(PublishPillowTalkActivity.TYPE, PublishPillowTalkActivity.BROADCAST), requestCode);
                    else
                        startActivity(new Intent(activity, PublishPillowTalkActivity.class)
                                .putExtra(PublishPillowTalkActivity.TYPE, PublishPillowTalkActivity.BROADCAST));
                });
    }

    @Override
    public void onRefresh() {
        srl.setEnabled(false);
        refreshing = true;
        presenter.refresh();
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        // 滑动到底部，加载更多数据，且没有正在加载更多没有正在刷新且当前不是最后一页数据
        if (!refreshing && !requestingMore && !isLastPage) {
            int lastInScreen = firstVisibleItem + visibleItemCount;
            if (lastInScreen >= totalItemCount) {
                requestingMore = true;
                presenter.loadMore();
            }
        }
    }

    @Override
    public void refreshData(List<PillowTalk> list, boolean isLastPage) {
        adapter.clear();
        adapter.add(list);
        this.isLastPage = isLastPage;
        if (adapter.isEmpty())
            empty.setText(R.string.empty_find);
    }

    @Override
    public void hideRefresh() {
        if (srl != null) {
            srl.setRefreshing(false);// 停止刷新
            srl.setEnabled(true);
        }
        refreshing = false;
    }

    @Override
    public void loadMoreData(List<PillowTalk> list, boolean isLastPage) {
        adapter.add(list);
        this.isLastPage = isLastPage;
        if (adapter.isEmpty())
            empty.setText(R.string.empty_find);
    }

    @Override
    public void loadMoreFinished() {
        requestingMore = false;
    }

    @OnItemClick(R.id.listview)
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        if (activity == null) return;
        PillowTalk pt = (PillowTalk) adapterView.getItemAtPosition(position);
        if (pt == null) return;
        int requestCode = -1;
        if (activity instanceof MainLoverActivity)
            requestCode = MainLoverActivity.FIND_PILLOW_TALK_ITEM;
        else if (activity instanceof MainSingleActivity)
            requestCode = MainSingleActivity.FIND_PILLOW_TALK_ITEM;
        if (requestCode != -1)
            BasePillowTalkDetailActivity.launch(activity, pt, requestCode);
        else startActivity(new Intent(activity, PillowTalkDetailActivity.class)
                .putExtra(BasePillowTalkDetailActivity.PILLOW_TALK, pt));
    }
}
