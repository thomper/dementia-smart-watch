package com.team7.smartwatch.server;

import com.team7.smartwatch.shared.Patient;
import com.team7.smartwatch.shared.PatientStatus;
import com.team7.smartwatch.shared.RadialFence;
import com.team7.smartwatch.shared.Utility;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

public class LocationUpdaterServlet extends HttpServlet {

	private static final long serialVersionUID = -7786496267895391536L;
	private static final Logger logger = Logger
			.getLogger(LocationUpdaterServlet.class.getName());
    private static final String SUCCESS_MESSAGE = "Location updated";
    private static final String LOST_MESSAGE = "You are outside the safe zone";
    private static final String ERROR_MESSAGE = "Failed to update location";
    
    private enum Result {
    	SUCCESS, ERROR, LOST
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
    		throws IOException, ServletException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        Result result = update(request);
        if (result != Result.ERROR) {
        	logger.log(Level.INFO, "Location update received from " +
        			request.getRemoteAddr() + ".");
        	if (result == Result.LOST) {
        		out.println(LOST_MESSAGE);
        	} else {
        		out.println(SUCCESS_MESSAGE);
        	}
        } else {
            out.println(ERROR_MESSAGE);
        }
        out.flush();
    }
    
    private class LocationUpdate {
    	
    	public Integer patientID;
    	public double latitude;
    	public double longitude;
    	
    	public boolean valid() {

    		if (Utility.arrayContainsNull(patientID, latitude, longitude)) {
    			return false;
    		}
    		if (patientID < 0) {
    			return false;
    		}
    		if (Math.abs(latitude) > 90.0 || Math.abs(longitude) > 180) {
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
    private Result update(HttpServletRequest request) {
    	
    	// Read and verify given location.
    	LocationUpdate locationUpdate = getRequestDetails(request);
    	if (locationUpdate == null) {
    		return Result.ERROR;
    	}
    	
    	// Check that user has permission to update this patient's location.
    	Integer userID = SessionUtility.getUserID(request);
    	boolean permitted = userIsCarerForPatient(userID,
    			locationUpdate.patientID);
    	if (!permitted) {
			logNoPermission(request.getRemoteAddr(), userID,
					locationUpdate.patientID);
    		return Result.ERROR;
    	}

    	// Update the patient's location.
    	try {
			boolean succeeded = DatabaseLocationWriter.writeLocation(
					locationUpdate.patientID, locationUpdate.latitude,
					locationUpdate.longitude);
			boolean lost = patientIsLost(locationUpdate);
			succeeded = succeeded && updateFenceStatus(locationUpdate, lost);
			if (!succeeded) {
				return Result.ERROR;
			} else if (lost) {
				return Result.LOST;
			} else {
				return Result.SUCCESS;
			}
		} catch (BadSQLParameterException e) {
			logger.log(Level.SEVERE, Utility.StringFromStackTrace(e));
			return Result.ERROR;
		}
    }
    
    private LocationUpdate getRequestDetails(HttpServletRequest request) {
    	
    	final String JSON_ERROR_MESSAGE = "One or more of patientID, " +
    			"latitude, and longitude missing or invalid.";
    
		try {
			JSONObject jObj;
			jObj = JSONConverter.getJSON(request);
			LocationUpdate locationUpdate = new LocationUpdate();
			locationUpdate.patientID = jObj.getInt("patientID");
			locationUpdate.latitude = jObj.getDouble("latitude");
			locationUpdate.longitude = jObj.getDouble("longitude");
	        if (!locationUpdate.valid()) {
	        	logger.log(Level.INFO, JSON_ERROR_MESSAGE);
	        	return null;
	        }
	        return locationUpdate;
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
    
    private boolean patientIsLost(LocationUpdate locationUpdate) {
    	
		boolean lost = true;
		
        List<RadialFence> fences = DatabaseFenceReader.readFencesByPatientID(
				locationUpdate.patientID);
		for (RadialFence fence : fences) {
			if (fence.containsLocation(locationUpdate.latitude,
					locationUpdate.longitude)) {
				lost = false;
				break;
			}
		}
		
		return lost;
    }
    
    private boolean updateFenceStatus(LocationUpdate locationUpdate,
    		boolean lost) {

		try {
			if (lost) {
				
				// Change status to LOST.
				logger.log(Level.INFO, "Patient with patientID=" +
						String.valueOf(locationUpdate.patientID) + " is LOST.");
				return DatabaseStatusWriter.updateStatus(
						locationUpdate.patientID, "LOST");
			} else {

				// Change status from LOST to FINE if necessary.
				Patient patient = DatabasePatientReader.readPatientByPatientID(
						locationUpdate.patientID);
				if (patient.status == PatientStatus.LOST) {
					logger.log(Level.INFO, "Patient with patientID=" +
							String.valueOf(locationUpdate.patientID) +
							" is no longer LOST.");
					return DatabaseStatusWriter.updateStatus(
							locationUpdate.patientID, "FINE");
				}
			}
		} catch (BadSQLParameterException e) {
			logger.log(Level.WARNING, Utility.StringFromStackTrace(e));
			return false;
		}
		
		return true;
    }
    
    private void logNoPermission(String address, Integer userID, Integer patientID) {
    	
    		logger.log(Level.INFO, address + " attempted to update location " +
    				"of patient that they are not the carer for: patientID=" +
    				String.valueOf(patientID) + ", userID=" +
    				String.valueOf(userID) + ".");
    }
}