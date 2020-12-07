package com.j256.ormlite.stmt;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.jdbc.BaseJdbcTest;

public class SqliteRawRowMapperImplTest extends BaseJdbcTest {

	@Override
	protected void setDatabaseParams() {
		databaseUrl = "jdbc:sqlite:";
	}

	@Test
	public void testBasic() throws Exception {
		Dao<MyDouble, Object> dao = createDao(MyDouble.class, true);

		MyDouble foo = new MyDouble();
		foo.val = 1.23456789123456;
		assertEquals(1, dao.create(foo));

		MyDouble result = dao.queryForId(foo.id);
		assertNotNull(result);
		assertEquals(foo.val, result.val, 0.0);

		GenericRawResults<String[]> results = dao.queryRaw(dao.queryBuilder().prepareStatementString());
		CloseableIterator<String[]> iterator = results.closeableIterator();
		try {
			assertTrue(iterator.hasNext());
			String[] strings = iterator.next();
			assertNotNull(strings);

			assertTrue(strings.length >= 2);
			assertEquals(Integer.toString(foo.id), strings[0]);
			assertEquals(Double.toString(foo.val), strings[1]);
		} finally {
			iterator.close();
		}
	}

	protected static class MyDouble {
		@DatabaseField(generatedId = true)
		public int id;
		@DatabaseField
		public double val;
		public MyDouble() {
			// needed for ormlite
		}
	}
}
