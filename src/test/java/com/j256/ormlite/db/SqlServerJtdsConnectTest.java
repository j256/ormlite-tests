package com.j256.ormlite.db;

import com.j256.ormlite.jdbc.db.SqlServerJtdsDatabaseConnectTypeTest;
import com.j256.ormlite.jdbc.db.SqlServerJtdsDatabaseType;

/**
 * Does tests on a real database connection. I didn't want to have to require someone to have have downloaded derby
 * database to have the ormlite tests work.
 */
public class SqlServerJtdsConnectTest extends SqlServerJtdsDatabaseConnectTypeTest {

	@Override
	protected void setDatabaseParams() {
		databaseHost = "wfs2.jprinc.net";
		databaseUrl = "jdbc:jtds:sqlserver://" + databaseHost + ":1433/ormlite;ssl=request";
		userName = "gwatson";
		password = "ormlite";
		databaseType = new SqlServerJtdsDatabaseType();
	}

	@Override
	protected boolean isDriverClassExpected() {
		return true;
	}
}
