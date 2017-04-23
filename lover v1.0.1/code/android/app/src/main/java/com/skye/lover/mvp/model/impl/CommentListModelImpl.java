package com.skye.lover.mvp.model.impl;

import android.content.Context;

import com.skye.lover.model.Cross;
import com.skye.lover.model.User;
import com.skye.lover.mvp.model.CommentListModel;
import com.skye.lover.util.OkHttpUtil;
import com.skye.lover.util.ShareDataUtil;
import com.skye.lover.util.URLConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * 评论列表模型实现
 */
public class CommentListModelImpl implements CommentListModel {
    private int page = 1;

    @Override
    public void setPage(int page) {
        this.page = page;
    }

    @Override
    public Cross refresh(Context context, String pillowTalkId) {
        page = 1;
        return loadMore(context, pillowTalkId);
    }

    @Override
    public Cross loadMore(Context context, String pillowTalkId) {
        Map<String, String> params = new HashMap<>();
        params.put("pillowTalkId", pillowTalkId);
        params.put("page", page + "");
        return OkHttpUtil.buildPostCall(URLConfig.ACTION_COMMENTS, params, true, true);
    }

    @Override
    public Cross deleteComment(Context context, String commentId, int position) {
        Map<String, String> params = new HashMap<>();
        params.put("commentId", commentId);
        params.put("userId", ShareDataUtil.get(context, User.ID));
        return OkHttpUtil.buildPostCall(URLConfig.ACTION_DELETE_COMMENT, params, false, false).setExtra(position);
    }
}
