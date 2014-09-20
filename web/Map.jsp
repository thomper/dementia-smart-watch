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
    
	<%-- This will add the rest of the head tag and navigation and alerts --%>	
	<jsp:include page = "includes/header.jsp" flush = "true" />
	<jsp:include page = "includes/headerP.jsp" flush = "true" />
				
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
			
				<h1>Patients Location Tracking</h1>
			
			<%											
				Class.forName("com.mysql.jdbc.Driver").newInstance();
				java.sql.Connection conn;
				conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dementiawatch_db?user=agile374&password=dementia374");
				Statement st = conn.createStatement();
				
				if (patientID != 0 ) {
				
					ResultSet rs = st.executeQuery("SELECT fName, lName, status, patientLat, patientLong FROM patients JOIN "+
						"patientLoc on patientLoc.patientID = patients.patientID WHERE patientLoc.patientID=" + patientID);	

					if (rs.next()) {
						
						Double lat = rs.getDouble(4);
						Double longtitude = rs.getDouble(5);
						Double fenceRadius = 100.00;
						String status = rs.getString(3);
						String name = rs.getString(1) + " " + rs.getString(2);%>
						
						<div id="mapcanvas" style="height:500px; width:800px; margin-left:auto; margin-right:auto;">
							<script src="https://maps.googleapis.com/maps/api/js?v=3.exp&sensor=false"></script>
							<script>google.maps.event.addDomListener(window, 'load', initialize(<%=lat%>, <%=longtitude%>, '<%=name%>', '<%=status%>', <%=fenceRadius%>, <%=lat%>, <%=longtitude%>));</script>
						</div>
					<%}	else {%>
						<p>There is currently no location data stored on this patient, please click <a href="PatientList.jsp">here</a> to return to your list of patients</p>
					<%}
					rs.close();
				}
				else {%>
					<p>Something has gone wrong, please click <a href="index.jsp">here</a> to return to the home page</p>
				<%}
				
				st.close();
				conn.close();		
			%>					
		</div>	
		
	<jsp:include page = "includes/footer.jsp" flush = "true" />
