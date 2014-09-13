package com.team7.smartwatch.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/* Simplifies getting a connection to the database. */
public class DatabaseConnector {

    // TODO: Read database name, username, and password from file.
    private final static String DB_NAME = "dementiawatch_db";
    private final static String CONNECTION_STRING = "jdbc:mysql://localhost" +
        ":3306/" + DB_NAME + "?user=agile374&password=dementia374";

	public static Connection getConnection() throws SQLException {
        registerJDBCDriver();
        return DriverManager.getConnection(CONNECTION_STRING);
	}
	
    private static void registerJDBCDriver() {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
        	// TODO: log critical
            e.printStackTrace();
        }
    }
}
