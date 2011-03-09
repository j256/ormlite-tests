package com.j256.ormlite.misc;


public class HsqldbTransactionManagerTest extends BaseTransactionManagerTest {

	@Override
	protected void setDatabaseParams() {
		databaseUrl = "jdbc:hsqldb:mem:ormlitehsqldb";
	}
}
