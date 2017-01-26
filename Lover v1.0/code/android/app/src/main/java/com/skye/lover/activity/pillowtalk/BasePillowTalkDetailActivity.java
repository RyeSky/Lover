package com.skye.lover.activity.pillowtalk;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.skye.lover.R;
import com.skye.lover.activity.base.BaseActivity;
import com.skye.lover.activity.common.localphotoalbum.ShowPictureActivity;
import com.skye.lover.exception.NoNetworkConnectException;
import com.skye.lover.model.BaseResponse;
import com.skye.lover.model.PillowTalk;
import com.skye.lover.model.PillowTalkProperties;
import com.skye.lover.util.CommonUtil;
import com.skye.lover.util.OkHttpUtil;
import com.skye.lover.util.ShareData;
import com.skye.lover.util.URLConfig;
import com.skye.lover.view.CustomGridView3Column;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 悄悄话或世界广播界面基类
 */
public abstract class BasePillowTalkDetailActivity extends BaseActivity implements CustomGridView3Column.OnItemClickListener {
    public static final String PILLOW_TALK_ID = "pillowTalkId";//悄悄话id
    public static final String PILLOW_TALK = "pillowTalk";//悄悄话内容
    public static final String BACK_REASON = "backReason";//返回上一页的原因
    public static final String OPEN = "open";//公开了悄悄话
    public static final String DELETE = "delete";//删除了悄悄话
    public static final String CANCEL_COLLECT = "cancelCollect";//取消收藏
    public static final String CANCEL_PRAISE = "cancelPraise";//取消赞
    protected Header header;
    protected PillowTalk pt;//悄悄话内容
    protected int currentPage, pageCount;//当前请求页跟随悄悄话总页数
    /**
     * 悄悄话详情接口回调
     */
    protected Callback callbackPillowTalkDetail = new Callback() {
        @Override
        public void onFailure(Call call, final IOException e) {
            e.printStackTrace();
            handler.post(new Runnable() {
                @Override
                public void run() {
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
                    try {
                        CommonUtil.log(body);
                        BaseResponse br = CommonUtil.parseToObject(body, BaseResponse.class);//转化为返回基类
                        if (br.check()) {
                            PillowTalk temp = CommonUtil.parseToObject(br.result, PillowTalk.class);
                            if (temp != null) {
                                pt = temp;
                                setBody();
                                getListFirstPageData();
                            }
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
    /**
     * 悄悄话部分属性接口回调
     */
    protected Callback callbackPillowTalkProperties = new Callback() {
        @Override
        public void onFailure(Call call, final IOException e) {
            e.printStackTrace();
            handler.post(new Runnable() {
                @Override
                public void run() {
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
                    try {
                        CommonUtil.log(body);
                        BaseResponse br = CommonUtil.parseToObject(body, BaseResponse.class);//转化为返回基类
                        if (br.check()) {
                            PillowTalkProperties ptp = CommonUtil.parseToObject(br.result, PillowTalkProperties.class);
                            pt.setPublisherOpen(ptp.getPublisherOpen());
                            pt.setAnotherOpen(ptp.getAnotherOpen());
                            pt.setPraiseCount(ptp.getPraiseCount());
                            pt.setCommentCount(ptp.getCommentCount());
                            updateProperties();
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
    /**
     * 删除悄悄话接口回调
     */
    protected Callback callbackDeletePillowTalk = new Callback() {
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
                            setResult(RESULT_OK, new Intent().putExtra(BACK_REASON, DELETE));
                            finish();
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
    /**
     * 收藏悄悄话接口回调
     */
    protected Callback callbackCollect = new Callback() {
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
                            pt.setCollectId(br.result.getAsString());
                            CommonUtil.toast(context, R.string.collect_success);
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
    /**
     * 取消收藏悄悄话接口回调
     */
    protected Callback callbackCancelCollect = new Callback() {
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
                            pt.setCollectId("");
                            CommonUtil.toast(context, R.string.cancel_collect_success);
                            setResult(RESULT_OK, new Intent().putExtra(BACK_REASON, CANCEL_COLLECT));
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
    /**
     * 赞悄悄话接口回调
     */
    protected Callback callbackPraise = new Callback() {
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
                            pt.setPraiseId(br.result.getAsString());
                            CommonUtil.toast(context, R.string.praise_success);
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
    };
    /**
     * 取消赞悄悄话接口回调
     */
    protected Callback callbackCancelPraise = new Callback() {
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
                            pt.setPraiseId("");
                            CommonUtil.toast(context, R.string.cancel_praise_success);
                            getProperties();
                            setResult(RESULT_OK, new Intent().putExtra(BACK_REASON, CANCEL_PRAISE));
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
    @BindView(R.id.listview)
    ListView listView;//列表
    @BindView(R.id.tv_pages)
    TextView tvPages;//页数

    /**
     * 启动界面
     */
    public static void launch(Activity activity, PillowTalk pt, int requestCode) {
        Intent intent = null;
        if (pt.getType() == PillowTalk.PILLOW_TALK) {
            intent = new Intent(activity, PillowTalkDetailActivity.class);
            intent.putExtra(PILLOW_TALK, pt);
        } else if (pt.getType() == PillowTalk.BROADCAST) {
            intent = new Intent(activity, BroadcastDetailActivity.class);
            intent.putExtra(PILLOW_TALK, pt);
        }
        if (intent != null) activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 初始化控件
     */
    public abstract void initViews();

    /**
     * 设置主体界面数据
     */
    public abstract void setBody();

    /**
     * 获取列表第一页数就
     */
    public abstract void getListFirstPageData();

    /**
     * 更新部分属性
     */
    public abstract void updateProperties();

    @SuppressLint("InflateParams")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTopbar();
        topbar.title.setText(R.string.pillow_talk_detail);
        topbar.left.setVisibility(View.VISIBLE);
        topbar.right.setText(R.string.more);
        topbar.right.setVisibility(View.VISIBLE);
        Intent intent = getIntent();
        String pillowTalkId = intent.getStringExtra(PILLOW_TALK_ID);
        pt = (PillowTalk) intent.getSerializableExtra(PILLOW_TALK);
        View view = LayoutInflater.from(context).inflate(R.layout.header_pillow_talk_detail, null);
        header = new Header(view);
        listView.addHeaderView(view, this, false);
        initViews();

        if (pt != null) {
            setBody();
            getListFirstPageData();
        } else if (!TextUtils.isEmpty(pillowTalkId)) {
            getDetail(pillowTalkId);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        intent = getIntent();
        String pillowTalkId = intent.getStringExtra(PILLOW_TALK_ID);
        pt = (PillowTalk) intent.getSerializableExtra(PILLOW_TALK);
        if (pt != null) {
            setBody();
            getListFirstPageData();
        } else if (!TextUtils.isEmpty(pillowTalkId)) {
            getDetail(pillowTalkId);
        }
    }

    @Override
    public void onItemClick(List<String> pictures, int position) {
        startActivity(new Intent(context, ShowPictureActivity.class).putExtra(ShowPictureActivity.PICTURES, (Serializable) pictures)
                .putExtra(ShowPictureActivity.POSITION, position));
    }

    /**
     * 获取悄悄话详情
     */
    protected void getDetail(String pillowTalkId) {
        Map<String, String> params = new HashMap<>();
        params.put("pillowTalkId", pillowTalkId);
        params.put("userId", ShareData.getShareStringData(ShareData.ID));
        OkHttpUtil.doPost(context, URLConfig.ACTION_PILLOW_TALK_DETAIL, params, callbackPillowTalkDetail);
    }

    /**
     * 悄悄话部分属性
     */
    protected void getProperties() {
        if (pt == null) return;
        Map<String, String> params = new HashMap<>();
        params.put("pillowTalkId", pt.getId());
        OkHttpUtil.doPost(context, URLConfig.ACTION_PILLOW_TALK_PROPERTIES, params, callbackPillowTalkProperties);
    }

    /**
     * 滚动位置
     */
    protected enum ScrollPosition {
        HEAD//头部
        , FIRST_ITEM//第一条跟随悄悄话
        , BOTTOM//底部
    }

    /**
     * 列表头视图holder
     */
    static class Header {
        // 相爱关系中的一方
        @BindView(R.id.img_avatar_one)
        ImageView avatarOne;//头像
        @BindView(R.id.tv_nickname)
        TextView nickname;//昵称
        //相爱关系中的另一方
        @BindView(R.id.img_avatar_another)
        ImageView avatarAnother;//头像
        @BindView(R.id.tv_nickname_another)
        TextView nicknameAnother;//昵称
        @BindView(R.id.tv_properties)
        TextView properties;//属性
        @BindView(R.id.tv_content)
        TextView content;//内容
        @BindView(R.id.pictures_container)
        CustomGridView3Column picturesContainer;//图片容器
        @BindView(R.id.split_line)
        View splitline;//分隔线

        Header(View view) {
            ButterKnife.bind(this, view);
        }
    }

    /**
     * 网络请求或页面跳转
     */
    protected class Request {
        String action;//接口地址
        Map<String, String> params;//接口所需参数
        Callback callback;//接口回调
        Intent intent;//页面跳转
    }


    /**
     * 翻页
     */
    protected class Paging {
        int page;//请求页数
        ScrollPosition position;//请求回数据后的滚动位置

        Paging(int page, ScrollPosition position) {
            this.page = page;
            this.position = position;
        }
    }
}
