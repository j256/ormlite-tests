package com.j256.ormlite.table;

import java.util.concurrent.atomic.AtomicInteger;

public class DerbyTableUtilsTest extends BaseTableUtilsTest {

	private final AtomicInteger derbyCount = new AtomicInteger();

	@Override
	protected void setDatabaseParams() {
		System.setProperty("derby.stream.error.file", "target/derby.log");
		databaseUrl = "jdbc:derby:target/ormlitederby" + derbyCount.incrementAndGet() + ";create=true";
	}
}
