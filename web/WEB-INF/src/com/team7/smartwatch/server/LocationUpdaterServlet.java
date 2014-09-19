package com.team7.smartwatch.server;

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

public class LocationUpdaterServlet extends HttpServlet {

	private static final long serialVersionUID = -7786496267895391536L;
	private static final Logger logger = Logger
			.getLogger(LocationUpdaterServlet.class.getName());
    private static final String SUCCESS_MESSAGE = "Location updated";
    private static final String ERROR_MESSAGE = "Failed to update location";

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
    
    private class LocationUpdate {
    	
    	public int patientID;
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
    private boolean update(HttpServletRequest request) {
    	
    	LocationUpdate locationUpdate = getRequestDetails(request);
    	if (locationUpdate == null) {
    		return false;
    	}

    	try {
			boolean succeeded = DatabaseLocationWriter.writeLocation(
					locationUpdate.patientID, locationUpdate.latitude,
					locationUpdate.longitude);
			return succeeded;
		} catch (BadSQLParameterException e) {
			logger.log(Level.SEVERE, Utility.StringFromStackTrace(e));
			return false;
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
}