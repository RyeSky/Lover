package com.skye.lover.mvp.presenter;

import android.text.TextUtils;

import com.skye.lover.R;
import com.skye.lover.model.Cross;
import com.skye.lover.model.OperationSubscriber;
import com.skye.lover.model.User;
import com.skye.lover.mvp.model.impl.UpdateGenderModelImpl;
import com.skye.lover.mvp.model.UpdateGenderModel;
import com.skye.lover.mvp.view.BaseView;
import com.skye.lover.util.CommonUtil;
import com.skye.lover.util.OkHttpUtil;
import com.skye.lover.util.ShareDataUtil;

/**
 * 更新性别主导器
 */
public class UpdateGenderPresenter extends BasePresenter<BaseView> {
    private UpdateGenderModel model;

    @Override
    public void attachView(BaseView view) {
        super.attachView(view);
        model = new UpdateGenderModelImpl();
    }

    @Override
    public void detachView() {
        model = null;
        super.detachView();
    }

    /**
     * 更新性别
     *
     * @param gender 新性别
     */
    public void updateGender(String gender) {
        BaseView view = getView();
        if (view == null) return;
        if (!CommonUtil.isNetworkConnected(view.getContext())) {
            view.toast(R.string.net_not_ok);
            return;
        }
        Cross cross = model.updateGender(view.getContext(), gender);
        add(cross);
        view.showDialog(view.getContext().getString(R.string.please_wait));
        addSubscribe(OkHttpUtil.executePostCall(cross), new OperationSubscriber(this, view) {
            @Override
            public void onSuccess(Object extra) {
                BaseView view = getView();
                if (view == null) return;
                String gender = (String) extra;
                ShareDataUtil.set(view.getContext(), User.GENDER, TextUtils.isEmpty(gender) ? User.GENDER_SECRET + "" : gender);
                view.toast(R.string.update_success);
                view.finish();
            }
        });
    }
}
