package com.j256.ormlite.table;


public class SqliteTableUtilsTest extends BaseTableUtilsTest {

	@Override
	protected void setDatabaseParams() {
		databaseUrl = "jdbc:sqlite:";
	}
}
