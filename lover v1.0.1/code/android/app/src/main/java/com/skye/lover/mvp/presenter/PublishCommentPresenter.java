package com.skye.lover.mvp.presenter;

import android.app.Activity;

import com.skye.lover.R;
import com.skye.lover.model.Cross;
import com.skye.lover.model.OperationSubscriber;
import com.skye.lover.mvp.model.PublishCommentModel;
import com.skye.lover.mvp.model.impl.PublishCommentModelImpl;
import com.skye.lover.mvp.view.BaseView;
import com.skye.lover.util.CommonUtil;
import com.skye.lover.util.OkHttpUtil;

/**
 * 发表评论主导器
 */
public class PublishCommentPresenter extends BasePresenter<BaseView> {
    private PublishCommentModel model;

    @Override
    public void attachView(BaseView view) {
        super.attachView(view);
        model = new PublishCommentModelImpl();
    }

    @Override
    public void detachView() {
        model = null;
        super.detachView();
    }


    /**
     * 发表评论
     *
     * @param pillowTalkId 被评论的悄悄话或世界广播id
     * @param content      评论内容
     */
    public void publishComment(String pillowTalkId, String content) {
        BaseView view = getView();
        if (view == null) return;
        if (!CommonUtil.isNetworkConnected(view.getContext())) {
            view.toast(R.string.net_not_ok);
            return;
        }
        Cross cross = model.publishComment(view.getContext(), pillowTalkId, content);
        add(cross);
        view.showDialog(view.getContext().getString(R.string.please_wait));
        addSubscribe(OkHttpUtil.executePostCall(cross), new OperationSubscriber(this, view) {
            @Override
            public void onSuccess(Object extra) {
                BaseView view = getView();
                if (view == null) return;
                view.toast(R.string.comment_success);
                view.setResult(Activity.RESULT_OK);
                view.finish();
            }
        });
    }
}
