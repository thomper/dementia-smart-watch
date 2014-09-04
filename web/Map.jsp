<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.sql.DriverManager" %>
<%@ page import="java.sql.Statement" %>
<%@ page import="java.sql.ResultSet" %>
<!DOCTYPE html>

<html>
    <head profile="http://www.w3.org/2005/10/profile">
		<title>Map – DementiaWatch Web Client</title>
		<meta http-equiv="content-type" content="text/html; charset=UTF-8">
		<link rel="icon" type="image/jpg" href="images/DementiaLogo.png">
		<link rel="stylesheet" type="text/css" href="css/mystyle.css">
		<script src="scripts/maps.js" type="text/javascript"></script>
		<%
			//If not logged in - redirect to error page and cancel processing od remaining jsp
			if (session.getAttribute("userid") == null) { response.sendRedirect("Error.jsp?error=5"); return; }
		%>			
    </head>
	
    <body>
	<div id="container">
		
		<div id="header">
			<div id="header-left">
				<p>Location: <a href="PatientList.jsp">Patient List</a> > Map</p>
			</div>
			<div id="header-right">
				<p><a href="ChangePassword.jsp">Change PW</a> | <a href="Logout.jsp">Logout</a></p>
			</div>
			<div id="header-middle">
				<p>DementiaWatch Web Client</p>
			</div>			
		</div>
	
		<div id="content">		
			
			<%
				String carerID = session.getAttribute("carerid").toString();
				int patientID = 0;
				try { 
					patientID = Integer.parseInt(request.getParameter("patientid"));
				}
				catch (Exception e) { patientID = 0; }	

				//The above code gets you the patientID to do whatever
				//This page needs to be called as:   Map.jsp?patientid=xxx
				
			%>
			
			<%			
				Double lat = 0.0;
				Double longtitude = 0.0;
				String status = "";
				String name = "";				
				
				Class.forName("com.mysql.jdbc.Driver").newInstance();
				java.sql.Connection conn;
				conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dementiawatch_db?user=agile374&password=dementia374");
				Statement st = conn.createStatement();
				
				if (patientID != 0 ) {
				
					ResultSet rs = st.executeQuery("SELECT fName, lName, status, patientLat, patientLong FROM patients JOIN "+
						"patientLoc on patientLoc.patientID = patients.patientID WHERE patientLoc.patientID=" + patientID);	

					if (rs.next()) {
						
						lat = rs.getDouble(4);
						longtitude = rs.getDouble(5);
						status = rs.getString(3);
						name = rs.getString(1) + " " + rs.getString(2);
					}	
					rs.close();
				}
				else {
					//No patientID has been passed (i.e. patientID==0)
					//use the carerID variable to potentially display the location of all patients?
				}
				
				st.close();
				conn.close();		
			%>	
			
			<h1>Map / Patient Tracking</h1>
			<p>Put map here</p><br>
			
			<div id="mapcanvas" style="height:500px; width:800px; margin-left:auto; margin-right:auto;">
				<script src="https://maps.googleapis.com/maps/api/js?v=3.exp&sensor=false"></script>
				<script>google.maps.event.addDomListener(window, 'load', initialize(<%=lat%>, <%=longtitude%>, '<%=name%>', '<%=status%>'));</script>
			</div>	
				
		</div>	
		
	</div>
    </body>
</html>

