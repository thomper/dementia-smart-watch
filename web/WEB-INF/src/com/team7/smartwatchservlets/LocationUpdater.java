package com.team7.smartwatchservlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class LocationUpdater extends HttpServlet {

    private final static String DB_NAME = "dementiawatch_db";
    private final static String CONNECTION_STRING = "jdbc:mysql://localhost" +
        ":3306/" + DB_NAME + "?user=agile374&password=dementia374";
    private final static String LOCATION_TABLE = "patientloc";
    private final static String REPLACE_STATEMENT =
        "REPLACE INTO " + LOCATION_TABLE +
        " (patientID, patientLat, patientLong, retrievalTime, retrievalDate) " +
        "values (?, ?, ?, ?, ?)";
    private final static String SUCCESS_MESSAGE = "Location updated";
    private final static String ERROR_MESSAGE = "ERROR: could not update location";

    public void doGet(HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException
    {

    }

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

        // TODO: Read db username and password from file.
        int rowsUpdated = 0;
        Connection conn = null;
        try {
            registerJDBCDriver();
            conn = DriverManager.getConnection(CONNECTION_STRING);
            PreparedStatement replaceLoc = conn.prepareStatement(REPLACE_STATEMENT);
            bindValues(replaceLoc, request);
            rowsUpdated = replaceLoc.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NullPostParameterException e) {
            // TODO: log error?
        } finally {
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
            throws NullPostParameterException {
        try {
            String patientID = request.getParameter("patientID");
            String latitude = request.getParameter("latitude");
            String longitude = request.getParameter("longitude");

            String values[] = new String[] {patientID, latitude, longitude};
            if (!arrayHasNoNulls(values)) {
                throw new NullPostParameterException();
            }

            // TODO: validate parameters
            System.out.println("Patient ID: " + patientID);
            replaceLoc.setInt(1, Integer.parseInt(patientID));
            replaceLoc.setDouble(2, Double.parseDouble(latitude));
            replaceLoc.setDouble(3, Double.parseDouble(longitude));
            
            // The time and date are set by a mysql trigger, we just need
            // to set any value here.
            replaceLoc.setTimestamp(4, new java.sql.Timestamp(0));
            replaceLoc.setTimestamp(5, new java.sql.Timestamp(0));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void registerJDBCDriver() {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private <T> boolean arrayHasNoNulls(T array[]) {
        for (T elem : array) {
            if (elem == null) {
                return false;
            }
        }
        return true;
    }
}
