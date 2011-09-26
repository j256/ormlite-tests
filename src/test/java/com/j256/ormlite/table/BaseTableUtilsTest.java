package com.j256.ormlite.table;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.sql.SQLException;

import org.junit.Test;

import com.j256.ormlite.BaseJdbcTest;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;

public class BaseTableUtilsTest extends BaseJdbcTest {

	@Test
	public void testMissingCreate() throws Exception {
		if (connectionSource == null) {
			return;
		}
		// we needed to do this because of some race conditions around table clearing
		dropTable(Foo.class, true);
		Dao<Foo, Integer> fooDao = createDao(Foo.class, false);
		try {
			fooDao.queryForAll();
			fail("Should have thrown");
		} catch (SQLException e) {
			// expected
		}
	}

	@Test
	public void testCreateTable() throws Exception {
		Dao<Foo, Integer> fooDao = createDao(Foo.class, false);
		// first we create the table
		createTable(Foo.class, false);
		// test it out
		assertEquals(0, fooDao.queryForAll().size());
		// now we drop it
		dropTable(Foo.class, true);
		try {
			// query should fail
			fooDao.queryForAll();
			fail("Was expecting a SQL exception");
		} catch (Exception expected) {
			// expected
		}
		// now create it again
		createTable(Foo.class, false);
		assertEquals(0, fooDao.queryForAll().size());
		dropTable(Foo.class, true);
	}

	@Test
	public void testDropThenQuery() throws Exception {
		Dao<Foo, Integer> fooDao = createDao(Foo.class, true);
		assertEquals(0, fooDao.queryForAll().size());
		dropTable(Foo.class, true);
		try {
			fooDao.queryForAll();
			fail("Should have thrown");
		} catch (SQLException e) {
			// expected
		}
	}

	@Test
	public void testRawExecuteDropThenQuery() throws Exception {
		Dao<Foo, Integer> fooDao = createDao(Foo.class, true);
		StringBuilder sb = new StringBuilder();
		sb.append("DROP TABLE ");
		if (databaseType.isEntityNamesMustBeUpCase()) {
			databaseType.appendEscapedEntityName(sb, "FOO");
		} else {
			databaseType.appendEscapedEntityName(sb, "foo");
		}
		// can't check the return value because of sql-server
		fooDao.executeRaw(sb.toString());
		try {
			fooDao.queryForAll();
			fail("Should have thrown");
		} catch (SQLException e) {
			// expected
		}
	}

	@Test
	public void testDoubleDrop() throws Exception {
		Dao<Foo, Integer> fooDao = createDao(Foo.class, false);
		// first we create the table
		createTable(Foo.class, false);
		// test it out
		assertEquals(0, fooDao.queryForAll().size());
		// now we drop it
		dropTable(Foo.class, true);
		// this should fail
		try {
			dropTable(Foo.class, false);
			fail("Should have thrown");
		} catch (SQLException e) {
			// expected
		}
	}

	@Test
	public void testClearTable() throws Exception {
		Dao<Foo, Integer> fooDao = createDao(Foo.class, true);
		assertEquals(0, fooDao.countOf());
		Foo foo = new Foo();
		assertEquals(1, fooDao.create(foo));
		assertEquals(1, fooDao.countOf());
		TableUtils.clearTable(connectionSource, Foo.class);
		assertEquals(0, fooDao.countOf());
	}

	@Test
	public void testCreateTableConfigIfNotExists() throws Exception {
		if (databaseType == null || connectionSource == null || !databaseType.isCreateIfNotExistsSupported()) {
			return;
		}
		TableUtils.dropTable(connectionSource, Foo.class, true);
		Dao<Foo, Integer> fooDao = createDao(Foo.class, false);
		try {
			fooDao.countOf();
			fail("Should have thrown an exception");
		} catch (Exception e) {
			// ignored
		}
		DatabaseTableConfig<Foo> tableConfig = DatabaseTableConfig.fromClass(connectionSource, Foo.class);
		TableUtils.createTableIfNotExists(connectionSource, tableConfig);
		assertEquals(0, fooDao.countOf());
		// should not throw
		TableUtils.createTableIfNotExists(connectionSource, tableConfig);
		assertEquals(0, fooDao.countOf());
	}

	protected static class Foo {
		@DatabaseField(generatedId = true)
		int id;
		@DatabaseField
		String name;
	}
}
