package com.skye.lover.mvp.presenter;

import android.content.Intent;

import com.skye.lover.R;
import com.skye.lover.model.Cross;
import com.skye.lover.model.OperationSubscriber;
import com.skye.lover.model.PillowTalk;
import com.skye.lover.model.PillowTalkProperties;
import com.skye.lover.model.ResponseSubscriber;
import com.skye.lover.mvp.model.BasePillowTalkDetailModel;
import com.skye.lover.mvp.view.BasePillowTalkDetailView;
import com.skye.lover.mvp.view.BaseView;
import com.skye.lover.util.CommonUtil;
import com.skye.lover.util.OkHttpUtil;

import static android.app.Activity.RESULT_OK;
import static com.skye.lover.activity.pillowtalk.BasePillowTalkDetailActivity.BACK_REASON;
import static com.skye.lover.activity.pillowtalk.BasePillowTalkDetailActivity.CANCEL_COLLECT;
import static com.skye.lover.activity.pillowtalk.BasePillowTalkDetailActivity.CANCEL_PRAISE;
import static com.skye.lover.activity.pillowtalk.BasePillowTalkDetailActivity.DELETE;

/**
 * 悄悄话或世界广播主导基类
 */
public abstract class BasePillowTalkDetailPresenter<V extends BasePillowTalkDetailView, M extends BasePillowTalkDetailModel> extends BasePresenter<V> {
    protected M model;

    /**
     * 创建模型
     */
    abstract M createModel();

    @Override
    public void attachView(BaseView view) {
        super.attachView(view);
        model = createModel();
    }

    @Override
    public void detachView() {
        model = null;
        super.detachView();
    }

    /**
     * 悄悄话详情
     *
     * @param pillowTalkId 悄悄话或世界广播id
     */
    public void pillowTalkDetail(String pillowTalkId) {
        V view = getView();
        if (view == null) return;
        if (!CommonUtil.isNetworkConnected(view.getContext())) {
            view.toast(R.string.net_not_ok);
            return;
        }
        Cross cross = model.pillowTalkDetail(view.getContext(), pillowTalkId);
        add(cross);
        addSubscribe(OkHttpUtil.executePostCall(cross), new ResponseSubscriber<PillowTalk>(this, view) {
                    @Override
                    public void onSuccess(PillowTalk result, Object extra) {
                        V view = getView();
                        if (view == null) return;
                        view.afterPillowTalkDetail(result);
                    }
                }
        );
    }

    /**
     * 悄悄话部分属性
     *
     * @param pillowTalkId 悄悄话或世界广播id
     */
    public void pillowTalkProperties(String pillowTalkId) {
        V view = getView();
        if (view == null) return;
        if (!CommonUtil.isNetworkConnected(view.getContext())) {
            view.toast(R.string.net_not_ok);
            return;
        }
        Cross cross = model.pillowTalkProperties(pillowTalkId);
        add(cross);
        addSubscribe(OkHttpUtil.executePostCall(cross), new ResponseSubscriber<PillowTalkProperties>(this, view) {
                    @Override
                    public void onSuccess(PillowTalkProperties result, Object extra) {
                        V view = getView();
                        if (view == null) return;
                        view.afterPillowTalkProperties(result);
                    }
                }
        );
    }

    /**
     * 删除悄悄话
     *
     * @param pillowTalkId 悄悄话或世界广播id
     */
    public void deletePillowTalk(String pillowTalkId) {
        V view = getView();
        if (view == null) return;
        if (!CommonUtil.isNetworkConnected(view.getContext())) {
            view.toast(R.string.net_not_ok);
            return;
        }
        Cross cross = model.deletePillowTalk(view.getContext(), pillowTalkId);
        add(cross);
        view.showDialog(view.getContext().getString(R.string.please_wait));
        addSubscribe(OkHttpUtil.executePostCall(cross), new OperationSubscriber(this, view) {
                    @Override
                    public void onSuccess(Object extra) {
                        V view = getView();
                        if (view == null) return;
                        view.toast(R.string.delete_success);
                        view.setResult(RESULT_OK, new Intent().putExtra(BACK_REASON, DELETE));
                        view.finish();
                    }
                }
        );
    }

