package com.j256.ormlite.test.stmt;

import com.j256.ormlite.stmt.JdbcQueryBuilderTest;

public class PostgresQueryBuilderTest extends JdbcQueryBuilderTest {

	private final static String DB_HOST = "db.be.256.com";

	@Override
	protected void setDatabaseParams() {
		databaseHost = DB_HOST;
		databaseUrl = "jdbc:postgresql://" + DB_HOST + "/ormlitetest";
		userName = "ormlitetest";
		password = "hibernate";
	}
}
