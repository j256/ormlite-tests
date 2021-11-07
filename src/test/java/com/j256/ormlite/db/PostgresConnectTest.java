package com.j256.ormlite.db;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.jdbc.db.PostgresDatabaseType;
import com.j256.ormlite.jdbc.db.PostgresDatabaseTypeTest;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Does tests on a real database connection. I didn't want to have to require someone to have have downloaded derby
 * database to have the ormlite tests work.
 */
public class PostgresConnectTest extends PostgresDatabaseTypeTest {

	@Override
	protected void setDatabaseParams() {
		databaseHost = "db.be.256stuff.com";
		databaseUrl = "jdbc:postgresql://" + databaseHost + "/ormlitetest";
		userName = "ormlitetest";
		password = "hibernate";
		databaseType = new PostgresDatabaseType();
	}

	@Override
	protected boolean isDriverClassExpected() {
		return true;
	}

	@Override
	public void testEscapedEntityName() {
		super.testEscapedEntityName();
	}

	@Test
	public void testMixedCaseSequenceName() throws Exception {
		if (connectionSource == null) {
			return;
		}
		Dao<TableNameCaseWithSeqeuence, Integer> dao = createDao(TableNameCaseWithSeqeuence.class, true);
		TableNameCaseWithSeqeuence obj = new TableNameCaseWithSeqeuence();
		obj.field = "hello";
		assertEquals(1, dao.create(obj));
		assertTrue(obj.id != 0);
		TableNameCaseWithSeqeuence result = dao.queryForId(obj.id);
		assertEquals(obj.id, result.id);
		assertEquals(obj.field, result.field);
	}

	@DatabaseTable(tableName = "TableNameCaseWithSeqeuence")
	private static class TableNameCaseWithSeqeuence {
		@DatabaseField(generatedId = true)
		public int id;
		@DatabaseField
		public String field;
	}
}
