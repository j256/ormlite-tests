package com.j256.ormlite.test.dao;

import static org.junit.Assert.assertFalse;

import org.junit.Test;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.dao.JdbcBaseDaoImplTest;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

public class HsqldbBaseDaoImplTest extends JdbcBaseDaoImplTest {

	private final static String MIXED_CASE_TABLE_NAME = "mixedCaseTable";

	@Override
	protected void setDatabaseParams() {
		databaseUrl = "jdbc:hsqldb:mem:ormlitehsqldb";
	}

	@Test
	public void testCreateQueryCaseIssue() throws Exception {
		Dao<MixedCase, Object> fooDao = createDao(MixedCase.class, true);
		GenericRawResults<String[]> results = fooDao.queryRaw("select * from " + MIXED_CASE_TABLE_NAME);
		assertFalse(results.iterator().hasNext());
	}

	@DatabaseTable(tableName = "mixedCaseTable")
	protected static class MixedCase {
		@DatabaseField(columnName = MIXED_CASE_TABLE_NAME)
		String stuff;
		MixedCase() {
		}
	}
}
