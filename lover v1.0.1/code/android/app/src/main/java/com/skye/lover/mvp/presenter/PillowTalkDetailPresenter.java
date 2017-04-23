package com.skye.lover.mvp.presenter;

import com.skye.lover.R;
import com.skye.lover.model.Cross;
import com.skye.lover.model.FollowPillowTalk;
import com.skye.lover.model.ListResponse;
import com.skye.lover.model.OperationSubscriber;
import com.skye.lover.model.ResponseSubscriber;
import com.skye.lover.model.ScrollPosition;
import com.skye.lover.mvp.model.PillowTalkDetailModel;
import com.skye.lover.mvp.model.impl.PillowTalkDetailModelImpl;
import com.skye.lover.mvp.view.PillowTalkDetailView;
import com.skye.lover.util.CommonUtil;
import com.skye.lover.util.OkHttpUtil;

import java.util.Map;

/**
 * 悄悄话详情主导器
 */
public class PillowTalkDetailPresenter extends BasePillowTalkDetailPresenter<PillowTalkDetailView, PillowTalkDetailModel> {
    @Override
    PillowTalkDetailModel createModel() {
        return new PillowTalkDetailModelImpl();
    }

    /**
     * 公开悄悄话
     *
     * @param pillowTalkId 悄悄话或世界广播id
     */
    public void open(String pillowTalkId) {
        PillowTalkDetailView view = getView();
        if (view == null) return;
        if (!CommonUtil.isNetworkConnected(view.getContext())) {
            view.toast(R.string.net_not_ok);
            return;
        }
        Cross cross = model.open(view.getContext(), pillowTalkId);
        add(cross);
        view.showDialog(view.getContext().getString(R.string.please_wait));
        addSubscribe(OkHttpUtil.executePostCall(cross), new OperationSubscriber(this, view) {
                    @Override
                    public void onSuccess(Object extra) {
                        PillowTalkDetailView view = getView();
                        if (view == null) return;
                        view.afterOpen();
                    }
                }
        );
    }

    /**
     * 获取跟随悄悄话列表
     *
     * @param pillowTalkId 悄悄话id
     * @param page         请求页数
     * @param position     请求回数据后的滚动位置
     */
    public void follows(String pillowTalkId, int page, ScrollPosition position) {
        PillowTalkDetailView view = getView();
        if (view == null) return;
        if (!CommonUtil.isNetworkConnected(view.getContext())) {
            view.toast(R.string.net_not_ok);
            return;
        }
        Cross cross = model.follows(pillowTalkId, page, position);
        add(cross);
        view.showDialog(view.getContext().getString(R.string.loading));
        addSubscribe(OkHttpUtil.executePostCall(cross), new ResponseSubscriber<ListResponse<FollowPillowTalk>>(this, view) {
                    @Override
                    public void onSuccess(ListResponse<FollowPillowTalk> result, Object extra) {
                        PillowTalkDetailView view = getView();
                        if (view == null) return;
                        @SuppressWarnings("unchecked")
                        Map<String, Object> extras = (Map<String, Object>) extra;
                        view.afterFollows(result.list, result.pageCount, extra == null ? 1 : (Integer) extras.get("page"),
                                extra == null ? ScrollPosition.HEAD : (ScrollPosition) extras.get("position"));
                    }
                }
        );
    }

    /**
     * 删除跟随悄悄话
     *
     * @param fpt 跟随悄悄话
     */
    public void deleteFollowPillowTalk(FollowPillowTalk fpt) {
        PillowTalkDetailView view = getView();
        if (view == null) return;
        if (!CommonUtil.isNetworkConnected(view.getContext())) {
            view.toast(R.string.net_not_ok);
            return;
        }
        Cross cross = model.deleteFollowPillowTalk(view.getContext(), fpt);
        add(cross);
        view.showDialog(view.getContext().getString(R.string.please_wait));
        addSubscribe(OkHttpUtil.executePostCall(cross), new OperationSubscriber(this, view) {
                    @Override
                    public void onSuccess(Object extra) {
                        PillowTalkDetailView view = getView();
                        if (view == null) return;
                        view.afterDeleteFollowPillowTalk((FollowPillowTalk) extra);
                    }
                }
        );
    }
}
