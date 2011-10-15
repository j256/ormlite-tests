package com.j256.ormlite;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import com.j256.ormlite.db.DatabaseType;

/**
 * Little command line JDBC interface so I can test connections to various databases to tune the associated
 * {@link DatabaseType} implementation.
 * 
 * @author graywatson
 */
public class CommandLine {

	public static void main(String argv[]) throws Exception {
		new CommandLine().doMain(argv);
	}

	public void doMain(String argv[]) throws Exception {
		String url = "jdbc:derby:target/x;create=true";
		Class.forName("org.apache.derby.jdbc.EmbeddedDriver");

		DataSource dataSource = new SingleConnectionDataSource(url, null, null, false);
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			readCommands(connection);
		} finally {
			if (connection != null) {
				connection.close();
			}
		}
	}

	private void readCommands(Connection connection) throws Exception {
		BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Exit by entering a 'q' or 'quit'");
		while (true) {
			System.out.println("-----------------------------------");
			System.out.print("Enter SQL: ");
			String line = stdin.readLine();
			if (line.equals("q") || line.equals("quit")) {
				System.out.println("Quitting...");
				break;
			}
			if (line.endsWith(";")) {
				line = line.substring(0, line.length() - 1);
			}
			executeStatement(connection, line);
		}
	}

	private void executeStatement(Connection connection, String statement) {
		try {
			PreparedStatement stmt = connection.prepareStatement(statement);
			if (statement.startsWith("select")) {
				handleSelect(stmt);
			} else {
				System.out.println("Statement updated " + stmt.executeUpdate() + " rows");
			}
		} catch (SQLException e) {
			System.out.println("SQL statement failed: " + e);
		}
	}

	private void handleSelect(PreparedStatement stmt) throws SQLException {
		System.out.println("Statment returned " + stmt.execute() + " with results:");
		ResultSet results = stmt.getResultSet();
		ResultSetMetaData metaData = results.getMetaData();
		int colN = metaData.getColumnCount();
		while (true) {
			if (!results.next()) {
				if (!stmt.getMoreResults()) {
					break;
				}
				results.next();
			}
			// SQL columns start at 1
			for (int i = 1; i <= colN; i++) {
				Object val = results.getObject(i);
				System.out.print(metaData.getColumnName(i) + "=" + val + ", ");
			}
			System.out.println("");
		}
	}
}
