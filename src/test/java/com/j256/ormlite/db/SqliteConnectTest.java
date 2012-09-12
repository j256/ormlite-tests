package com.j256.ormlite.db;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;

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

	@Test
	public void testUpdateBuilderSerializable() throws Exception {
		Dao<SerializedUpdate, Integer> dao = createDao(SerializedUpdate.class, true);
		SerializedUpdate foo = new SerializedUpdate();
		SerializedField serialized1 = new SerializedField("wow");
		foo.serialized = serialized1;
		assertEquals(1, dao.create(foo));

		SerializedUpdate result = dao.queryForId(foo.id);
		assertNotNull(result);
		assertNotNull(result.serialized);
		assertEquals(serialized1.foo, result.serialized.foo);

		// update with dao.update
		SerializedField serialized2 = new SerializedField("zip");
		foo.serialized = serialized2;
		assertEquals(1, dao.update(foo));

		result = dao.queryForId(foo.id);
		assertNotNull(result);
		assertNotNull(result.serialized);
		assertEquals(serialized2.foo, result.serialized.foo);

		// update with UpdateBuilder
		SerializedField serialized3 = new SerializedField("crack");
		UpdateBuilder<SerializedUpdate, Integer> ub = dao.updateBuilder();
		ub.updateColumnValue(SerializedUpdate.SERIALIZED_FIELD_NAME, serialized3);
		ub.where().idEq(foo.id);
		assertEquals(1, ub.update());

		result = dao.queryForId(foo.id);
		assertNotNull(result);
		assertNotNull(result.serialized);
		assertEquals(serialized3.foo, result.serialized.foo);
	}

	@Test
	public void testDateStrftime() throws Exception {
		Dao<DateStrftime, Object> dao = createDao(DateStrftime.class, true);
		DateTime dateTime = new DateTime();

		DateStrftime foo1 = new DateStrftime();
		foo1.date = dateTime.toDate();
		assertEquals(1, dao.create(foo1));

		DateStrftime foo2 = new DateStrftime();
		foo2.date = dateTime.plusMonths(1).toDate();
		assertEquals(1, dao.create(foo2));

		DateTimeFormatter formatter = DateTimeFormat.forPattern("MM-YYYY");
		String format = formatter.print(dateTime);

		QueryBuilder<DateStrftime, Object> qb = dao.queryBuilder();
		qb.where().raw("strftime('%m-%Y', date) = '" + format + "'");
		List<DateStrftime> results = qb.query();
		assertEquals(1, results.size());
		assertEquals(foo1.id, results.get(0).id);
	}

	@Test(expected = SQLException.class)
	public void testIdConstraint() throws Exception {
		Dao<IdConstraint, Integer> dao = createDao(IdConstraint.class, true);
		IdConstraint foo = new IdConstraint();
		foo.id = 10;
		assertEquals(1, dao.create(foo));

		// try to insert it again
		dao.create(foo);
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

	protected static class SerializedUpdate {
		public final static String SERIALIZED_FIELD_NAME = "serialized";
		@DatabaseField(generatedId = true)
		public int id;
		@DatabaseField(dataType = DataType.SERIALIZABLE, columnName = SERIALIZED_FIELD_NAME)
		public SerializedField serialized;
		public SerializedUpdate() {
		}
	}

	protected static class SerializedField implements Serializable {
		private static final long serialVersionUID = 4531762180289888888L;
		String foo;
		public SerializedField(String foo) {
			this.foo = foo;
		}
	}

	protected static class DateStrftime {
		@DatabaseField(generatedId = true)
		int id;
		@DatabaseField(dataType = DataType.DATE_STRING)
		Date date;
		public DateStrftime() {
		}
	}

	protected static class IdConstraint {
		@DatabaseField(id = true)
		int id;
		@DatabaseField
		String stuff;
		public IdConstraint() {
		}
	}
}
