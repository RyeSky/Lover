package com.skye.lover.mvp.presenter;

import com.skye.lover.R;
import com.skye.lover.model.Cross;
import com.skye.lover.model.OperationSubscriber;
import com.skye.lover.mvp.model.PraisedPillowTalkListModel;
import com.skye.lover.mvp.view.PraisedPillowTalkListView;
import com.skye.lover.util.CommonUtil;
import com.skye.lover.util.OkHttpUtil;

/**
 * 用户赞过的悄悄话列表主导器
 */
public class PraisedPillowTalkListPresenter extends BaseMyOperatedPillowTalkListPresenter<PraisedPillowTalkListView, PraisedPillowTalkListModel> {
    public PraisedPillowTalkListPresenter(PraisedPillowTalkListModel model) {
        super(model);
    }

    /**
     * 取消赞悄悄话
     *
     * @param pillowTalkId 悄悄话或世界广播id
     * @param position     被取消赞的悄悄话或世界广播的位置
     */
    public void cancelPraise(String pillowTalkId, int position) {
        PraisedPillowTalkListView view = getView();
        if (view == null) return;
        if (!CommonUtil.isNetworkConnected(view.getContext())) {
            view.toast(R.string.net_not_ok);
            return;
        }
        Cross cross = model.cancelPraise(view.getContext(), pillowTalkId, position);
        add(cross);
        view.showDialog(view.getContext().getString(R.string.please_wait));
        addSubscribe(OkHttpUtil.executePostCall(cross), new OperationSubscriber(this, view) {
                    @Override
                    public void onSuccess(Object extra) {
                        PraisedPillowTalkListView view = getView();
                        if (view == null) return;
                        view.afterCancelPraise((Integer) extra);
                    }
                }
        );
    }
}
