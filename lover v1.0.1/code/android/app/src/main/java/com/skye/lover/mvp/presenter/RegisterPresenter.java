package com.skye.lover.mvp.presenter;

import com.skye.lover.R;
import com.skye.lover.model.Cross;
import com.skye.lover.model.OperationSubscriber;
import com.skye.lover.mvp.model.impl.RegisterModelImpl;
import com.skye.lover.mvp.model.RegisterModel;
import com.skye.lover.mvp.view.BaseView;
import com.skye.lover.util.CommonUtil;
import com.skye.lover.util.OkHttpUtil;

/**
 * 注册主导器
 */
public class RegisterPresenter extends BasePresenter<BaseView> {
    private RegisterModel model;

    @Override
    public void attachView(BaseView view) {
        super.attachView(view);
        model = new RegisterModelImpl();
    }

    @Override
    public void detachView() {
        model = null;
        super.detachView();
    }


    /**
     * 注册
     *
     * @param name     用户名
     * @param password 密码
     * @param gender   性别【0：保密；1：男；2：女】
     */
    public void register(String name, String password, String gender) {
        BaseView view = getView();
        if (view == null) return;
        if (!CommonUtil.isNetworkConnected(view.getContext())) {
            view.toast(R.string.net_not_ok);
            return;
        }
        Cross cross = model.register(name, password, gender);
        add(cross);
        view.showDialog(view.getContext().getString(R.string.please_wait));
        addSubscribe(OkHttpUtil.executePostCall(cross), new OperationSubscriber(this, view) {
            @Override
            public void onSuccess(Object extra) {
                BaseView view = getView();
                if (view == null) return;
                view.toast(R.string.register_success);
                view.finish();
            }
        });
    }
}
