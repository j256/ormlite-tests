package com.j256.ormlite.field;

public class MysqlDataTypeTest extends BaseDataTypeTest {

	@Override
	protected void setDatabaseParams() {
		databaseHost = "db.be.256stuff.com";
		databaseUrl = "jdbc:mysql://" + databaseHost + "/ormlitetest";
		userName = "ormlitetest";
		password = "hibernate";
	}
}
