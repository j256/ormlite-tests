package com.j256.ormlite.misc;

public class PostgresTransactionManagerTest extends BaseTransactionManagerTest {

	@Override
	protected void setDatabaseParams() {
		databaseHost = "db.be.256stuff.com";
		databaseUrl = "jdbc:postgresql://" + databaseHost + "/ormlitetest";
		userName = "ormlitetest";
		password = "hibernate";
	}
}
