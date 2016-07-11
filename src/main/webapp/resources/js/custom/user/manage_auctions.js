$(document).ready(function (){
	
	
	
	getCategoryList();
	countUserAuctions()
	getUserAuctions();
	initListeners();
	
	
	modifyMenu();
	
});

/* --------- Global Variables ----------- */

var map;
var geocoder;
var location_marker;
var user_auctions;

/* --------- Global Variables ----------- */


function initListeners() {
	
	$("#create-tab").on('shown.bs.tab', function() {

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
	
	$('#modify-tab').on('shown.bs.tab', function() {

	  	$('#user-auctions').css("display","block");
	});
	
	$('#user-auctions-grid tbody').on('click', 'button.edit-button', function () {
		//alert("you clicked edit")
		var tr = $(this).parents('tr');
		var row = user_auctions.row(tr);
		
		var id = row.data()[0];
        console.log("auction_id: " + id);
        
       
    } );
	
	
	
	
	
}


function modifyMenu(){
	
	var username = getUser();
	
	document.getElementById('auctions1').style.display = 'none';
	document.getElementById('auctions2').style.display = 'block';
	document.getElementById('user-right-col').style.display = 'block';
	
	console.log("the url contains the user " + username);
	var btn_text = document.getElementById('username');
	btn_text.innerHTML = username + " <span class=\"caret\"></span>";
	
	
	//document.getElementById('sidebar').style.display = 'block';

}

function getUser(){
	var patharray = window.location.pathname.split( '/' );
	var username = patharray[3];
	return username;
}

function countUserAuctions() {
	
	var username = getUser();
	$.ajax({
		type : "GET",
		dataType:'json',
		url  : window.location.href + "/count-user-auctions",
		data: {username:username},
		success : function(data) {
			$('#auctions-number').text("Your Auctions ( " + data.user_auctions_num + " ) ");
			console.log("Auctions num: " + data.user_auctions_num)
		}	
	});
}


function getUserAuctions() {
	
	var username = getUser();
	user_auctions = $('#user-auctions-grid').DataTable( {
		"processing": true,
	    "serverSide": true,
	    "ajax": {
	    	"url": window.location.href + "/get-user-auctions",
	    	"data":{"username":username} 
	    },
	  columns: [
	            { title: "AuctionID" },
	            { title: "ItemID" },
	            { title: "Title" },
	            { title: "Seller" },
	            { title: "BuyPrice" },
	            { title: "StartTime" },
	            { title: "EndTime" },
	            {
	                 title: "Options",
	                //"className":      'edit-control delete-control',
	                "orderable":      false,
	                "data":           null,
	                "defaultContent": '<button type=\"button\" id=\"edit-button\" class=\"btn btn-primary btn-sm edit-button\">Edit</button>'+
	                '&nbsp<button type=\"button\" id=\"delete-button\" class=\"btn btn-danger btn-sm delete-button\" data-toggle=\"modal\" data-target=\"#deleteModal\">Delete</button>'
	            }
	        ],
	        "columnDefs": [
	                       {
	                           "targets": [ 0 ],
	                           "visible": false,
	                           "searchable": false
	                       },
	                       {
	                           "targets": [ 1 ],
	                           "visible": false,
	                           "searchable": false
	                       },
	                       {"className": "dt-center", "targets": "_all"}
	          ]             
	
    	});
}


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




