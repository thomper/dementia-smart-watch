<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.sql.DriverManager" %>
<%@ page import="java.sql.Statement" %>
<%@ page import="java.sql.ResultSet" %>
<%
	//If not logged in - redirect to error page and cancel processing of remaining jsp
	if (session.getAttribute("userid") == null) { response.sendRedirect("Error.jsp?error=5"); return; }

	String carerID = session.getAttribute("carerid").toString();
	int patientID = 0;
	
	try { 
		patientID = Integer.parseInt(request.getParameter("patientid"));
	}
	catch (Exception e) { patientID = 0; }
	
	if (patientID != 0 ) {
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		java.sql.Connection conn;
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dementiawatch_db?user=agile374&password=dementia374");
			Statement st = conn.createStatement();
			st.execute("UPDATE patients SET status = 'FINE' WHERE patientID = " + patientID + " AND carerID = '" + carerID + "'");
			conn.close();
			st.close();
		} catch (Exception e) {
			
		}			
	}
%>
