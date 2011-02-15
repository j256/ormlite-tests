package com.j256.ormlite.test.table;

import com.j256.ormlite.table.JdbcTableUtilsTest;

public class DerbyTableUtilsTest extends JdbcTableUtilsTest {

	@Override
	protected void setDatabaseParams() {
		System.setProperty("derby.stream.error.file", "target/derby.log");
		databaseUrl = "jdbc:derby:target/ormlitederby;create=true";
	}
}
