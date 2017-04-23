package com.skye.lover.mvp.presenter;

import com.skye.lover.R;
import com.skye.lover.model.AppVersion;
import com.skye.lover.model.Cross;
import com.skye.lover.model.ResponseSubscriber;
import com.skye.lover.mvp.model.SettingModel;
import com.skye.lover.mvp.model.impl.SettingModelImpl;
import com.skye.lover.mvp.view.BaseView;
import com.skye.lover.mvp.view.SettingView;
import com.skye.lover.util.CommonUtil;
import com.skye.lover.util.OkHttpUtil;

/**
 * 设置主导器
 */
public class SettingPresenter extends BasePresenter<SettingView> {
    private SettingModel model;

    @Override
    public void attachView(BaseView view) {
        super.attachView(view);
        model = new SettingModelImpl();
    }

    @Override
    public void detachView() {
        model = null;
        super.detachView();
    }

    /**
     * 版本更新
     */
    public void lastAppVersion() {
        SettingView view = getView();
        if (view == null) return;
        if (!CommonUtil.isNetworkConnected(view.getContext())) {
            view.toast(R.string.net_not_ok);
            return;
        }
        Cross cross = model.lastAppVersion();
        add(cross);
        view.showDialog(view.getContext().getString(R.string.please_wait));
        addSubscribe(OkHttpUtil.executePostCall(cross), new ResponseSubscriber<AppVersion>(this, view) {
                    @Override
                    public void onSuccess(AppVersion result, Object extra) {
                        SettingView view = getView();
                        if (view == null) return;
                        if (result != null) view.afterLastAppVersion(result);
                    }
                }
        );
    }
}
