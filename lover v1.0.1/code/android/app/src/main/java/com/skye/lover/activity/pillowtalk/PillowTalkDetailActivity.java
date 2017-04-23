package com.skye.lover.activity.pillowtalk;

import android.content.Intent;
import android.content.res.Resources;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;

import com.jakewharton.rxbinding.view.RxView;
import com.skye.actionsheet.ActionSheet;
import com.skye.lover.R;
import com.skye.lover.activity.pillowtalk.comment.CommentListActivity;
import com.skye.lover.adapter.FollowPillowTalkAdapter;
import com.skye.lover.listener.OnClickAvatarListener;
import com.skye.lover.model.FollowPillowTalk;
import com.skye.lover.model.Paging;
import com.skye.lover.model.PillowTalk;
import com.skye.lover.model.Request;
import com.skye.lover.model.ScrollPosition;
import com.skye.lover.model.User;
import com.skye.lover.mvp.model.PillowTalkDetailModel;
import com.skye.lover.mvp.presenter.PillowTalkDetailPresenter;
import com.skye.lover.mvp.view.PillowTalkDetailView;
import com.skye.lover.util.CommonUtil;
import com.skye.lover.util.ShareDataUtil;
import com.skye.lover.util.URLConfig;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;

/**
 * 悄悄话详情界面
 */
