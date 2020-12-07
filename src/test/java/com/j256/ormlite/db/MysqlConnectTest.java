package com.j256.ormlite.db;

import java.sql.SQLException;

import org.junit.Test;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.jdbc.db.MysqlDatabaseType;
import com.j256.ormlite.jdbc.db.MysqlDatabaseTypeTest;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Does tests on a real database connection. I didn't want to have to require someone to have have downloaded derby
 * database to have the ormlite tests work.
 */
public class MysqlConnectTest extends MysqlDatabaseTypeTest {

	private static final String FOOINT_TABLE_NAME = "fooint";

	@Override
	protected void setDatabaseParams() {
		databaseHost = "db.be.256stuff.com";
		databaseUrl = "jdbc:mysql://" + databaseHost + "/ormlitetest";
		userName = "ormlitetest";
		password = "hibernate";
		databaseType = new MysqlDatabaseType();
	}

	@Override
	protected boolean isDriverClassExpected() {
		return true;
	}

	@Test(expected = SQLException.class)
	public void testGeneratedIdNoReturn() throws Exception {
		createDao(FooNotGeneratedId.class, true);
		Dao<FooInt, Object> genDao = createDao(FooInt.class, false);
		FooInt foo = new FooInt();
		foo.stuff = "hello";
		genDao.create(foo);
	}

	@DatabaseTable(tableName = FOOINT_TABLE_NAME)
	protected static class FooInt {
		@DatabaseField(generatedId = true)
		public int id;
		@DatabaseField
		public String stuff;
		FooInt() {
		}
	}

	@DatabaseTable(tableName = FOOINT_TABLE_NAME)
	protected static class FooNotGeneratedId {
		@DatabaseField
		public int id;
		@DatabaseField
		public String stuff;
		FooNotGeneratedId() {
		}
	}
}
