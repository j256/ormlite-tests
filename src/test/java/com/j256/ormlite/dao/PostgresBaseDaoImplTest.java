package com.j256.ormlite.dao;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.types.CharType;

public class PostgresBaseDaoImplTest extends JdbcBaseDaoImplTest {

	@Override
	protected void setDatabaseParams() {
		databaseHost = "db.be.256.com";
		databaseUrl = "jdbc:postgresql://" + databaseHost + "/ormlitetest";
		userName = "ormlitetest";
		password = "hibernate";
	}

	/**
	 * Postgres doesn't like storing a '\0' character. Without the specific code in
	 * {@link CharType#javaToSqlArg(com.j256.ormlite.field.FieldType, Object)} it was giving the error:
	 * 
	 * Caused by: org.postgresql.util.PSQLException: ERROR: invalid byte sequence for encoding "UTF8": 0x00
	 */
	@Test
	public void testPostgresChar() throws Exception {
		Dao<PostgresCharNull, Integer> dao = createDao(PostgresCharNull.class, true);
		PostgresCharNull nullChar = new PostgresCharNull();
		nullChar.charField = '\0';
		assertEquals(1, dao.create(nullChar));
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

	@Test
	@Override
	public void testCloseIteratorBeforeRemove() {
		// ignored here
	}

	protected static class PostgresCharNull {
		@DatabaseField(generatedId = true)
		int id;
		@DatabaseField
		char charField;
		PostgresCharNull() {
		}
	}
}
