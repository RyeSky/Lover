package com.skye.lover.activity.pillowtalk.comment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.skye.actionsheet.ActionSheet;
import com.skye.lover.R;
import com.skye.lover.activity.base.BaseActivity;
import com.skye.lover.adapter.CommentAdapter;
import com.skye.lover.model.Comment;
import com.skye.lover.model.User;
import com.skye.lover.mvp.presenter.CommentListPresenter;
import com.skye.lover.mvp.view.CommentListView;
import com.skye.lover.util.ShareDataUtil;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnItemLongClick;

/**
 * 评论列表界面
 */
public class CommentListActivity extends BaseActivity<CommentListView, CommentListPresenter> implements CommentListView, AbsListView.OnScrollListener, SwipeRefreshLayout.OnRefreshListener {
    public static final String PILLOW_TALK_ID = "pillowTalkId";
    private final int PUBLISH_COMMENT = 0;//发表评论
    @BindView(R.id.empty_list_tip)
    TextView empty;//空列表提示
    @BindView(R.id.srl)
    SwipeRefreshLayout srl;//下拉刷新控件
    @BindView(R.id.listview)
    ListView listView;//列表
    private CommentAdapter adapter;
    private boolean refreshing = false, isLastPage = false, requestingMore = false;
    // 是否正在刷新、是否为最后一页、是否正在加载更多
    private String pillowTalkId;//悄悄话id

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_comment_list;
    }

    @Override
    public CommentListPresenter createPresenter() {
        return new CommentListPresenter();
    }

    @Override
    public int getDescription() {
        return R.string.comment_list_activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTopbar();
        topbar.title.setText(R.string.comment_list);
        topbar.left.setVisibility(View.VISIBLE);
        topbar.right.setText(R.string.comment);
        topbar.right.setVisibility(View.VISIBLE);
        pillowTalkId = getIntent().getStringExtra(PILLOW_TALK_ID);
        srl.setOnRefreshListener(this);
        srl.setColorSchemeResources(R.color.dark_red_normal, R.color.light_red_normal, R.color.dark_yellow_normal,
                R.color.light_yellow_normal, R.color.light_green_normal, R.color.dark_green_normal, R.color.light_blue_normal,
                R.color.dark_blue_normal, R.color.light_purple_normal, R.color.dark_purple_normal);
        listView.setEmptyView(empty);
        adapter = new CommentAdapter(context);
        listView.setAdapter(adapter);
        listView.setOnScrollListener(this);

        //评论
        RxView.clicks(topbar.right).throttleFirst(1, TimeUnit.SECONDS).subscribe((v) ->
                startActivityForResult(new Intent(context, PublishCommentActivity.class)
                        .putExtra(PublishCommentActivity.PILLOW_TALK_ID, pillowTalkId), PUBLISH_COMMENT));
    }

    @Override
    public void onRefresh() {
        srl.setEnabled(false);
        refreshing = true;
        presenter.refresh(pillowTalkId);
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
                presenter.loadMore(pillowTalkId);
            }
        }
    }

    @Override
    public void refreshData(List<Comment> list, boolean isLastPage) {
        adapter.clear();
        adapter.add(list);
        this.isLastPage = isLastPage;
        if (adapter.isEmpty())
            empty.setText(R.string.empty_comment);
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
    public void loadMoreData(List<Comment> list, boolean isLastPage) {
        adapter.add(list);
        this.isLastPage = isLastPage;
        if (adapter.isEmpty())
            empty.setText(R.string.empty_comment);
    }

    @Override
    public void loadMoreFinished() {
        requestingMore = false;
    }

    @Override
    public void afterDeleteComment(int position) {
        adapter.remove(position);
        if (adapter.isEmpty()) empty.setText(R.string.empty_comment);
        toast(R.string.delete_success);
        setResult(RESULT_OK);
    }

    @OnItemLongClick(R.id.listview)
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        final Comment comment = (Comment) parent.getItemAtPosition(position);
        if (comment == null) return false;
        if (!ShareDataUtil.get(context, User.ID).equals(comment.getCommenter()))
            return false;//登录用户不是发表者，不能删除
        setTheme(R.style.ActionSheetStyleIOS7);
        ActionSheet menuView = new ActionSheet(context);
        menuView.setCancelButtonTitle("取消");// before add items
        menuView.addItems(getString(R.string.delete));
        menuView.setItemClickListener((int itemPosition) -> {
            switch (itemPosition) {
                case 0:
                    presenter.deleteComment(comment.getId(), position);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        switch (requestCode) {
            case PUBLISH_COMMENT://发表评论
                setResult(RESULT_OK);
                onRefresh();
                break;
            default:
                break;
        }
    }
}
