g<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.sql.DriverManager" %>
<%@ page import="java.sql.Statement" %>
<%@ page import="java.sql.ResultSet" %>
<!DOCTYPE html>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<link rel="icon" type="image/jpg" href="images/DementiaLogo.png">
        <title>UpdatePatient</title>
		<%
			//If not logged in - redirect to error page and cancel processing od remaining jsp
			if (session.getAttribute("userid") == null) { response.sendRedirect("../Error.jsp?error=5"); return; }
		%>
    </head>
    <body>
		<%
			boolean patientExists = false;
			try { patientExists = Boolean.parseBoolean(request.getParameter("patientexists")); }
			catch (Exception e) { response.sendRedirect("../Error.jsp?error=666"); } //Unreachable if page called correctly
			
			String carerID = session.getAttribute("carerid").toString();
					
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			java.sql.Connection conn;
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dementiawatch_db?user=root");
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("SELECT * FROM patients WHERE carerID='"+carerID+"'");		
			
			if(rs.next()) {
				if (patientExists) {
					st.executeUpdate("UPDATE patients SET fName='"+request.getParameter("firstName")+
						"', lName='"+request.getParameter("surname")+"', gender='"+request.getParameter("gender")+"', age='"+request.getParameter("age")+
						"', bloodType='"+request.getParameter("bloodType")+"', medication='"+request.getParameter("medication")+
						"', status='"+request.getParameter("status")+"', homeAddress='"+request.getParameter("address")+
						"', homeSuburb='"+request.getParameter("suburb")+"', contactNum='"+request.getParameter("conNum")+
						"', emergencyContactName='"+request.getParameter("emergName")+"', emergencyContactAddress='"+request.getParameter("emergAddress")+
						"', emergencyContactSuburb='"+request.getParameter("emergSuburb")+"', emergencyContactNum='"+request.getParameter("emergNum")+
						"', uniqueKey='"+request.getParameter("uniqueKey")+"' WHERE patientID='"+rs.getString(1)+"';");
					
					response.sendRedirect("../MyPatient.jsp?success=1");				
				}
			}
			else {
				//We need to create the parient-carer assoiation and generate new patient ID
				
				st.executeUpdate("INSERT INTO patients VALUES (0, '"+carerID+"', '"+request.getParameter("firstName")+"', '"+request.getParameter("surname")+
					"', '"+request.getParameter("gender")+"', '"+request.getParameter("age")+"', '"+request.getParameter("bloodType")+
					"',  '"+request.getParameter("medication")+"', '"+request.getParameter("status")+"', '"+request.getParameter("address")+
					"', '"+request.getParameter("suburb")+"', '"+request.getParameter("conNum")+"', '"+request.getParameter("emergName")+"', '"+request.getParameter("emergAddress")+
					"', '"+request.getParameter("emergSuburb")+"', '"+request.getParameter("emergNum")+"', '"+request.getParameter("uniqueKey")+"');");
				
				response.sendRedirect("../MyPatient.jsp?success=1");							
			}
			
			rs.close();				
			st.close();
			conn.close();	
				
		%>
    </body>
</html>