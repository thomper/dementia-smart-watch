//Function to retrieve map
function initialize(patientLat, patientLong, patientName, patientStatus, fenceRadius, fenceLat, fenceLng) {
	var map_canvas = document.getElementById('mapcanvas');
	var LatLng = new google.maps.LatLng(patientLat, patientLong);
	var FenceLatLng = new google.maps.LatLng(fenceLat, fenceLong);
	//Create string for marker window
	var patientString = '<div id="mapcontent">' +
      '<div id="siteNotice">' +
      '</div>' +
      '<h1>' + patientName + '</h1>' +
      '<div id="bodyContent">' +
      '<p><b>' + patientStatus + '</b></p>' +
	  '</div>' +
	  '</div>';
	
	//Set starting point, zoom level and map type
	var map_options = {
		center: LatLng,
		zoom: 12,
		mapTypeId: google.maps.MapTypeId.ROADMAP
	}
	
	//Circle Details
	var fenceOptions = {
	      strokeColor: '#FF0000',
	      strokeOpacity: 0.8,
	      strokeWeight: 2,
	      fillColor: '#FF0000',
	      fillOpacity: 0.35,
	      map: map,
	      center: FenceLatLng;
	      radius: fenceRadius;
	    };
	    
	//Creates a circle
	var radialFence = new google.maps.Circle(fenceOptions);

	//Creates map
	var map = new google.maps.Map(map_canvas, map_options)

	var patientInfowindow = new google.maps.InfoWindow({
	content: patientString});

	//Creates a marker
	var patientMarker = new google.maps.Marker({
	position: LatLng,
	map: map,
	title: patientName});

	google.maps.event.addListener(patientMarker, 'click', function() {
	patientInfowindow.open(map, patientMarker);});
}
