package com.team7.smartwatch.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.team7.smartwatch.shared.Utility;

/* Simplifies getting a connection to the database. */
public class DatabaseConnector {

    // TODO: Read database name, username, and password from file.
	private static final Logger logger = Logger
			.getLogger(DatabaseConnector.class.getName());
    private final static String DB_NAME = "dementiawatch_db";
    private final static String CONNECTION_STRING = "jdbc:mysql://localhost" +
        ":3306/" + DB_NAME + "?user=agile374&password=dementia374";

	public static Connection getConnection() throws SQLException {

        registerJDBCDriver();
        return DriverManager.getConnection(CONNECTION_STRING);
	}
	
	public static void closeConnection(Connection conn) {

			try {
	            if (conn != null) {
	            	conn.close();
	            	conn = null;
	            }
			} catch (SQLException e) {
				logger.log(Level.WARNING,
						"Could not close database connection.");
			}
	}
	
    private static void registerJDBCDriver() {

        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (ClassNotFoundException | InstantiationException |
        		IllegalAccessException e) {
        	logger.log(Level.SEVERE, "Could not register JBDC driver:\n" +
        			Utility.StringFromStackTrace(e));
        }
    }
}
