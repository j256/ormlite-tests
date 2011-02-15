package com.j256.ormlite.test.db;

import com.j256.ormlite.db.PostgresDatabaseType;
import com.j256.ormlite.db.PostgresDatabaseTypeTest;

/**
 * Does tests on a real database connection. I didn't want to have to require someone to have have downloaded derby
 * database to have the ormlite tests work.
 */
public class PostgresConnectTest extends PostgresDatabaseTypeTest {

	private final static String DB_HOST = "db.be.256.com";

	@Override
	protected void setDatabaseParams() {
		databaseHost = DB_HOST;
		databaseUrl = "jdbc:postgresql://" + DB_HOST + "/ormlitetest";
		userName = "ormlitetest";
		password = "hibernate";
		databaseType = new PostgresDatabaseType();
	}

	@Override
	protected boolean isDriverClassExpected() {
		return true;
	}

	@Override
	public void testEscapedEntityName() throws Exception {
		super.testEscapedEntityName();
	}
}
