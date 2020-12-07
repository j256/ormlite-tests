package com.j256.ormlite.misc;

import java.util.concurrent.atomic.AtomicInteger;

public class DerbyTransactionManagerTest extends BaseTransactionManagerTest {

	private final AtomicInteger derbyCount = new AtomicInteger();

	@Override
	protected void setDatabaseParams() {
		System.setProperty("derby.stream.error.file", "target/derby.log");
		databaseUrl = "jdbc:derby:target/ormlitederby" + derbyCount.incrementAndGet() + ";create=true";
	}
}
