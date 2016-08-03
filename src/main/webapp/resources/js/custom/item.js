

$(document).ready(function(){
	
	console.log("base url: " + baseURL);
	console.log("window.location.href: " + window.location.href)
	
	var url = window.location.href;
	var itemID = url.substring(url.lastIndexOf("/")+1); 
	console.log("item ID: " + itemID);
	getItemModule(itemID);
	checkForUser();
	
});

/* --------- Global Variables ----------- */

var map;
var geocoder;
var location_marker;
var user_auctions;
var latitude;
var longtitude;

/* --------- Global Variables ----------- */

function getItemModule(itemID) {
	
	console.log("getting the item module ");
	$.get( window.location.href + "/details-module", function( details_module ) {
		getDetails(itemID,details_module);
		
	});
	
}

function getDetails(itemID, details_module) {
	
	console.log("getting item details");
	$.ajax({
		type : "GET",
		dataType:'json',
		//url  : baseURL + "/auctions/view-auctions/",
		url  :window.location.href + "/details",
		data :{itemID:itemID},
		success : function(data) {
			$("#item-details").empty();
			console.log("moving on formatting the page")
			if(data.length == 0){
				alert("No data found");
			}else {
				console.log(data)
				
				var panel = $("<div>" + details_module + "</div>");
				panel.find('#Title').text(data.name);
				panel.find('#description-textarea').text(data.description);
				panel.find('#itemID').text(data.id);
				panel.find('#location').text(data.location);
				panel.find('#latitude').text(data.lat);
				panel.find('#longtitude').text(data.lon);
				panel.find('#allcategories').text(data.category);
				panel.find('#byuprice').text(data.byuprice);
				panel.find('#seller').text(data.seller);
				panel.find('#firstbid').text(data.firstbid);
				latitude = data.lat;
				longtitude = data.lon;
				
				html = panel.html();
				$("#item-details").append(html);
				
			}
		}	
	}); 
	console.log("end of getting item details")
}


function initMap(){
	console.log("Creating google maps")
	var center = new google.maps.LatLng(latitude,longtitude);
	var mapDiv = document.getElementById('itemMap');
	map = new google.maps.Map(mapDiv, {
	    center: center,
	    zoom: 8,
	    mapTypeId: google.maps.MapTypeId.ROADMAP
	});
	
	//var position = new google.maps.LatLng(latitude,longtitude);
	console.log("center: " + center)
	location_marker = new google.maps.Marker({
        position: center,
        map: map
    });
	location_marker.setMap(map);

	
	/*map.addListener('click', function(e) {
		
		if(typeof location_marker !== 'undefined')
			location_marker.setMap(null);
		
		location_marker = new google.maps.Marker({
	        position: e.latLng,
	        map: map
	    });
		location_marker.setMap(map);
	});*/
	
	console.log("end of creation")
}