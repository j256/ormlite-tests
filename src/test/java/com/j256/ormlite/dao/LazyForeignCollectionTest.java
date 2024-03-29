package com.j256.ormlite.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.stream.Stream;

import org.junit.Test;

import com.j256.ormlite.BaseCoreTest;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;

public class LazyForeignCollectionTest extends BaseCoreTest {

	@Test
	public void testConnectionReleasedWhenClosingStream() throws Exception {
		Dao<Lazy, Integer> lazyDao = createDao(Lazy.class, true);
		Lazy lazy = new Lazy();
		lazyDao.create(lazy);

		Dao<Foreign, Integer> foreignDao = createDao(Foreign.class, true);
		Foreign foreign1 = new Foreign();
		foreign1.lazy = lazy;
		foreignDao.create(foreign1);

		Foreign foreign2 = new Foreign();
		foreign2.lazy = lazy;
		foreignDao.create(foreign2);

		Lazy result = lazyDao.queryForId(lazy.id);
		assertNotNull(result.foreign);
		assertFalse(result.foreign.isEager());

		assertEquals(0, connectionSource.getConnectionCount());

		try (Stream<Foreign> foreign = result.foreign.stream()) {
			assertEquals(1, connectionSource.getConnectionCount());
			Foreign foreignFromStream = foreign.findFirst().orElse(null);
			assertNotNull(foreignFromStream);
		}

		assertEquals(0, connectionSource.getConnectionCount());
	}

	protected static class Lazy {
		@DatabaseField(generatedId = true)
		int id;
		@ForeignCollectionField
		LazyForeignCollection<Foreign, Integer> foreign;

		public Lazy() {
		}
	}

	protected static class Foreign {
		@DatabaseField(generatedId = true)
		int id;
		@DatabaseField(foreign = true)
		Lazy lazy;

		public Foreign() {
		}
	}
}
