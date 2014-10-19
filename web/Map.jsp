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
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.8.3/jquery.min.js"></script>
	    	
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
			Statement updb = conn.createStatement();
			ResultSet rs = null;
			if (patientID == 0){ 				
				rs = st.executeQuery("SELECT fName, lName, status, patientLat, patientLong, fenceLat, fenceLong, radius, patients.patientID, retrievalTime, retrievalDate FROM patients JOIN patientloc ON patientloc.patientID = patients.patientID JOIN patientfences ON patientfences.patientID = patients.patientID WHERE carerID = "+carerID+" GROUP BY patients.patientID ORDER BY retrievalDate DESC, retrievalTime DESC");
			} else {
				rs = st.executeQuery("SELECT fName, lName, status, patientLat, patientLong, fenceLat, fenceLong, radius, patients.patientID, retrievalTime, retrievalDate FROM patients JOIN patientloc ON patientloc.patientID = patients.patientID JOIN patientfences ON patientfences.patientID = patients.patientID WHERE carerID = "+carerID+" AND patients.patientID = "+patientID+" GROUP BY patients.patientID ORDER BY retrievalDate DESC, retrievalTime DESC");
			}
			
			Double patientLat = 0.00;  
			Double patientLng = 0.00; 
			String name = "";
			String status = "";
			Double fenceLat = 0.00; 
			Double fenceLng = 0.00;
			Double fenceRad  = 0.00;
			Integer currentPatientID = 0;
		%> 	
		
		<script>
		var fenceMap = {};
		var patientMap = {};
		var fence;
		var marker;
		
	<%			
			while (rs.next()) {
				 patientLat = rs.getDouble(4); 
				 patientLng = rs.getDouble(5);
				 currentPatientID = rs.getInt(9);
				 name = rs.getString(1) + " " + rs.getString(2);
				 status = rs.getString(3);
				
				 if (rs.getDouble(6) == 0){; 
					fenceLat = patientLat; 
					fenceLng = patientLng;
					updb.executeUpdate("INSERT INTO patientfences (patientID, fenceLat, fenceLong) VALUES ("+currentPatientID + ", " + fenceLat  +", " + fenceLng+")");
				 } else {
					fenceLat = rs.getDouble(6); 
					fenceLng = rs.getDouble(7);
				 }
				if (rs.getDouble(8) == 0) {
					fenceRad = 30.00;
				} else {
					fenceRad = rs.getDouble(8);
				 }
		%>		
				patientMap['<%=name%>'] = {
				  center: new google.maps.LatLng(<%=patientLat%>, <%=patientLng%>),
				  name: '<%=name%>',
				  id: <%=currentPatientID%>
				};
				
				fenceMap['<%=name%>'] = {
				  center: new google.maps.LatLng(<%=fenceLat%>, <%=fenceLng%>),
				  radius: <%=fenceRad%>,
				  owner: <%=currentPatientID%>
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
				mapTypeId: google.maps.MapTypeId.ROADMAP
			  };

			  var map = new google.maps.Map(document.getElementById('map-canvas'),
				  mapOptions);

			  // Construct the circle for each value in fenceMap.
			  // Note: We scale the area of the circle based on the population.
			  for (var fence in fenceMap) {
				var fenceOptions = {
				  strokeColor: '#FF0000',
				  strokeOpacity: 0.8,
				  strokeWeight: 2,
				  fillColor: '#FF0000',
				  fillOpacity: 0.35,
				  map: map,
				  center: fenceMap[fence].center,
				  radius: fenceMap[fence].radius,
				  editable: true,
				  draggable: true
				};
				for (var patient in patientMap){
				var marker = new google.maps.Marker({
					position: patientMap[patient].center,
					map: map,
					title: patientMap[patient].name
				}); 
				}
				// Add the circle for this city to the map.
				fenceMap[fence] = {
					object: new google.maps.Circle(fenceOptions),
					window: new google.maps.InfoWindow({
					content: "<p>put butten here</p>" 					 
					})

				};
				
				 google.maps.event.addListener(fenceMap[fence].object, 'radius_changed', function() {
					var newRadius = fenceMap[fence].object.getRadius();
					var patientUpdated = fenceMap[fence].owner; //may need .object
					$.ajax({
						url:"processing/UpdateFence.jsp",
						type: "POST",
						data: { "radius": newRadius, "patientID": patientUpdated},
						success:function(e){
							alert(e);
						}
					});
					alert(" Successfully Updated Database. Radius now set to: " + newRadius+"metres");
				 
				 });
				 google.maps.event.addListener(fenceMap[fence].object, 'center_changed', function() {
					/*var newLat = fenceMap[fence].object.getCenter().lat();
					var newLng = fenceMap[fence].object.getCenter().lng();
					var patientUpdated = fenceMap[fence].owner; //may need .object
					$.ajax({
						url:"processing/UpdateFence.jsp",
						type: "POST",
						data: { "lat": newLat, "lng": newLng, "patientID":  patientUpdated },
						success:function(e){
							alert(e);
						}
					});*/
					alert(" Successfully Updated Database");
				 });
				
				
				
				
			  
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
			updb.close();
			conn.close();
		%>
	</div>
<jsp:include page = "includes/footer.jsp" flush = "true" />
