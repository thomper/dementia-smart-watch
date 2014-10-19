package com.team7.smartwatch.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.team7.smartwatch.shared.RadialFence;
import com.team7.smartwatch.shared.Utility;

public class DatabaseFenceReader {

	private static final Logger logger = Logger
			.getLogger(DatabaseFenceReader.class.getName());
	private static final String FENCES_TABLE = "patientfences";
	private static final String SELECT_STATEMENT = "SELECT * FROM " +
			FENCES_TABLE + " WHERE patientID = ?";
	
	public static List<RadialFence> readFencesByPatientID(Integer patientID) {
		
		ArrayList<RadialFence> fences = new ArrayList<RadialFence>();
		Connection conn = null;
		
		try {
			conn = DatabaseConnector.getConnection();
			PreparedStatement selectPatient = conn
					.prepareStatement(SELECT_STATEMENT);
			bindValues(selectPatient, patientID);
			ResultSet resultSet = selectPatient.executeQuery();
			while (resultSet.next()) {
				fences.add(readFenceFromResultSet(resultSet));
			}
		} catch (SQLException e){
			logger.log(Level.WARNING,
					"Error accessing fence info from database: " + 
					Utility.StringFromStackTrace(e));
		} finally {
			DatabaseConnector.closeConnection(conn);
		}
		
		return fences;
	}
	
	private static void bindValues(PreparedStatement statement, int patientID)
			throws SQLException {

		statement.setInt(1, patientID);
	}
	
	private static RadialFence readFenceFromResultSet(ResultSet rs)
			throws SQLException {

		double latitude = rs.getDouble("fenceLat");
		double longitude = rs.getDouble("fenceLong");
		double radius = rs.getDouble("radius");

        return new RadialFence(latitude, longitude, radius);
	}
}