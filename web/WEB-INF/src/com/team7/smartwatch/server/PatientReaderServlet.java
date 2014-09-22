package com.team7.smartwatch.server;


import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;

import com.team7.smartwatch.shared.Patient;
import com.team7.smartwatch.shared.User;

public class PatientReaderServlet extends HttpServlet {

	private static final long serialVersionUID = 7036161095998650941L;
	private static final Logger logger = Logger
			.getLogger(PatientReaderServlet.class.getName());

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		// Get the logged in user, if any.
		User user = getUser(request);
		if (user == null) {
                        logger.log(Level.INFO, request.getRemoteAddr() + " attempted " +
                                "to read patient info when not logged in.");
			return;
		}
		
		// Get the logged in user's patients.
		List<Patient> patients = DatabasePatientReader
				.readPatientsByCarerID(user.carerID);
		if (patients.size() == 0) {
                        logger.log(Level.INFO, request.getRemoteAddr() + " read patient " +
                                "info but has no patients assigned to them.");
			return;
		}

		// Write the patients out as JSON.
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
        out.println(JSONFromPatientsList(patients));
        logger.log(Level.INFO, request.getRemoteAddr() + " read information " +
        		"on their " + String.valueOf(patients.size()) + " patient(s).");
        out.flush();
	}
	
	/* Returns the User with the ID that has been stored in the request
	 * session.
	 **/
	private User getUser(HttpServletRequest request) {
		
		Integer userID = SessionUtility.getUserID(request);
		if (userID == null) {
			logger.log(Level.INFO, request.getRemoteAddr() + " attempted " +
					"to read patient information while not logged in.");
			return null;
		}

		try {
			User user = DatabaseUserReader.readUserByUserID(userID);
			return user;
		} catch (BadSQLParameterException e) {
			logger.log(Level.SEVERE, e.getLocalizedMessage());
			return null;
		}
	}
	
	private JSONArray JSONFromPatientsList(List<Patient> patients) {
		
		JSONArray jsonArray = new JSONArray();
		
		for (Patient patient : patients) {
			jsonArray.put(patient.toJSON());
		}
		
		return jsonArray;
	}
}
