package com.j256.ormlite.dao;

import org.junit.Test;

public class PostgresBaseDaoImplTest extends JdbcBaseDaoImplTest {

	@Override
	protected void setDatabaseParams() {
		databaseHost = "db.be.256.com";
		databaseUrl = "jdbc:postgresql://" + databaseHost + "/ormlitetest";
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
		CloseableIterator<Foo> iterator = fooDao.iterator();
		while (iterator.hasNext()) {
			iterator.next();
			// does not break the iterator in postgres
			closeConnectionSource();
		}
		iterator.close();
	}

	@Test
	@Override
	public void testCloseIteratorBeforeNext() throws Exception {
		Dao<Foo, Integer> fooDao = createDao(Foo.class, true);
		Foo foo1 = new Foo();
		foo1.stuff = "s1";
		fooDao.create(foo1);
		CloseableIterator<Foo> iterator = fooDao.iterator();
		while (iterator.hasNext()) {
			// does not close the result set in postgres
			closeConnectionSource();
			iterator.next();
		}
		iterator.close();
	}
}
