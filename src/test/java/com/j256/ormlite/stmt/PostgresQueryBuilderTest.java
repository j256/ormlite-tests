package com.j256.ormlite.stmt;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.jdbc.stmt.JdbcQueryBuilderTest;

public class PostgresQueryBuilderTest extends JdbcQueryBuilderTest {

	@Override
	protected void setDatabaseParams() {
		databaseHost = "db.be.256stuff.com";
		databaseUrl = "jdbc:postgresql://" + databaseHost + "/ormlitetest";
		userName = "ormlitetest";
		password = "hibernate";
	}

	@Override
	@Test
	public void testOffsetNoLimit() throws Exception {
		Dao<Foo, Object> dao = createDao(Foo.class, true);
		Foo foo1 = new Foo();
		foo1.id = "stuff1";
		assertEquals(1, dao.create(foo1));
		Foo foo2 = new Foo();
		foo2.id = "stuff2";
		assertEquals(1, dao.create(foo2));
		assertEquals(2, dao.queryForAll().size());

		QueryBuilder<Foo, Object> qb = dao.queryBuilder();
		qb.offset(1L);
		List<Foo> results = dao.query(qb.prepare());
		assertEquals(1, results.size());
	}
}
