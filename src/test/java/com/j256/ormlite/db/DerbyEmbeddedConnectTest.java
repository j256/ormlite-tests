package com.j256.ormlite.db;

import java.util.concurrent.atomic.AtomicInteger;

import com.j256.ormlite.jdbc.db.DerbyEmbeddedDatabaseTypeTest;

/**
 * Does tests on a real database connection. I didn't want to have to require someone to have have downloaded derby
 * database to have the ormlite tests work.
 */
public class DerbyEmbeddedConnectTest extends DerbyEmbeddedDatabaseTypeTest {

	private final AtomicInteger derbyCount = new AtomicInteger();

	@Override
	protected void setDatabaseParams() {
		System.setProperty("derby.stream.error.file", "target/derby.log");
		databaseUrl = "jdbc:derby:target/ormlitederby" + derbyCount.incrementAndGet() + ";create=true";
	}

	@Override
	protected boolean isDriverClassExpected() {
		return true;
	}
}
