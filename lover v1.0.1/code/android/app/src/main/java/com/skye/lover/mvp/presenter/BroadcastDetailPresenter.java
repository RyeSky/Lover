package com.skye.lover.mvp.presenter;

import android.content.Intent;

import com.skye.lover.R;
import com.skye.lover.model.Comment;
import com.skye.lover.model.Cross;
import com.skye.lover.model.ListResponse;
import com.skye.lover.model.OperationSubscriber;
import com.skye.lover.model.ResponseSubscriber;
import com.skye.lover.model.ScrollPosition;
import com.skye.lover.mvp.model.BroadcastDetailModel;
import com.skye.lover.mvp.model.impl.BroadcastDetailModelImpl;
import com.skye.lover.mvp.view.BroadcastDetailView;
import com.skye.lover.util.CommonUtil;
import com.skye.lover.util.OkHttpUtil;

import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static com.skye.lover.activity.pillowtalk.BasePillowTalkDetailActivity.BACK_REASON;
import static com.skye.lover.activity.pillowtalk.BasePillowTalkDetailActivity.DELETE_COMMENT;

/**
 * 世界广播详情主导器
 */
public class BroadcastDetailPresenter extends BasePillowTalkDetailPresenter<BroadcastDetailView, BroadcastDetailModel> {
    @Override
    BroadcastDetailModel createModel() {
        return new BroadcastDetailModelImpl();
    }

    /**
     * 获取世界广播评论列表
     *
     * @param pillowTalkId 悄悄话id
     * @param page         请求页数
     * @param position     请求回数据后的滚动位置
     */
    public void comments(String pillowTalkId, int page, ScrollPosition position) {
        BroadcastDetailView view = getView();
        if (view == null) return;
        if (!CommonUtil.isNetworkConnected(view.getContext())) {
            view.toast(R.string.net_not_ok);
            return;
        }
        Cross cross = model.comments(pillowTalkId, page, position);
        add(cross);
        view.showDialog(view.getContext().getString(R.string.loading));
        addSubscribe(OkHttpUtil.executePostCall(cross), new ResponseSubscriber<ListResponse<Comment>>(this, view) {
                    @Override
                    public void onSuccess(ListResponse<Comment> result, Object extra) {
                        BroadcastDetailView view = getView();
                        if (view == null) return;
                        @SuppressWarnings("unchecked")
                        Map<String, Object> extras = (Map<String, Object>) extra;
                        view.afterComments(result.list, result.pageCount, extra == null ? 1 : (Integer) extras.get("page"),
                                extra == null ? ScrollPosition.HEAD : (ScrollPosition) extras.get("position"));
                    }
                }
        );
    }

    /**
     * 删除评论
     *
     * @param comment 评论
     */
    public void deleteComment(Comment comment) {
        BroadcastDetailView view = getView();
        if (view == null) return;
        if (!CommonUtil.isNetworkConnected(view.getContext())) {
            view.toast(R.string.net_not_ok);
            return;
        }
        Cross cross = model.deleteComment(view.getContext(), comment);
        add(cross);
        view.showDialog(view.getContext().getString(R.string.please_wait));
        addSubscribe(OkHttpUtil.executePostCall(cross), new OperationSubscriber(this, view) {
                    @Override
                    public void onSuccess(Object extra) {
                        BroadcastDetailView view = getView();
                        if (view == null) return;
                        view.afterDeleteComment((Comment) extra);
                        view.setResult(RESULT_OK, new Intent().putExtra(BACK_REASON, DELETE_COMMENT));
                    }
                }
        );
    }
}
