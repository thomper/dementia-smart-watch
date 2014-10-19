package com.team7.smartwatch.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONException;

import com.team7.smartwatch.shared.Utility;

public class DatabaseLocationWriter {

	private static final Logger logger = Logger
			.getLogger(DatabaseLocationWriter.class.getName());
	private static final String LOCATION_TABLE = "patientloc";
	private static final String REPLACE_STATEMENT = "REPLACE INTO "
			+ LOCATION_TABLE
			+ " (patientID, patientLat, patientLong, retrievalTime, retrievalDate) "
			+ "values (?, ?, ?, ?, ?)";

	public static boolean writeLocation(int patientID, double latitude,
			double longitude)
			throws BadSQLParameterException {
		
		Connection conn = null;

        try {
            conn = DatabaseConnector.getConnection();
            PreparedStatement replaceLoc = conn.prepareStatement(REPLACE_STATEMENT);
            bindValues(replaceLoc, patientID, latitude, longitude);
            int rowsUpdated = replaceLoc.executeUpdate();
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
	
    private static void bindValues(PreparedStatement replaceLoc, int patientID,
    		double latitude, double longitude)
            throws BadPostParameterException {
    	
        try {
            replaceLoc.setInt(1, patientID);
            replaceLoc.setDouble(2, latitude);
            replaceLoc.setDouble(3, longitude);
            
            // The time and date here will be ignored, as they are set by a
            // mysql trigger. We still need to set them to some dummy value
            // though because they are both defined NOT NULL.
            replaceLoc.setTimestamp(4, new java.sql.Timestamp(0));
            replaceLoc.setTimestamp(5, new java.sql.Timestamp(0));
        } catch (SQLException e) {
        	logger.log(Level.WARNING, Utility.StringFromStackTrace(e));
        } catch (JSONException e) {
        	logger.log(Level.INFO, "Error encountered reading JSON " +
        			"parameters:\n" + Utility.StringFromStackTrace(e));
        	throw new BadPostParameterException();
        }
    }

	public static boolean writeAlert(int patientID) throws BadSQLParameterException {
		Connection conn = null;
		double lat;
        double l0ng;
		
        try {
            conn = DatabaseConnector.getConnection();
            PreparedStatement getloc = conn.prepareStatement("SELECT patientLat, patientLong "
        			+ " FROM patientloc WHERE patientID = ? ");
            getloc.setInt(1, patientID);
            ResultSet rs = getloc.executeQuery();
            
            rs.next();
            lat = rs.getDouble(1);
            l0ng = rs.getDouble(2);
            rs.close();
            
            PreparedStatement createAlert = conn.prepareStatement("INSERT INTO patientalerts "
        			+ " (patientID, alertTime, alertDate, alertLat, alertLong) "
        			+ "values (?, ?, ?, ?, ?)");
            bindValues(createAlert, patientID, lat, l0ng);
            int rowsUpdated = createAlert.executeUpdate();
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
}