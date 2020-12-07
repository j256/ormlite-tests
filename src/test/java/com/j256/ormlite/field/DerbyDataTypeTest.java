package com.j256.ormlite.field;

import java.util.concurrent.atomic.AtomicInteger;

public class DerbyDataTypeTest extends BaseDataTypeTest {

	private final AtomicInteger derbyCount = new AtomicInteger();

	@Override
	protected void setDatabaseParams() {
		System.setProperty("derby.stream.error.file", "target/derby.log");
		databaseUrl = "jdbc:derby:target/ormlitederby" + derbyCount.incrementAndGet() + ";create=true";
	}

	@Override
	protected boolean byteArrayComparisonsWork() {
		return false;
	}
}
