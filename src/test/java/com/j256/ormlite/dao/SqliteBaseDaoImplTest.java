package com.j256.ormlite.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class SqliteBaseDaoImplTest extends JdbcBaseDaoImplTest {

	@Override
	protected void setDatabaseParams() {
		databaseUrl = "jdbc:sqlite:";
	}

	@Test
	public void testStoreUnicode() throws Exception {
		Dao<Foo, Object> dao = createDao(Foo.class, true);
		Foo foo = new Foo();
		String unicodeString = "上海";
		foo.stuff = unicodeString;
		assertEquals(1, dao.create(foo));

		Foo result = dao.queryForId(foo.id);
		assertNotNull(result);
		assertEquals(unicodeString, result.stuff);
	}
}
