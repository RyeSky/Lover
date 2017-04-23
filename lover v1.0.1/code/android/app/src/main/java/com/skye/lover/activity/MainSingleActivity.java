package com.skye.lover.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.widget.RadioButton;

import com.skye.lover.R;
import com.skye.lover.activity.base.BaseActivity;
import com.skye.lover.fragment.ChatFragment;
import com.skye.lover.fragment.FindFragment;
import com.skye.lover.fragment.MyFragment;
import com.skye.lover.model.AppVersion;
import com.skye.lover.mvp.presenter.MainPresenter;
import com.skye.lover.mvp.view.MainView;
import com.skye.lover.util.CommonUtil;
import com.skye.lover.view.FlowRadioGroup;

import java.util.List;

import butterknife.BindView;

/**
 * 单身者主界面
 */
public class MainSingleActivity extends BaseActivity<MainView, MainPresenter> implements MainView, FlowRadioGroup.OnCheckedChangeListener {
    public static final int FIND_PILLOW_TALK_ITEM = 0;//发现
    public static final int PUBLISH_BROADCAST = 1;//发表广播
    @BindView(R.id.rg)
    FlowRadioGroup rg;// 底部导航栏
    // 双击退出
    long exitTime = 0L;
    private Fragment current, fragments[];
    private FindFragment ff;//发现碎片

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_main_single;
    }

    @Override
    public MainPresenter createPresenter() {
        return new MainPresenter();
    }

    @Override
    public int getDescription() {
        return R.string.main_single_activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtil.updateJpushTag();
        //移除之前的碎片
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        List<Fragment> ls = fm.getFragments();
        if (ls != null && !ls.isEmpty()) {
            for (Fragment item : ls) {
                transaction.remove(item);
            }
        }
        //新建碎片并添加默认
        fragments = new Fragment[3];
        ff = new FindFragment();
        fragments[0] = ff;
        fragments[1] = new ChatFragment();
        fragments[2] = new MyFragment();
        transaction.add(R.id.content_frame, fragments[0]).commit();
        current = fragments[0];
        //默认选中发现
        rg.setOnCheckedChangeListener(this);
        ((RadioButton) findViewById(R.id.rb_find)).setChecked(true);
        presenter.lastAppVersion();
    }

    @Override
    public void afterLastAppVersion(AppVersion version) {
        CommonUtil.foundNewAppVersion(this, version);
    }

    @Override
    public void onCheckedChanged(FlowRadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_find://发现
                switchFragment(0);
                break;
            case R.id.rb_chat://聊聊
                switchFragment(1);
                break;
            case R.id.rb_my://我的
                switchFragment(2);
                break;
            default:
                break;
        }
    }

    /**
     * 切换Fragment
     *
     * @param index 碎片数组下标
     */
    private void switchFragment(int index) {
        try {
            if (current != fragments[index]) {
                if (!fragments[index].isAdded()) {// 添加
                    getSupportFragmentManager().beginTransaction().hide(current)
                            .add(R.id.content_frame, fragments[index]).commit();
                } else {// 隐藏显示
                    getSupportFragmentManager().beginTransaction().hide(current).show(fragments[index])
                            .commit();
                }
                current = fragments[index];
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PUBLISH_BROADCAST://发表广播
                    ff.onRefresh();
                    break;
                case FIND_PILLOW_TALK_ITEM://发现
                    if (data == null) return;
//                    String backReason = data.getStringExtra(BasePillowTalkDetailActivity.BACK_REASON);
//                    if (BasePillowTalkDetailActivity.DELETE.equals(backReason) ||
//                            BasePillowTalkDetailActivity.CANCEL_PRAISE.equals(backReason))
//                        ff.onRefresh();
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            // 在两秒钟内,连续点击了2次退出程序
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                CommonUtil.toast(context, R.string.tip_again_to_exit);
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                CommonUtil.exit();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
