package com.team7.smartwatch.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.team7.smartwatch.shared.BloodType;
import com.team7.smartwatch.shared.EmergencyContact;
import com.team7.smartwatch.shared.Gender;
import com.team7.smartwatch.shared.Patient;
import com.team7.smartwatch.shared.PatientStatus;
import com.team7.smartwatch.shared.Utility;

public class DatabasePatientReader {

	private static final Logger logger = Logger
			.getLogger(DatabasePatientReader.class.getName());
	private static final String PATIENTS_TABLE = "patients";
	private static final String SELECT_PATIENTID_STATEMENT = "SELECT * FROM " +
			PATIENTS_TABLE + " WHERE patientID = ?";
	private static final String SELECT_CARERID_STATEMENT = "SELECT * FROM " +
			PATIENTS_TABLE + " WHERE carerID = ?";
	
	public static List<Patient> readPatientsByCarerID(Integer carerID) {
		
		// TODO: Refactor: extract common code!
		ArrayList<Patient> patients = new ArrayList<Patient>();
		Connection conn = null;
		
		try {
			conn = DatabaseConnector.getConnection();
			PreparedStatement selectPatient = conn
					.prepareStatement(SELECT_CARERID_STATEMENT);
			bindValues(selectPatient, carerID);
			ResultSet resultSet = selectPatient.executeQuery();
			while (resultSet.next()) {
				patients.add(readPatientFromResultSet(resultSet));
			}
		} catch (SQLException e){
			logger.log(Level.WARNING,
					"Error accessing carer or patient info from database: " + 
					Utility.StringFromStackTrace(e));
		} finally {
			DatabaseConnector.closeConnection(conn);
		}
		
		return patients;
	}

	/**
	 * Creates a Patient with fields filled out using the database row which has
	 * the given patient patientID.
	 * 
	 * @param  patientID the patient patientID to select by.
	 * @return the patient read from the database, or null if patientID does not
	 * 		   exist in the database or a database error is encountered.
	 */
	public static Patient readPatientByPatientID(Integer patientID) {
		// TODO: Extract common code from this and DatabaseUserReader

		Connection conn = null;

		try {
			conn = DatabaseConnector.getConnection();
			PreparedStatement selectPatient = conn
					.prepareStatement(SELECT_PATIENTID_STATEMENT);
			bindValues(selectPatient, patientID);
			ResultSet resultSet = selectPatient.executeQuery();
			if (resultSet.next()) {
				return readPatientFromResultSet(resultSet);
			}
			return null;
		} catch (SQLException e){
			logger.log(Level.WARNING,
					"Error accessing patient info from database: " + 
					Utility.StringFromStackTrace(e));
			return null;
		} finally {
			DatabaseConnector.closeConnection(conn);
		}
	}
	
	/* Place given integer in the first place in a prepared statement. */
	private static void bindValues(PreparedStatement statement, int num)
			throws SQLException {

		statement.setInt(1, num);
	}
	
	private static Patient readPatientFromResultSet(ResultSet rs)
			throws SQLException {

		Patient patient = new Patient();
		
		patient.patientID = rs.getInt("patientID");
		patient.carerID = DatabaseUtility.getNullableInt(rs, "carerID");
		patient.firstName = rs.getString("fName");
		patient.lastName = rs.getString("lName");
		patient.gender = DatabaseUtility.getEnum(Gender.class, rs, "gender");
		patient.age = rs.getInt("age");
		patient.bloodType = DatabaseUtility.getEnum(BloodType.class, rs,
				"bloodType");
		patient.medication = rs.getString("medication");
		patient.status = DatabaseUtility.getEnum(PatientStatus.class, rs,
				"status");
		patient.homeAddress = rs.getString("homeAddress");
		patient.homeSuburb = rs.getString("homeSuburb");
		patient.contactNum = rs.getString("contactNum");
		
		// Emergency contact.
		patient.emergencyContact = new EmergencyContact();
		patient.emergencyContact.name = rs.getString("emergencyContactName");
		patient.emergencyContact.address = rs
				.getString("emergencyContactAddress");
		patient.emergencyContact.suburb = rs.getString("emergencyContactSuburb");
		patient.emergencyContact.num = rs.getString("emergencyContactNum");
		
		return patient;
	}
}