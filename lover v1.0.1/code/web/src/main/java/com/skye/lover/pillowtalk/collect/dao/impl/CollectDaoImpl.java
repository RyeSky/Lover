package com.skye.lover.pillowtalk.collect.dao.impl;

import com.skye.lover.common.dao.BaseDao;
import com.skye.lover.pillowtalk.collect.dao.CollectDao;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

/**
 * 收藏悄悄话DAO
 */
@Repository
public class CollectDaoImpl extends BaseDao implements CollectDao {
    /**
     * 收藏悄悄话
     *
     * @param pillowTalkId 悄悄话id
     * @param collecter    收藏者
     * @return 收藏记录id
     */
    @Override
    public String insert(String pillowTalkId, String collecter) {
        String sql = "INSERT INTO collect_pillow_talk (pillow_talk_id,collecter) VALUES (:pillow_talk_id,:collecter)";
        KeyHolder key = new GeneratedKeyHolder();
        jdbcTemplate.update(sql, new MapSqlParameterSource().addValue("pillow_talk_id", pillowTalkId).addValue("collecter", collecter), key);
        return String.valueOf(key.getKey().longValue());
    }

    /**
     * 取消收藏
     *
     * @param pillowTalkId 悄悄话id
     * @param collecter    收藏者
     * @return 数据是否插入成功
     */
    @Override
    public boolean delete(String pillowTalkId, String collecter) {
        String sql = "DELETE FROM collect_pillow_talk WHERE pillow_talk_id = :pillow_talk_id AND collecter = :collecter";
        Map<String, Object> params = new HashMap<>();
        params.put("pillow_talk_id", pillowTalkId);
        params.put("collecter", collecter);
        return jdbcTemplate.update(sql, params) > 0;
    }
}
