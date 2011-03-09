package com.j256.ormlite.stmt;

import com.j256.ormlite.db.MysqlDatabaseType;
import com.j256.ormlite.stmt.JdbcQueryBuilderTest;

public class MysqlQueryBuilderTest extends JdbcQueryBuilderTest {

	@Override
	protected void setDatabaseParams() {
		databaseHost = "db.be.256.com";
		databaseUrl = "jdbc:mysql://" + databaseHost + "/ormlitetest";
		userName = "ormlitetest";
		password = "hibernate";
		databaseType = new MysqlDatabaseType();
	}
}
