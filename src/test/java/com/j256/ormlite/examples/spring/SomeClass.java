package com.j256.ormlite.examples.spring;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.sql.SQLException;
import java.util.Date;
import java.util.concurrent.Callable;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.misc.TransactionManager;

/**
 * Utility class which shows how the dao's are injected and used.
 */
public class SomeClass implements InitializingBean {

	private Dao<Account, Integer> accountDao;
	private Dao<Delivery, Integer> deliveryDao;
	private TransactionManager transactionManager;

	/**
	 * After this class is configured by Spring, this method is called by Spring after configuration. It is just here as
	 * an example.
	 */
	@Override
	public void afterPropertiesSet() throws Exception {

		// create an instance of Account
		String name = "Billy Bob Spring";
		final Account account = new Account(name);

		// persist the account object to the database, it should return 1
		if (accountDao.create(account) != 1) {
			throw new Exception("Could not create Account in database");
		}
		System.out.println("Created account: " + name);

		// make sure we can read it back
		Account account2 = accountDao.queryForId(account.getId());
		if (account2 == null) {
			throw new Exception("Should have found name '" + name + "' in the database");
		}
		assertEquals("expected name does not equal account name", account.getName(), account2.getName());
		assertEquals("expected password does not equal account name", account.getPassword(), account2.getPassword());
		System.out.println("Queried for account: " + name);

		try {
			// try something in a transaction
			transactionManager.callInTransaction(new Callable<Void>() {
				@Override
				public Void call() throws Exception {
					// we do the delete
					assertEquals(1, accountDao.delete(account));
					assertNull(accountDao.queryForId(account.getId()));
					// but then (as an example) we throw an exception which rolls back the delete
					throw new Exception("We throw to roll back!!");
				}
			});
			fail("This should have thrown");
		} catch (SQLException e) {
			// expected
		}

		// we should still see the account in the db
		assertNotNull(accountDao.queryForId(account.getId()));

		// now try to persist a delivery
		Delivery delivery = new Delivery(new Date(), "Mr. Ed", account);
		assertEquals(1, deliveryDao.create(delivery));
		Delivery delivery2 = deliveryDao.queryForId(delivery.getId());
		assertNotNull(delivery2);
		System.out.println("Create a delivery: " + delivery.getId());

		// see Main in the simple example for more use examples
	}

	@Required
	public void setAccountDao(Dao<Account, Integer> accountDao) {
		this.accountDao = accountDao;
	}

	@Required
	public void setDeliveryDao(Dao<Delivery, Integer> deliveryDao) {
		this.deliveryDao = deliveryDao;
	}

	@Required
	public void setTransactionManager(TransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}
}
