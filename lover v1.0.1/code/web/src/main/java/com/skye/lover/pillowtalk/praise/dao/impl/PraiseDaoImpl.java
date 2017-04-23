package com.skye.lover.pillowtalk.praise.dao.impl;

import com.skye.lover.common.dao.BaseDao;
import com.skye.lover.pillowtalk.praise.dao.PraiseDao;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

/**
 * 赞DAO实现
 */
@Repository
public class PraiseDaoImpl extends BaseDao implements PraiseDao {
    /**
     * 赞悄悄话
     *
     * @param pillowTalkId 悄悄话id
     * @param praiser      赞者
     * @return 赞记录id
     */
    @Override
    public String insert(String pillowTalkId, String praiser) {
        String sql = "INSERT INTO praise_pillow_talk (pillow_talk_id,praiser) VALUES (:pillow_talk_id,:praiser)";
        KeyHolder key = new GeneratedKeyHolder();
        jdbcTemplate.update(sql, new MapSqlParameterSource().addValue("pillow_talk_id", pillowTalkId).addValue("praiser", praiser), key);
        return String.valueOf(key.getKey().longValue());
    }

    /**
     * 取消收藏
     *
     * @param pillowTalkId 悄悄话id
     * @param praiser      赞者
     * @return 数据是否删除成功
     */
    @Override
    public boolean delete(String pillowTalkId, String praiser) {
        String sql = "DELETE FROM praise_pillow_talk WHERE pillow_talk_id = :pillow_talk_id AND praiser = :praiser";
        Map<String, Object> params = new HashMap<>();
        params.put("pillow_talk_id",pillowTalkId);
        params.put("praiser",praiser);
        return jdbcTemplate.update(sql, params) > 0;
    }
}
