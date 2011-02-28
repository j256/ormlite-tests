package com.j256.ormlite.test.stmt;

import java.sql.SQLException;

import org.junit.Test;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.JdbcQueryBuilderTest;
import com.j256.ormlite.stmt.QueryBuilder;

public class SqlServerJtdsQueryBuilderTest extends JdbcQueryBuilderTest {

	private final static String DB_HOST = "wfs2.jprinc.net";

	@Override
	protected void setDatabaseParams() {
		databaseHost = DB_HOST;
		databaseUrl = "jdbc:jtds:sqlserver://" + DB_HOST + ":1433/ormlite;ssl=request";
		userName = "gwatson";
		password = "ormlite";
	}
	
	@Override
	@Test(expected = SQLException.class)
	public void testOffsetWithLimit() throws Exception {
		Dao<Foo, Object> dao = createDao(Foo.class, true);
		QueryBuilder<Foo, Object> qb = dao.queryBuilder();
		qb.limit(1);
		qb.offset(1);
	}

	@Override
	@Test(expected = SQLException.class)
	public void testOffsetNoLimit() throws Exception {
		Dao<Foo, Object> dao = createDao(Foo.class, true);
		QueryBuilder<Foo, Object> qb = dao.queryBuilder();
		qb.offset(1);
	}
}
