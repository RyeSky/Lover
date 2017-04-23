package com.skye.lover.mvp.presenter;

import android.app.Activity;
import android.text.TextUtils;

import com.skye.lover.R;
import com.skye.lover.model.Cross;
import com.skye.lover.model.FilePaths;
import com.skye.lover.model.OperationSubscriber;
import com.skye.lover.model.ResponseSubscriber;
import com.skye.lover.mvp.model.impl.PublishPillowTalkModelImpl;
import com.skye.lover.mvp.model.PublishPillowTalkModel;
import com.skye.lover.mvp.view.BaseView;
import com.skye.lover.util.CommonUtil;
import com.skye.lover.util.OkHttpUtil;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 发表悄悄话主导器
 */
public class PublishPillowTalkPresenter extends BasePresenter<BaseView> {
    private PublishPillowTalkModel model;

    @Override
    public void attachView(BaseView view) {
        super.attachView(view);
        model = new PublishPillowTalkModelImpl();
    }

    @Override
    public void detachView() {
        model = null;
        super.detachView();
    }

    /**
     * 发表悄悄话
     *
     * @param type    类型【0:悄悄话；1:广播】
     * @param content 悄悄话内容
     * @param files   悄悄话中附带的图片对象集合
     * @return 穿越
     */
    public void publishPillowTalk(String type, String content, List<File> files) {
        BaseView view = getView();
        if (view == null) return;
        if (!CommonUtil.isNetworkConnected(view.getContext())) {
            view.toast(R.string.net_not_ok);
            return;
        }
        Cross cross = model.uploadFiles(files);
        Map<String, String> extra = new HashMap<>(2);
        extra.put("type", type);
        extra.put("content", content);
        cross.setExtra(extra);
        add(cross);
        view.showDialog(view.getContext().getString(R.string.please_wait));
        addSubscribe(OkHttpUtil.executePostCall(cross), new ResponseSubscriber<FilePaths>(this, view) {
            @Override
            public void onSuccess(FilePaths result, Object extra) {
                if (result == null || TextUtils.isEmpty(result.getFilePaths())) return;
                BaseView view = getView();
                if (view == null) return;
                @SuppressWarnings("unchecked")
                Map<String, String> extras = (Map<String, String>) extra;
                publishPillowTalk(extras == null ? "" : extras.get("type"), extras == null ? "" : extras.get("content")
                        , result.getFilePaths(), false);
            }
        });
    }

    /**
     * 发表悄悄话
     *
     * @param type    类型【0:悄悄话；1:广播】
     * @param content 悄悄话内容
     * @return 穿越
     */
    public void publishPillowTalk(String type, String content) {
        publishPillowTalk(type, content, "", true);
    }

    /**
     * 发表悄悄话
     *
     * @param type             类型【0:悄悄话；1:广播】
     * @param content          悄悄话内容
     * @param imgs             悄悄话中附带的图片url
     * @param isNeedShowDialog 是否需要显示加载框
     * @return 穿越
     */
    private void publishPillowTalk(String type, String content, String imgs, boolean isNeedShowDialog) {
        BaseView view = getView();
        if (view == null) return;
        if (!CommonUtil.isNetworkConnected(view.getContext())) {
            view.toast(R.string.net_not_ok);
            return;
        }
        Cross cross = model.publishPillowTalk(view.getContext(), type, content, imgs);
        add(cross);
        if (isNeedShowDialog) view.showDialog(view.getContext().getString(R.string.please_wait));
        addSubscribe(OkHttpUtil.executePostCall(cross), new OperationSubscriber(this, view) {
            @Override
            public void onSuccess(Object extra) {
                BaseView view = getView();
                if (view == null) return;
                view.toast(R.string.publish_success);
                view.setResult(Activity.RESULT_OK);
                view.finish();
            }
        });
    }
}
