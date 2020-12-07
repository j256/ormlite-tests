package com.j256.ormlite.stmt;

import com.j256.ormlite.jdbc.stmt.JdbcQueryBuilderTest;

public class HsqldbQueryBuilderTest extends JdbcQueryBuilderTest {

	@Override
	protected void setDatabaseParams() {
		databaseUrl = "jdbc:hsqldb:mem:ormlitehsqldb";
	}
}
