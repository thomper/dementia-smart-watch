<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.sql.DriverManager" %>
<%@ page import="java.sql.Statement" %>
<%@ page import="java.sql.ResultSet" %>

<!DOCTYPE html>
<html>
  <head profile="http://www.w3.org/2005/10/profile">
	<title>Map – DementiaWatch Web Client</title>
	<meta http-equiv="content-type" content="text/html; charset=UTF-8">	
    <meta charset="utf-8">
	<link rel="icon" type="image/jpg" href="images/DementiaLogo.png">
	<link rel="stylesheet" type="text/css" href="css/mystyle.css">
    <script src="https://maps.googleapis.com/maps/api/js?v=3.exp"></script>
	<script src="https://maps.googleapis.com/maps/api/js?v=3.exp"></script>
	    	
		<%
			//If not logged in - redirect to error page and cancel processing od remaining jsp
			if (session.getAttribute("userid") == null) { response.sendRedirect("Error.jsp?error=5"); return; }
		%>

		<%  
			String carerID = session.getAttribute("carerid").toString(); 
			int patientID = 0;
				try { 
					patientID = Integer.parseInt(request.getParameter("patientid"));
				}
				catch (Exception e) { patientID = 0; }	

			Class.forName("com.mysql.jdbc.Driver").newInstance();
			java.sql.Connection conn;
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dementiawatch_db?user=agile374&password=dementia374");
			Statement st = conn.createStatement();
			ResultSet rs = null;
			if (patientID == 0){ 				
				rs = st.executeQuery("SELECT fName, lName, status, patientLat, patientLong FROM patients NATURAL JOIN "+
					"patientloc WHERE carerID = '"+carerID+"'");
			} else {
				rs = st.executeQuery("SELECT fName, lName, status, patientLat, patientLong FROM patients NATURAL JOIN "+
				"patientloc WHERE carerID = '"+carerID+"' AND patientID = '" + patientID+ "'");
			}
			Double patientLat = 0.00;  
			Double patientLng = 0.00; 
			String name = "";
			String status;
			Double fenceLat = 0.00; 
			Double fenceLng = 0.00;
			Double fenceRad;
			
		%> 	
		
		<script>
		var patientMap = {};
		var fenceMap = {};
		var fence;
		var marker;
	<%			
			while (rs.next()) {
				 patientLat = rs.getDouble(4); 
				 patientLng = rs.getDouble(5);
				 name = rs.getString(1) + " " + rs.getString(2);
				 status = rs.getString(3);
				 fenceLat = rs.getDouble(4); 
				 fenceLng = rs.getDouble(5);
				 fenceRad = 100.00; //
				
		%>		
				fenceMap['<%=name%>'] = {
				  center: new google.maps.LatLng(<%=patientLat%>, <%=patientLng%>),
				  name: '<%=name%>'
				};
				
				patientMap['<%=name%>'] = {
				  center: new google.maps.LatLng(<%=fenceLat%>, <%=fenceLng%>),
				  radius: <%=fenceRad%>
				};
			<% } //end While%>

		function initialize(centerLat, centerLng) {
			  // Create the map.
			  var mapOptions = {
				<% if (patientID != 0) { %>
					zoom: 15,
				<% } else { %>
					zoom: 12,
				<% } %>
				center: new google.maps.LatLng(centerLat, centerLng),				
				mapTypeId: google.maps.MapTypeId.TERRAIN
			  };

			  var map = new google.maps.Map(document.getElementById('map-canvas'),
				  mapOptions);

			  // Construct the circle for each value in patientMap.
			  // Note: We scale the area of the circle based on the population.
			  for (var patient in patientMap) {
				var fenceOptions = {
				  strokeColor: '#FF0000',
				  strokeOpacity: 0.8,
				  strokeWeight: 2,
				  fillColor: '#FF0000',
				  fillOpacity: 0.35,
				  map: map,
				  center: patientMap[patient].center,
				  radius: patientMap[patient].radius
				};
				for (var fence in fenceMap){
				var marker = new google.maps.Marker({
					position: fenceMap[fence].center,
					map: map,
					title: fenceMap[fence].name
				}); 
				}	
				// Add the circle for this city to the map.
				fence = new google.maps.Circle(fenceOptions);
			  }
			}
		</script>
		
		
		
		
		
    
	<%-- This will add the rest of the head tag and navigation and alerts --%>	
	<jsp:include page = "includes/header.jsp" flush = "true" />
	<jsp:include page = "includes/headerP.jsp" flush = "true" />

	<div id="content">		
		<div id="map-canvas" style="height:500px; width:800px; margin-left:auto; margin-right:auto;"> 
			<script>
				google.maps.event.addDomListener(window, 'load', initialize(<%=fenceLat%>, <%=fenceLng%>)) ;
			</script>
		</div>
		<%
			
			st.close();
			conn.close();
		%>
	</div>
<jsp:include page = "includes/footer.jsp" flush = "true" />
