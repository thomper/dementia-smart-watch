package com.team7.smartwatch.server;

import com.team7.smartwatch.shared.Utility;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

@SuppressWarnings("serial")
public class LocationUpdaterServlet extends HttpServlet {

    private final static String LOCATION_TABLE = "patientloc";
    private final static String REPLACE_STATEMENT =
        "REPLACE INTO " + LOCATION_TABLE +
        " (patientID, patientLat, patientLong, retrievalTime, retrievalDate) " +
        "values (?, ?, ?, ?, ?)";
    private final static String SUCCESS_MESSAGE = "Location updated";
    private final static String ERROR_MESSAGE = "ERROR: could not update location";

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
    		throws IOException, ServletException
    {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        int rowsUpdated = update(request);
        if (rowsUpdated == 0) {
            out.println(ERROR_MESSAGE);
        } else {
            out.println(SUCCESS_MESSAGE);
        }
    }

    private int update(HttpServletRequest request) {
        int rowsUpdated = 0;
        Connection conn = null;

        try {
            conn = DatabaseConnector.getConnection();
            PreparedStatement replaceLoc = conn.prepareStatement(REPLACE_STATEMENT);
            bindValues(replaceLoc, request);
            rowsUpdated = replaceLoc.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (BadPostParameterException e) {
            // TODO: log error?
		} finally {
			
			// Close database connection.
            try {
                if (conn != null) {
                    conn.close();
                    conn = null;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return rowsUpdated;
    }

    private void bindValues(PreparedStatement replaceLoc, HttpServletRequest request)
            throws BadPostParameterException {
    	
        try {
        	JSONObject jObj = JSONConverter.getJSON(request);
            String patientID = jObj.getString("patientID");
            Double latitude = jObj.getDouble("latitude");
            Double longitude = jObj.getDouble("longitude");

            if (Utility.arrayContainsNull(patientID, latitude, longitude)) {
                throw new BadPostParameterException();
            }

            // TODO: validate parameters
            replaceLoc.setInt(1, Integer.parseInt(patientID));
            replaceLoc.setDouble(2, latitude);
            replaceLoc.setDouble(3, longitude);
            
            // The time and date here will be ignored, as they are set by a
            // mysql trigger. We still need to set them as something though
            // because they are both defined NOT NULL.
            replaceLoc.setTimestamp(4, new java.sql.Timestamp(0));
            replaceLoc.setTimestamp(5, new java.sql.Timestamp(0));
        } catch (SQLException e) {
        	// TODO: log
            e.printStackTrace();
        } catch (IOException | JSONException e) {
        	throw new BadPostParameterException();
        }
    }
    
	private JSONObject getJSON(HttpServletRequest request)
			throws BadPostParameterException {

		JSONObject jObj;
		String text;

		try {
			text = getRawText(request);
			jObj = new JSONObject(text);
		} catch (IOException e) {
			throw new BadPostParameterException();
		}

		return jObj;
	}

    private String getRawText(HttpServletRequest request) throws IOException {
		StringBuffer sb = new StringBuffer();
		BufferedReader reader = request.getReader();
		String line;
		
		while ((line = reader.readLine()) != null) {
			sb.append(line);
		}
		
		return sb.toString();
    }

    private void registerJDBCDriver() {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
        	// TODO: log critical
            e.printStackTrace();
        }
    }
}
