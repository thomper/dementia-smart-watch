package com.team7.smartwatch.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.team7.smartwatch.shared.Utility;

public class DatabaseStatusWriter {

	private static final Logger logger = Logger
			.getLogger(DatabaseStatusWriter.class.getName());
	private static final String PATIENTS_TABLE = "patients";
	private static final String UPDATE_STATEMENT = "UPDATE " + PATIENTS_TABLE +
			" SET status = ? WHERE patientID = ?";

	public static boolean updateStatus(int patientID, String status)
			throws BadSQLParameterException {
		
		Connection conn = null;

        try {
            conn = DatabaseConnector.getConnection();
            PreparedStatement updateStatus = conn.prepareStatement(
            		UPDATE_STATEMENT);
            bindValues(updateStatus, patientID, status);
            int rowsUpdated = updateStatus.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
        	logger.log(Level.WARNING, Utility.StringFromStackTrace(e));
        	return false;
        } catch (BadPostParameterException e) {
        	return false;
		} finally {
			DatabaseConnector.closeConnection(conn);
        }
	}
	
	private static void bindValues(PreparedStatement insertBattery,
			int patientID, String status)
			throws BadPostParameterException {
    	
        try {
        	insertBattery.setString(1, status);
        	insertBattery.setInt(2, patientID);
        } catch (SQLException e) {
        	logger.log(Level.WARNING, Utility.StringFromStackTrace(e));
        }
    }
}