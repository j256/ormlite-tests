package com.j256.ormlite.test.misc;

import com.j256.ormlite.db.MysqlDatabaseType;

/**
 * This will fail unless the MySQL database engine for the tables is transactional. That's why the InnoDB engine is the
 * default in {@link MysqlDatabaseType#appendCreateTableSuffix}.
 */
public class MysqlTransactionManagerTest extends BaseTransactionManagerTest {

	private final static String DB_HOST = "db.be.256.com";

	@Override
	protected void setDatabaseParams() {
		databaseHost = DB_HOST;
		databaseUrl = "jdbc:mysql://" + DB_HOST + "/ormlitetest";
		userName = "ormlitetest";
		password = "hibernate";
	}
}
