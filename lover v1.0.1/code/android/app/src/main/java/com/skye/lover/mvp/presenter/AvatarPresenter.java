package com.skye.lover.mvp.presenter;

import android.text.TextUtils;

import com.skye.lover.R;
import com.skye.lover.model.Cross;
import com.skye.lover.model.FilePaths;
import com.skye.lover.model.OperationSubscriber;
import com.skye.lover.model.ResponseSubscriber;
import com.skye.lover.model.User;
import com.skye.lover.mvp.model.impl.AvatarModelImpl;
import com.skye.lover.mvp.model.AvatarModel;
import com.skye.lover.mvp.view.AvatarView;
import com.skye.lover.mvp.view.BaseView;
import com.skye.lover.util.CommonUtil;
import com.skye.lover.util.OkHttpUtil;
import com.skye.lover.util.ShareDataUtil;

import java.io.File;

/**
 * 头像主导器
 */
public class AvatarPresenter extends BasePresenter<AvatarView> {
    private AvatarModel model;

    @Override
    public void attachView(BaseView view) {
        super.attachView(view);
        model = new AvatarModelImpl();
    }

    @Override
    public void detachView() {
        model = null;
        super.detachView();
    }

    /**
     * 上传头像文件
     *
     * @param file 头像文件
     */
    public void uploadAvatarFile(File file) {
        AvatarView view = getView();
        if (view == null) return;
        if (!CommonUtil.isNetworkConnected(view.getContext())) {
            view.toast(R.string.net_not_ok);
            return;
        }
        Cross cross = model.uploadAvatarFile(file);
        add(cross);
        view.showDialog(view.getContext().getString(R.string.please_wait));
        addSubscribe(OkHttpUtil.executePostCall(cross), new ResponseSubscriber<FilePaths>(this, view) {
            @Override
            public void onSuccess(FilePaths result, Object extra) {
                if (result == null || TextUtils.isEmpty(result.getFilePaths())) return;
                AvatarView view = getView();
                if (view == null) return;
                updateAvatar(result.getFilePaths());
            }
        });
    }

    /**
     * 更新用户头像
     *
     * @param avatar 头像url
     */
    private void updateAvatar(String avatar) {
        AvatarView view = getView();
        if (view == null) return;
        if (!CommonUtil.isNetworkConnected(view.getContext())) {
            view.toast(R.string.net_not_ok);
            return;
        }
        Cross cross = model.updateAvatar(view.getContext(), avatar);
        add(cross);
//        view.showDialog(view.getContext().getString(R.string.please_wait));
        addSubscribe(OkHttpUtil.executePostCall(cross), new OperationSubscriber(this, view) {
            @Override
            public void onSuccess(Object extra) {
                AvatarView view = getView();
                if (view == null) return;
                ShareDataUtil.set(view.getContext(), User.AVATAR, (String) extra);
                view.updateAvatar();
            }
        });
    }
}
