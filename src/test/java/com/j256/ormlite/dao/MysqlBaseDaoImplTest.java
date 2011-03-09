package com.j256.ormlite.dao;

import com.j256.ormlite.dao.JdbcBaseDaoImplTest;

public class MysqlBaseDaoImplTest extends JdbcBaseDaoImplTest {

	@Override
	protected void setDatabaseParams() {
		databaseHost = "db.be.256.com";
		databaseUrl = "jdbc:mysql://" + databaseHost + "/ormlitetest";
		userName = "ormlitetest";
		password = "hibernate";
	}
}
