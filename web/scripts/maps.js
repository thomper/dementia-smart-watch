//Function to retrieve map
function initialize(patientLat, patientLong, patientName, patientStatus) {
	var map_canvas = document.getElementById('mapcanvas');
	var LatLng = new google.maps.LatLng(patientLat, patientLong);
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
	
	//Creates map
	var map = new google.maps.Map(map_canvas, map_options)
	
	var patientInfowindow = new google.maps.InfoWindow({
	content: patientString});

	var patientMarker = new google.maps.Marker({
	position: LatLng,
	map: map,
	title: patientName});

	google.maps.event.addListener(patientMarker, 'click', function() {
	patientInfowindow.open(map, patientMarker);});
}