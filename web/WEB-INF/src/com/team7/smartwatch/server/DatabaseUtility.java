package com.team7.smartwatch.server;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;
import java.util.logging.Logger;
import java.util.logging.Level;

public class DatabaseUtility {

	private static final Logger logger = Logger
			.getLogger(DatabaseUtility.class.getName());

	public static Integer getNullableInt(ResultSet resultSet, String column)
			throws SQLException {
		
		Integer result = resultSet.getInt(column);
		if (resultSet.wasNull()) {
			return null;
		}
		return result;
	}
	
	public static <E extends Enum<E>> E getEnum(Class<E> clazz,
			ResultSet resultSet, String column) throws SQLException {
		
		String resultString = resultSet.getString(column);
		if (resultString == null) {
			return null;
		}
		
		// We use all caps for Java enumerations but not for database
		// enumerations.
		resultString = resultString.toUpperCase(Locale.ENGLISH);
		try {
			return Enum.valueOf(clazz, resultString);
		} catch (IllegalArgumentException e) {
			logger.log(Level.SEVERE, "Failure reading enumerated type from " +
					"database for Enum type: " + clazz.getName() + ".");
			return null;
		}
	}
}
