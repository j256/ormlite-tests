package com.j256.ormlite.table;


public class PostgresTableUtilsTest extends BaseTableUtilsTest {

	@Override
	protected void setDatabaseParams() {
		databaseHost = "db.be.256.com";
		databaseUrl = "jdbc:postgresql://" + databaseHost + "/ormlitetest";
		userName = "ormlitetest";
		password = "hibernate";
	}
}
