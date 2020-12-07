package com.j256.ormlite.stmt;

import com.j256.ormlite.jdbc.stmt.JdbcQueryBuilderTest;

public class SqliteQueryBuilderTest extends JdbcQueryBuilderTest {

	@Override
	protected void setDatabaseParams() {
		databaseUrl = "jdbc:sqlite:";
	}
}
