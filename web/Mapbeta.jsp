<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.sql.DriverManager" %>
<%@ page import="java.sql.Statement" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import ="java.lang.Math" %>


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

		<%!
			private static class Geo{
				private Double lat = 0.00;
				private Double lng = 0.00;
				
				// 1 degree = * pi / 180 * 6360Km = 110.947Km where pi = 3.14.
				// 1 * DEGREES_PER_METRE = 1 metre converted to degrees
				public final Double DEGREES_PER_METRE = 180 / Math.PI / 6360 / 1000;
				
				// 1 * METRES_PER_DEGREE  = 1 degree converted to metres
				public final Double METRES_PER_DEGREE = Math.PI * 6360 * 1000 / 180;
				
				public Geo(Double lat, Double lng){
					this.lat = lat;
					this.lng = lng;
				}
				
				public Double getLat(){
					return this.lat;
				}
				
				public Double getLng(){
					return this.lng;
				}
				
				public void setLat(Double lat){
					this.lat = lat;
				}
				
				public void setLng(Double lng){
					this.lng = lng;
				}
				
				// To be used on a fence to calculate a new point that represents the radius		
				public  Double geoAddLng( Double metres ){			
					this.lng += DEGREES_PER_METRE * metres;
					return this.lng;
				}
				
				// Calculate distance between this point and  another point. 
				// To be used to determine radius
				public  Double dist( Geo point ){
					Double dew = this.lat - point.lat;
					Double dns = this.lng - point.lng;
					Double dist = Math.sqrt( dew * dew + dns * dns );
					dist = dist * METRES_PER_DEGREE;
					return dist;
				}	
			
				// To be used on a patient 
				public  boolean isInsideFence(Geo fence, Geo radiusPoint){	
					Double radius = fence.dist( radiusPoint );
					Double distanceFromFence = this.dist( fence );
					if (distanceFromFence <= radius ){
						return true;
					} else {
						return false;
					}
				}
			}
		
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
				rs = st.executeQuery("SELECT fName, lName, status, patientLat, patientLong, fenceLat, fenceLong, radiusLat, radiusLong FROM patients  JOIN "+
					"patientloc ON patients.patientID = patientloc.patientID LEFT JOIN patientfences ON patientfences.patientID = patientloc.patientID WHERE carerID = '"+carerID+"'");
			} else {
				rs = st.executeQuery("SELECT fName, lName, status, patientLat, patientLong, fenceLat, fenceLong, radiusLat, radiusLong FROM patients JOIN "+
				"patientloc ON patients.patientID = patientloc.patientID LEFT JOIN patientfences ON patientfences.patientID = patientloc.patientID WHERE carerID = '"+carerID+"' AND patientID = '" + patientID+ "'");
			}
			
			Geo patient;
			Geo fence;
			Geo radius;
			String name = "";
			String status = "";
			
			Double patientLat = 0.00;  
			Double patientLng = 0.00; 
			Double fenceLat = 0.00; 
			Double fenceLng = 0.00;
			Double radiusLat = 0.00;
			Double radiusLng = 0.00;
			Double fenceRad = 0.00; // must kill
		%> 	
		
		<script>
		var patientMap = {};
		var fenceMap = {};
		var fence;
		var marker;
	<%			
			while (rs.next()) {
				 fenceRad = 100.00; //must kill
				
				patient = new Geo ( rs.getDouble(4), rs.getDouble(5) );
				 //patientLat = rs.getDouble(4); 
				 //patientLng = rs.getDouble(5);
				 name = rs.getString(1) + " " + rs.getString(2);
				 status = rs.getString(3);
				 
				 if (rs.getDouble(6) != 0.00){
					// if there is a fence
					fence = new Geo(rs.getDouble(6), rs.getDouble(7));
					 
					if (rs.getDouble(8) == 0.00){
						// if no radius is set, set one for 10 metres
						radius = new Geo(fence.getLat(), fence.geoAddLng(10.00));
					} else  {
						radius = new Geo(rs.getDouble(8), rs.getDouble(9));
						}
				 } else {
						// if there is a fence, add it to the fenceMap array			
	%>		
				fenceMap['<%=name%>'] = {
				  center: new google.maps.LatLng(<%=patient.getLat()%>, <%=patient.getLng()%>),
				  name: '<%=name%>'
				};
			<% } // end if %>
				
				patientMap['<%=name%>'] = {
				  center: new google.maps.LatLng(<%=fenceLat%>, <%=fenceLng%>),
				  radius: <%=fenceRad%>
				};
			<% } //end While %>

		function initialize(centerLat, centerLng) {
			  // Create the map.
			  var mapOptions = {
				<% if (patientID != 0) { %>
					zoom: 15,
					center: new google.maps.LatLng(<%=patient.getLat()%>, <%=patient.getLng() %>),
				<% } else { %>
					zoom: 10,
					center: new google.maps.LatLng(centerLat, centerLng),
				<% } %>
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
				google.maps.event.addDomListener(window, 'load', initialize(-27.4073899,153.0028595)) ;
			</script>
		</div>
		<%
			
			st.close();
			conn.close();
		%>
	</div>
<jsp:include page = "includes/footer.jsp" flush = "true" />
