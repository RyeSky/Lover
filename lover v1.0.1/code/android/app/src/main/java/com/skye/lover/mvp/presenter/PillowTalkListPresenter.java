package com.skye.lover.mvp.presenter;

import com.skye.lover.R;
import com.skye.lover.model.Cross;
import com.skye.lover.model.ListResponse;
import com.skye.lover.model.PillowTalk;
import com.skye.lover.model.ResponseSubscriber;
import com.skye.lover.mvp.model.PillowTalkListModel;
import com.skye.lover.mvp.view.PillowTalkListView;
import com.skye.lover.util.OkHttpUtil;

/**
 * 悄悄话列表主导器
 */
public class PillowTalkListPresenter extends BasePresenter<PillowTalkListView> {
    private PillowTalkListModel model;

    public PillowTalkListPresenter(PillowTalkListModel model) {
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
        PillowTalkListView view = getView();
        if (view == null) return;
        Cross cross = model.refresh(view.getContext());
        add(cross);
        if (cross.isNetworkConnected)
            view.showDialog(view.getContext().getString(R.string.loading));
        addSubscribe(OkHttpUtil.executePostCall(cross), new ResponseSubscriber<ListResponse<PillowTalk>>(this, view) {
            @Override
            public void onSuccess(ListResponse<PillowTalk> result, Object extra) {
                PillowTalkListView view = getView();
                if (view == null) return;
                view.refreshData(result.list, result.page > result.pageCount);
                model.setPage(result.page);
            }

            @Override
            public void onCompleted() {
                super.onCompleted();
                PillowTalkListView view = getView();
                if (view == null) return;
                view.hideRefresh();
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                PillowTalkListView view = getView();
                if (view == null) return;
                view.hideRefresh();
            }
        });
    }

    /**
     * 加载更多
     */
    public void loadMore() {
        PillowTalkListView view = getView();
        if (view == null) return;
        Cross cross = model.loadMore(view.getContext());
        add(cross);
        if (cross.isNetworkConnected)
            view.showDialog(view.getContext().getString(R.string.loading));
        addSubscribe(OkHttpUtil.executePostCall(cross), new ResponseSubscriber<ListResponse<PillowTalk>>(this, view) {
            @Override
            public void onSuccess(ListResponse<PillowTalk> result, Object extra) {
                PillowTalkListView view = getView();
                if (view == null) return;
                view.loadMoreData(result.list, result.page > result.pageCount);
                model.setPage(result.page);
            }

            @Override
            public void onCompleted() {
                super.onCompleted();
                PillowTalkListView view = getView();
                if (view == null) return;
                view.loadMoreFinished();
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                PillowTalkListView view = getView();
                if (view == null) return;
                view.loadMoreFinished();
            }
        });
    }
}
