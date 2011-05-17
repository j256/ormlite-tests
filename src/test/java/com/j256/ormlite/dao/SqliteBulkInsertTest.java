package com.j256.ormlite.dao;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import org.junit.Test;

public class SqliteBulkInsertTest {

	// private static final String DATABASE_PATH = "/tmp/foo.sqlite";
	private static final String DATABASE_PATH = "";
	private static final String TABLE_NAME = "foo";

	@Test
	public void testBulkInsert() throws Exception {
		if (DATABASE_PATH.length() > 0) {
			new File(DATABASE_PATH).delete();
		}

		Class.forName("org.sqlite.JDBC");
		Connection connection = DriverManager.getConnection("jdbc:sqlite:" + DATABASE_PATH);

		connection.prepareStatement(
				"CREATE TABLE `" + TABLE_NAME + "` (`id` INTEGER PRIMARY KEY AUTOINCREMENT , `val` INTEGER )")
				.execute();
		System.out.println("Created table " + TABLE_NAME);

		int numPages = 10;
		// int numPages = 100;
		int pageSize = 10000;
		int valC = 0;
		long before = System.currentTimeMillis();
		for (int pageC = 0; pageC < numPages; pageC++) {
			connection.setAutoCommit(false);
			for (int i = 0; i < pageSize; i++) {
				PreparedStatement stmt =
						connection.prepareStatement("INSERT INTO `" + TABLE_NAME + "` (`val` ) VALUES (?)");
				stmt.setInt(1, valC);
				stmt.execute();
				stmt.close();
				valC++;
			}
			System.out.println("Inserted page, valC = " + valC);
			connection.setAutoCommit(true);
		}
		long after = System.currentTimeMillis();
		System.out.println("Took " + (after - before) + "ms");
	}
}
