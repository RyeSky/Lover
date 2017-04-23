package com.skye.lover.loverrelatiionship.dao;

/**
 * 相爱关系DAO
 */
public interface LoverRelationshipDao {

    /**
     * 建立相爱关系（单方面，需要另一方确认后生效）
     *
     * @param one     相爱关系的一方
     * @param another 相爱关系的另一方
     * @return 插入是否成功
     */
     boolean insert(String one, String another);

    /**
     * 删除相爱关系
     *
     * @param one     相爱关系的一方
     * @param another 相爱关系的另一方
     * @return 删除是否成功
     */
     boolean delete(String one, String another);

    /**
     * 查询有相爱关系的另一方
     *
     * @param one 相爱关系的一方
     * @return 相爱关系另一方的id
     */
     String queryAnother(String one);
}
