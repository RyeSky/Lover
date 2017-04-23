package com.skye.lover.mvp.presenter;

import android.text.TextUtils;

import com.skye.lover.R;
import com.skye.lover.model.Cross;
import com.skye.lover.model.ResponseSubscriber;
import com.skye.lover.model.User;
import com.skye.lover.mvp.model.impl.LoginModelImpl;
import com.skye.lover.mvp.model.LoginModel;
import com.skye.lover.mvp.view.BaseView;
import com.skye.lover.mvp.view.LoginView;
import com.skye.lover.util.CommonUtil;
import com.skye.lover.util.OkHttpUtil;

/**
 * 登录主导器
 */
public class LoginPresenter extends BasePresenter<LoginView> {
    private LoginModel model;

    @Override
    public void attachView(BaseView view) {
        super.attachView(view);
        model = new LoginModelImpl();
    }

    @Override
    public void detachView() {
        model = null;
        super.detachView();
    }

    /**
     * 登录
     *
     * @param name     用户名
     * @param password 密码
     */
    public void login(String name, String password) {
        LoginView view = getView();
        if (view == null) return;
        if (!CommonUtil.isNetworkConnected(view.getContext())) {
            view.toast(R.string.net_not_ok);
            return;
        }
        Cross cross = model.login(name, password);
        add(cross);
        view.showDialog(view.getContext().getString(R.string.logining));
        addSubscribe(OkHttpUtil.executePostCall(cross), new ResponseSubscriber<User>(this, view) {
                    @Override
                    public void onSuccess(User result, Object extra) {
                        LoginView view = getView();
                        if (view == null) return;
                        if (result != null) {
                            User.save(view.getContext(), result);//保存用户信息
                            view.toast(R.string.login_success);
                            view.jumpToMainActivity(TextUtils.isEmpty(result.getAnother()));
                            view.finish();
                        } else view.toast(R.string.login_fail);
                    }
                }
        );
    }
}
