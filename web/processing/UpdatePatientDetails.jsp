<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.sql.DriverManager" %>
<%@ page import="java.sql.Statement" %>
<%@ page import="java.util.UUID" %>
<!DOCTYPE html>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<link rel="icon" type="image/jpg" href="images/DementiaLogo.png">
        <title>UpdatePatientDetails</title>
		<%
			//If not logged in - redirect to error page and cancel processing od remaining jsp
			if (session.getAttribute("userid") == null) { response.sendRedirect("../Error.jsp?error=5"); return; }
		%>
    </head>
    <body>
		<%
			int patientID = 0;
			try { patientID = Integer.parseInt(request.getParameter("patientid")); }
			catch (Exception e) { response.sendRedirect("../Error.jsp?error=666"); } //Unreachable
			
			String carerID = session.getAttribute("carerid").toString();
		
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			java.sql.Connection conn;
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dementiawatch_db?user=agile374&password=dementia374");
			Statement st = conn.createStatement();	
			
			//Note: This page handles processing for both Addpatient & PatientDetails
			
			if (patientID != 0) {
				st.executeUpdate("UPDATE patients SET fName='"+request.getParameter("firstName")+
					"', lName='"+request.getParameter("surname")+"', gender='"+request.getParameter("gender")+"', age='"+request.getParameter("age")+
					"', bloodType='"+request.getParameter("bloodType")+"', medication='"+request.getParameter("medication")+
					"', homeAddress='"+request.getParameter("address")+
					"', homeSuburb='"+request.getParameter("suburb")+"', contactNum='"+request.getParameter("conNum")+
					"', emergencyContactName='"+request.getParameter("emergName")+"', emergencyContactAddress='"+request.getParameter("emergAddress")+
					"', emergencyContactSuburb='"+request.getParameter("emergSuburb")+"', emergencyContactNum='"+request.getParameter("emergNum")+
					"' WHERE patientID='"+patientID+"';");
				
				response.sendRedirect("../PatientDetails.jsp?patientid="+patientID+"&success=1");						
			}
			else {
				//We need to create the parient-carer assoiation and generate new patient ID
				String uniqueKey = UUID.randomUUID().toString();
				
				st.executeUpdate("INSERT INTO patients VALUES (0, '"+carerID+"', '"+request.getParameter("firstName")+"', '"+request.getParameter("surname")+
					"', '"+request.getParameter("gender")+"', '"+request.getParameter("age")+"', '"+request.getParameter("bloodType")+
					"',  '"+request.getParameter("medication")+"', 'OK', '"+request.getParameter("address")+
					"', '"+request.getParameter("suburb")+"', '"+request.getParameter("conNum")+"', '"+request.getParameter("emergName")+"', '"+request.getParameter("emergAddress")+
					"', '"+request.getParameter("emergSuburb")+"', '"+request.getParameter("emergNum")+"', '"+uniqueKey+"');");
				
				response.sendRedirect("../AddPatient.jsp?success=1");							
			}
						
			st.close();
			conn.close();	
				
		%>
    </body>
</html>