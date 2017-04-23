package com.skye.lover.mvp.presenter;

import com.skye.lover.R;
import com.skye.lover.model.Cross;
import com.skye.lover.model.ListResponse;
import com.skye.lover.model.PillowTalk;
import com.skye.lover.model.ResponseSubscriber;
import com.skye.lover.mvp.model.BaseMyOperatedPillowTalkListModel;
import com.skye.lover.mvp.view.BaseMyOperatedPillowTalkListView;
import com.skye.lover.util.OkHttpUtil;

/**
 * 用户操作过的悄悄话的列表主导器基类
 */
public class BaseMyOperatedPillowTalkListPresenter<V extends BaseMyOperatedPillowTalkListView, M extends BaseMyOperatedPillowTalkListModel>
        extends BasePresenter<V> {
    protected M model;

    public BaseMyOperatedPillowTalkListPresenter(M model) {
        this.model = model;
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
        V view = getView();
        if (view == null) return;
        Cross cross = model.refresh(view.getContext());
        add(cross);
        if (cross.isNetworkConnected)
            view.showDialog(view.getContext().getString(R.string.loading));
        addSubscribe(OkHttpUtil.executePostCall(cross), new ResponseSubscriber<ListResponse<PillowTalk>>(this, view) {
            @Override
            public void onSuccess(ListResponse<PillowTalk> result, Object extra) {
                V view = getView();
                if (view == null) return;
                view.refreshData(result.list, result.page > result.pageCount);
                model.setPage(result.page);
            }

            @Override
            public void onCompleted() {
                super.onCompleted();
                V view = getView();
                if (view == null) return;
                view.hideRefresh();
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                V view = getView();
                if (view == null) return;
                view.hideRefresh();
            }
        });
    }

    /**
     * 加载更多
     */
    public void loadMore() {
        V view = getView();
        if (view == null) return;
        Cross cross = model.loadMore(view.getContext());
        add(cross);
        if (cross.isNetworkConnected)
            view.showDialog(view.getContext().getString(R.string.loading));
        addSubscribe(OkHttpUtil.executePostCall(cross), new ResponseSubscriber<ListResponse<PillowTalk>>(this, view) {
            @Override
            public void onSuccess(ListResponse<PillowTalk> result, Object extra) {
                V view = getView();
                if (view == null) return;
                view.loadMoreData(result.list, result.page > result.pageCount);
                model.setPage(result.page);
            }

            @Override
            public void onCompleted() {
                super.onCompleted();
                V view = getView();
                if (view == null) return;
                view.loadMoreFinished();
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                V view = getView();
                if (view == null) return;
                view.loadMoreFinished();
            }
        });
    }
}
