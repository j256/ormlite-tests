package com.j256.ormlite.db;

import com.j256.ormlite.jdbc.db.PostgresDatabaseType;
import com.j256.ormlite.jdbc.db.PostgresDatabaseTypeTest;

/**
 * Does tests on a real database connection. I didn't want to have to require someone to have have downloaded derby
 * database to have the ormlite tests work.
 */
public class PostgresConnectTest extends PostgresDatabaseTypeTest {

	@Override
	protected void setDatabaseParams() {
		databaseHost = "db.be.256stuff.com";
		databaseUrl = "jdbc:postgresql://" + databaseHost + "/ormlitetest";
		userName = "ormlitetest";
		password = "hibernate";
		databaseType = new PostgresDatabaseType();
	}

	@Override
	protected boolean isDriverClassExpected() {
		return true;
	}

	@Override
	public void testEscapedEntityName() {
		super.testEscapedEntityName();
	}
}
