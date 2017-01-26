package com.skye.lover.util;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class C3P0ConnentionProvider {
	private DataSource ds;
	private static C3P0ConnentionProvider pool;

	private C3P0ConnentionProvider() {
		ds = new ComboPooledDataSource();
	}

	public static final C3P0ConnentionProvider getInstance() {
		if (pool == null) {
			try {
				pool = new C3P0ConnentionProvider();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return pool;
	}

	public synchronized final Connection getConnection() {
		try {
			return ds.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