public class PillowTalkDetailActivity extends BasePillowTalkDetailActivity<PillowTalkDetailView, PillowTalkDetailModel, PillowTalkDetailPresenter> implements PillowTalkDetailView, AdapterView.OnItemLongClickListener {
    private final int REPLY = 0;//回复
    private final int LOOK_COMMENTS = 1;//看评论
    @BindView(R.id.tv_reply)
    View tvReply;//回复
    private FollowPillowTalkAdapter adapter;//跟随悄悄话列表适配器

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_pillow_talk_detail;
    }

    @Override
    public PillowTalkDetailPresenter createPresenter() {
        return new PillowTalkDetailPresenter();
    }

    @Override
    public int getDescription() {
        return R.string.pillow_talk_detail_activity;
    }

    @Override
    public void initViews() {
        recycler.setOnItemLongClickListener(this);
        adapter = new FollowPillowTalkAdapter(context);
        recycler.setAdapter(adapter);

        //更多
        RxView.clicks(topbar.right).throttleFirst(1, TimeUnit.SECONDS).subscribe((v) -> {
            if (pt == null) return;
            List<String> items = new ArrayList<>();
            final List<Request> list = new ArrayList<>();
            String userId = ShareDataUtil.get(context, User.ID);
            Request request;
            boolean canOpen = false;
            if (pt.isOpen()) canOpen = false;//已经公开
            else if (userId.equals(pt.getPublisherId()) && pt.getPublisherOpen() != PillowTalk.OPEN)//发表者未公开
                canOpen = true;
            else if (userId.equals(pt.getAnotherId()) && pt.getAnotherOpen() != PillowTalk.OPEN)//另一方未公开
                canOpen = true;
            if (canOpen) {
                items.add(getString(R.string.open));
                request = new Request();
                request.action = URLConfig.ACTION_OPEN_PILLOW_TALK;
                list.add(request);
            }
            //删除
            if (userId.equals(pt.getPublisherId())) {//发表者可以删除
                items.add(getString(R.string.delete));
                request = new Request();
                request.action = URLConfig.ACTION_DELETE_PILLOW_TALK;
                list.add(request);
            }
            //收藏
            if (pt.isCollected()) {//已经收藏，应该显示“取消收藏”
                items.add(getString(R.string.cancel_collect));
                request = new Request();
                request.action = URLConfig.ACTION_CANCEL_COLLECT;
                list.add(request);
            } else {//没有收藏过，应该显示“收藏”
                items.add(getString(R.string.collect));
                request = new Request();
                request.action = URLConfig.ACTION_COLLET;
                list.add(request);
            }
            //赞
            if (pt.isPraised()) {//已经赞过，应该显示“取消赞”
                items.add(getString(R.string.cancel_praise));
                request = new Request();
                request.action = URLConfig.ACTION_CANCEL_PRAISE;
                list.add(request);
            } else {//没有赞过，应该显示“赞”
                items.add(getString(R.string.praise));
                request = new Request();
                request.action = URLConfig.ACTION_PRAISE;
                list.add(request);
            }
            //举报
            items.add(getString(R.string.report));
            request = new Request();
            request.intent = new Intent(context, ReportPillowTalkActivity.class);
            request.intent.putExtra(ReportPillowTalkActivity.PILLOW_TALK_ID, pt.getId());
            list.add(request);
            setTheme(R.style.ActionSheetStyleIOS7);
            ActionSheet menuView = new ActionSheet(context);
            menuView.setCancelButtonTitle(getString(R.string.cancel));
            menuView.addItems((String[]) items.toArray(new String[items.size()]));
            menuView.setItemClickListener((int itemPosition) -> {
                Request req = list.get(itemPosition);
                if (req.intent == null) parseAction(req.action);//网络请求
                else startActivity(req.intent);//页面跳转
            });
            menuView.setCancelableOnTouchMenuOutside(true);
            menuView.showMenu();
        });
        //回复
        RxView.clicks(findViewById(R.id.tv_reply)).throttleFirst(1, TimeUnit.SECONDS).subscribe((v) -> {
            if (pt == null) return;
            startActivityForResult(new Intent(context, ReplyActivity.class)
                    .putExtra(ReplyActivity.PILLOW_TALK_ID, pt.getId()), REPLY);
        });
        //看评论
        RxView.clicks(findViewById(R.id.tv_look_comments)).throttleFirst(1, TimeUnit.SECONDS).subscribe((v) -> {
            if (pt == null) return;
            startActivityForResult(new Intent(context, CommentListActivity.class)
                    .putExtra(CommentListActivity.PILLOW_TALK_ID, pt.getId()), LOOK_COMMENTS);
        });
        //页数
        RxView.clicks(findViewById(R.id.tv_pages)).throttleFirst(1, TimeUnit.SECONDS)
                .subscribe((v) -> {
                    List<String> items = new ArrayList<>();
                    final List<Paging> ls = new ArrayList<>();
                    //首页、上一页
                    if (currentPage > 1) {
                        items.add(getString(R.string.first_page));
                        ls.add(new Paging(1, ScrollPosition.FIRST_ITEM));
                        items.add(getString(R.string.previous_page));
                        ls.add(new Paging(currentPage - 1, ScrollPosition.BOTTOM));
                    }
                    //下一页、末页
                    if (currentPage < pageCount) {
                        items.add(getString(R.string.next_page));
                        ls.add(new Paging(currentPage + 1, ScrollPosition.FIRST_ITEM));
                        items.add(getString(R.string.last_page));
                        ls.add(new Paging(pageCount, ScrollPosition.BOTTOM));
                    }
                    setTheme(R.style.ActionSheetStyleIOS7);
                    ActionSheet menuView = new ActionSheet(context);
                    menuView.setCancelButtonTitle(getString(R.string.cancel));
                    menuView.addItems((String[]) items.toArray(new String[items.size()]));
                    menuView.setItemClickListener((int itemPosition) -> {
                        if (itemPosition < 0 || itemPosition >= ls.size()) return;
                        Paging paging = ls.get(itemPosition);
                        getFollows(paging.page, paging.position);
                    });
                    menuView.setCancelableOnTouchMenuOutside(true);
                    menuView.showMenu();
                });
    }

    @Override
    public void setBody() {
        Resources res = getResources();
        header.avatarOne.setImageURI(URLConfig.SERVER_HOST + pt.getPublisherAvatar());
        header.avatarOne.getHierarchy().setFailureImage(CommonUtil.getFailureImage(pt.getPublisherGender()));
        header.avatarOne.setOnClickListener(new OnClickAvatarListener(context, pt.getPublisherId(), pt.getPublisherNickname()));
        header.avatarAnother.setImageURI(URLConfig.SERVER_HOST + pt.getAnotherAvatar());
        header.avatarAnother.getHierarchy().setFailureImage(CommonUtil.getFailureImage(pt.getAnotherGender()));
        header.avatarAnother.setOnClickListener(new OnClickAvatarListener(context, pt.getAnotherId(), pt.getAnotherNickname()));
        header.nickname.setText(pt.getPublisherNickname());
        header.nickname.setTextColor(res.getColor(CommonUtil.getTextColor(pt.getPublisherGender())));
        header.nicknameAnother.setText(pt.getAnotherNickname());
        header.nicknameAnother.setTextColor(res.getColor(CommonUtil.getTextColor(pt.getAnotherGender())));
        StringBuilder properties = new StringBuilder();
        properties.append(pt.getCreateTime());
        if (pt.isOpen()) {
            properties.append(" 赞:");
            properties.append(pt.getPraiseCount());
            properties.append(" 评论:");
            properties.append(pt.getCommentCount());
        } else {
            properties.append(" ");
            properties.append(getString(R.string.unopened));
        }
        header.properties.setText(properties);
        if (!TextUtils.isEmpty(pt.getContent())) {
            header.content.setText(pt.getContent());
            header.content.setVisibility(View.VISIBLE);
        } else header.content.setVisibility(View.GONE);
        if (!TextUtils.isEmpty(pt.getImgs())) {
            String imgs[] = pt.getImgs().split(",");
            if (imgs.length > 0) {
                List<String> pictures = new ArrayList<>();
                pictures.addAll(Arrays.asList(imgs));
                header.picturesContainer.reset();
                header.picturesContainer.setPictures(pictures);
                header.picturesContainer.setOnItemClickListener(this);
                header.picturesContainer.setVisibility(View.VISIBLE);
            } else header.picturesContainer.setVisibility(View.GONE);
        } else header.picturesContainer.setVisibility(View.GONE);
        header.splitline.setVisibility(View.VISIBLE);

        String userId = ShareDataUtil.get(context, User.ID);
        //是悄悄话发表者或另一方，则显示“回复”
        tvReply.setVisibility(userId.equals(pt.getPublisherId()) || userId.equals(pt.getAnotherId()) ? View.VISIBLE : View.GONE);
    }

    @Override
    public boolean parseAction(String action) {
        boolean flag = false;//是否已经处理
        switch (action) {
            case URLConfig.ACTION_OPEN_PILLOW_TALK://公开悄悄话
                presenter.open(pt.getId());
                flag = true;
                break;
            default:
                break;
        }
        return flag || super.parseAction(action);
    }

    @Override
    public void getListFirstPageData() {
        getFollows(1, ScrollPosition.HEAD);
    }

    @Override
    public void updateProperties() {
        if (pt == null) return;
        StringBuilder properties = new StringBuilder();
        properties.append(pt.getCreateTime());
        if (pt.isOpen()) {
            properties.append(" 赞:");
            properties.append(pt.getPraiseCount());
            properties.append(" 评论:");
            properties.append(pt.getCommentCount());
        } else {
            properties.append(" ");
            properties.append(getString(R.string.unopened));
        }
        header.properties.setText(properties);
    }

    /**
     * 获取跟随悄悄话列表
     *
     * @param page     请求页数
     * @param position 请求回数据后的滚动位置
     */
    private void getFollows(int page, ScrollPosition position) {
        presenter.follows(pt.getId(), page, position);
    }

    @Override
    public void afterFollows(List<FollowPillowTalk> list, int pageCount, int requestPage, ScrollPosition position) {
        currentPage = requestPage;
        this.pageCount = pageCount;
        adapter.setList(list);
        tvPages.setText(currentPage + "/" + pageCount + "页");
        tvPages.setVisibility(pageCount > 1 ? View.VISIBLE : View.GONE);
        recycler.post(() -> {
            if (ScrollPosition.HEAD == position) recycler.smoothScrollToPosition(0, true);
            else if (ScrollPosition.FIRST_ITEM == position)
                recycler.smoothScrollToPosition(1, true);
            else if (ScrollPosition.BOTTOM == position)
                recycler.smoothScrollToPosition(adapter.getItemCount(), true);
        });
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        final FollowPillowTalk fpt = (FollowPillowTalk) adapter.get(position);
        if (fpt == null) return false;
        if (!ShareDataUtil.get(context, User.ID).equals(fpt.getPublisher()))
            return false;//登录用户不是发表者，不能删除
        setTheme(R.style.ActionSheetStyleIOS7);
        ActionSheet menuView = new ActionSheet(context);
        menuView.setCancelButtonTitle(getString(R.string.cancel));
        menuView.addItems(getString(R.string.delete));
        menuView.setItemClickListener((int itemPosition) -> presenter.deleteFollowPillowTalk(fpt));
        menuView.setCancelableOnTouchMenuOutside(true);
        menuView.showMenu();
        return true;
    }

    @Override
    public void afterDeleteFollowPillowTalk(FollowPillowTalk fpt) {
        toast(R.string.delete_success);
        adapter.remove(fpt);
    }

    @Override
    public void afterOpen() {
        if (ShareDataUtil.get(context, User.ID).equals(pt.getPublisherId()))//悄悄话发表者
            pt.setPublisherOpen(1);
        else pt.setAnotherOpen(1);//相爱关系中的另一方
        if (pt.isOpen()) {
            CommonUtil.toast(context, R.string.open_success);
            StringBuilder properties = new StringBuilder();
            properties.append(pt.getCreateTime());
            properties.append(" 赞:");
            properties.append(pt.getPraiseCount());
            properties.append(" 评论:");
            properties.append(pt.getCommentCount());
            header.properties.setText(properties);
            setResult(RESULT_OK, new Intent().putExtra(BACK_REASON, OPEN));
        } else toast(R.string.wait_another_open);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        switch (requestCode) {
            case REPLY://回复
                getFollows(1, ScrollPosition.FIRST_ITEM);
                break;
            case LOOK_COMMENTS://看评论
                getProperties();
                break;
            default:
                break;
        }
    }
}
