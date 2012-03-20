package com.j256.ormlite.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

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

	@Test
	public void testAlterTable() throws Exception {
		Dao<Foo, Object> dao = createDao(Foo.class, true);
		Dao<FooAge, Object> ageDao = createDao(FooAge.class, false);
		Foo foo = new Foo();
		String stuff = "feowpjfpewf";
		foo.stuff = stuff;
		assertEquals(1, dao.create(foo));
		String newCol = "age";
		int newColDefault = 1;
		dao.executeRaw("ALTER TABLE `footable` ADD COLUMN `" + newCol + "` INTEGER DEFAULT " + newColDefault);

		List<FooAge> list = ageDao.queryForEq(newCol, newColDefault);
		assertEquals(1, list.size());
		assertEquals(newColDefault, list.get(0).age);

		int newVal = 123231;
		dao.executeRaw("UPDATE `footable` SET `" + newCol + "` = " + newVal);
		list = ageDao.queryForEq(newCol, newVal);
		assertEquals(1, list.size());
		assertEquals(newVal, list.get(0).age);
	}

	@Override
	@Test
	public void testIteratorMove() {
		// noop because SQLite only supports forward
	}

	@DatabaseTable(tableName = FOO_TABLE_NAME)
	protected static class FooAge extends Foo {
		@DatabaseField
		public int age;
		public FooAge() {
		}
	}
}
