package com.j256.ormlite.dao;

import static org.junit.Assert.assertFalse;

import java.io.File;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.jdbc.dao.JdbcBaseDaoImplTest;
import com.j256.ormlite.table.DatabaseTable;

public class DerbyEmbeddedBaseDaoImplTest extends JdbcBaseDaoImplTest {

	private final static String MIXED_CASE_TABLE_NAME = "mixedCaseTable";
	private static final String DB_DIR_PREFIX = "ormlite_derby";
	private final AtomicInteger derbyCount = new AtomicInteger();

	@BeforeClass
	public static void beforeClass() {
		for (File file : new File("target").listFiles()) {
			if (file.getName().startsWith(DB_DIR_PREFIX)) {
				removeDirectory(file);
			}
		}
	}

	@Override
	protected void setDatabaseParams() {
		System.setProperty("derby.stream.error.file", "target/derby.log");
		System.setProperty("derby.language.logStatementText", "true");
		String dbDir = "target/" + DB_DIR_PREFIX + derbyCount.incrementAndGet();
		databaseUrl = "jdbc:derby:" + dbDir + ";create=true";
	}

	@Test
	public void testCreateQueryCaseIssue() throws Exception {
		Dao<MixedCase, Object> fooDao = createDao(MixedCase.class, true);
		GenericRawResults<String[]> results = fooDao.queryRaw("select * from " + MIXED_CASE_TABLE_NAME);
		assertFalse(results.iterator().hasNext());
	}

	@Test
	@Ignore
	@Override
	public void testAutoCommitClose() {
		// getting some IOException here
	}

	@DatabaseTable(tableName = "mixedCaseTable")
	protected static class MixedCase {
		@DatabaseField(columnName = MIXED_CASE_TABLE_NAME)
		String stuff;

		MixedCase() {
		}
	}
}
