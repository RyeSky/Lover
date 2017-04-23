package com.skye.lover.mvp.presenter;

import com.skye.lover.R;
import com.skye.lover.model.Cross;
import com.skye.lover.model.OperationSubscriber;
import com.skye.lover.model.PrivateMessageWrapper;
import com.skye.lover.model.ResponseSubscriber;
import com.skye.lover.mvp.model.impl.PrivateMessageModelImpl;
import com.skye.lover.mvp.model.PrivateMessageModel;
import com.skye.lover.mvp.view.BaseView;
import com.skye.lover.mvp.view.PrivateMessageView;
import com.skye.lover.util.CommonUtil;
import com.skye.lover.util.OkHttpUtil;

/**
 * 私信聊天主导器
 */
public class PrivateMessagePresenter extends BasePresenter<PrivateMessageView> {
    private PrivateMessageModel model;

    @Override
    public void attachView(BaseView view) {
        super.attachView(view);
        model = new PrivateMessageModelImpl();
    }

    @Override
    public void detachView() {
        model = null;
        super.detachView();
    }

    /**
     * 刷新数据
     *
     * @param another          私信聊天中的另一方id
     * @param privateMessageId 当前界面显示的最早一条私信的id
     */
    public void refresh(String another, String privateMessageId) {
        PrivateMessageView view = getView();
        if (view == null) return;
        Cross cross = model.refresh(view.getContext(), another, privateMessageId);
        add(cross);
        if (cross.isNetworkConnected)
            view.showDialog(view.getContext().getString(R.string.loading));
        addSubscribe(OkHttpUtil.executePostCall(cross), new ResponseSubscriber<PrivateMessageWrapper>(this, view) {
            @Override
            public void onSuccess(PrivateMessageWrapper result, Object extra) {
                PrivateMessageView view = getView();
                if (view == null) return;
                view.refreshData(result);
            }

            @Override
            public void onCompleted() {
                super.onCompleted();
                PrivateMessageView view = getView();
                if (view == null) return;
                view.hideRefresh();
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                PrivateMessageView view = getView();
                if (view == null) return;
                view.hideRefresh();
            }
        });
    }

    /**
     * 根据私信记录id和聊天者id删除私信
     *
     * @param privateMessageId 要被删除的私信记录id
     * @param position         要被删除的私信记录所在位置
     */
    public void deletePrivateMessage(String privateMessageId, int position) {
        PrivateMessageView view = getView();
        if (view == null) return;
        if (!CommonUtil.isNetworkConnected(view.getContext())) {
            view.toast(R.string.net_not_ok);
            return;
        }
        Cross cross = model.deletePrivateMessage(view.getContext(), privateMessageId, position);
        add(cross);
        view.showDialog(view.getContext().getString(R.string.please_wait));
        addSubscribe(OkHttpUtil.executePostCall(cross), new OperationSubscriber(this, view) {
                    @Override
                    public void onSuccess(Object extra) {
                        PrivateMessageView view = getView();
                        if (view == null) return;
                        int position = (Integer) extra;
                        view.toast(R.string.delete_success);
                        view.deleteSuccess(position);
                    }
                }
        );
    }
}
