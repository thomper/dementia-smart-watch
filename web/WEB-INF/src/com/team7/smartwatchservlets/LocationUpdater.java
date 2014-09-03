package com.team7.smartwatchservlets;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class LocationUpdater extends HttpServlet {

    private final static String CONNECTION_STRING = "jdbc:mysql://localhost" +
        ":3306/dementiawatch_db?user=agile374&password=dementia374";
    private final static String DB_NAME = "dementiawatch_db";
    private final static String LOCATION_TABLE = "patientLoc";
    private final static String REPLACE_STATEMENT =
        "REPLACE INTO " + LOCATION_TABLE +
        " (patientID, patientLat, patientLong) " + "values (?, ?, ?)";
    private final static String SUCCESS_MESSAGE = "Location updated";
    private final static String ERROR_MESSAGE = "ERROR: could not update location";

    public void doGet(HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException
    {

    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException
    {
        registerJDBCDriver();
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
        try {
            Connection conn = DriverManager.getConnection(CONNECTION_STRING);
            PreparedStatement replaceLoc = conn.prepareStatement(REPLACE_STATEMENT);
            bindValues(replaceLoc, request);
            rowsUpdated = replaceLoc.executeUpdate();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NullPostParameterException e) {
            // TODO: log error?
        }

        return rowsUpdated;
    }

    private void bindValues(PreparedStatement replaceLoc, HttpServletRequest request)
            throws NullPostParameterException {
        try {
            String patientID = request.getParameter("patientID");
            String latitude = request.getParameter("latitude");
            String longitude = request.getParameter("longitude");

            if (!arrayHasNoNulls(new String[] {patientID, latitude, longitude})) {
                throw new NullPostParameterException();
            }

            replaceLoc.setInt(1, Integer.parseInt(patientID));
            replaceLoc.setDouble(2, Double.parseDouble(latitude));
            replaceLoc.setDouble(3, Double.parseDouble(longitude));
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
