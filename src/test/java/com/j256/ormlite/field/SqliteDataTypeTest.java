package com.j256.ormlite.field;

public class SqliteDataTypeTest extends BaseDataTypeTest {

	@Override
	protected void setDatabaseParams() {
		databaseUrl = "jdbc:sqlite:";
	}
}