    /**
     * 收藏悄悄话
     *
     * @param pillowTalkId 悄悄话或世界广播id
     */
    public void collect(String pillowTalkId) {
        V view = getView();
        if (view == null) return;
        if (!CommonUtil.isNetworkConnected(view.getContext())) {
            view.toast(R.string.net_not_ok);
            return;
        }
        Cross cross = model.collect(view.getContext(), pillowTalkId);
        add(cross);
        view.showDialog(view.getContext().getString(R.string.please_wait));
        addSubscribe(OkHttpUtil.executePostCall(cross), new ResponseSubscriber<String>(this, view) {
                    @Override
                    public void onSuccess(String result, Object extra) {
                        V view = getView();
                        if (view == null) return;
                        view.afterCollect(result);
                        view.toast(R.string.collect_success);
                    }
                }
        );
    }

    /**
     * 取消收藏悄悄话
     *
     * @param pillowTalkId 悄悄话或世界广播id
     */
    public void cancelCollect(String pillowTalkId) {
        V view = getView();
        if (view == null) return;
        if (!CommonUtil.isNetworkConnected(view.getContext())) {
            view.toast(R.string.net_not_ok);
            return;
        }
        Cross cross = model.cancelCollect(view.getContext(), pillowTalkId);
        add(cross);
        view.showDialog(view.getContext().getString(R.string.please_wait));
        addSubscribe(OkHttpUtil.executePostCall(cross), new OperationSubscriber(this, view) {
                    @Override
                    public void onSuccess(Object extra) {
                        V view = getView();
                        if (view == null) return;
                        view.afterCancelCollect();
                        view.toast(R.string.cancel_collect_success);
                        view.setResult(RESULT_OK, new Intent().putExtra(BACK_REASON, CANCEL_COLLECT));
                    }
                }
        );
    }

    /**
     * 赞悄悄话
     *
     * @param pillowTalkId 悄悄话或世界广播id
     */
    public void praise(String pillowTalkId) {
        V view = getView();
        if (view == null) return;
        if (!CommonUtil.isNetworkConnected(view.getContext())) {
            view.toast(R.string.net_not_ok);
            return;
        }
        Cross cross = model.praise(view.getContext(), pillowTalkId);
        add(cross);
        view.showDialog(view.getContext().getString(R.string.please_wait));
        addSubscribe(OkHttpUtil.executePostCall(cross), new ResponseSubscriber<String>(this, view) {
                    @Override
                    public void onSuccess(String result, Object extra) {
                        V view = getView();
                        if (view == null) return;
                        view.toast(R.string.praise_success);
                        view.afterPraise(result);
                    }
                }
        );
    }

    /**
     * 取消赞悄悄话
     *
     * @param pillowTalkId 悄悄话或世界广播id
     */
    public void cancelPraise(String pillowTalkId) {
        V view = getView();
        if (view == null) return;
        if (!CommonUtil.isNetworkConnected(view.getContext())) {
            view.toast(R.string.net_not_ok);
            return;
        }
        Cross cross = model.cancelPraise(view.getContext(), pillowTalkId);
        add(cross);
        view.showDialog(view.getContext().getString(R.string.please_wait));
        addSubscribe(OkHttpUtil.executePostCall(cross), new OperationSubscriber(this, view) {
                    @Override
                    public void onSuccess(Object extra) {
                        V view = getView();
                        if (view == null) return;
                        view.toast(R.string.cancel_praise_success);
                        view.afterCancelPraise();
                        view.setResult(RESULT_OK, new Intent().putExtra(BACK_REASON, CANCEL_PRAISE));
                    }
                }
        );
    }
}
