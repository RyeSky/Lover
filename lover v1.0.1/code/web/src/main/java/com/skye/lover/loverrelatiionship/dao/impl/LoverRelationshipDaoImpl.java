package com.skye.lover.loverrelatiionship.dao.impl;

import com.skye.lover.common.dao.BaseDao;
import com.skye.lover.loverrelatiionship.dao.LoverRelationshipDao;
import com.skye.lover.loverrelatiionship.model.LoverRelationship;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 相爱关系Dao实现
 */
@Repository
public class LoverRelationshipDaoImpl extends BaseDao implements LoverRelationshipDao {
    /**
     * 建立相爱关系（单方面，需要另一方确认后生效）
     *
     * @param one     相爱关系的一方
     * @param another 相爱关系的另一方
     * @return 插入是否成功
     */
    @Override
    public boolean insert(String one, String another) {
        String sql = "INSERT INTO lover_relationship (one, another) VALUES (:one,:another)";
        Map<String, Object> params = new HashMap<>();
        params.put(LoverRelationship.ONE, one);
        params.put(LoverRelationship.ANOTHER, another);
        return jdbcTemplate.update(sql, params) > 0;
    }

    /**
     * 删除相爱关系
     *
     * @param one     相爱关系的一方
     * @param another 相爱关系的另一方
     * @return 删除是否成功
     */
    @Override
    public boolean delete(String one, String another) {
        String sql = "UPDATE lover_relationship SET deleted = 1 WHERE one = :one AND another = :another";
        Map<String, Object> params = new HashMap<>();
        params.put(LoverRelationship.ONE, one);
        params.put(LoverRelationship.ANOTHER, another);
        return jdbcTemplate.update(sql, params) > 0;
    }

    /**
     * 查询有相爱关系的另一方
     *
     * @param one 相爱关系的一方
     * @return 相爱关系另一方的id
     */
    @Override
    public String queryAnother(String one) {
        String sql = "SELECT one FROM lover_relationship WHERE deleted = 0 AND another = :another AND one IN " +
                "(SELECT another FROM lover_relationship WHERE deleted = 0 AND one = :one)";
        Map<String, Object> params = new HashMap<>();
        params.put(LoverRelationship.ANOTHER, one);
        params.put(LoverRelationship.ONE, one);
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, params);
        return list == null || list.isEmpty() ? null : list.get(0).get(LoverRelationship.ONE).toString();
    }
}
