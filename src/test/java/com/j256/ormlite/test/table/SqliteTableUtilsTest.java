package com.j256.ormlite.test.table;


public class SqliteTableUtilsTest extends BaseTableUtilsTest {

	@Override
	protected void setDatabaseParams() {
		databaseUrl = "jdbc:sqlite:";
	}
}
