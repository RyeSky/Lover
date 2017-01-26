package com.skye.lover.activity.my.setting;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.skye.lover.ConstantUtil;
import com.skye.lover.R;
import com.skye.lover.activity.base.BaseActivity;
import com.skye.lover.activity.user.LoginActivity;
import com.skye.lover.util.CommonUtil;
import com.skye.lover.util.ShareData;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 设置界面
 */
public class SettingActivity extends BaseActivity {
    @BindView(R.id.tv_clear_cache)
    TextView clearCache;//清理缓存

    @Override
    public void handleMsg(Message msg) {
        super.handleMsg(msg);
        switch (msg.what) {
            case 0://成功清理缓存
                CommonUtil.closeLoadingDialog(dialog);
                CommonUtil.toast(context, R.string.clear_cache_success);
                clearCache.setText(CommonUtil.getAutoFileOrFilesSize(ConstantUtil.BASE_PATH));
                break;
            default:
                break;
        }
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_setting;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTopbar();
        topbar.title.setText(R.string.setting);
        topbar.left.setVisibility(View.VISIBLE);
        clearCache.setText(CommonUtil.getAutoFileOrFilesSize(ConstantUtil.BASE_PATH));
    }

    @OnClick({R.id.ll_clear_cache, R.id.ll_feedback, R.id.ll_about, R.id.tv_exit})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_clear_cache://清理缓存
                dialog = CommonUtil.showLoadingDialog(context, R.string.please_wait);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        CommonUtil.RecursionDeleteFile(new File(ConstantUtil.BASE_PATH));
                        handler.sendEmptyMessage(0);
                    }
                }).start();
                break;
            case R.id.ll_feedback://意见反馈
                startActivity(new Intent(context, FeedbackActivity.class));
                break;
            case R.id.ll_about://关于
                break;
            case R.id.tv_exit://退出登录
                ShareData.clear();
                CommonUtil.toast(context, R.string.exit_success);
                CommonUtil.updateJpushTag();
                //跳到登录界面
                Intent intent = new Intent(context, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
