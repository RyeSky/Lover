package com.skye.lover.mvp.presenter;

import com.skye.lover.R;
import com.skye.lover.model.Cross;
import com.skye.lover.model.OperationSubscriber;
import com.skye.lover.mvp.model.impl.FeedbackModelImpl;
import com.skye.lover.mvp.model.FeedbackModel;
import com.skye.lover.mvp.view.BaseView;
import com.skye.lover.util.CommonUtil;
import com.skye.lover.util.OkHttpUtil;

/**
 * 意见反馈主导器
 */
public class FeedbackPresenter extends BasePresenter<BaseView> {
    FeedbackModel model;

    @Override
    public void attachView(BaseView view) {
        super.attachView(view);
        model = new FeedbackModelImpl();
    }

    @Override
    public void detachView() {
        model = null;
        super.detachView();
    }

    /**
     * 意见反馈
     *
     * @param content 反馈内容
     */
    public void submitFeedback(String content) {
        BaseView view = getView();
        if (view == null) return;
        if (!CommonUtil.isNetworkConnected(view.getContext())) {
            view.toast(R.string.net_not_ok);
            return;
        }
        Cross cross = model.submitFeedback(view.getContext(), content);
        add(cross);
        view.showDialog(view.getContext().getString(R.string.please_wait));
        addSubscribe(OkHttpUtil.executePostCall(cross), new OperationSubscriber(this, view) {
            @Override
            public void onSuccess(Object extra) {
                BaseView view = getView();
                if (view == null) return;
                view.toast(R.string.submit_success);
                view.finish();
            }
        });
    }
}
