package com.j256.ormlite.test.table;

import com.j256.ormlite.table.JdbcTableUtilsTest;

public class HsqldbTableUtilsTest extends JdbcTableUtilsTest {

	@Override
	protected void setDatabaseParams() {
		databaseUrl = "jdbc:hsqldb:mem:ormlitehsqldb";
	}
}
