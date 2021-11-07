package com.j256.ormlite.dao;

import static org.junit.Assert.assertEquals;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.stream.Stream;

import org.junit.Test;

import com.j256.ormlite.BaseCoreTest;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.logger.LogBackendType;
import com.j256.ormlite.logger.LoggerFactory;

/**
 * Effectively a unit test for the {@link StreamableLazyForeignCollection} class which can't be tested (as of 11/2021)
 * because we aren't compiling using JDK8 in ormlite-core.
 * 
 * @author graywatson
 */
public class StreamableLazyForeignCollectionTest extends BaseCoreTest {

	static {
		LoggerFactory.setLogBackendType(LogBackendType.LOCAL);
	}

	@Test
	public void testStuff() throws SQLException {
		Dao<Account, Object> accountDao = createDao(Account.class, true);
		Dao<Order, Object> orderDao = createDao(Order.class, true);

		Account account1 = new Account();
		Account account2 = new Account();
		accountDao.create(Arrays.asList(account1, account2));

		Order order1 = new Order(account1);
		Order order2 = new Order(account1);
		Order order3 = new Order(account1);
		Order order4 = new Order(account2);
		Order order5 = new Order(account2);
		orderDao.create(Arrays.asList(order1, order2, order3, order4, order5));

		assertEquals(0, connectionSource.getConnectionCount());
		try (Stream<Account> stream = accountDao.queryForAll().stream();) {
			stream.forEach((account) -> {
				System.out.println("account.orders = " + account.orders);
				try (Stream<Order> orderStream = account.orders.stream();) {
					Order order = orderStream.findFirst().orElse(null);
					if (account.id == account1.id) {
						assertEquals(order1.id, order.id);
					} else {
						assertEquals(order4.id, order.id);
					}
				}
				assertEquals(0, connectionSource.getConnectionCount());
			});
		}
		assertEquals(0, connectionSource.getConnectionCount());
	}

	public static class Account {
		@DatabaseField(generatedId = true)
		private int id;

		@ForeignCollectionField(eager = false)
		private ForeignCollection<Order> orders;
	}

	public static class Order {
		@DatabaseField(generatedId = true)
		private int id;

		@DatabaseField(foreign = true)
		private Account account;

		public Order() {
			// ormlite
		}

		public Order(Account account) {
			this.account = account;
		}
	}
}
