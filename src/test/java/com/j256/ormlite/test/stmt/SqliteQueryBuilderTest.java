package com.j256.ormlite.test.stmt;

import com.j256.ormlite.stmt.JdbcQueryBuilderTest;

public class SqliteQueryBuilderTest extends JdbcQueryBuilderTest {

	@Override
	protected void setDatabaseParams() {
		databaseUrl = "jdbc:sqlite:";
	}
}
