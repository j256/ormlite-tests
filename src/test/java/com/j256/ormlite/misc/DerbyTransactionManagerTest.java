package com.j256.ormlite.misc;


public class DerbyTransactionManagerTest extends BaseTransactionManagerTest {

	@Override
	protected void setDatabaseParams() {
		System.setProperty("derby.stream.error.file", "target/derby.log");
		databaseUrl = "jdbc:derby:target/ormlitederby;create=true";
	}
}
