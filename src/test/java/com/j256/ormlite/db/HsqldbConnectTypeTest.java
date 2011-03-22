package com.j256.ormlite.db;

/**
 * Does tests on a real database connection. I didn't want to have to require someone to have have downloaded derby
 * database to have the ormlite tests work.
 */
public class HsqldbConnectTypeTest extends HsqldbDatabaseTypeTest {

	@Override
	protected void setDatabaseParams() {
		databaseUrl = "jdbc:hsqldb:mem:ormlitehsqldb";
	}

	@Override
	protected boolean isDriverClassExpected() {
		return true;
	}
}
