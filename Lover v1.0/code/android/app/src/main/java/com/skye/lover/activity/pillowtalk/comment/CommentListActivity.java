package com.skye.lover.activity.pillowtalk.comment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.skye.actionsheet.ActionSheet;
import com.skye.lover.R;
import com.skye.lover.activity.base.BaseActivity;
import com.skye.lover.adapter.CommentAdapter;
import com.skye.lover.exception.NoNetworkConnectException;
import com.skye.lover.model.BaseResponse;
import com.skye.lover.model.Comment;
import com.skye.lover.model.ListResponse;
import com.skye.lover.util.CommonUtil;
import com.skye.lover.util.OkHttpUtil;
import com.skye.lover.util.ShareData;
import com.skye.lover.util.URLConfig;

import java.io.IOException;
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
 * 评论列表界面
 */
public class CommentListActivity extends BaseActivity implements AbsListView.OnScrollListener, SwipeRefreshLayout.OnRefreshListener {
    public static final String PILLOW_TALK_ID = "pillowTalkId";
    private final int PUBLISH_COMMENT = 0;//发表评论
    @BindView(R.id.empty_list_tip)
    TextView empty;//空列表提示
    @BindView(R.id.srl)
    SwipeRefreshLayout srl;//下拉刷新控件
    @BindView(R.id.listview)
    ListView listView;//列表
    private CommentAdapter adapter;
    private int page = 1;//页数
    private boolean refreshing = false, isLastPage = false, requestingMore = false;
    // 是否正在刷新、是否为最后一页、是否正在加载更多
    private String pillowTalkId;//悄悄话id
    /**
     * 下拉刷新接口回调
     */
    private Callback callbackRefresh = new Callback() {
        @Override
        public void onFailure(Call call, final IOException e) {
            e.printStackTrace();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    CommonUtil.closeLoadingDialog(dialog);
                    if (!(e instanceof NoNetworkConnectException)) {//不是没有网络
                        CommonUtil.toast(context, R.string.bad_request);
                        empty.setText(R.string.bad_request);
                    } else
                        empty.setText(R.string.net_not_ok);
                    if (srl != null) {
                        srl.setRefreshing(false);// 停止刷新
                        srl.setEnabled(true);
                    }
                    refreshing = false;
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
                            adapter.clear();
                            adapter.add(data.list);
                            page = data.page;
                            isLastPage = data.page > data.pageCount;
                            if (adapter.isEmpty())
                                empty.setText(R.string.empty_comment);
                        } else {
                            CommonUtil.toast(context, br.message);
                            empty.setText(br.message);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        CommonUtil.toast(context, R.string.bad_request);
                        empty.setText(R.string.bad_request);
                    }
                    if (srl != null) {
                        srl.setRefreshing(false);// 停止刷新
                        srl.setEnabled(true);
                    }
                    refreshing = false;
                }
            });
        }
    };
    /**
     * 加載更多接口回调
     */
    private Callback callbackLoadMore = new Callback() {
        @Override
        public void onFailure(Call call, final IOException e) {
            e.printStackTrace();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    CommonUtil.closeLoadingDialog(dialog);
                    if (!(e instanceof NoNetworkConnectException)) {//不是没有网络
                        CommonUtil.toast(context, R.string.bad_request);
                        empty.setText(R.string.bad_request);
                    } else
                        empty.setText(R.string.net_not_ok);
                    requestingMore = false;
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
                            adapter.add(data.list);
                            page = data.page;
                            isLastPage = data.page > data.pageCount;
                            if (adapter.isEmpty())
                                empty.setText(R.string.empty_comment);
                        } else {
                            CommonUtil.toast(context, br.message);
                            empty.setText(br.message);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        CommonUtil.toast(context, R.string.bad_request);
                        empty.setText(R.string.bad_request);
                    }
                    requestingMore = false;
                }
            });
        }
    };

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_comment_list;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTopbar();
        topbar.title.setText(R.string.comment_list);
        topbar.left.setVisibility(View.VISIBLE);
        topbar.right.setText(R.string.comment);
        topbar.right.setVisibility(View.VISIBLE);
        pillowTalkId = getIntent().getStringExtra(PILLOW_TALK_ID);
        srl.setOnRefreshListener(this);
        srl.setColorScheme(R.color.dark_red_normal, R.color.light_red_normal, R.color.dark_yellow_normal,
                R.color.light_yellow_normal, R.color.light_green_normal, R.color.dark_green_normal, R.color.light_blue_normal,
                R.color.dark_blue_normal, R.color.light_purple_normal, R.color.dark_purple_normal);
        listView.setEmptyView(empty);
        adapter = new CommentAdapter(context);
        listView.setAdapter(adapter);
        listView.setOnScrollListener(this);
    }

    @Override
    public void onRefresh() {
        srl.setEnabled(false);
        refreshing = true;
        page = 1;
        Map<String, String> params = new HashMap<>();
        params.put("pillowTalkId", pillowTalkId);
        params.put("page", page + "");
        dialog = CommonUtil.showLoadingDialog(context, R.string.loading);
        OkHttpUtil.doPost(context, URLConfig.ACTION_COMMENTS, params, callbackRefresh);
    }

    @Override
    public void onScrollStateChanged(final AbsListView view, final int scrollState) {
    }

    @Override
    public void onScroll(final AbsListView view, final int firstVisibleItem, final int visibleItemCount,
                         final int totalItemCount) {
        // 滑动到底部，加载更多数据，且没有正在加载更多没有正在刷新且当前不是最后一页数据
        if (!refreshing && !requestingMore && !isLastPage) {
            int lastInScreen = firstVisibleItem + visibleItemCount;
            if (lastInScreen >= totalItemCount) {
                requestingMore = true;
                Map<String, String> params = new HashMap<>();
                params.put("pillowTalkId", pillowTalkId);
                params.put("page", page + "");
                dialog = CommonUtil.showLoadingDialog(context, R.string.loading);
                OkHttpUtil.doPost(context, URLConfig.ACTION_COMMENTS, params, callbackLoadMore);
            }
        }
    }

    @OnClick(R.id.tv_right)
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_right://评论
                startActivityForResult(new Intent(context, PublishCommentActivity.class)
                        .putExtra(PublishCommentActivity.PILLOW_TALK_ID, pillowTalkId), PUBLISH_COMMENT);
                break;
            default:
                break;
        }
    }

    @OnItemLongClick(R.id.listview)
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        final Comment comment = (Comment) parent.getItemAtPosition(position);
        if (comment == null) return false;
        if (!ShareData.getShareStringData(ShareData.ID).equals(comment.getCommenter()))
            return false;//登录用户不是发表者，不能删除
        setTheme(R.style.ActionSheetStyleIOS7);
        ActionSheet menuView = new ActionSheet(context);
        menuView.setCancelButtonTitle("取消");// before add items
        menuView.addItems(getString(R.string.delete));
        menuView.setItemClickListener(new ActionSheet.MenuItemClickListener() {
            @Override
            public void onItemClick(int itemPosition) {
                switch (itemPosition) {
                    case 0:
                        Map<String, String> params = new HashMap<>();
                        params.put("userId", ShareData.getShareStringData(ShareData.ID));
                        params.put("commentId", comment.getId());
                        dialog = CommonUtil.showLoadingDialog(context, R.string.please_wait);
                        OkHttpUtil.doPost(context, URLConfig.ACTION_DELETE_COMMENT, params, new CallbackDeleteComment(position));
                        break;
                    default:
                        break;
                }
            }

        });
        menuView.setCancelableOnTouchMenuOutside(true);
        menuView.showMenu();
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        switch (requestCode) {
            case PUBLISH_COMMENT://发表评论
                setResult(RESULT_OK);
                onRefresh();
                break;
            default:
                break;
        }
    }

    /**
     * 删除评论接口回调
     */
    private class CallbackDeleteComment implements Callback {
        private int position;

        CallbackDeleteComment(int position) {
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
                            adapter.remove(position);
                            if (adapter.isEmpty())
                                empty.setText(R.string.empty_comment);
                            CommonUtil.toast(context, R.string.delete_success);
                            setResult(RESULT_OK);
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
