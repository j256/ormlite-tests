package com.j256.ormlite.dao;

import static org.junit.Assert.assertFalse;

import org.junit.Ignore;
import org.junit.Test;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

public class DerbyEmbeddedBaseDaoImplTest extends JdbcBaseDaoImplTest {

	private final static String MIXED_CASE_TABLE_NAME = "mixedCaseTable";

	@Override
	protected void setDatabaseParams() {
		System.setProperty("derby.stream.error.file", "target/derby.log");
		System.setProperty("derby.language.logStatementText", "true");
		databaseUrl = "jdbc:derby:target/ormlitederby;create=true";
	}

	@Test
	public void testCreateQueryCaseIssue() throws Exception {
		Dao<MixedCase, Object> fooDao = createDao(MixedCase.class, true);
		GenericRawResults<String[]> results = fooDao.queryRaw("select * from " + MIXED_CASE_TABLE_NAME);
		assertFalse(results.iterator().hasNext());
	}

	@Override
	@Test
	@Ignore
	public void testCloseInIterator() {
		// Seem like this causes some sort of memory fault under derby
	}

	@Override
	@Test
	@Ignore
	public void testCloseIteratorBeforeNext() {
		// Seem like this causes some sort of memory fault under derby
	}

	@Override
	@Test
	@Ignore
	public void testCloseIteratorBeforeRemove() {
		// Seem like this causes some sort of memory fault under derby
	}

	@DatabaseTable(tableName = "mixedCaseTable")
	protected static class MixedCase {
		@DatabaseField(columnName = MIXED_CASE_TABLE_NAME)
		String stuff;
		MixedCase() {
		}
	}
}
