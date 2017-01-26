package com.skye.lover.activity.pillowtalk;

import android.content.Intent;
import android.content.res.Resources;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.skye.actionsheet.ActionSheet;
import com.skye.lover.R;
import com.skye.lover.activity.pillowtalk.comment.PublishCommentActivity;
import com.skye.lover.adapter.CommentAdapter;
import com.skye.lover.exception.NoNetworkConnectException;
import com.skye.lover.listener.OnClickAvatarListener;
import com.skye.lover.model.BaseResponse;
import com.skye.lover.model.Comment;
import com.skye.lover.model.ListResponse;
import com.skye.lover.util.CommonUtil;
import com.skye.lover.util.OkHttpUtil;
import com.skye.lover.util.ShareData;
import com.skye.lover.util.URLConfig;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.OnClick;
import butterknife.OnItemLongClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 世界广播界面
 */
public class BroadcastDetailActivity extends BasePillowTalkDetailActivity {
    private final int COMMENT = 0;//评论
    private CommentAdapter adapter;//评论列表适配器

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_broadcast_detail;
    }

    @Override
    public void initViews() {
        adapter = new CommentAdapter(context);
        listView.setAdapter(adapter);
    }

    @Override
    public void setBody() {
        Resources res = getResources();
        ImageLoader.getInstance().displayImage(pt.getPublisherAvatar(), header.avatarOne, CommonUtil.getDisplayOptions(pt.getPublisherGender()));
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
        Map<String, String> params = new HashMap<>();
        params.put("pillowTalkId", pt.getId());
        params.put("page", page + "");
        dialog = CommonUtil.showLoadingDialog(context, R.string.loading);
        OkHttpUtil.doPost(context, URLConfig.ACTION_COMMENTS, params, new CallbackComments(page, position));
    }

    @OnItemLongClick(R.id.listview)
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        final Comment comment = (Comment) parent.getItemAtPosition(position);
        if (comment == null) return false;
        if (!ShareData.getShareStringData(ShareData.ID).equals(comment.getCommenter()))
            return false;//登录用户不是发表者，不能删除
        setTheme(R.style.ActionSheetStyleIOS7);
        ActionSheet menuView = new ActionSheet(context);
        menuView.setCancelButtonTitle(getString(R.string.cancel));
        menuView.addItems(getString(R.string.delete));
        menuView.setItemClickListener(new ActionSheet.MenuItemClickListener() {
            @Override
            public void onItemClick(int itemPosition) {
                Map<String, String> params = new HashMap<>();
                params.put("userId", ShareData.getShareStringData(ShareData.ID));
                params.put("commentId", comment.getId());
                dialog = CommonUtil.showLoadingDialog(context, R.string.please_wait);
                OkHttpUtil.doPost(context, URLConfig.ACTION_DELETE_COMMENT, params, new CallbackDeleteComment(comment));
            }
        });
        menuView.setCancelableOnTouchMenuOutside(true);
        menuView.showMenu();
        return true;
    }

    @OnClick({R.id.tv_right, R.id.tv_comment, R.id.tv_pages})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_right://更多
                if (pt == null) return;
                List<String> items = new ArrayList<>();
                final List<Request> list = new ArrayList<>();
                String userId = ShareData.getShareStringData(ShareData.ID);
                Request request;
                //删除
                if (userId.equals(pt.getPublisherId())) {//发表者可以删除
                    items.add(getString(R.string.delete));
                    request = new Request();
                    request.action = URLConfig.ACTION_DELETE_PILLOW_TALK;
                    request.params = new HashMap<>();
                    request.params.put("userId", userId);
                    request.params.put("pillowTalkId", pt.getId());
                    request.callback = callbackDeletePillowTalk;
                    list.add(request);
                }
                //收藏
                if (pt.isCollected()) {//已经收藏，应该显示“取消收藏”
                    items.add(getString(R.string.cancel_collect));
                    request = new Request();
                    request.action = URLConfig.ACTION_CANCEL_COLLET;
                    request.params = new HashMap<>();
                    request.params.put("userId", userId);
                    request.params.put("pillowTalkId", pt.getId());
                    request.callback = callbackCancelCollect;
                    list.add(request);
                } else {//没有收藏过，应该显示“收藏”
                    items.add(getString(R.string.collect));
                    request = new Request();
                    request.action = URLConfig.ACTION_COLLET;
                    request.params = new HashMap<>();
                    request.params.put("userId", userId);
                    request.params.put("pillowTalkId", pt.getId());
                    request.callback = callbackCollect;
                    list.add(request);
                }
                //赞
                if (pt.isPraised()) {//已经赞过，应该显示“取消赞”
                    items.add(getString(R.string.cancel_praise));
                    request = new Request();
                    request.action = URLConfig.ACTION_CANCEL_PRAISE;
                    request.params = new HashMap<>();
                    request.params.put("userId", userId);
                    request.params.put("pillowTalkId", pt.getId());
                    request.callback = callbackCancelPraise;
                    list.add(request);
                } else {//没有赞过，应该显示“赞”
                    items.add(getString(R.string.praise));
                    request = new Request();
                    request.action = URLConfig.ACTION_PRAISE;
                    request.params = new HashMap<>();
                    request.params.put("userId", userId);
                    request.params.put("pillowTalkId", pt.getId());
                    request.callback = callbackPraise;
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
                menuView.setItemClickListener(new ActionSheet.MenuItemClickListener() {
                    @Override
                    public void onItemClick(int itemPosition) {
                        Request request = list.get(itemPosition);
                        if (request.intent == null) {//网络请求
                            dialog = CommonUtil.showLoadingDialog(context, R.string.please_wait);
                            OkHttpUtil.doPost(context, request.action, request.params, request.callback);
                        } else startActivity(request.intent);//页面跳转
                    }
                });
                menuView.setCancelableOnTouchMenuOutside(true);
                menuView.showMenu();
                break;
            case R.id.tv_comment://评论
                if (pt == null) return;
                startActivityForResult(new Intent(context, PublishCommentActivity.class)
                        .putExtra(PublishCommentActivity.PILLOW_TALK_ID, pt.getId()), COMMENT);
                break;
            case R.id.tv_pages://页数
                items = new ArrayList<>();
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
                menuView = new ActionSheet(context);
                menuView.setCancelButtonTitle(getString(R.string.cancel));
                menuView.addItems((String[]) items.toArray(new String[items.size()]));
                menuView.setItemClickListener(new ActionSheet.MenuItemClickListener() {
                    @Override
                    public void onItemClick(int itemPosition) {
                        if (itemPosition < 0 || itemPosition >= ls.size()) return;
                        Paging paging = ls.get(itemPosition);
                        getComments(paging.page, paging.position);
                    }
                });
                menuView.setCancelableOnTouchMenuOutside(true);
                menuView.showMenu();
                break;
            default:
                break;
        }
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

    /**
     * 删除评论接口回调
     */
    private class CallbackDeleteComment implements Callback {
        private Comment comment;

        CallbackDeleteComment(Comment comment) {
            this.comment = comment;
        }

        @Override
        public void onFailure(Call call, final IOException e) {
            e.printStackTrace();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    CommonUtil.closeLoadingDialog(dialog);
                    if (!(e instanceof NoNetworkConnectException)) //不是没有网络
                        CommonUtil.toast(context, R.string.bad_request);
                }
            });
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            final String body = response.body().string();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    CommonUtil.closeLoadingDialog(dialog);
                    try {
                        CommonUtil.log(body);
                        BaseResponse br = CommonUtil.parseToObject(body, BaseResponse.class);//转化为返回基类
                        if (br.check()) {
                            CommonUtil.toast(context, R.string.delete_success);
                            adapter.remove(comment);
                            getProperties();
                        } else
                            CommonUtil.toast(context, br.message);
                    } catch (Exception e) {
                        e.printStackTrace();
                        CommonUtil.toast(context, R.string.bad_request);
                    }
                }
            });
        }
    }

    /**
     * 世界广播评论列表接口回调
     */
    private class CallbackComments implements Callback {
        private int requestPage;
        private ScrollPosition position;

        CallbackComments(int requestPage, ScrollPosition position) {
            this.requestPage = requestPage;
            this.position = position;
        }

        @Override
        public void onFailure(Call call, final IOException e) {
            e.printStackTrace();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    CommonUtil.closeLoadingDialog(dialog);
                    if (!(e instanceof NoNetworkConnectException)) //不是没有网络
                        CommonUtil.toast(context, R.string.bad_request);
                }
            });
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            final String body = response.body().string();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    CommonUtil.closeLoadingDialog(dialog);
                    try {
                        CommonUtil.log(body);
                        BaseResponse br = CommonUtil.parseToObject(body, BaseResponse.class);//转化为返回基类
                        if (br.check()) {
                            Data data = CommonUtil.parseToObject(br.result, Data.class);
                            currentPage = requestPage;
                            pageCount = data.pageCount;
                            adapter.clear();
                            adapter.add(data.list);
                            tvPages.setText(currentPage + "/" + pageCount + "页");
                            tvPages.setVisibility(pageCount > 1 ? View.VISIBLE : View.GONE);
                            listView.post(new Runnable() {
                                @Override
                                public void run() {
                                    if (ScrollPosition.HEAD == position) listView.setSelection(0);
                                    else if (ScrollPosition.FIRST_ITEM == position)
                                        listView.setSelection(1);
                                    else if (ScrollPosition.BOTTOM == position)
                                        listView.setSelection(listView.getCount());
                                }
                            });
                        } else
                            CommonUtil.toast(context, br.message);
                    } catch (Exception e) {
                        e.printStackTrace();
                        CommonUtil.toast(context, R.string.bad_request);
                    }
                }
            });
        }
    }

    private class Data extends ListResponse {
        public List<Comment> list;
    }
}
