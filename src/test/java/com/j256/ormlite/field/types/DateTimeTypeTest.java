package com.j256.ormlite.field.types;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;

import org.joda.time.DateTime;
import org.junit.Test;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.field.SqlType;
import com.j256.ormlite.support.DatabaseResults;
import com.j256.ormlite.table.DatabaseTable;

/**
 * For testing {@link DateTime} which we can't do in com.ormlite.core because we don't want the dependency.
 * 
 * @author graywatson
 */
public class DateTimeTypeTest extends BaseTypeTest {

	private static final String DATE_TIME_COLUMN = "dateTime";

	@Test
	public void testDateString() throws Exception {
		Class<LocalDateTime> clazz = LocalDateTime.class;
		Dao<LocalDateTime, Object> dao = createDao(clazz, true);
		DateTime val = new DateTime();
		long sqlVal = val.getMillis();
		String valStr = Long.toString(sqlVal);
		LocalDateTime foo = new LocalDateTime();
		foo.dateTime = val;
		assertEquals(1, dao.create(foo));
		testType(dao, foo, clazz, val, sqlVal, sqlVal, valStr, DataType.DATE_TIME, DATE_TIME_COLUMN, false, false,
				false, false, false, false, true, false);
	}

	@Test
	public void testDateTimeNull() throws Exception {
		Class<LocalDateTime> clazz = LocalDateTime.class;
		Dao<LocalDateTime, Object> dao = createDao(clazz, true);
		LocalDateTime foo = new LocalDateTime();
		assertEquals(1, dao.create(foo));
		testType(dao, foo, clazz, null, null, null, null, DataType.DATE_TIME, DATE_TIME_COLUMN, false, false, false,
				false, false, false, true, false);
	}

	@Test
	public void testJavaToSqlArg() throws Exception {
		DateTime dateTime = new DateTime();
		assertEquals(dateTime.getMillis(), DateTimeType.getSingleton().javaToSqlArg(null, dateTime));
	}

	@Test
	public void testParseDefaultString() throws SQLException {
		Long value = 423424234234L;
		assertEquals(value, DateTimeType.getSingleton().parseDefaultString(null, value.toString()));
	}

	@Test
	public void testResultToSqlArg() throws Exception {
		DatabaseResults results = createMock(DatabaseResults.class);
		int col = 21;
		long value = 2094234324L;
		expect(results.getLong(col)).andReturn(value);
		replay(results);
		assertEquals(new DateTime(value), DateTimeType.getSingleton().resultToJava(null, results, col));
	}

	@Test
	public void testPersist() throws Exception {
		Dao<StoreDateTime, Object> dao = createDao(StoreDateTime.class, true);
		StoreDateTime foo = new StoreDateTime();
		foo.dateTime = new DateTime();
		assertEquals(1, dao.create(foo));

		StoreDateTime result = dao.queryForId(foo.id);
		assertEquals(result.dateTime, foo.dateTime);
	}

	@Test(expected = SQLException.class)
	public void testDateTimeParseInvalid() throws Exception {
		FieldType fieldType = FieldType.createFieldType(connectionSource, TABLE_NAME,
				LocalDateTime.class.getDeclaredField(DATE_TIME_COLUMN), LocalDateTime.class);
		DataType.DATE_TIME.getDataPersister().parseDefaultString(fieldType, "not valid long number");
	}

	@Test
	public void testCoverage() {
		new DateTimeType(SqlType.LONG, new Class[0]);
		assertEquals(DateTime.class, DateTimeType.getSingleton().getPrimaryClass());
	}

	@Test
	public void testVersion() throws Exception {
		assertTrue(DateTimeType.getSingleton().isValidForVersion());

		Dao<VersionDateTime, Object> dao = createDao(VersionDateTime.class, true);
		VersionDateTime foo = new VersionDateTime();
		long before = System.currentTimeMillis();
		assertEquals(1, dao.create(foo));
		long after = System.currentTimeMillis();
		assertNotNull(foo.dateTime);
		long first = foo.dateTime.getMillis();
		assertTrue(first >= before);
		assertTrue(first <= after);

		before = System.currentTimeMillis();
		assertEquals(1, dao.update(foo));
		after = System.currentTimeMillis();
		assertTrue(foo.dateTime.getMillis() >= before);
		assertTrue(foo.dateTime.getMillis() <= after);
		assertTrue(foo.dateTime.getMillis() > first);
	}

	@DatabaseTable(tableName = TABLE_NAME)
	protected static class LocalDateTime {
		@DatabaseField(columnName = DATE_TIME_COLUMN)
		DateTime dateTime;
	}

	@DatabaseTable(tableName = TABLE_NAME)
	protected static class VersionDateTime {
		@DatabaseField(generatedId = true)
		int id;
		@DatabaseField(columnName = DATE_TIME_COLUMN, version = true)
		DateTime dateTime;
	}

	@DatabaseTable(tableName = TABLE_NAME)
	protected static class StoreDateTime {
		@DatabaseField(generatedId = true)
		int id;
		@DatabaseField
		DateTime dateTime;
	}
}
