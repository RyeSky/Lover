package com.skye.lover.dao.impl;

import com.skye.lover.dao.BaseDao;
import com.skye.lover.dao.interf.LoverRelationshipDao;
import com.skye.lover.model.LoverRelationship;

import java.sql.SQLException;

/**
 * 相爱关系DAO实现
 */
public class LoverRelationshipDaoImpl extends BaseDao implements
        LoverRelationshipDao {

    /**
     * 建立相爱关系（单方面，需要另一方确认后生效）
     *
     * @param one     相爱关系的一方
     * @param another 相爱关系的另一方
     * @return 插入是否成功
     */
    @Override
    public boolean insert(String one, String another) {
        String sql = "insert into " + LoverRelationship.TABLE_NAME + " (" + LoverRelationship.ONE + "," + LoverRelationship.ANOTHER + ") values (?,?)";
        int update = 0;
        try {
            conn = BaseDao.getConnection();
            pst = conn.prepareStatement(sql);
            pst.setString(1, one);
            pst.setString(2, another);
            update = pst.executeUpdate();
            close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return update > 0;
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
        String sql = "update " + LoverRelationship.TABLE_NAME + " set " + LoverRelationship.DELETED + " = 1 where "
                + LoverRelationship.ONE + " = ? and " + LoverRelationship.ANOTHER + " = ?";
        int update = 0;
        try {
            conn = BaseDao.getConnection();
            pst = conn.prepareStatement(sql);
            pst.setString(1, one);
            pst.setString(2, another);
            update = pst.executeUpdate();
            close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return update > 0;
    }

    /**
     * 查询有相爱关系的另一方
     *
     * @param one 相爱关系的一方
     * @return 相爱关系另一方的id
     */
    @Override
    public String queryAnother(String one) {
        String sql = "select " + LoverRelationship.ONE + " from "
                + LoverRelationship.TABLE_NAME + " where "
                + LoverRelationship.DELETED + " = 0 and "
                + LoverRelationship.ANOTHER + " = ? and "
                + LoverRelationship.ONE + " in (select "
                + LoverRelationship.ANOTHER + " from "
                + LoverRelationship.TABLE_NAME + " where "
                + LoverRelationship.DELETED + " = 0 and "
                + LoverRelationship.ONE + " = ?)";
        String another = "";
        try {
            conn = BaseDao.getConnection();
            pst = conn.prepareStatement(sql);
            pst.setString(1, one);
            pst.setString(2, one);
            rs = pst.executeQuery();
            if (rs.next())
                another = rs.getString(LoverRelationship.ONE);
            close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return another;
    }
}
