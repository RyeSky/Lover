package com.skye.lover.mvp.presenter;

import com.skye.lover.R;
import com.skye.lover.model.Cross;
import com.skye.lover.model.OperationSubscriber;
import com.skye.lover.mvp.model.impl.ReportPillowTalkModelImpl;
import com.skye.lover.mvp.model.ReportPillowTalkModel;
import com.skye.lover.mvp.view.BaseView;
import com.skye.lover.util.CommonUtil;
import com.skye.lover.util.OkHttpUtil;

/**
 * 举报悄悄话主导器
 */
public class ReportPillowTalkPresenter extends BasePresenter<BaseView> {
    private ReportPillowTalkModel model;

    @Override
    public void attachView(BaseView view) {
        super.attachView(view);
        model = new ReportPillowTalkModelImpl();
    }

    @Override
    public void detachView() {
        model = null;
        super.detachView();
    }

    /**
     * 举报悄悄话
     *
     * @param pillowTalkId 要举报的悄悄话id
     * @param content      举报内容
     */
    public void reportPillowTalk(String pillowTalkId, String content) {
        BaseView view = getView();
        if (view == null) return;
        if (!CommonUtil.isNetworkConnected(view.getContext())) {
            view.toast(R.string.net_not_ok);
            return;
        }
        Cross cross = model.reportPillowTalk(view.getContext(), pillowTalkId, content);
        add(cross);
        view.showDialog(view.getContext().getString(R.string.please_wait));
        addSubscribe(OkHttpUtil.executePostCall(cross), new OperationSubscriber(this, view) {
            @Override
            public void onSuccess(Object extra) {
                BaseView view = getView();
                if (view == null) return;
                view.toast(R.string.report_success);
                view.finish();
            }
        });
    }
}
