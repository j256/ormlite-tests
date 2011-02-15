package com.j256.ormlite.test.misc;

import com.j256.ormlite.misc.JdbcTransactionManagerTest;

public class HsqldbTransactionManagerTest extends JdbcTransactionManagerTest {

	@Override
	protected void setDatabaseParams() {
		databaseUrl = "jdbc:hsqldb:mem:ormlitehsqldb";
	}
}
