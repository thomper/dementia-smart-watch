<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.sql.DriverManager" %>
<%@ page import="java.sql.Statement" %>
<%@ page import="java.sql.ResultSet" %>
<!DOCTYPE html>

<html>
	<head>
		<%
			//If not logged in - redirect to error page and cancel processing of remaining jsp
			if (session.getAttribute("userid") == null) { response.sendRedirect("Error.jsp?error=5"); return; }
		%>
	</head>
	<body>
		<%
			String carerID = session.getAttribute("carerid").toString();
			String checkPreviousPage = "";
			int patientID = 0;
			
			try { 
				patientID = Integer.parseInt(request.getParameter("patientid"));
			}
			catch (Exception e) { patientID = 0; }

			try {
				checkPreviousPage = request.getParameter("page").toString();
			} catch (Exception e) { checkPreviousPage = ""; }
			
			if (patientID != 0 ) {
				Class.forName("com.mysql.jdbc.Driver").newInstance();
				java.sql.Connection conn;
				conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dementiawatch_db?user=agile374&password=dementia374");
				Statement st = conn.createStatement();
				st.execute("UPDATE patients SET status = 'fine' WHERE patientID = " + patientID + " AND carerID = '" + carerID + "'");		
			}
			
			conn.close();
			st.close();
			
			if (checkPreviousPage.equals("List") == true) {
				response.sendRedirect("PatientList.jsp");
			} else { 
				response.sendRedirect("Home.jsp");
			}
		%>
	</body>
</html>