package com.j256.ormlite.test.stmt;

import com.j256.ormlite.stmt.JdbcQueryBuilderTest;

public class DerbyEmbeddedQueryBuilderTest extends JdbcQueryBuilderTest {

	@Override
	protected void setDatabaseParams() {
		System.setProperty("derby.stream.error.file", "target/derby.log");
		databaseUrl = "jdbc:derby:target/ormlitederby;create=true";
	}
}
