package com.j256.ormlite.test.misc;

import com.j256.ormlite.misc.JdbcTransactionManagerTest;

public class DerbyTransactionManagerTest extends JdbcTransactionManagerTest {

	@Override
	protected void setDatabaseParams() {
		System.setProperty("derby.stream.error.file", "target/derby.log");
		databaseUrl = "jdbc:derby:target/ormlitederby;create=true";
	}
}
