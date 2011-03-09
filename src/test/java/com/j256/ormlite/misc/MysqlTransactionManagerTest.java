package com.j256.ormlite.misc;

import com.j256.ormlite.db.MysqlDatabaseType;

/**
 * This will fail unless the MySQL database engine for the tables is transactional. That's why the InnoDB engine is the
 * default in {@link MysqlDatabaseType#appendCreateTableSuffix}.
 */
public class MysqlTransactionManagerTest extends BaseTransactionManagerTest {

	@Override
	protected void setDatabaseParams() {
		databaseHost = "db.be.256.com";
		databaseUrl = "jdbc:mysql://" + databaseHost + "/ormlitetest";
		userName = "ormlitetest";
		password = "hibernate";
	}
}
