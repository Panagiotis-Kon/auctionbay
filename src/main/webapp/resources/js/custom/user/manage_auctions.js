$(document).ready(function (){
	
	
	
	getCategoryList();
	
	$("#mapTab").on('shown.bs.tab', function() {

	  	console.log("resizing map...")
		google.maps.event.trigger(map, 'resize');
	});
	$('#country2').change(function(){
        var country = $(this).val();
        console.log("country: " + country)
        geocodeAddress(geocoder, map, country);
	});
	
	$("#category_list").multiselect({
		selectedList: 4
	});
});
var map;
var geocoder;
var location_marker;
function initGoogleMap(){
	center = new google.maps.LatLng(51.508742,-0.120850)
	map = new google.maps.Map(document.getElementById('googleMap'), {
	    center: center,
	    zoom: 8,
	    mapTypeId: google.maps.MapTypeId.ROADMAP
	});
	geocoder = new google.maps.Geocoder();

	location_marker = new google.maps.Marker({
        position: center,
        map: map
    });
	location_marker.setMap(map);

	
	map.addListener('click', function(e) {
		
		if(typeof location_marker !== 'undefined')
			location_marker.setMap(null);
		
		location_marker = new google.maps.Marker({
	        position: e.latLng,
	        map: map
	    });
		location_marker.setMap(map);
	});
}

function geocodeAddress(geocoder, resultsMap, country) {
   
    geocoder.geocode({'address': country}, function(results, status) {
      if (status === google.maps.GeocoderStatus.OK) {
        resultsMap.setCenter(results[0].geometry.location);
       
      } else {
        alert('Geocode was not successful for the following reason: ' + status);
      }
    });
  }


function getCategoryList(){
	/* Make ajax call to receive the categories from the db */
	console.log("getting the categories");

	$.ajax({
		type : "GET",
		dataType:'json',
		url  : baseURL + "/auctions/categories",
		//url  : window.location.href + "/categories",
		success : function(data) {
			
			if(data.length == 0){
				$('#category_list').css("display","none");
				$('#no-categories').css("display","block");
				
			} else {
				for(var i = 0; i < data.length; i++) {			
					var category = data[i].category;
					var option = $('<option value="'+data[i].category+'">');
					option.text(category);
					$("#category_list").append(option);
					
					
				}
				$("#category_list").multiselect("refresh");
			}
			
		}	
	}); 
	
	console.log("getting the categories ended");
}


function addCategoryInput(){
	console.log("you clicked")
	$('#new-category').css("display","block");
	$('#add-cat-label').css("display","none");
	$('#remove-cat-label').css("display","block");
}

function removeCategoryInput(){
	console.log("removing category input");
	$('#new-category').css("display","none");
	$('#remove-cat-label').css("display","none");
	$('#add-cat-label').css("display","block");
}




