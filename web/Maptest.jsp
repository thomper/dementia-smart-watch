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
	<script type="javascript" src="scripts/googleMaps.js"></script>
	
	
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
				rs = st.executeQuery("SELECT fName, lName, status, patientLat, patientLong, fenceLat, fenceLong, radiusLat, radiusLong FROM patients  JOIN "+
					"patientloc ON patients.patientID = patientloc.patientID LEFT JOIN patientfences ON patientfences.patientID = patientloc.patientID WHERE carerID = '"+carerID+"'");
			} else {
				rs = st.executeQuery("SELECT fName, lName, status, patientLat, patientLong, fenceLat, fenceLong, radiusLat, radiusLong FROM patients JOIN "+
				"patientloc ON patients.patientID = patientloc.patientID LEFT JOIN patientfences ON patientfences.patientID = patientloc.patientID WHERE carerID = '"+carerID+"' AND patientID = '" + patientID+ "'");
			}
			Double patientLat = 0.00;  
			Double patientLng = 0.00; 
			String name = "";
			String status = "";
			Double fenceLat = 0.00; 
			Double fenceLng = 0.00;
			Double radiusLat = 0.00;
			Double radiusLong = 0.00;
			Double fenceRad = 0.00; // must kill
		%> 	
		
		<script>
		var patientMap = {};
		var fenceMap = {};
		var fence;
		var marker;
		
	$(document).ready(function() {
    var mapCenter = new google.maps.LatLng(47.6145, -122.3418); //Google map Coordinates
    var map;

    map_initialize(); // initialize google map
    
    //############### Google Map Initialize ##############
    function map_initialize()
    {
        var googleMapOptions = 
        { 
            center: mapCenter, // map center
            zoom: 17, //zoom level, 0 = earth view to higher value
            panControl: true, //enable pan Control
            zoomControl: true, //enable zoom control
            zoomControlOptions: {
            style: google.maps.ZoomControlStyle.SMALL //zoom control size
        },
            scaleControl: true, // enable scale control
            mapTypeId: google.maps.MapTypeId.ROADMAP // google map type
        };
    
        map = new google.maps.Map(document.getElementById("google_map"), googleMapOptions);         
       
		<%			
			while (rs.next()) {
				 patientLat = rs.getDouble(4); 
				 patientLng = rs.getDouble(5);
				 name = rs.getString(1) + " " + rs.getString(2);
				 status = rs.getString(3);
				 if (rs.getDouble(6) == 0){; 
					fenceLat = rs.getDouble(4);  //new value TODO: use GEO class
					fenceLng = rs.getDouble(5); //new value TODO: use GEO class
				 } else {
					fenceLat = rs.getDouble(6); 
					fenceLng = rs.getDouble(7);
				 }
				 
				 fenceRad = 100.00; //must kill
				
		%>
				var mapPos = new google.maps.LatLng(<%=fenceLat%>,<%=fenceLng%>);
				var mapTitle = '<%= name%>';
				var mapDesc = '<%=status%>';
				
	   
        //drop a new marker on right click
        google.maps.event.addListener(map, 'rightclick', function(event) {
            //Edit form to be displayed with new marker
            var EditForm = '<p><div class="marker-edit">'+
            '<form action="" method="POST" name="SaveMarker" id="SaveMarker">'+
            '<label for="pName"><span>Place Name :</span><input type="text" name="pName" class="save-name" placeholder="Enter Title" maxlength="40" /></label>'+
            '<label for="pDesc"><span>Description :</span><textarea name="pDesc" class="save-desc" placeholder="Enter Address" maxlength="150"></textarea></label>'+
            '<label for="pType"><span>Type :</span> <select name="pType" class="save-type"><option value="restaurant">Rastaurant</option><option value="bar">Bar</option>'+
            '<option value="house">House</option></select></label>'+
            '</form>'+
            '</div></p><button name="save-marker" class="save-marker">Save Marker Details</button>';

            //call create_marker() function
            create_marker(mapPos, mapTitle, EditForm,  false, true, true);
        });
		<% } %>
        		
    }
});

</script>


	<%-- This will add the rest of the head tag and navigation and alerts --%>	
	<jsp:include page = "includes/header.jsp" flush = "true" />
	<jsp:include page = "includes/headerP.jsp" flush = "true" />

	<div id="content">

		<div id="map-canvas" style="height:500px; width:500px; margin-left:auto; margin-right:auto;"> 
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