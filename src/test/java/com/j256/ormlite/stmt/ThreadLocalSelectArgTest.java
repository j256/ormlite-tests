package com.j256.ormlite.stmt;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.Test;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class ThreadLocalSelectArgTest {

	@Test
	public void testLotOThreads() throws Exception {
		JdbcPooledConnectionSource cs = new JdbcPooledConnectionSource("jdbc:h2:mem:ThreadLocalSelectArgTest");
		try {
			TableUtils.createTable(cs, Foo.class);
			doTest(cs);
		} finally {
			TableUtils.dropTable(cs, Foo.class, true);
		}
	}

	private void doTest(ConnectionSource cs) throws Exception {
		final Dao<Foo, String> dao = DaoManager.createDao(cs, Foo.class);
		int numThreads = 10;
		// create a number of objects
		for (int i = 0; i < numThreads; i++) {
			Foo foo = new Foo();
			foo.val = i;
			assertEquals(1, dao.create(foo));
		}

		// build our query
		QueryBuilder<Foo, String> qb = dao.queryBuilder();
		final ThreadLocalSelectArg arg = new ThreadLocalSelectArg();
		qb.where().eq("val", arg);
		final PreparedQuery<Foo> query = qb.prepare();

		// now spawn some threads to do some i/o
		ExecutorService pool = Executors.newFixedThreadPool(numThreads);
		Future<?>[] futures = new Future[numThreads];
		for (int i = 0; i < numThreads; i++) {
			final int num = i;
			futures[i] = pool.submit(new Runnable() {
				public void run() {
					arg.setValue(num);
					// try N queries each looking for this thread's val field
					for (int j = 0; j < 100000; j++) {
						Foo result;
						try {
							result = dao.queryForFirst(query);
						} catch (SQLException e) {
							throw new RuntimeException(e);
						}
						// make sure we got this right
						assertNotNull(result);
						assertEquals(num, result.val);
					}
				}
			});
		}
		pool.shutdown();
		// now join with our finishing tasks
		for (int i = 0; i < numThreads; i++) {
			// this may throw if the inner callable threw above
			futures[i].get();
		}
	}

	protected static class Foo {
		@DatabaseField(generatedId = true)
		public int id;
		@DatabaseField
		public int val;
		public Foo() {
		}
	}
}
