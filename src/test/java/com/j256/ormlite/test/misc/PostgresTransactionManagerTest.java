package com.j256.ormlite.test.misc;

import com.j256.ormlite.misc.JdbcTransactionManagerTest;

public class PostgresTransactionManagerTest extends JdbcTransactionManagerTest {

	private final static String DB_HOST = "db.be.256.com";

	@Override
	protected void setDatabaseParams() {
		databaseHost = DB_HOST;
		databaseUrl = "jdbc:postgresql://" + DB_HOST + "/ormlitetest";
		userName = "ormlitetest";
		password = "hibernate";
	}
}
