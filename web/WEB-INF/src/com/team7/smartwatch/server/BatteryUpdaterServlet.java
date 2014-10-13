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

public class BatteryUpdaterServlet extends HttpServlet {

	private static final long serialVersionUID = -7786496267895391536L;
	private static final Logger logger = Logger
			.getLogger(BatteryUpdaterServlet.class.getName());
    private static final String SUCCESS_MESSAGE = "Battery updated";
    private static final String ERROR_MESSAGE = "Failed to update battery level";

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
    		throws IOException, ServletException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        boolean succeeded = update(request);
        if (succeeded) {
        	logger.log(Level.INFO, "Battery update received from " +
        			request.getRemoteAddr() + ".");
            out.println(SUCCESS_MESSAGE);
        } else {
            out.println(ERROR_MESSAGE);
        }
        out.flush();
    }
    
    private class BatteryUpdate {
    	
    	public Integer patientID;
    	public Integer batteryLevel;
    	
    	public boolean valid() {

    		if (Utility.arrayContainsNull(patientID, batteryLevel)) {
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
    	
    	// Read and verify given battery.
    	BatteryUpdate batteryUpdate = getRequestDetails(request);
    	if (batteryUpdate == null) {
    		return false;
    	}
    	
    	// Check that user has permission to update this patient's battery.
    	Integer userID = SessionUtility.getUserID(request);
    	boolean permitted = userIsCarerForPatient(userID,
    			batteryUpdate.patientID);
    	if (!permitted) {
			logNoPermission(request.getRemoteAddr(), userID,
					batteryUpdate.patientID);
    		return false;
    	}

    	// Update the patient's battery.
    	try {
			boolean succeeded = DatabaseBatteryWriter.writeBattery(
					batteryUpdate.patientID, batteryUpdate.batteryLevel);
			return succeeded;
		} catch (BadSQLParameterException e) {
			logger.log(Level.SEVERE, Utility.StringFromStackTrace(e));
			return false;
		}
    }
    
    private BatteryUpdate getRequestDetails(HttpServletRequest request) {
    	
    	final String JSON_ERROR_MESSAGE = "One or more of patientID, " +
    			"battery is missing or invalid.";
    
		try {
			JSONObject jObj;
			jObj = JSONConverter.getJSON(request);
			BatteryUpdate batteryUpdate = new BatteryUpdate();
			batteryUpdate.patientID = jObj.getInt("patientID");
			batteryUpdate.batteryLevel = jObj.getInt("batteryLevel");

	        if (!batteryUpdate.valid()) {
	        	logger.log(Level.INFO, JSON_ERROR_MESSAGE);
	        	return null;
	        }
	        return batteryUpdate;
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
    
    private void logNoPermission(String address, Integer userID, Integer patientID) {
    	
    		logger.log(Level.INFO, address + " attempted to update location " +
    				"of patient that they are not the carer for: patientID=" +
    				String.valueOf(patientID) + ", userID=" +
    				String.valueOf(userID) + ".");
    }
}