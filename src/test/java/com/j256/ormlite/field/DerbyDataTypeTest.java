package com.j256.ormlite.field;

public class DerbyDataTypeTest extends BaseDataTypeTest {

	@Override
	protected void setDatabaseParams() {
		System.setProperty("derby.stream.error.file", "target/derby.log");
		databaseUrl = "jdbc:derby:target/ormlitederby;create=true";
	}
}
