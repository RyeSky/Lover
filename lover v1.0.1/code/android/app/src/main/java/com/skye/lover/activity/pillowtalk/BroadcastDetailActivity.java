package com.skye.lover.activity.pillowtalk;

import android.content.Intent;
import android.content.res.Resources;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;

import com.jakewharton.rxbinding.view.RxView;
import com.skye.actionsheet.ActionSheet;
import com.skye.lover.R;
import com.skye.lover.activity.pillowtalk.comment.PublishCommentActivity;
import com.skye.lover.adapter.RcyclerCommentAdapter;
import com.skye.lover.listener.OnClickAvatarListener;
import com.skye.lover.model.Comment;
import com.skye.lover.model.Paging;
import com.skye.lover.model.Request;
import com.skye.lover.model.ScrollPosition;
import com.skye.lover.model.User;
import com.skye.lover.mvp.model.BroadcastDetailModel;
import com.skye.lover.mvp.presenter.BroadcastDetailPresenter;
import com.skye.lover.mvp.view.BroadcastDetailView;
import com.skye.lover.util.CommonUtil;
import com.skye.lover.util.ShareDataUtil;
import com.skye.lover.util.URLConfig;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 世界广播详情界面
 */
public class BroadcastDetailActivity extends BasePillowTalkDetailActivity<BroadcastDetailView, BroadcastDetailModel, BroadcastDetailPresenter> implements BroadcastDetailView, AdapterView.OnItemLongClickListener {
    private final int COMMENT = 0;//评论
    private RcyclerCommentAdapter adapter;//评论列表适配器

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_broadcast_detail;
    }

    @Override
    public BroadcastDetailPresenter createPresenter() {
        return new BroadcastDetailPresenter();
    }

    @Override
    public int getDescription() {
        return R.string.broadcast_detail_activity;
    }

    @Override
    public void initViews() {
        recycler.setOnItemLongClickListener(this);
        adapter = new RcyclerCommentAdapter(context);
        recycler.setAdapter(adapter);

        //更多
        RxView.clicks(topbar.right).throttleFirst(1, TimeUnit.SECONDS).subscribe((v) -> {
            if (pt == null) return;
            List<String> items = new ArrayList<>();
            final List<Request> list = new ArrayList<>();
            String userId = ShareDataUtil.get(context, User.ID);
            Request request;
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
        //评论
        RxView.clicks(findViewById(R.id.tv_comment)).throttleFirst(1, TimeUnit.SECONDS).subscribe((v) -> {
            if (pt == null) return;
            startActivityForResult(new Intent(context, PublishCommentActivity.class)
                    .putExtra(PublishCommentActivity.PILLOW_TALK_ID, pt.getId()), COMMENT);
        });
        //页数
        RxView.clicks(findViewById(R.id.tv_pages)).throttleFirst(1, TimeUnit.SECONDS).subscribe((v) -> {
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
                getComments(paging.page, paging.position);
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
        header.nickname.setText(pt.getPublisherNickname());
        header.nickname.setTextColor(res.getColor(CommonUtil.getTextColor(pt.getPublisherGender())));
        StringBuilder properties = new StringBuilder();
        properties.append(pt.getCreateTime());
        properties.append(" 赞:");
        properties.append(pt.getPraiseCount());
        properties.append(" 评论:");
        properties.append(pt.getCommentCount());
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
    }

    @Override
    public void getListFirstPageData() {
        getComments(1, ScrollPosition.HEAD);
    }

    @Override
    public void updateProperties() {
        if (pt == null) return;
        StringBuilder properties = new StringBuilder();
        properties.append(pt.getCreateTime());
        properties.append(" 赞:");
        properties.append(pt.getPraiseCount());
        properties.append(" 评论:");
        properties.append(pt.getCommentCount());
        header.properties.setText(properties);
    }

    /**
     * 获取世界广播评论列表
     *
     * @param page     请求页数
     * @param position 请求回数据后的滚动位置
     */
    private void getComments(int page, ScrollPosition position) {
        presenter.comments(pt.getId(), page, position);
    }

    @Override
    public void afterComments(List<Comment> list, int pageCount, int requestPage, ScrollPosition position) {
        currentPage = requestPage;
        this.pageCount = pageCount;
        adapter.clear();
        adapter.add(list);
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
        final Comment comment = (Comment) adapter.get(position);
        if (comment == null) return false;
        if (!ShareDataUtil.get(context, User.ID).equals(comment.getCommenter()))
            return false;//登录用户不是发表者，不能删除
        setTheme(R.style.ActionSheetStyleIOS7);
        ActionSheet menuView = new ActionSheet(context);
        menuView.setCancelButtonTitle(getString(R.string.cancel));
        menuView.addItems(getString(R.string.delete));
        menuView.setItemClickListener((int itemPosition) -> presenter.deleteComment(comment));
        menuView.setCancelableOnTouchMenuOutside(true);
        menuView.showMenu();
        return true;
    }

    @Override
    public void afterDeleteComment(Comment comment) {
        toast(R.string.delete_success);
        adapter.remove(comment);
        getProperties();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        switch (requestCode) {
            case COMMENT://评论
                getProperties();
                getComments(1, ScrollPosition.FIRST_ITEM);
                break;
            default:
                break;
        }
    }
}
