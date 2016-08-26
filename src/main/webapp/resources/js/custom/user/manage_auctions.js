$(document).ready(function (){
	
	/*test test*/
	
	getCategoryList();
	countUserAuctions()
	getUserAuctions();
	countryList('countries-list');
	initListeners();
	
	checkForUser();
	
	
});

/* --------- Global Variables ----------- */

var map;
var geocoder;
var location_marker;
var lat;
var lon;
var user_auctions;
var countries = ["Afghanistan","Albania","Algeria","Andorra",
                    "Angola","Anguilla","Antigua &amp; Barbuda","Argentina",
                    "Armenia","Aruba","Australia","Austria","Azerbaijan","Bahamas",
                    "Bahrain","Bangladesh","Barbados","Belarus","Belgium","Belize",
                    "Benin","Bermuda","Bhutan","Bolivia","Bosnia &amp; Herzegovina",
                    "Botswana","Brazil","British Virgin Islands","Brunei","Bulgaria",
                    "Burkina Faso","Burundi","Cambodia","Cameroon","Cape Verde","Cayman Islands",
                    "Chad","Chile","China","Colombia","Congo","Cook Islands",
                    "Costa Rica","Cote D Ivoire","Croatia","Cruise Ship","Cuba",
                    "Cyprus","Czech Republic","Denmark","Djibouti","Dominica",
                    "Dominican Republic","Ecuador","Egypt","El Salvador","Equatorial Guinea",
                    "Estonia","Ethiopia","Falkland Islands","Faroe Islands","Fiji",
                    "Finland","France","French Polynesia","French West Indies","Gabon",
                    "Gambia","Georgia","Germany","Ghana","Gibraltar","Greece","Greenland",
                    "Grenada","Guam","Guatemala","Guernsey","Guinea","Guinea Bissau","Guyana",
                    "Haiti","Honduras","Hong Kong","Hungary","Iceland","India","Indonesia",
                    "Iran","Iraq","Ireland","Isle of Man","Israel","Italy","Jamaica","Japan",
                    "Jersey","Jordan","Kazakhstan","Kenya","Kuwait","Kyrgyz Republic","Laos",
                    "Latvia","Lebanon","Lesotho","Liberia","Libya","Liechtenstein","Lithuania",
                    "Luxembourg","Macau","Macedonia","Madagascar","Malawi","Malaysia","Maldives",
                    "Mali","Malta","Mauritania","Mauritius","Mexico","Moldova","Monaco","Mongolia",
                    "Montenegro","Montserrat","Morocco","Mozambique","Namibia","Nepal","Netherlands",
                    "Netherlands Antilles","New Caledonia","New Zealand","Nicaragua","Niger","Nigeria",
                    "Norway","Oman","Pakistan","Palestine","Panama","Papua New Guinea","Paraguay","Peru",
                    "Philippines","Poland","Portugal","Puerto Rico","Qatar","Reunion","Romania","Russia",
                    "Rwanda","Saint Pierre &amp; Miquelon","Samoa","San Marino","Satellite","Saudi Arabia",
                    "Senegal","Serbia","Seychelles","Sierra Leone","Singapore","Slovakia","Slovenia","South Africa",
                    "South Korea","Spain","Sri Lanka","St Kitts &amp; Nevis","St Lucia","St Vincent","St. Lucia","Sudan",
                    "Suriname","Swaziland","Sweden","Switzerland","Syria","Taiwan","Tajikistan","Tanzania","Thailand",
                    "Timor L'Este","Togo","Tonga","Trinidad &amp; Tobago","Tunisia","Turkey","Turkmenistan",
                    "Turks &amp; Caicos","Uganda","Ukraine","United Arab Emirates","United Kingdom","Uruguay",
                    "Uzbekistan","Venezuela","Vietnam","Virgin Islands (US)","Yemen","Zambia","Zimbabwe"];

/* --------- Global Variables ----------- */

