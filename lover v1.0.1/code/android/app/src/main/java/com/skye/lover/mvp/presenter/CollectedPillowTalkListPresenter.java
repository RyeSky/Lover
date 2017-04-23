package com.skye.lover.mvp.presenter;

import com.skye.lover.R;
import com.skye.lover.model.Cross;
import com.skye.lover.model.OperationSubscriber;
import com.skye.lover.mvp.model.CollectedPillowTalkListModel;
import com.skye.lover.mvp.view.CollectedPillowTalkListView;
import com.skye.lover.util.CommonUtil;
import com.skye.lover.util.OkHttpUtil;

/**
 * 用户收藏的悄悄话列表主导器
 */
public class CollectedPillowTalkListPresenter extends BaseMyOperatedPillowTalkListPresenter<CollectedPillowTalkListView, CollectedPillowTalkListModel> {
    public CollectedPillowTalkListPresenter(CollectedPillowTalkListModel model) {
        super(model);
    }

    /**
     * 取消收藏悄悄话
     *
     * @param pillowTalkId 悄悄话或世界广播id
     * @param position     被取消收藏的悄悄话或世界广播的位置
     */
    public void cancelCollect(String pillowTalkId, int position) {
        CollectedPillowTalkListView view = getView();
        if (view == null) return;
        if (!CommonUtil.isNetworkConnected(view.getContext())) {
            view.toast(R.string.net_not_ok);
            return;
        }
        Cross cross = model.cancelCollect(view.getContext(), pillowTalkId, position);
        add(cross);
        view.showDialog(view.getContext().getString(R.string.please_wait));
        addSubscribe(OkHttpUtil.executePostCall(cross), new OperationSubscriber(this, view) {
                    @Override
                    public void onSuccess(Object extra) {
                        CollectedPillowTalkListView view = getView();
                        if (view == null) return;
                        view.afterCancelCollect((Integer) extra);
                    }
                }
        );
    }
}
