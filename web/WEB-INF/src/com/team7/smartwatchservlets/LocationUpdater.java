package com.team7.smartwatchservlets;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class LocationUpdater extends HttpServlet {

    private final static String LOCATION_TABLE = "patientLoc";

    public void doGet(HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException
    {
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException
    {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dementiawatch_db?user=root");
            Statement s = (Statement) conn.createStatement();
            s.executeUpdate(getReplaceString(request));
            s.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("Location received");
    }

    private String getReplaceString(HttpServletRequest request) {
        String patientID = request.getParameter("patientID");
        String latitude = request.getParameter("latitude");
        String longitude = request.getParameter("longitude");
        return "REPLACE INTO " + LOCATION_TABLE + " VALUES ('" + patientID +
               "', '" + latitude + "', '" + longitude + "')";
    }
}
