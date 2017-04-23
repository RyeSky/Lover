package com.skye.lover.mvp.presenter;

import com.skye.lover.R;
import com.skye.lover.model.Cross;
import com.skye.lover.model.ListResponse;
import com.skye.lover.model.Message;
import com.skye.lover.model.OperationSubscriber;
import com.skye.lover.model.ResponseSubscriber;
import com.skye.lover.mvp.model.MessageListModel;
import com.skye.lover.mvp.model.impl.MessageListModelImpl;
import com.skye.lover.mvp.view.BaseView;
import com.skye.lover.mvp.view.MessageListView;
import com.skye.lover.util.CommonUtil;
import com.skye.lover.util.OkHttpUtil;

/**
 * 消息列表主导器
 */
public class MessageListPresenter extends BasePresenter<MessageListView> {
    private MessageListModel model;

    @Override
    public void attachView(BaseView view) {
        super.attachView(view);
        model = new MessageListModelImpl();
    }

    @Override
    public void detachView() {
        model = null;
        super.detachView();
    }

    /**
     * 刷新数据
     */
    public void refresh() {
        MessageListView view = getView();
        if (view == null) return;
        Cross cross = model.refresh(view.getContext());
        add(cross);
        if (cross.isNetworkConnected)
            view.showDialog(view.getContext().getString(R.string.loading));
        addSubscribe(OkHttpUtil.executePostCall(cross), new ResponseSubscriber<ListResponse<Message>>(this, view) {
            @Override
            public void onSuccess(ListResponse<Message> result, Object extra) {
                MessageListView view = getView();
                if (view == null) return;
                view.refreshData(result.list, result.page > result.pageCount);
                model.setPage(result.page);
            }

            @Override
            public void onCompleted() {
                super.onCompleted();
                MessageListView view = getView();
                if (view == null) return;
                view.hideRefresh();
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                MessageListView view = getView();
                if (view == null) return;
                view.hideRefresh();
            }
        });
    }

    /**
     * 加载更多
     */
    public void loadMore() {
        MessageListView view = getView();
        if (view == null) return;
        Cross cross = model.loadMore(view.getContext());
        add(cross);
        if (cross.isNetworkConnected)
            view.showDialog(view.getContext().getString(R.string.loading));
        addSubscribe(OkHttpUtil.executePostCall(cross), new ResponseSubscriber<ListResponse<Message>>(this, view) {
            @Override
            public void onSuccess(ListResponse<Message> result, Object extra) {
                MessageListView view = getView();
                if (view == null) return;
                view.loadMoreData(result.list, result.page > result.pageCount);
                model.setPage(result.page);
            }

            @Override
            public void onCompleted() {
                super.onCompleted();
                MessageListView view = getView();
                if (view == null) return;
                view.loadMoreFinished();
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                MessageListView view = getView();
                if (view == null) return;
                view.loadMoreFinished();
            }
        });
    }

    /**
     * 删除评论
     *
     * @param messageId 被删除的消息id
     * @param position  被删除消息的位置
     */
    public void deleteMessage(String messageId, int position) {
        MessageListView view = getView();
        if (view == null) return;
        if (!CommonUtil.isNetworkConnected(view.getContext())) {
            view.toast(R.string.net_not_ok);
            return;
        }
        Cross cross = model.deleteMessage(view.getContext(), messageId, position);
        add(cross);
        view.showDialog(view.getContext().getString(R.string.please_wait));
        addSubscribe(OkHttpUtil.executePostCall(cross), new OperationSubscriber(this, view) {
                    @Override
                    public void onSuccess(Object extra) {
                        MessageListView view = getView();
                        if (view == null) return;
                        view.afterDeleteMessage((Integer) extra);
                    }
                }
        );
    }
}
