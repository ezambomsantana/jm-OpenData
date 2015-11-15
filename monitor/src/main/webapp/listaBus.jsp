<html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<head>
<title>Lista de ônibus</title>
<meta name="viewport" content="initial-scale=1.0, user-scalable=no">
<meta charset="utf-8">
<meta http-equiv="refresh" content="30">
<style>
html, body, #map-canvas {
	height: 100%;
	margin: 0px;
	padding: 0px
}
</style>
<script src="https://maps.googleapis.com/maps/api/js?v=3.exp"></script>
<script>
	  

    function initialize() {

    	  var myLatlng = new google.maps.LatLng(-23.573741625000004, -46.726239);
    	  var mapOptions = {
    	    zoom: 11,
    	    center: myLatlng
    	  }
    	  var map = new google.maps.Map(document.getElementById('map-canvas'), mapOptions);
    	  var text = '';

    	  
	      <c:forEach  var="bus" items="${lista}">
	      
	      mark(map, <c:out value="${bus.lon}"/>, <c:out value="${bus.lat}"/>, '<c:out value="${bus.letters}"/>');
		
		  </c:forEach>	
			

    	}

    function mark(map, lat, lon, text) {

  	  var myLatlng = new google.maps.LatLng(lat, lon);
  	  var marker = new google.maps.Marker({
	      position: myLatlng,
	      map: map,
	      title: text
	  });
    }
    
    	google.maps.event.addDomListener(window, 'load', initialize);

    </script>
</head>
<body>
	<div id="map-canvas"></div>


	<table>
		<tr>
			<td>Nome</td>
			<td>Logintude</td>
			<td>Latitude</td>
		</tr>

		<c:forEach var="bus" items="${lista}">
			<tr>
				<td><c:out value="${bus.letters}"></c:out></td>
				<td><c:out value="${bus.lat}"></c:out></td>
				<td><c:out value="${bus.lon}"></c:out></td>

			</tr>
		</c:forEach>
	</table>

</body>
</html>