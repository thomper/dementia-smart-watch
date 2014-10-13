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
			String fName = request.getParameter("firstName");
			String lName = request.getParameter("surname");
			String age = request.getParameter("age");
			String bloodType = request.getParameter("bloodType");
			String medication = request.getParameter("medication");
			String address = request.getParameter("address");
			String suburb = request.getParameter("suburb");
			String conNum = request.getParameter("conNum");
			String emergName = request.getParameter("emergName");
			String emergAddress = request.getParameter("emergAddress");
			String emergSuburb = request.getParameter("emergSuburb");
			String emergNum = request.getParameter("emergNum");
			
			String nameReg = "^[a-zA-Z][-\' a-zA-Z]+$";
			String numberReg = "^(\\+|\\d)[0-9]{7,16}$";
			String addressReg = "^[A-Za-z0-9][ A-Za-z0-9]*$";
			String suburbReg = "^[A-Za-z][A-Za-z]*$";
			String medicationReg = "^[A-Za-z0-9][ -'A-Za-z0-9]*$";
			
			boolean valid = true;
		
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			java.sql.Connection conn;
			
			try {
				conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dementiawatch_db?user=agile374&password=dementia374");
				Statement st = conn.createStatement();	
				
				if (!fName.matches(nameReg)) {
					valid = false;
				} else if (!lName.matches(nameReg)) {
					valid = false;
				} else if (!emergNum.matches(numberReg)) {
					valid = false;
				} else if (!conNum.matches(numberReg)) {
					valid = false;
				} else if (!emergAddress.matches(addressReg)) {
					valid = false;
				} else if (!medication.matches(medicationReg)) {
					valid = false;
				} else if (!address.matches(addressReg)) {
					valid = false;
				} else if (!suburb.matches(suburbReg)) {
					valid = false;
				} else if (!emergSuburb.matches(suburbReg)) {
					valid = false;
				} else if (!emergName.matches(nameReg)) {
					valid = false;
				}
	
				if (bloodType.contains("+")) {
					bloodType = bloodType.replace("+", "_POS");
				} else {
					bloodType = bloodType.replace("-", "_NEG");
				}
				
				
				//Note: This page handles processing for both Addpatient & PatientDetails
				
				if (valid) {
					if (patientID != 0) {
						st.executeUpdate("UPDATE patients SET fName='"+fName+
							"', lName='"+lName+"', gender='"+request.getParameter("gender")+"', age='"+age+
							"', bloodType='"+bloodType+"', medication='"+medication+
							"', homeAddress='"+address+
							"', homeSuburb='"+suburb+"', contactNum='"+conNum+
							"', emergencyContactName='"+emergName+"', emergencyContactAddress='"+emergAddress+
							"', emergencyContactSuburb='"+emergSuburb+"', emergencyContactNum='"+emergNum+
							"' WHERE patientID='"+patientID+"' AND carerID = '" + carerID + "';");
						
						response.sendRedirect("../PatientDetails.jsp?patientid="+patientID+"&success=1");						
					}
					else {
						//We need to create the parient-carer assoiation and generate new patient ID
						String uniqueKey = UUID.randomUUID().toString();
						
						st.executeUpdate("INSERT INTO patients VALUES (0, '"+carerID+"', '"+fName+"', '"+lName+
							"', '"+request.getParameter("gender")+"', '"+age+"', '"+bloodType+
							"',  '"+medication+"', 'FINE', '"+address+
							"', '"+suburb+"', '"+conNum+"', '"+emergName+"', '"+emergAddress+
							"', '"+emergSuburb+"', '"+emergNum+"', '"+uniqueKey+"');");
						
						response.sendRedirect("../AddPatient.jsp?success=1");							
					}
								
					st.close();
					conn.close();	
				} else {
					response.sendRedirect("../Error.jsp?error=2");
				}
			} catch (Exception e) {
				response.sendRedirect("../Error.jsp?error=9");
			}
		%>
    </body>
</html>