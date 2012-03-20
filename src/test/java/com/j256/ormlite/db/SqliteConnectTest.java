package com.j256.ormlite.db;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;

import org.junit.Test;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;

/**
 * Does tests on a real database connection. I didn't want to have to require someone to have have downloaded derby
 * database to have the ormlite tests work.
 */
public class SqliteConnectTest extends SqliteDatabaseTypeTest {

	@Override
	protected void setDatabaseParams() {
		databaseUrl = "jdbc:sqlite:";
	}

	@Override
	protected boolean isDriverClassExpected() {
		return true;
	}

	@Test
	public void testGeneratedIdInt() throws Exception {
		Dao<IntAutoIncrement, Object> dao = createDao(IntAutoIncrement.class, true);
		IntAutoIncrement foo = new IntAutoIncrement();
		foo.stuff = "fwpojfpwejf";
		assertEquals(0, foo.id);
		assertEquals(1, dao.create(foo));
		assertEquals(1, foo.id);
		IntAutoIncrement result = dao.queryForId(foo.id);
		assertNotSame(foo, result);
		assertEquals(foo.stuff, result.stuff);
	}

	@Test
	public void testGeneratedIdLong() throws Exception {
		Dao<LongAutoIncrement, Object> dao = createDao(LongAutoIncrement.class, true);
		LongAutoIncrement foo = new LongAutoIncrement();
		foo.stuff = "fwpojfpwejf";
		assertEquals(0L, foo.id);
		assertEquals(1, dao.create(foo));
		assertEquals(1L, foo.id);
		LongAutoIncrement result = dao.queryForId(foo.id);
		assertNotSame(foo, result);
		assertEquals(foo.stuff, result.stuff);
	}

	@Test
	public void testWierdColumnNames() throws Exception {
		Dao<WeirdColumnNames, Object> dao = createDao(WeirdColumnNames.class, true);
		WeirdColumnNames foo = new WeirdColumnNames();
		foo.stuff = "peowjfpwjfowefwe";
		assertEquals(1, dao.create(foo));

		WeirdColumnNames result = dao.queryForId(foo.id);
		assertNotNull(result);
		assertEquals(foo.stuff, result.stuff);
	}

	/* ==================================================================== */

	protected static class IntAutoIncrement {
		@DatabaseField(generatedId = true)
		int id;
		@DatabaseField
		String stuff;
		public IntAutoIncrement() {
		}
	}

	protected static class LongAutoIncrement {
		@DatabaseField(generatedId = true)
		long id;
		@DatabaseField
		String stuff;
		public LongAutoIncrement() {
		}
	}

	protected static class WeirdColumnNames {
		@DatabaseField(generatedId = true)
		int id;
		@DatabaseField(columnName = "foo.bar")
		String stuff;
		public WeirdColumnNames() {
		}
	}
}
