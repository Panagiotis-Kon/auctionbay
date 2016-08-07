

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

function setListeners(){
	console.log("Setting listeners")
	$('#bidNow').click( function(event){
		console.log("clicked")
		
		var bid_amount = $("#bid-amount").val();
		var first_bid = $("#firstbid").text();
		if(bid_amount == "") {
			alert("Please enter an amount")
		} else {
			console.log("first_bid: " + first_bid + " and bid_amount: " + bid_amount);
			if(parseFloat(bid_amount) >= parseFloat(first_bid)){
				console.log(">=")
				$("#confirm-bid-btn").css("display","block");
				$("#warningBid-text").html("Are you sure that you want to bid " + bid_amount + " $ ?");
				$("#warningBidModal").modal('show');
			} else {
				$("#warningBid-text").html("Sorry but your bid must be greater or equal than the first bid");
				$("#warningBidModal").modal('show');
			}
		}
		
	});
	
	$('#confirm-bid-btn').click(function(){
		
		console.log("confirm");
		var bid_amount = $("#bid-amount").val();
		$("#warningBidModal").modal('hide');
		
	});
}


function submitOffer(bid_amount){
	
	var itemID = url.substring(url.lastIndexOf("/")+1);
	var username = getUser();
	console.log("Sending: " + itemID + " bid: " + bid_amount);
	$.ajax({
		type : "GET",
		dataType:'json',
		url  :window.location.href + "/submit-bid",
		data :{username:username,itemID:itemID,bid_amount:bid_amount},
		success : function(data) {
			alert(data)
		}
			
		
	});
}

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
				panel.find('#buyprice').text(data.buyprice);
				panel.find('#seller').text(data.seller);
				panel.find('#firstbid').text(data.firstbid);
				panel.find('#expire').text(data.expires);
				panel.find('#highest-bid').html(data.highest_bid + " $");
				panel.find('#bids-num').text(data.numOfBids);
				
				latitude = data.lat;
				longtitude = data.lon;
				
				html = panel.html();
				$("#item-details").append(html);
				setListeners();
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