var map;
var myLatLng = { lat: 46.7693924, lng: 23.5902006 };

function initialize() {
  var mapCanvas = document.getElementById("map");
  var mapOptions = {
    center: new google.maps.LatLng(myLatLng),
    zoom: 8,
    mapTypeId: google.maps.MapTypeId.ROADMAP,
  };

  map = new google.maps.Map(mapCanvas, mapOptions);
}

function addStaticMarker() {
  const locations = JSON.parse(localStorage.getItem("locations"));
  for (var i = 0; i < locations.length; i++) {
    var pos = {
      lat: Number(locations[i].latitude),
      lng: Number(locations[i].longitude),
    };

    new google.maps.Marker({
      position: pos,
      map: map,
      title: locations[i].terminalId + " " + locations[i].creationDate,
    });
  }
  localStorage.setItem('locations', JSON.stringify({}));
}
