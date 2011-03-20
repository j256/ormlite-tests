package com.j256.ormlite.db;


/**
 * Does tests on a real database connection. I didn't want to have to require someone to have have downloaded derby
 * database to have the ormlite tests work.
 */
public class DerbyEmbeddedConnectTest extends DerbyEmbeddedDatabaseTypeTest {

	@Override
	protected void setDatabaseParams() {
		System.setProperty("derby.stream.error.file", "target/derby.log");
		databaseUrl = "jdbc:derby:target/ormlitederby;create=true";
	}

	@Override
	protected boolean isDriverClassExpected() {
		return true;
	}
}
