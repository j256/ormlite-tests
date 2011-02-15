package com.j256.ormlite.test.dao;

import com.j256.ormlite.dao.JdbcBaseDaoImplTest;

public class SqliteBaseDaoImplTest extends JdbcBaseDaoImplTest {

	@Override
	protected void setDatabaseParams() {
		databaseUrl = "jdbc:sqlite:";
	}
}
