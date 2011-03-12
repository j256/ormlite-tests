package com.j256.ormlite.misc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Callable;

import org.junit.Before;
import org.junit.Test;

import com.j256.ormlite.BaseJdbcTest;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;

public abstract class BaseTransactionManagerTest extends BaseJdbcTest {

	@Override
	@Before
	public void before() throws Exception {
		if (connectionSource != null) {
			return;
		}
		super.before();
		if (connectionSource != null) {
			connectionSource = new JdbcPooledConnectionSource(databaseUrl, userName, password);
		}
	}

	/* ============================================================================================================== */

	@Test
	public void testDaoTransactionManagerCommitted() throws Exception {
		if (connectionSource == null) {
			return;
		}
		TransactionManager mgr = new TransactionManager(connectionSource);
		final Dao<Foo, Integer> fooDao = createDao(Foo.class, true);
		testTransactionManager(mgr, null, fooDao, false);
	}

	@Test
	public void testRollBack() throws Exception {
		if (connectionSource == null) {
			return;
		}
		TransactionManager mgr = new TransactionManager(connectionSource);
		final Dao<Foo, Integer> fooDao = createDao(Foo.class, true);
		testTransactionManager(mgr, new RuntimeException("What!!  I protest!!"), fooDao, false);
	}

	@Test
	public void testSpringWiredRollBack() throws Exception {
		if (connectionSource == null) {
			return;
		}
		TransactionManager mgr = new TransactionManager();
		mgr.setConnectionSource(connectionSource);
		mgr.initialize();
		final Dao<Foo, Integer> fooDao = createDao(Foo.class, true);
		testTransactionManager(mgr, new RuntimeException("What!!  I protest!!"), fooDao, false);
	}

	@Test
	public void testNonRuntimeExceptionWiredRollBack() throws Exception {
		if (connectionSource == null) {
			return;
		}
		TransactionManager mgr = new TransactionManager();
		mgr.setConnectionSource(connectionSource);
		mgr.initialize();
		final Dao<Foo, Integer> fooDao = createDao(Foo.class, true);
		testTransactionManager(mgr, new Exception("What!!  I protest via an Exception!!"), fooDao, false);
	}

	@Test
	public void testTransactionWithinTransaction() throws Exception {
		if (connectionSource == null) {
			return;
		}
		final TransactionManager mgr = new TransactionManager(connectionSource);
		final Dao<Foo, Integer> fooDao = createDao(Foo.class, true);
		mgr.callInTransaction(new Callable<Void>() {
			public Void call() throws Exception {
				testTransactionManager(mgr, null, fooDao, true);
				return null;
			}
		});
	}

	@Test
	public void testTransactionWithinTransactionThrows() throws Exception {
		if (connectionSource == null || !databaseType.isNestedSavePointsSupported()) {
			return;
		}
		final TransactionManager mgr = new TransactionManager(connectionSource);
		final Dao<Foo, Integer> fooDao = createDao(Foo.class, true);
		final Foo outerFoo = new Foo();
		String stuff = "outer stuff";
		outerFoo.stuff = stuff;
		assertEquals(1, fooDao.create(outerFoo));
		assertNotNull(fooDao.queryForId(outerFoo.id));
		assertEquals(1, fooDao.queryForAll().size());
		mgr.callInTransaction(new Callable<Void>() {
			public Void call() throws Exception {
				assertEquals(1, fooDao.delete(outerFoo));
				assertNull(fooDao.queryForId(outerFoo.id));
				assertEquals(0, fooDao.queryForAll().size());
				testTransactionManager(mgr, new Exception("The inner transaction should throw and get rolled back"),
						fooDao, true);
				return null;
			}
		});
		List<Foo> fooList = fooDao.queryForAll();
		assertEquals(1, fooList.size());
		assertNull(fooDao.queryForId(outerFoo.id));
		// however we should have deleted the outer foo and are left with the inner foo
		assertTrue(fooList.get(0).id != outerFoo.id);
	}

	@Test
	public void testNestedTransactionsNotSupported() throws Exception {
		if (connectionSource == null || databaseType.isNestedSavePointsSupported()) {
			return;
		}
		final TransactionManager mgr = new TransactionManager(connectionSource);
		final Dao<Foo, Integer> fooDao = createDao(Foo.class, true);
		final Foo outerFoo = new Foo();
		String stuff = "outer stuff";
		outerFoo.stuff = stuff;
		assertEquals(1, fooDao.create(outerFoo));
		assertNotNull(fooDao.queryForId(outerFoo.id));
		assertEquals(1, fooDao.queryForAll().size());
		mgr.callInTransaction(new Callable<Void>() {
			public Void call() throws Exception {
				assertEquals(1, fooDao.delete(outerFoo));
				assertNull(fooDao.queryForId(outerFoo.id));
				assertEquals(0, fooDao.queryForAll().size());
				testTransactionManager(mgr, new Exception("The inner transaction should throw and get rolled back"),
						fooDao, true);
				return null;
			}
		});
		List<Foo> fooList = fooDao.queryForAll();
		/*
		 * The inner transaction throws an exception but we don't want the inner transaction rolled back since an
		 * exception is caught there.
		 */
		assertEquals(0, fooList.size());
	}

	private void testTransactionManager(TransactionManager mgr, final Exception exception,
			final Dao<Foo, Integer> fooDao, boolean inner) throws Exception {
		final Foo foo1 = new Foo();
		String stuff = "stuff";
		foo1.stuff = stuff;
		assertEquals(1, fooDao.create(foo1));
		assertNotNull(fooDao.queryForId(foo1.id));
		try {
			final int val = 13431231;
			int returned = mgr.callInTransaction(new Callable<Integer>() {
				public Integer call() throws Exception {
					// we delete it inside a transaction
					assertEquals(1, fooDao.delete(foo1));
					// we can't find it
					assertNull(fooDao.queryForId(foo1.id));
					if (exception != null) {
						// but then we throw an exception which rolls back the transaction
						throw exception;
					} else {
						return val;
					}
				}
			});
			if (exception == null) {
				assertEquals(val, returned);
			} else {
				fail("Should have thrown");
			}
		} catch (SQLException e) {
			if (exception == null) {
				throw e;
			} else {
				// expected
			}
		}

		if (exception == null) {
			// still doesn't find it after we delete it
			assertNull(fooDao.queryForId(foo1.id));
		} else {
			// still finds it after we delete it since we threw inside of transaction
			Foo foo2 = fooDao.queryForId(foo1.id);
			if (databaseType.isNestedSavePointsSupported() || (!inner)) {
				assertNotNull(foo2);
				assertEquals(stuff, foo2.stuff);
			} else {
				assertNull(foo2);
			}
		}
	}

	public static class Foo {
		@DatabaseField(generatedId = true)
		int id;
		@DatabaseField
		String stuff;
		Foo() {
			// for ormlite
		}
	}
}
