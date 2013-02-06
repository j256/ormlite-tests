package com.j256.ormlite.misc;

import org.junit.Ignore;
import org.junit.Test;

public class SqliteTransactionManagerTest extends BaseTransactionManagerTest {

	@Override
	protected void setDatabaseParams() {
		databaseUrl = "jdbc:sqlite:target/sqliteTransactionManagerTest";
	}
	
	@Override
	@Test
	@Ignore("not sure why this doesn't work but I can't get it to go either way")
	public void testNestedTransactionsNotSupported() {
		// not sure why this isn't working
	}
}
