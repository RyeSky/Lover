package com.skye.lover.mvp.presenter;

import com.skye.lover.R;
import com.skye.lover.model.Cross;
import com.skye.lover.model.OperationSubscriber;
import com.skye.lover.model.User;
import com.skye.lover.mvp.model.impl.UpdateNicknameModelImpl;
import com.skye.lover.mvp.model.UpdateNicknameModel;
import com.skye.lover.mvp.view.BaseView;
import com.skye.lover.util.CommonUtil;
import com.skye.lover.util.OkHttpUtil;
import com.skye.lover.util.ShareDataUtil;

/**
 * 更新昵称主导器
 */
public class UpdateNicknamePresenter extends BasePresenter<BaseView> {
    private UpdateNicknameModel model;

    @Override
    public void attachView(BaseView view) {
        super.attachView(view);
        model = new UpdateNicknameModelImpl();
    }

    @Override
    public void detachView() {
        model = null;
        super.detachView();
    }

    /**
     * 更新昵称
     *
     * @param nickname 新昵称
     */
    public void updateNickname(String nickname) {
        BaseView view = getView();
        if (view == null) return;
        if (!CommonUtil.isNetworkConnected(view.getContext())) {
            view.toast(R.string.net_not_ok);
            return;
        }
        Cross cross = model.updateNickname(view.getContext(), nickname);
        add(cross);
        view.showDialog(view.getContext().getString(R.string.please_wait));
        addSubscribe(OkHttpUtil.executePostCall(cross), new OperationSubscriber(this, view) {
            @Override
            public void onSuccess(Object extra) {
                BaseView view = getView();
                if (view == null) return;
                ShareDataUtil.set(view.getContext(), User.NICKNAME, (String) extra);
                view.toast(R.string.update_success);
                view.finish();
            }
        });
    }
}
