package com.j256.ormlite.table;


public class DerbyTableUtilsTest extends BaseTableUtilsTest {

	@Override
	protected void setDatabaseParams() {
		System.setProperty("derby.stream.error.file", "target/derby.log");
		databaseUrl = "jdbc:derby:target/ormlitederby;create=true";
	}
}
