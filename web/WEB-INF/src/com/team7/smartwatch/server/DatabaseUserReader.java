package com.team7.smartwatch.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.team7.smartwatch.shared.User;

public class DatabaseUserReader {

	private static final Logger logger = Logger
			.getLogger(DatabaseUserReader.class.getName());
	private static final String USERS_TABLE = "users";
	private static final String SELECT_USERID_STATEMENT = "SELECT * FROM " +
			USERS_TABLE + " WHERE userID = ?";
	private static final String SELECT_USERNAME_STATEMENT = "SELECT * FROM " +
			USERS_TABLE + " WHERE userName = ?";

	public static User readUserByUserID(Integer userID)
			throws BadSQLParameterException {

		return readUser(SELECT_USERID_STATEMENT, null, userID);
	}
	
	public static User readUserByUsername(String username)
			throws BadSQLParameterException {

		return readUser(SELECT_USERNAME_STATEMENT, username, null);
	}
	
	/**
	 * Creates a User with fields filled out using the database row which has
	 * the given username or user ID.  At most, one of userID and username may
	 * be null.
	 * 
	 * @param  selectStatement the SQL select statement to use.
	 * @param  username the username to select by or null if userID is to be
	 * 		   used.
	 * @param  userID the user ID to select by or null if username is to be
	 * 		   used.
	 * @return the user read from the database, or null if the username or
	 * 		   userID does not exist in the database or a database error is
	 * 		   encountered.
	 * @throws BadSQLParameterException if both userID and username are null.
	 */
	private static User readUser(String selectStatement, String username,
			Integer userID) throws BadSQLParameterException {

		Connection conn = null;

		try {
			conn = DatabaseConnector.getConnection();
			PreparedStatement selectUser = conn
					.prepareStatement(selectStatement);
			bindValues(selectUser, username, userID);
			ResultSet resultSet = selectUser.executeQuery();
			if (resultSet.next()) {
				return readUserFromResultSet(resultSet);
			}
			return null;
		} catch (SQLException e){
			logger.log(Level.WARNING,
					"Error accessing user info from database.");
			e.printStackTrace();
			return null;
		} finally {
			DatabaseConnector.closeConnection(conn);
		}
	}
	
	/* Place given username or user ID in a prepared statement. */
	private static void bindValues(PreparedStatement statement, String username,
			Integer userID) throws SQLException, BadSQLParameterException {

		if (username != null) {
			statement.setString(1, username);
		} else if (userID != null) {
			statement.setInt(1, userID);
		} else {
			throw new BadSQLParameterException();
		}
	}
	
	private static User readUserFromResultSet(ResultSet resultSet)
			throws SQLException {

		User user = new User();
		
		user.userID = resultSet.getInt("userID");
		user.patientID = resultSet.getInt("patientID");
		user.carerID = resultSet.getInt("carerID");
		user.email = resultSet.getString("email");
		user.username = resultSet.getString("userName");
		user.storedHash = resultSet.getString("userPass");
		user.salt = resultSet.getString("salt");
		
		return user;
	}
	
}