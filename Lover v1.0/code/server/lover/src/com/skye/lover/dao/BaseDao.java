package com.skye.lover.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.skye.lover.util.C3P0ConnentionProvider;

public class BaseDao {
	private static final C3P0ConnentionProvider pool = C3P0ConnentionProvider
			.getInstance();
	public Connection conn = null;
	public PreparedStatement pst = null;
	public ResultSet rs = null;

	public static Connection getConnection() {
		return pool.getConnection();
	}

	public void close() throws SQLException {
		if (conn != null) {
			conn.close();
		}
		if (pst != null) {
			pst.close();
		}
		if (rs != null) {
			rs.close();
		}
	}
}
