package com.skye.lover.model;

/**
 * 相爱关系
 */
public class LoverRelationship {
    /**
     * 表名称
     */
    public static final String TABLE_NAME = "lover_relationship";
    /**
     * 相爱关系中的一方
     */
    public static final String ONE = "one";
    /**
     * 相爱关系中的另一方
     */
    public static final String ANOTHER = "another";
    /**
     * 相爱关系是否被删除【0：没有被删除；1：已经被删除】
     */
    public static final String DELETED = "deleted";
}
