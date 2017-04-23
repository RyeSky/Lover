package com.skye.lover.mvp.presenter;

import com.skye.lover.R;
import com.skye.lover.model.Cross;
import com.skye.lover.model.OperationSubscriber;
import com.skye.lover.model.PrivateMessageSession;
import com.skye.lover.model.ResponseSubscriber;
import com.skye.lover.mvp.model.impl.ChatModelImpl;
import com.skye.lover.mvp.model.ChatModel;
import com.skye.lover.mvp.view.BaseView;
import com.skye.lover.mvp.view.ChatView;
import com.skye.lover.util.CommonUtil;
import com.skye.lover.util.OkHttpUtil;

import java.util.List;

/**
 * 聊聊主导器
 */
public class ChatPresenter extends BasePresenter<ChatView> {
    private ChatModel model;//模型

    @Override
    public void attachView(BaseView view) {
        super.attachView(view);
        model = new ChatModelImpl();
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
        ChatView view = getView();
        if (view == null) return;
        Cross cross = model.refresh(view.getContext());
        add(cross);
        if (cross.isNetworkConnected)
            view.showDialog(view.getContext().getString(R.string.loading));
        addSubscribe(OkHttpUtil.executePostCall(cross), new ResponseSubscriber<List<PrivateMessageSession>>(this, view) {
            @Override
            public void onSuccess(List<PrivateMessageSession> result, Object extra) {
                ChatView view = getView();
                if (view == null) return;
                view.refreshData(result);
            }

            @Override
            public void onCompleted() {
                super.onCompleted();
                ChatView view = getView();
                if (view == null) return;
                view.hideRefresh();
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                ChatView view = getView();
                if (view == null) return;
                view.hideRefresh();
            }
        });
    }

    /**
     * 根据聊天双方删除聊天会话
     *
     * @param another  聊天中的另一方
     * @param position item position
     */
    public void deleteByPrivateMessageSession(String another, int position) {
        ChatView view = getView();
        if (view == null) return;
        if (!CommonUtil.isNetworkConnected(view.getContext())) {
            view.toast(R.string.net_not_ok);
            return;
        }
        Cross cross = model.deleteByPrivateMessageSession(view.getContext(), another, position);
        add(cross);
        view.showDialog(view.getContext().getString(R.string.please_wait));
        addSubscribe(OkHttpUtil.executePostCall(cross), new OperationSubscriber(this, view) {
            @Override
            public void onSuccess(Object extra) {
                ChatView view = getView();
                if (view == null) return;
                view.deleteByPrivateMessageSession((int) extra);
            }
        });
    }
}
