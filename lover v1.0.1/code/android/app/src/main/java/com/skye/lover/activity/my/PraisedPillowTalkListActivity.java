package com.skye.lover.activity.my;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.skye.actionsheet.ActionSheet;
import com.skye.lover.R;
import com.skye.lover.activity.pillowtalk.BasePillowTalkDetailActivity;
import com.skye.lover.model.PillowTalk;
import com.skye.lover.mvp.model.PraisedPillowTalkListModel;
import com.skye.lover.mvp.model.impl.PraisedPillowTalkListModelImpl;
import com.skye.lover.mvp.presenter.PraisedPillowTalkListPresenter;
import com.skye.lover.mvp.view.PraisedPillowTalkListView;

import butterknife.OnItemLongClick;

/**
 * 用户赞过的悄悄话列表界面
 */
public class PraisedPillowTalkListActivity extends BaseMyOperatedPillowTalkListActivity<PraisedPillowTalkListView, PraisedPillowTalkListModel, PraisedPillowTalkListPresenter>
        implements PraisedPillowTalkListView {

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_praised_pillow_talk_list;
    }

    @Override
    public PraisedPillowTalkListPresenter createPresenter() {
        return new PraisedPillowTalkListPresenter(new PraisedPillowTalkListModelImpl());
    }

    @Override
    public int getDescription() {
        return R.string.praised_pillow_talk_list_activity;
    }

    @Override
    public int getEmptyTip() {
        return R.string.empty_praise;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        topbar.title.setText(R.string.praise_list);
    }

    @OnItemLongClick(R.id.listview)
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        final PillowTalk pt = (PillowTalk) parent.getItemAtPosition(position);
        if (pt == null) return false;
        setTheme(R.style.ActionSheetStyleIOS7);
        ActionSheet menuView = new ActionSheet(context);
        menuView.setCancelButtonTitle("取消");// before add items
        menuView.addItems(getString(R.string.cancel_praise));
        menuView.setItemClickListener((int itemPosition) -> {
            switch (itemPosition) {
                case 0:
                    presenter.cancelPraise(pt.getId(), position);
                    break;
                default:
                    break;
            }
        });
        menuView.setCancelableOnTouchMenuOutside(true);
        menuView.showMenu();
        return true;
    }

    @Override
    public void afterCancelPraise(int position) {
        adapter.remove(position);
        if (adapter.isEmpty())
            empty.setText(R.string.empty_praise);
        toast(R.string.cancel_praise_success);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        switch (requestCode) {
            case PILLOW_TALK_ITEM:
                if (data == null) return;
                String backReason = data.getStringExtra(BasePillowTalkDetailActivity.BACK_REASON);
                if (BasePillowTalkDetailActivity.OPEN.equals(backReason) || BasePillowTalkDetailActivity.DELETE.equals(backReason) ||
                        BasePillowTalkDetailActivity.CANCEL_PRAISE.equals(backReason))
                    onRefresh();
                break;
            default:
                break;
        }
    }
}
