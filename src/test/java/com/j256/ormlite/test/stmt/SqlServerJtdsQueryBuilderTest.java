package com.j256.ormlite.test.stmt;

import com.j256.ormlite.stmt.JdbcQueryBuilderTest;

public class SqlServerJtdsQueryBuilderTest extends JdbcQueryBuilderTest {

	private final static String DB_HOST = "wfs2.jprinc.net";

	@Override
	protected void setDatabaseParams() {
		databaseHost = DB_HOST;
		databaseUrl = "jdbc:jtds:sqlserver://" + DB_HOST + ":1433/ormlite;ssl=request";
		userName = "gwatson";
		password = "ormlite";
	}
}
