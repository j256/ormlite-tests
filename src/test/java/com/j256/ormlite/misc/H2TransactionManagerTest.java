package com.j256.ormlite.misc;

public class H2TransactionManagerTest extends BaseTransactionManagerTest {

	// H2 is already the default
	@Override
	protected void setDatabaseParams() {
		databaseUrl = "jdbc:h2:target/h2TransactionManagerTest";
	}
}
