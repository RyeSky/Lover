package com.skye.lover.mvp.presenter;

import com.skye.lover.R;
import com.skye.lover.model.Cross;
import com.skye.lover.model.OperationSubscriber;
import com.skye.lover.model.ResponseSubscriber;
import com.skye.lover.model.User;
import com.skye.lover.mvp.model.OtherInfoModel;
import com.skye.lover.mvp.model.impl.OtherInfoModelImpl;
import com.skye.lover.mvp.view.BaseView;
import com.skye.lover.mvp.view.OtherInfoView;
import com.skye.lover.util.CommonUtil;
import com.skye.lover.util.OkHttpUtil;

/**
 * 其他用户信息主导器
 */
public class OtherInfoPresenter extends BasePresenter<OtherInfoView> {
    private OtherInfoModel model;

    @Override
    public void attachView(BaseView view) {
        super.attachView(view);
        model = new OtherInfoModelImpl();
    }

    @Override
    public void detachView() {
        model = null;
        super.detachView();
    }

    /**
     * 其他用户信息
     *
     * @param userId 用户id
     */
    public void otherInfo(String userId) {
        OtherInfoView view = getView();
        if (view == null) return;
        if (!CommonUtil.isNetworkConnected(view.getContext())) {
            view.toast(R.string.net_not_ok);
            return;
        }
        Cross cross = model.otherInfo(userId);
        add(cross);
        view.showDialog(view.getContext().getString(R.string.loading));
        addSubscribe(OkHttpUtil.executePostCall(cross), new ResponseSubscriber<User>(this, view) {
                    @Override
                    public void onSuccess(User result, Object extra) {
                        OtherInfoView view = getView();
                        if (view == null) return;
                        view.afterOtherInfo(result);
                    }
                }
        );
    }

    /**
     * 分手
     *
     * @param another 用户id
     */
    public void breakUp(String another) {
        OtherInfoView view = getView();
        if (view == null) return;
        if (!CommonUtil.isNetworkConnected(view.getContext())) {
            view.toast(R.string.net_not_ok);
            return;
        }
        Cross cross = model.breakUp(view.getContext(), another);
        add(cross);
        view.showDialog(view.getContext().getString(R.string.please_wait));
        addSubscribe(OkHttpUtil.executePostCall(cross), new OperationSubscriber(this, view) {
                    @Override
                    public void onSuccess(Object extra) {
                        OtherInfoView view = getView();
                        if (view == null) return;
                        view.afterBreakUp();
                    }
                }
        );
    }

    /**
     * 坠入爱河
     *
     * @param another 用户id
     */
    public void fallInLove(String another) {
        OtherInfoView view = getView();
        if (view == null) return;
        if (!CommonUtil.isNetworkConnected(view.getContext())) {
            view.toast(R.string.net_not_ok);
            return;
        }
        Cross cross = model.fallInLove(view.getContext(), another);
        add(cross);
        view.showDialog(view.getContext().getString(R.string.please_wait));
        addSubscribe(OkHttpUtil.executePostCall(cross), new ResponseSubscriber<User>(this, view) {
                    @Override
                    public void onSuccess(User result, Object extra) {
                        OtherInfoView view = getView();
                        if (view == null) return;
                        view.afterFallInLove(result);
                    }
                }
        );
    }
}
