package com.j256.ormlite.test.dao;

import com.j256.ormlite.dao.JdbcBaseDaoImplTest;

public class MysqlBaseDaoImplTest extends JdbcBaseDaoImplTest {

	private final static String DB_HOST = "db.be.256.com";

	@Override
	protected void setDatabaseParams() {
		databaseHost = DB_HOST;
		databaseUrl = "jdbc:mysql://" + DB_HOST + "/ormlitetest";
		userName = "ormlitetest";
		password = "hibernate";
	}
}
