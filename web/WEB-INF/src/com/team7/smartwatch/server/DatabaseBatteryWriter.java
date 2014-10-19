package com.team7.smartwatch.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONException;

import com.team7.smartwatch.shared.Utility;

public class DatabaseBatteryWriter {

	private static final Logger logger = Logger
			.getLogger(DatabaseBatteryWriter.class.getName());
	private static final String BATTERY_TABLE = "patientbatteryalerts";
	private static final String INSERT_STATEMENT = "INSERT INTO "
			+ BATTERY_TABLE
			+ " (patientID, alertTime, alertDate, batteryLevel) "
			+ "values (?, ?, ?, ?)";

	public static boolean writeBattery(int patientID, Double battery)
			throws BadSQLParameterException {
		
		Connection conn = null;

        try {
            conn = DatabaseConnector.getConnection();
            PreparedStatement insertBattery = conn.prepareStatement(INSERT_STATEMENT);
            bindValues(insertBattery, patientID, battery);
            int rowsUpdated = insertBattery.executeUpdate();
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
	
    private static void bindValues(PreparedStatement insertBattery, int patientID,
    		Double battery)
            throws BadPostParameterException {
    	
        try {
        	insertBattery.setInt(1, patientID);
        	insertBattery.setString(4, String.format("%.2f", battery));

        	// The time and date given here will be ignored, but they are
        	// required by mysql.
        	insertBattery.setTimestamp(2, new java.sql.Timestamp(0));
        	insertBattery.setTimestamp(3, new java.sql.Timestamp(0));
        } catch (SQLException e) {
        	logger.log(Level.WARNING, Utility.StringFromStackTrace(e));
        } catch (JSONException e) {
        	logger.log(Level.INFO, "Error encountered reading JSON " +
        			"parameters:\n" + Utility.StringFromStackTrace(e));
        	throw new BadPostParameterException();
        }
    }
}