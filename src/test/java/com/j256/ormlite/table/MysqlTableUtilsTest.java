package com.j256.ormlite.table;

import com.j256.ormlite.db.MysqlDatabaseType;

public class MysqlTableUtilsTest extends BaseTableUtilsTest {

	@Override
	protected void setDatabaseParams() {
		databaseHost = "db.be.256.com";
		databaseUrl = "jdbc:mysql://" + databaseHost + "/ormlitetest";
		userName = "ormlitetest";
		password = "hibernate";
		databaseType = new MysqlDatabaseType();
	}
}
