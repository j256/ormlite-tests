package com.j256.ormlite.test.table;

import com.j256.ormlite.db.MysqlDatabaseType;
import com.j256.ormlite.table.JdbcTableUtilsTest;

public class MysqlTableUtilsTest extends JdbcTableUtilsTest {

	private final static String DB_HOST = "db.be.256.com";

	@Override
	protected void setDatabaseParams() {
		databaseHost = DB_HOST;
		databaseUrl = "jdbc:mysql://" + DB_HOST + "/ormlitetest";
		userName = "ormlitetest";
		password = "hibernate";
		databaseType = new MysqlDatabaseType();
	}
}
