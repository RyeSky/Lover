package com.skye.lover.activity.pillowtalk;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.skye.lover.R;
import com.skye.lover.activity.base.BaseActivity;
import com.skye.lover.activity.common.ShowPictureActivity;
import com.skye.lover.model.PillowTalk;
import com.skye.lover.model.PillowTalkProperties;
import com.skye.lover.mvp.model.BasePillowTalkDetailModel;
import com.skye.lover.mvp.presenter.BasePillowTalkDetailPresenter;
import com.skye.lover.mvp.view.BasePillowTalkDetailView;
import com.skye.lover.util.URLConfig;
import com.skye.lover.view.CustomGridView3Column;
import com.skye.lover.view.exrecyclerview.ExRecyclerView;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 悄悄话或世界广播界面基类
 */
public abstract class BasePillowTalkDetailActivity
        <V extends BasePillowTalkDetailView, M extends BasePillowTalkDetailModel, P extends BasePillowTalkDetailPresenter<V, M>> extends BaseActivity<V, P>
        implements CustomGridView3Column.OnItemClickListener {
    public static final String PILLOW_TALK_ID = "pillowTalkId";//悄悄话id
    public static final String PILLOW_TALK = "pillowTalk";//悄悄话内容
    public static final String BACK_REASON = "backReason";//返回上一页的原因
    public static final String OPEN = "open";//公开了悄悄话
    public static final String DELETE = "delete";//删除了悄悄话
    public static final String CANCEL_COLLECT = "cancelCollect";//取消收藏
    public static final String CANCEL_PRAISE = "cancelPraise";//取消赞
    public static final String DELETE_COMMENT = "deleteComment";//删除了评论
    protected Header header;
    protected PillowTalk pt;//悄悄话内容
    protected int currentPage, pageCount;//当前请求页跟随悄悄话总页数
    @BindView(R.id.recycler)
    ExRecyclerView recycler;//列表
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
        pt = intent.getParcelableExtra(PILLOW_TALK);
        View view = LayoutInflater.from(context).inflate(R.layout.header_pillow_talk_detail, null);
        header = new Header(view);
        recycler.addHeaderView(view);
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(context));

        initViews();

        if (pt != null) {
            setBody();
            getDetail(pt.getId());
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
            getDetail(pt.getId());
        } else if (!TextUtils.isEmpty(pillowTalkId)) {
            getDetail(pillowTalkId);
        }
    }

    /**
     * 解析请求动作
     *
     * @param action 请求接口地址
     */
    public boolean parseAction(String action) {
        boolean flag = false;//是否已经处理
        switch (action) {
            case URLConfig.ACTION_DELETE_PILLOW_TALK://悄悄话详情
                presenter.deletePillowTalk(pt.getId());
                flag = true;
                break;
            case URLConfig.ACTION_COLLET://收藏悄悄话
                presenter.collect(pt.getId());
                flag = true;
                break;
            case URLConfig.ACTION_CANCEL_COLLECT://取消收藏悄悄话
                presenter.cancelCollect(pt.getId());
                flag = true;
                break;
            case URLConfig.ACTION_PRAISE://赞悄悄话
                presenter.praise(pt.getId());
                flag = true;
                break;
            case URLConfig.ACTION_CANCEL_PRAISE://取消赞悄悄话
                presenter.cancelPraise(pt.getId());
                flag = true;
                break;
            default:
                break;
        }
        return flag;
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
        presenter.pillowTalkDetail(pillowTalkId);
    }

    /**
     * 悄悄话部分属性
     */
    protected void getProperties() {
        if (pt == null) return;
        presenter.pillowTalkProperties(pt.getId());
    }

    /**
     * 获取详情之后
     *
     * @param pt 悄悄话或世界广播实体
     */
    public void afterPillowTalkDetail(PillowTalk pt) {
        if (pt != null) {
            this.pt = pt;
            setBody();
            getListFirstPageData();
        }
    }

    /**
     * 获取悄悄话部分属性之后
     *
     * @param ptp 悄悄话部分信息
     */
    public void afterPillowTalkProperties(PillowTalkProperties ptp) {
        pt.setPublisherOpen(ptp.getPublisherOpen());
        pt.setAnotherOpen(ptp.getAnotherOpen());
        pt.setPraiseCount(ptp.getPraiseCount());
        pt.setCommentCount(ptp.getCommentCount());
        updateProperties();
    }

    /**
     * 收藏悄悄话之后
     *
     * @param collectId 收藏记录id
     */
    public void afterCollect(String collectId) {
        pt.setCollectId(collectId);
    }

    /**
     * 取消收藏悄悄话之后
     */
    public void afterCancelCollect() {
        pt.setCollectId("");
    }

    /**
     * 赞悄悄话之后
     *
     * @param praiseId 收藏记录id
     */
    public void afterPraise(String praiseId) {
        pt.setPraiseId(praiseId);
        getProperties();
    }

    /**
     * 取消赞悄悄话之后
     */
    public void afterCancelPraise() {
        pt.setPraiseId("");
        getProperties();
    }

    /**
     * 列表头视图holder
     */
    static class Header {
        // 相爱关系中的一方
        @BindView(R.id.img_avatar_one)
        SimpleDraweeView avatarOne;//头像
        @BindView(R.id.tv_nickname)
        TextView nickname;//昵称
        //相爱关系中的另一方
        @BindView(R.id.img_avatar_another)
        SimpleDraweeView avatarAnother;//头像
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
}
