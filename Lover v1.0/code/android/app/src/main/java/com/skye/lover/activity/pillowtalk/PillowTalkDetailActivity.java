package com.skye.lover.activity.pillowtalk;

import android.content.Intent;
import android.content.res.Resources;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.skye.actionsheet.ActionSheet;
import com.skye.lover.R;
import com.skye.lover.activity.pillowtalk.comment.CommentListActivity;
import com.skye.lover.adapter.FollowPillowTalkAdapter;
import com.skye.lover.exception.NoNetworkConnectException;
import com.skye.lover.listener.OnClickAvatarListener;
import com.skye.lover.model.BaseResponse;
import com.skye.lover.model.FollowPillowTalk;
import com.skye.lover.model.ListResponse;
import com.skye.lover.model.PillowTalk;
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

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnItemLongClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 悄悄话详情界面
 */
public class PillowTalkDetailActivity extends BasePillowTalkDetailActivity {
    private final int REPLY = 0;//回复
    private final int LOOK_COMMENTS = 1;//看评论
    @BindView(R.id.tv_reply)
    View tvReply;//回复
    private FollowPillowTalkAdapter adapter;//跟随悄悄话列表适配器

    /**
     * 公开悄悄话接口回调
     */
    private Callback callbackOpenPillowTalk = new Callback() {
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
                            if (ShareData.getShareStringData(ShareData.ID).equals(pt.getPublisherId()))//悄悄话发表者
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
                            } else CommonUtil.toast(context, R.string.wait_another_open);
                        } else
                            CommonUtil.toast(context, br.message);
                    } catch (Exception e) {
                        e.printStackTrace();
                        CommonUtil.toast(context, R.string.bad_request);
                    }
                }
            });
        }
    };

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_pillow_talk_detail;
    }

    @Override
    public void initViews() {
        adapter = new FollowPillowTalkAdapter(context);
        listView.setAdapter(adapter);
    }

    @Override
    public void setBody() {
        Resources res = getResources();
        ImageLoader.getInstance().displayImage(pt.getPublisherAvatar(), header.avatarOne, CommonUtil.getDisplayOptions(pt.getPublisherGender()));
        header.avatarOne.setOnClickListener(new OnClickAvatarListener(context, pt.getPublisherId(), pt.getPublisherNickname()));
        ImageLoader.getInstance().displayImage(pt.getAnotherAvatar(), header.avatarAnother, CommonUtil.getDisplayOptions(pt.getAnotherGender()));
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

        String userId = ShareData.getShareStringData(ShareData.ID);
        //是悄悄话发表者或另一方，则显示“回复”
        tvReply.setVisibility(userId.equals(pt.getPublisherId()) || userId.equals(pt.getAnotherId()) ? View.VISIBLE : View.GONE);
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
        Map<String, String> params = new HashMap<>();
        params.put("pillowTalkId", pt.getId());
        params.put("page", page + "");
        dialog = CommonUtil.showLoadingDialog(context, R.string.loading);
        OkHttpUtil.doPost(context, URLConfig.ACTION_FOLLOWS, params, new CallbackFollows(page, position));
    }

    @OnItemLongClick(R.id.listview)
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        final FollowPillowTalk fpt = (FollowPillowTalk) parent.getItemAtPosition(position);
        if (fpt == null) return false;
        if (!ShareData.getShareStringData(ShareData.ID).equals(fpt.getPublisher()))
            return false;//登录用户不是发表者，不能删除
        setTheme(R.style.ActionSheetStyleIOS7);
        ActionSheet menuView = new ActionSheet(context);
        menuView.setCancelButtonTitle(getString(R.string.cancel));
        menuView.addItems(getString(R.string.delete));
        menuView.setItemClickListener(new ActionSheet.MenuItemClickListener() {
            @Override
            public void onItemClick(int itemPosition) {
                Map<String, String> params = new HashMap<>();
                params.put("followPillowTalkId", fpt.getId());
                params.put("userId", ShareData.getShareStringData(ShareData.ID));
                dialog = CommonUtil.showLoadingDialog(context, R.string.please_wait);
                OkHttpUtil.doPost(context, URLConfig.ACTION_DELETE_FOLLOW_PILLOW_TALK, params, new CallbackDeleteFollowPillowTalk(fpt));
            }
        });
        menuView.setCancelableOnTouchMenuOutside(true);
        menuView.showMenu();
        return true;
    }

    @OnClick({R.id.tv_right, R.id.tv_reply, R.id.tv_look_comments, R.id.tv_pages})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_right://更多
                if (pt == null) return;
                List<String> items = new ArrayList<>();
                final List<Request> list = new ArrayList<>();
                String userId = ShareData.getShareStringData(ShareData.ID);
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
                    request.params = new HashMap<>();
                    request.params.put("userId", userId);
                    request.params.put("pillowTalkId", pt.getId());
                    request.callback = callbackOpenPillowTalk;
                    list.add(request);
                }
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
            case R.id.tv_reply://回复
                if (pt == null) return;
                startActivityForResult(new Intent(context, ReplyActivity.class)
                        .putExtra(ReplyActivity.PILLOW_TALK_ID, pt.getId()), REPLY);
                break;
            case R.id.tv_look_comments://看评论
                if (pt == null) return;
                startActivityForResult(new Intent(context, CommentListActivity.class)
                        .putExtra(CommentListActivity.PILLOW_TALK_ID, pt.getId()), LOOK_COMMENTS);
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
                        getFollows(paging.page, paging.position);
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

    /**
     * 删除跟随悄悄话接口回调
     */
    private class CallbackDeleteFollowPillowTalk implements Callback {
        private FollowPillowTalk fpt;

        CallbackDeleteFollowPillowTalk(FollowPillowTalk fpt) {
            this.fpt = fpt;
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
                            adapter.remove(fpt);
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
     * 跟随悄悄话列表接口回调
     */
    private class CallbackFollows implements Callback {
        private int requestPage;
        private ScrollPosition position;

        CallbackFollows(int requestPage, ScrollPosition position) {
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
                            adapter.setList(data.list);
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
        public List<FollowPillowTalk> list;
    }
}
