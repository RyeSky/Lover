package com.skye.lover.mvp.presenter;

import android.app.Activity;

import com.skye.lover.R;
import com.skye.lover.model.Cross;
import com.skye.lover.model.OperationSubscriber;
import com.skye.lover.mvp.model.impl.ReplyModelImpl;
import com.skye.lover.mvp.model.ReplyModel;
import com.skye.lover.mvp.view.BaseView;
import com.skye.lover.util.CommonUtil;
import com.skye.lover.util.OkHttpUtil;

/**
 * 回复悄悄话主导器
 */
public class ReplyPresenter extends BasePresenter<BaseView> {
    private ReplyModel model;

    @Override
    public void attachView(BaseView view) {
        super.attachView(view);
        model = new ReplyModelImpl();
    }

    @Override
    public void detachView() {
        model = null;
        super.detachView();
    }

    /**
     * 回复悄悄话
     *
     * @param pillowTalkId 要回复的悄悄话id
     * @param content      回复内容
     */
    public void reply(String pillowTalkId, String content) {
        BaseView view = getView();
        if (view == null) return;
        if (!CommonUtil.isNetworkConnected(view.getContext())) {
            view.toast(R.string.net_not_ok);
            return;
        }
        Cross cross = model.reply(view.getContext(), pillowTalkId, content);
        add(cross);
        view.showDialog(view.getContext().getString(R.string.please_wait));
        addSubscribe(OkHttpUtil.executePostCall(cross), new OperationSubscriber(this, view) {
            @Override
            public void onSuccess(Object extra) {
                BaseView view = getView();
                if (view == null) return;
                view.toast(R.string.reply_success);
                view.setResult(Activity.RESULT_OK);
                view.finish();
            }
        });
    }
}
