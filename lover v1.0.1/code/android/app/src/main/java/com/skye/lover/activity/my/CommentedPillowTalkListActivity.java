package com.skye.lover.activity.my;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.skye.lover.R;
import com.skye.lover.activity.base.BaseActivity;
import com.skye.lover.activity.pillowtalk.BasePillowTalkDetailActivity;
import com.skye.lover.adapter.PillowTalkAdapter;
import com.skye.lover.model.PillowTalk;
import com.skye.lover.mvp.model.impl.CommentedPillowTalkListImpl;
import com.skye.lover.mvp.presenter.PillowTalkListPresenter;
import com.skye.lover.mvp.view.PillowTalkListView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnItemClick;

/**
 * 用户评论过的悄悄话列表界面
 */
public class CommentedPillowTalkListActivity extends BaseActivity<PillowTalkListView, PillowTalkListPresenter> implements PillowTalkListView, AbsListView.OnScrollListener, SwipeRefreshLayout.OnRefreshListener {
    private final int PILLOW_TALK_ITEM = 0;//点击悄悄话item
    @BindView(R.id.empty_list_tip)
    TextView empty;//空列表提示
    @BindView(R.id.srl)
    SwipeRefreshLayout srl;//下拉刷新控件
    @BindView(R.id.listview)
    ListView listView;//列表
    private PillowTalkAdapter adapter;
    private boolean refreshing = false, isLastPage = false, requestingMore = false;
    // 是否正在刷新、是否为最后一页、是否正在加载更多

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_commented_pillow_talk_list;
    }

    @Override
    public PillowTalkListPresenter createPresenter() {
        return new PillowTalkListPresenter(new CommentedPillowTalkListImpl());
    }

    @Override
    public int getDescription() {
        return R.string.commented_pillow_talk_list_activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTopbar();
        topbar.title.setText(R.string.comment_list);
        topbar.left.setVisibility(View.VISIBLE);
        srl.setOnRefreshListener(this);
        srl.setColorSchemeResources(R.color.dark_red_normal, R.color.light_red_normal, R.color.dark_yellow_normal,
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
        presenter.refresh();
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
            empty.setText(R.string.empty_commented);
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
            empty.setText(R.string.empty_commented);
    }

    @Override
    public void loadMoreFinished() {
        requestingMore = false;
    }

    @OnItemClick(R.id.listview)
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        PillowTalk pt = (PillowTalk) adapterView.getItemAtPosition(position);
        if (pt == null) return;
        BasePillowTalkDetailActivity.launch(this, pt, PILLOW_TALK_ITEM);
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
                        BasePillowTalkDetailActivity.CANCEL_PRAISE.equals(backReason) || BasePillowTalkDetailActivity.DELETE_COMMENT.equals(backReason))
                    onRefresh();
                break;
            default:
                break;
        }
    }
}
