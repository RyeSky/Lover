package com.skye.lover.mvp.presenter;

import com.skye.lover.R;
import com.skye.lover.model.Cross;
import com.skye.lover.model.OperationSubscriber;
import com.skye.lover.model.User;
import com.skye.lover.mvp.model.impl.UpdatePasswordModelImpl;
import com.skye.lover.mvp.model.UpdatePasswordModel;
import com.skye.lover.mvp.view.BaseView;
import com.skye.lover.util.CommonUtil;
import com.skye.lover.util.OkHttpUtil;
import com.skye.lover.util.ShareDataUtil;

/**
 * 更新密码主导器
 */
public class UpdatePasswordPresenter extends BasePresenter<BaseView> {
    private UpdatePasswordModel model;

    @Override
    public void attachView(BaseView view) {
        super.attachView(view);
        model = new UpdatePasswordModelImpl();
    }

    @Override
    public void detachView() {
        model = null;
        super.detachView();
    }

    /**
     * 更新密码
     *
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     */
    public void updatePassword(String oldPassword, String newPassword) {
        BaseView view = getView();
        if (view == null) return;
        if (!CommonUtil.isNetworkConnected(view.getContext())) {
            view.toast(R.string.net_not_ok);
            return;
        }
        Cross cross = model.updatePassword(view.getContext(), oldPassword, newPassword);
        add(cross);
        view.showDialog(view.getContext().getString(R.string.please_wait));
        addSubscribe(OkHttpUtil.executePostCall(cross), new OperationSubscriber(this, view) {
            @Override
            public void onSuccess(Object extra) {
                BaseView view = getView();
                if (view == null) return;
                ShareDataUtil.set(view.getContext(), User.PASSWORD, (String) extra);
                view.toast(R.string.update_success);
                view.finish();
            }
        });
    }
}
