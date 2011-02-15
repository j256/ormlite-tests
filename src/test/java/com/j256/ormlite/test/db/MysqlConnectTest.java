package com.j256.ormlite.test.db;

import com.j256.ormlite.db.MysqlDatabaseType;
import com.j256.ormlite.db.MysqlDatabaseTypeTest;

/**
 * Does tests on a real database connection. I didn't want to have to require someone to have have downloaded derby
 * database to have the ormlite tests work.
 */
public class MysqlConnectTest extends MysqlDatabaseTypeTest {

	private final static String DB_HOST = "db.be.256.com";

	@Override
	protected void setDatabaseParams() {
		databaseHost = DB_HOST;
		databaseUrl = "jdbc:mysql://" + DB_HOST + "/ormlitetest";
		userName = "ormlitetest";
		password = "hibernate";
		databaseType = new MysqlDatabaseType();
	}

	@Override
	protected boolean isDriverClassExpected() {
		return true;
	}
}
