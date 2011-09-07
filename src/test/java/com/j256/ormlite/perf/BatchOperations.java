package com.j256.ormlite.perf;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import org.junit.Test;

import com.j256.ormlite.BaseJdbcTest;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;

public class BatchOperations extends BaseJdbcTest {

	@Override
	protected void setDatabaseParams() {
		databaseUrl = "jdbc:sqlite:";
	}

	@Test
	public void testMassCreates() throws Exception {
		final Dao<GenIdFoo, Integer> fooDao = createDao(GenIdFoo.class, true);
		final List<Integer> fooIdList = new ArrayList<Integer>();
		fooDao.callBatchTasks(new Callable<Void>() {
			public Void call() throws Exception {
				for (int i = 0; i < 10000; i++) {
					GenIdFoo foo = new GenIdFoo();
					assertEquals(1, fooDao.create(foo));
					fooIdList.add(foo.id);
				}
				return null;
			}
		});
	}

	@Test
	public void testNonBatch() throws Exception {
		final Dao<GenIdFoo, Integer> fooDao = createDao(GenIdFoo.class, true);
		final List<Integer> fooIdList = new ArrayList<Integer>();
		for (int i = 0; i < 10000; i++) {
			GenIdFoo foo = new GenIdFoo();
			assertEquals(1, fooDao.create(foo));
			fooIdList.add(foo.id);
		}
	}

	protected static class GenIdFoo {
		@DatabaseField(generatedId = true)
		int id;

		@DatabaseField
		String stuff;

		public GenIdFoo() {
		}
	}

}
