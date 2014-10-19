<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.sql.DriverManager" %>
<%@ page import="java.sql.Statement" %>
<%@ page import="java.util.UUID" %>
		<%
			//If not logged in - redirect to error page and cancel processing od remaining jsp
			if (session.getAttribute("userid") == null) { response.sendRedirect("../Error.jsp?error=5"); return; }
		%>
		<%
			int patientID = 0;
			double radius = 0.00;
			double lat = 0.00;
			double lng = 0.00;
			String sql = "";
			
			try { patientID = Integer.parseInt(request.getParameter("patientID")); }
			catch (Exception e) { System.out.println("Error parsing patient" ); } //outresponse.sendRedirect("../Error.jsp?error=666"); }
			
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			java.sql.Connection conn;

		
			radius = Double.parseDouble(request.getParameter("radius"));
			lat = Double.parseDouble(request.getParameter("lat"));
			lng = Double.parseDouble(request.getParameter("lng"));
			
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
						
						//st.executeUpdate("INSERT INTO patientfences (patientID, fenceLat, fenceLong, radius) VALUES ('"+patientID+"', '"+lat +"', '"+lng+"', '"+radius+"');");
							
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