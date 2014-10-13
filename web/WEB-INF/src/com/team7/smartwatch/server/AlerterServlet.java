package com.team7.smartwatch.server;

import com.team7.smartwatch.shared.Patient;
import com.team7.smartwatch.shared.Utility;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

public class AlerterServlet extends HttpServlet {

	private static final long serialVersionUID = -7786496267895391536L;
	private static final Logger logger = Logger
			.getLogger(AlerterServlet.class.getName());
    private static final String SUCCESS_MESSAGE = "Alert created";
    private static final String ERROR_MESSAGE = "Failed to create alert";

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
    		throws IOException, ServletException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        boolean succeeded = update(request);
        if (succeeded) {
        	logger.log(Level.INFO, "Location update received from " +
        			request.getRemoteAddr() + ".");
            out.println(SUCCESS_MESSAGE);
        } else {
            out.println(ERROR_MESSAGE);
        }
    }
    
    private class Alerter {
    	
    	public int patientID;
    	
    	public boolean valid() {

    		if (Utility.arrayContainsNull(patientID)) {
    			return false;
    		}
    		if (patientID < 0) {
    			return false;
    		}
    		return true;
    	}
    }

    /**
     * 
     * @param request
     * @return true if the update was successful, false otherwise.
     */
    private boolean update(HttpServletRequest request) {
    	
    	// Read and verify given location.
    	Alerter alerter = getRequestDetails(request);
    	if (alerter == null) {
    		return false;
    	}
    	
    	// Check that user has permission to create the alert
    	Integer userID = SessionUtility.getUserID(request);
    	boolean permitted = userIsCarerForPatient(userID,
    			alerter.patientID);
    	if (!permitted) {
			logNoPermission(request.getRemoteAddr(), userID,
					alerter.patientID);
    		return false;
    	}

    	// Update the patient's location.
    	try {
			boolean succeeded = DatabaseLocationWriter.writeAlert(
					alerter.patientID);
			return succeeded;
		} catch (BadSQLParameterException e) {
			logger.log(Level.SEVERE, Utility.StringFromStackTrace(e));
			return false;
		}
    }
    
    private Alerter getRequestDetails(HttpServletRequest request) {
    	
    	final String JSON_ERROR_MESSAGE = "One or more of patientID, " +
    			"latitude, and longitude missing or invalid.";
    
		try {
			JSONObject jObj;
			jObj = JSONConverter.getJSON(request);
			Alerter alert = new Alerter();
			alert.patientID = jObj.getInt("patientID");
	        if (!alert.valid()) {
	        	logger.log(Level.INFO, JSON_ERROR_MESSAGE);
	        	return null;
	        }
	        return alert;
		} catch (JSONException e) {
	        logger.log(Level.INFO, JSON_ERROR_MESSAGE);
	        return null;
		} catch (IOException e) {
			logger.log(Level.WARNING, Utility.StringFromStackTrace(e));
			return null;
		}
    }
    
    private boolean userIsCarerForPatient(Integer userID, Integer patientID) {
    	
    	if (userID == null || patientID == null) {
    		return false;
    	}

		Patient patient = DatabasePatientReader
				.readPatientByPatientID(patientID);
		return (patient != null) && (patient.carerID == userID);
    }
    
    private void logNoPermission(String address, int userID, int patientID) {
    	
    		logger.log(Level.INFO, address + " attempted to update location " +
    				"of patient that they are not the carer for: patientID=" +
    				String.valueOf(patientID) + ", userID=" +
    				String.valueOf(userID) + ".");
    }
}