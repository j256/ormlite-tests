package com.j256.ormlite.jdbc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;
import java.util.Random;

import org.junit.Test;

import com.j256.ormlite.support.DatabaseConnection;

public class JdbcPooledConnectionSourceThreadedTest {

	private static final String DEFAULT_DATABASE_URL = "jdbc:h2:mem:ormlite";
	private static final int NUM_THREADS = 20;
	private static final int NUM_ITERATIONS = 50;
	private static final int MAX_WAIT_MILLIS = 300;
	private static final int MAX_CONNECTIONS_FREE = 5;
	private static final int CHECK_CONN_EVERY_MILLIS = 100;

	private final JdbcPooledConnectionSource pooled;

	{
		try {
			pooled = new JdbcPooledConnectionSource(DEFAULT_DATABASE_URL);
			pooled.setMaxConnectionsFree(MAX_CONNECTIONS_FREE);
			pooled.setCheckConnectionsEveryMillis(CHECK_CONN_EVERY_MILLIS);
		} catch (SQLException e) {
			throw new RuntimeException("Could not allocate pooled connection source", e);
		}
	}

	@Test
	public void threadedPummel() throws Exception {
		Thread[] threads = new Thread[NUM_THREADS];
		try {
			for (int threadC = 0; threadC < NUM_THREADS; threadC++) {
				threads[threadC] = new OurThread();
				threads[threadC].start();
			}
			for (int threadC = 0; threadC < NUM_THREADS; threadC++) {
				threads[threadC].join();
			}
			// we synchronize here to true to get good stats
			synchronized (pooled) {
				// * 2 because they could have a saved connection and inner connection
				assertTrue(NUM_THREADS * 2 >= pooled.getMaxConnectionsEverUsed());
				// should still have 5 connections in our cache
				assertEquals(MAX_CONNECTIONS_FREE, pooled.getCurrentConnectionsManaged());
			}
		} finally {
			pooled.close();
		}
		// we synchronize here to true to get good stats
		synchronized (pooled) {
			assertEquals(pooled.getOpenCount(), pooled.getCloseCount());
			assertEquals(0, pooled.getCurrentConnectionsManaged());
		}
	}

	private class OurThread extends Thread {

		private DatabaseConnection conn = null;
		private DatabaseConnection savedConn = null;

		@Override
		public void run() {
			try {
				doStuff();
			} catch (SQLException e) {
				throw new RuntimeException("Got a SQLException in doStuff()", e);
			}
		}

		public void doStuff() throws SQLException {
			Random random = new Random();
			for (int iterC = 0; iterC < NUM_ITERATIONS; iterC++) {
				try {
					Thread.sleep(random.nextInt(MAX_WAIT_MILLIS));
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					break;
				}
				switch (random.nextInt(10)) {
					case 0 :
						// save connection
						if (savedConn == null) {
							savedConn = pooled.getReadWriteConnection();
							pooled.saveSpecialConnection(savedConn);
						}
						break;
					case 1 :
						// clear saved connection
						if (savedConn != null) {
							// the inner transactions should be released as well
							if (conn != null) {
								closeConn();
							}
							closeSavedConn();
						}
						break;
					default :
						if (conn == null) {
							// get new connection
							conn = pooled.getReadWriteConnection();
						} else {
							// release our existing one
							pooled.releaseConnection(conn);
							conn = null;
						}
						break;
				}
			}
			if (conn != null) {
				closeConn();
			}
			if (savedConn != null) {
				closeSavedConn();
			}
		}

		private void closeSavedConn() throws SQLException {
			pooled.clearSpecialConnection(savedConn);
			// transaction manaager releases after clear
			pooled.releaseConnection(savedConn);
			savedConn = null;
		}

		private void closeConn() throws SQLException {
			pooled.releaseConnection(conn);
			conn = null;
		}
	};
}
