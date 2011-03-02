package com.j256.ormlite.test.table;


public class HsqldbTableUtilsTest extends BaseTableUtilsTest {

	@Override
	protected void setDatabaseParams() {
		databaseUrl = "jdbc:hsqldb:mem:ormlitehsqldb";
	}
}