function countryList(divName) {
	console.log("countries!!!")
	var sel = document.getElementById(divName);
	var empty = document.createElement('option');
    empty.innerHTML = "";
    empty.value = "";
    sel.appendChild(empty);
    
	for(var i = 0; i < countries.length; i++) {
	    var opt = document.createElement('option');
	    opt.innerHTML = countries[i];
	    opt.value = countries[i];
	    sel.appendChild(opt);
	}
}



function initListeners() {
	
	
	var nowTemp = new Date();
    var now = new Date(nowTemp.getFullYear(), nowTemp.getMonth(), nowTemp.getDate(), 0, 0, 0, 0);
    $('#datetimepicker-create').datetimepicker({
        minDate: now,
        format:"YYYY-MM-DD HH:mm:ss"
    });
	
	
	$("#create-tab").on('shown.bs.tab', function() {

	  	console.log("resizing map...")
		google.maps.event.trigger(map, 'resize');
	});
	$('#countries-list').change(function(){
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
		
		var auction_id = row.data()[0];
		var item_id = row.data()[1];
		console.log("item_id: " + row.data()[1])
		$('#user-auctions').hide();
		
		editAuctionModule(auction_id,item_id);
        console.log("auction_id: " + auction_id);
        
       
    } );
	
	$('#user-auctions-grid tbody').on('click', 'button.delete-button', function () {
		
		var tr = $(this).parents('tr');
		var row = user_auctions.row(tr);
		
		//var id = row.data()[0];
		var name = row.data()[2];
		
		$("#auction-name-modal").html("<strong>" + name + " ?</strong>");
		$('#deleteModal').modal('show');
         
    });
	
	$('#delete-auction-btn').click(function(){
		var tr = $(this).parents('tr');
		var row = user_auctions.row(tr);
		
		var auction_id = row.data()[0];
		var item_id = row.data()[1];
		console.log("auction_id: " + auction_id);
		deleteAuction(auction_id, item_id);
	});
	
	$('#toAuctions-button').click(function(){
		$('#edit-area').hide();
		user_auctions.destroy();
		getUserAuctions();
		
	});
	
	$('#create-auction').click(function(){
		var input = {};
		
		var auction_name = $("#auction-name").val();
		if(auction_name == "") {
			//alert("Please insert a name for your auction")
			$('#warningModal').modal('show');
			$('#warning-text').html("Please insert a name for your auction");
		} else {
			var auction_desc = $("#auction-description").val();
			var auction_category = $("#category_list").val();
			
			input["auction_name"] = auction_name;
			input["auction_desc"] = auction_desc;
			
			if(auction_category == null) {
				var new_auction_cat = [];
				var new_temp = $("#new-category").val();
				new_auction_cat.push(new_temp);
				if(new_auction_cat == "") {
					$('#warningModal').modal('show');
					$('#warning-text').html("Please provide a category for your auction");
				}
				console.log(new_auction_cat)
				input["auction_category"] = new_auction_cat;
			} else {
				
				var new_auction_cat = $("#new-category").val();
				if(new_auction_cat != "") {
					auction_category.push(new_auction_cat);
				}
				input["auction_category"] = auction_category;
				
				var auction_country = $("#countries-list").val();
				var deadline = $("#datetime-field").val();
				var buyPrice = $("#buy-price").val();
				var first_bid = $("#first-bid").val();
				
				if(first_bid == "" || buyPrice == "" || deadline == "" || auction_country=="") {
					//alert("Please provide all the neccessary fields")
					$('#warningModal').modal('show');
					$('#warning-text').html("Please provide all the neccessary fields");
				} else {
					input["auction_country"] = auction_country;
					input["deadline"] = deadline;
					
					if(isNumeric(buyPrice) && isNumeric(first_bid)){
						input["buyPrice"] = buyPrice;
						
						input["first_bid"] = first_bid;
						input["lon"] = lon;
						input["lat"] = lat;
						console.log(input);
						createAuction(input);
					} else {
						$('#warningModal').modal('show');
						$('#warning-text').html("Buy Price or First Bid are not numeric");
					}
					
				}
			}	
		}
		
		});

}

