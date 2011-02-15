package com.j256.ormlite.test.db;

import com.j256.ormlite.db.SqlServerJtdsDatabaseConnectTypeTest;
import com.j256.ormlite.db.SqlServerJtdsDatabaseType;

/**
 * Does tests on a real database connection. I didn't want to have to require someone to have have downloaded derby
 * database to have the ormlite tests work.
 */
public class SqlServerJtdsConnectTypeTest extends SqlServerJtdsDatabaseConnectTypeTest {

	private final static String DB_HOST = "wfs2.jprinc.net";

	@Override
	protected void setDatabaseParams() {
		databaseHost = DB_HOST;
		databaseUrl = "jdbc:jtds:sqlserver://" + DB_HOST + ":1433/ormlite;ssl=request";
		userName = "gwatson";
		password = "ormlite";
		databaseType = new SqlServerJtdsDatabaseType();
	}

	@Override
	protected boolean isDriverClassExpected() {
		return true;
	}
}
