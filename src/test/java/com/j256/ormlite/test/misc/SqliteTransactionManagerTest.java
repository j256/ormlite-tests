package com.j256.ormlite.test.misc;


public class SqliteTransactionManagerTest extends BaseTransactionManagerTest {

	@Override
	protected void setDatabaseParams() {
		databaseUrl = "jdbc:sqlite:";
	}
}