function isNumeric(n) {
	  return !isNaN(parseFloat(n)) && isFinite(n);
}

function deleteAuction(auctionID,itemID){
	
	var username = getUser();
	$.ajax({
		type : "POST",
		dataType:'json',
		url  :window.location.href + "/delete-auction",
		data :{username:username,auctionID:auctionID,itemID:itemID},
		success : function(data) {
			alert("Deleted");
		}	
	});
}


function do_refresh() {
	console.log("reloading location");
	$('#createModal').modal('hide');
	location.reload();
}


function createAuction(input) {
	var auction_data = JSON.stringify(input);
	var username = getUser();
	console.log(auction_data);
	$.ajax({
		type : "POST",
		dataType:'json',
		url  :window.location.href + "/create-auction",
		data :{username:username,input:auction_data},
		success : function(data) {
			console.log("Successssssssss ********** ********")
			$('#createModal').modal('show');
		}	
	});
}

function editAuctionModule(auction_id,item_id) {
	$.get( window.location.href + "/edit-module", function( edit_module ) {
		var panel = $("<div id=\"edit-form\">" + edit_module + "</div>");
		var html = panel.html();
		$("#auction-edit").append(html);
		$('#edit-area').show();
		
		getAuctionDetails(auction_id,item_id);
	});
}

function getAuctionDetails(auction_id,item_id) {
	$.ajax({
		type : "GET",
		dataType:'json',
		url  :window.location.href + "/auction-details",
		data :{auction_id:auction_id,item_id:item_id},
		success : function(data) {
			console.log(data);
			
		}	
	}); 
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
	
	console.log("get user auctions....");
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
	                "defaultContent": '<button type=\"button\" id=\"view-button\" class=\"btn btn-primary btn-sm view-button\">View</button>'+
	                '<button type=\"button\" id=\"edit-button\" class=\"btn btn-warning btn-sm edit-button\">Edit</button>'+
	                '&nbsp<button type=\"button\" id=\"delete-button\" class=\"btn btn-danger btn-sm delete-button\">Delete</button>'
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
	$('#user-auctions').show();
	console.log("ENDING get user auctions....");
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
        draggable: true,
        map: map
    });
	location_marker.setMap(map);

	
	map.addListener('click', function(e) {
		
		if(typeof location_marker !== 'undefined')
			location_marker.setMap(null);
		
		location_marker = new google.maps.Marker({
	        position: e.latLng,
	        draggable: true,
	        map: map
	    });
		
		lat = e.latLng.lat().toFixed(3);
		lon = e.latLng.lng().toFixed(3);
		location_marker.setMap(map);
		console.log("lat: " + lat);
		console.log("lon: " + lon);
	});
	
	/*google.maps.event.addListener(location_marker,'dragend', function(a) {
		 alert("yaaay")
		 lat = a.latLng.lat().toFixed(4);
		 lon = a.latLng.lng().toFixed(4);
		 console.log("Drag --- lat: " + lat + " **** " + " lon: " + lon)
	 });*/
	 
	 google.maps.event.addListener(location_marker, 'click', function(a) {
		 lat = a.latLng.lat().toFixed(4);
		 lon = a.latLng.lng().toFixed(4);
		 console.log("Click --- lat: " + lat + " **** " + " lon: " + lon)
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

function getAuctionCategory(auction_id) {
	
	
}


function addCategoryInput(option){
	console.log("you clicked")
	/**
	 * option 0 : for the create tab
	 * option 1 : for the edit tab 
	 */
	if(option == 0) {
		//console.log("option: " + option)
		$('#new-category').css("display","block");
		$('#add-cat-label').css("display","none");
		$('#remove-cat-label').css("display","block");
	} else {
		
	}
	
}

function removeCategoryInput(option){
	console.log("removing category input");
	/**
	 * option 0 : for the create tab
	 * option 1 : for the edit tab 
	 */
	if(option == 0) {
		$('#new-category').css("display","none");
		$('#remove-cat-label').css("display","none");
		$('#add-cat-label').css("display","block");
	} else {
		
	}
	
}




