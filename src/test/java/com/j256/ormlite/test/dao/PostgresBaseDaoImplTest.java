package com.j256.ormlite.test.dao;

import java.util.Iterator;

import org.junit.Test;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.JdbcBaseDaoImplTest;

public class PostgresBaseDaoImplTest extends JdbcBaseDaoImplTest {

	private final static String DB_HOST = "db.be.256.com";

	@Override
	protected void setDatabaseParams() {
		databaseHost = DB_HOST;
		databaseUrl = "jdbc:postgresql://" + DB_HOST + "/ormlitetest";
		userName = "ormlitetest";
		password = "hibernate";
	}

	@Test
	@Override
	public void testCloseInIterator() throws Exception {
		Dao<Foo, Integer> fooDao = createDao(Foo.class, true);
		Foo foo1 = new Foo();
		foo1.stuff = "s1";
		fooDao.create(foo1);
		Iterator<Foo> iterator = fooDao.iterator();
		while (iterator.hasNext()) {
			iterator.next();
			// does not break the iterator in postgres
			closeConnectionSource();
		}
	}

	@Test
	@Override
	public void testCloseIteratorBeforeNext() throws Exception {
		Dao<Foo, Integer> fooDao = createDao(Foo.class, true);
		Foo foo1 = new Foo();
		foo1.stuff = "s1";
		fooDao.create(foo1);
		Iterator<Foo> iterator = fooDao.iterator();
		while (iterator.hasNext()) {
			// does not close the result set in postgres
			closeConnectionSource();
			iterator.next();
		}
	}
}
