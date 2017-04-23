package com.skye.lover.mvp.presenter;

import com.skye.lover.R;
import com.skye.lover.model.Comment;
import com.skye.lover.model.Cross;
import com.skye.lover.model.ListResponse;
import com.skye.lover.model.OperationSubscriber;
import com.skye.lover.model.ResponseSubscriber;
import com.skye.lover.mvp.model.CommentListModel;
import com.skye.lover.mvp.model.impl.CommentListModelImpl;
import com.skye.lover.mvp.view.BaseView;
import com.skye.lover.mvp.view.CommentListView;
import com.skye.lover.util.CommonUtil;
import com.skye.lover.util.OkHttpUtil;

/**
 * 评论列表主导器
 */
public class CommentListPresenter extends BasePresenter<CommentListView> {
    private CommentListModel model;

    @Override
    public void attachView(BaseView view) {
        super.attachView(view);
        model = new CommentListModelImpl();
    }

    @Override
    public void detachView() {
        model = null;
        super.detachView();
    }

    /**
     * 刷新数据
     *
     * @param pillowTalkId 悄悄话或世界广播id
     */
    public void refresh(String pillowTalkId) {
        CommentListView view = getView();
        if (view == null) return;
        Cross cross = model.refresh(view.getContext(), pillowTalkId);
        add(cross);
        if (cross.isNetworkConnected)
            view.showDialog(view.getContext().getString(R.string.loading));
        addSubscribe(OkHttpUtil.executePostCall(cross), new ResponseSubscriber<ListResponse<Comment>>(this, view) {
            @Override
            public void onSuccess(ListResponse<Comment> result, Object extra) {
                CommentListView view = getView();
                if (view == null) return;
                view.refreshData(result.list, result.page > result.pageCount);
                model.setPage(result.page);
            }

            @Override
            public void onCompleted() {
                super.onCompleted();
                CommentListView view = getView();
                if (view == null) return;
                view.hideRefresh();
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                CommentListView view = getView();
                if (view == null) return;
                view.hideRefresh();
            }
        });
    }

    /**
     * 加载更多
     *
     * @param pillowTalkId 悄悄话或世界广播id
     */
    public void loadMore(String pillowTalkId) {
        CommentListView view = getView();
        if (view == null) return;
        Cross cross = model.loadMore(view.getContext(), pillowTalkId);
        add(cross);
        if (cross.isNetworkConnected)
            view.showDialog(view.getContext().getString(R.string.loading));
        addSubscribe(OkHttpUtil.executePostCall(cross), new ResponseSubscriber<ListResponse<Comment>>(this, view) {
            @Override
            public void onSuccess(ListResponse<Comment> result, Object extra) {
                CommentListView view = getView();
                if (view == null) return;
                view.loadMoreData(result.list, result.page > result.pageCount);
                model.setPage(result.page);
            }

            @Override
            public void onCompleted() {
                super.onCompleted();
                CommentListView view = getView();
                if (view == null) return;
                view.loadMoreFinished();
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                CommentListView view = getView();
                if (view == null) return;
                view.loadMoreFinished();
            }
        });
    }

    /**
     * 删除评论
     *
     * @param commentId 评论id
     * @param position  被删除的评论的位置
     */
    public void deleteComment(String commentId, int position) {
        CommentListView view = getView();
        if (view == null) return;
        if (!CommonUtil.isNetworkConnected(view.getContext())) {
            view.toast(R.string.net_not_ok);
            return;
        }
        Cross cross = model.deleteComment(view.getContext(), commentId, position);
        add(cross);
        view.showDialog(view.getContext().getString(R.string.please_wait));
        addSubscribe(OkHttpUtil.executePostCall(cross), new OperationSubscriber(this, view) {
                    @Override
                    public void onSuccess(Object extra) {
                        CommentListView view = getView();
                        if (view == null) return;
                        view.afterDeleteComment((Integer) extra);
                    }
                }
        );
    }
}
