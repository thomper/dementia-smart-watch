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
			double radius = 0.00;
			double lat = 0.00;
			double lng = 0.00;
			
			try { patientID = Integer.parseInt(request.getParameter("patientID")); }
			catch (Exception e) { response.sendRedirect("../Error.jsp?error=666"); }
%> <script>alert("trying patient ID");</script> <%
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			java.sql.Connection conn;

			//try {
			//	radius = Double.parseDouble(request.getParameter("radius"));
			//	lat = Double.parseDouble(request.getParameter("lat"));
			//	lat = Double.parseDouble(request.getParameter("lng"));
			//} catch (Exception e) {
			//}
			
			//if (tryParseDouble(request.getParameter("radius"))) {
			//	radius = Double.parseDouble(request.getParameter("radius"));
			//}
			//
			//if (Double.parseDouble(request.getParameter("lat"))) {
			//	lat = Double.parseDouble(request.getParameter("lat"));
			//}
			//
			//if (Double.parseDouble(request.getParameter("lng"))) {
			//	lat = Double.parseDouble(request.getParameter("lng"));
			//}
			
			Boolean valid = true; 
			
			try {
				conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dementiawatch_db?user=agile374&password=dementia374");
				Statement st = conn.createStatement();	

				if (valid) {
					if (patientID != 0) {
						st.executeUpdate("UPDATE patientfences SET fenceLat='"+lat+
							"', fenceLong='"+lng+"', radius='"+radius+"'" +
							" WHERE patientID='"+patientID+"';");
						
						// response.sendRedirect("../PatientDetails.jsp?patientid="+patientID+"&success=1");						
					}
					else {
						//We need to create the parient-carer assoiation and generate new patient ID
						/*String uniqueKey = UUID.randomUUID().toString();
						
						st.executeUpdate("INSERT INTO patientfences (patientID, fenceLat, fenceLong, radius) VALUES ('"+patientID+"', '"+lat +"', '"+lng+"', '"+radius+"');");
							
						//response.sendRedirect("../AddPatient.jsp?success=1");*/							
					}	
				} else {
					response.sendRedirect("../Error.jsp?error=2");
				}
				st.close();
				conn.close();
			} catch (Exception e) {
				response.sendRedirect("../Error.jsp?error=9");
			}
		%>
    </body>
</html>