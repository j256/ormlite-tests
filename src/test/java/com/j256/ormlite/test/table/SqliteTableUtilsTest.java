package com.j256.ormlite.test.table;

import com.j256.ormlite.table.JdbcTableUtilsTest;

public class SqliteTableUtilsTest extends JdbcTableUtilsTest {

	@Override
	protected void setDatabaseParams() {
		databaseUrl = "jdbc:sqlite:";
	}
}
