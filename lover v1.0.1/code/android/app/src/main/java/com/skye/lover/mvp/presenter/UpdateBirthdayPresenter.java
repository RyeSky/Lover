package com.skye.lover.mvp.presenter;

import com.skye.lover.R;
import com.skye.lover.model.Cross;
import com.skye.lover.model.OperationSubscriber;
import com.skye.lover.model.User;
import com.skye.lover.mvp.model.impl.UpdateBirthdayModelImpl;
import com.skye.lover.mvp.model.UpdateBirthdayModel;
import com.skye.lover.mvp.view.BaseView;
import com.skye.lover.util.CommonUtil;
import com.skye.lover.util.OkHttpUtil;
import com.skye.lover.util.ShareDataUtil;

/**
 * 更新生日主导器
 */
public class UpdateBirthdayPresenter extends BasePresenter<BaseView> {
    private UpdateBirthdayModel model;

    @Override
    public void attachView(BaseView view) {
        super.attachView(view);
        model = new UpdateBirthdayModelImpl();
    }

    @Override
    public void detachView() {
        model = null;
        super.detachView();
    }

    /**
     * 更新更新生日
     *
     * @param birthday 新生日
     */
    public void updateBirthday(String birthday) {
        BaseView view = getView();
        if (view == null) return;
        if (!CommonUtil.isNetworkConnected(view.getContext())) {
            view.toast(R.string.net_not_ok);
            return;
        }
        Cross cross = model.updateBirthday(view.getContext(), birthday);
        add(cross);
        view.showDialog(view.getContext().getString(R.string.please_wait));
        addSubscribe(OkHttpUtil.executePostCall(cross), new OperationSubscriber(this, view) {
            @Override
            public void onSuccess(Object extra) {
                BaseView view = getView();
                if (view == null) return;
                ShareDataUtil.set(view.getContext(), User.BIRTHDAY, (String) extra);
                view.toast(R.string.update_success);
                view.finish();
            }
        });
    }
}
