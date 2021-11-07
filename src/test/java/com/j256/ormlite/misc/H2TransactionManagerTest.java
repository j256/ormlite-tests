package com.j256.ormlite.misc;

import java.io.File;

public class H2TransactionManagerTest extends BaseTransactionManagerTest {

	// H2 is already the default
	@Override
	protected void setDatabaseParams() {
		String path = new File("target", H2TransactionManagerTest.class.getSimpleName()).getAbsolutePath();
		databaseUrl = "jdbc:h2:" + path;
	}
}
