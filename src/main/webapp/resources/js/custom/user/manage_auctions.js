$(document).ready(function (){
	
	/*test test*/
	
	getCategoryList();
	countUserAuctions("active");
	getUserAuctions();
	countryList('countries-list');
	initListeners();
	
	checkForUser();
	
	
});

/* --------- Global Variables ----------- */

var map;
var map_edit;
var center_edit;
var location_edit_marker;
var lat_edit;
var lon_edit;
var geocoder;
var location_marker;
var lat;
var lon;
var allcategories;
var user_auctions,active_auctions;
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
	
	/*$('#modify-tab').on('shown.bs.tab', function() {

	  	$('#user-auctions').css("display","block");
	});*/
	
	
	
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
		$('#user-auctions').show();
		//user_auctions.destroy();
		//getUserAuctions();
		
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
		var nowTemp = new Date();
	    var now = new Date(nowTemp.getFullYear(), nowTemp.getMonth(), nowTemp.getDate(), 0, 0, 0, 0);
	    $('#datetimepicker-edit').datetimepicker({
	        minDate: now,
	        format:"YYYY-MM-DD HH:mm:ss"
	    });
	    $("#googleMap-edit").css("display","block");
	    initEditMap();
	    $("#category-list-edit").multiselect({
			selectedList: 4
		});
	    if(allcategories.length == 0){
			$('#category-list-edit').css("display","none");
			$('#no-categories-edit').css("display","block");
			
		} else {
			for(var i = 0; i < allcategories.length; i++) {			
				var category = allcategories[i].category;
				var option = $('<option value="'+allcategories[i].category+'">');
				option.text(category);
				$("#category-list-edit").append(option);
				
				
			}
			$("#category-list-edit").multiselect("refresh");
		}
	    
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
			setAuctionPosition(data.lat,data.lon);
			
			var sel = document.getElementById('edit-countries-list');
			var existing = document.createElement('option');
		    existing.innerHTML = data.location;
		    existing.value = data.location;
		    sel.appendChild(existing);
		    
			for(var i = 0; i < countries.length; i++) {
			    var opt = document.createElement('option');
			    opt.innerHTML = countries[i];
				opt.value = countries[i];
				sel.appendChild(opt);
			    
			    
			}
			$("#auctionID-edit").val(data.auctionID);
			$("#itemID-edit").val(data.itemID);
			$("#current-category").text(data.category);
			$("#auction-name-edit").val(data.name);
			$("#auction-description-edit").text(data.description);
			$("#edit-buy-price").val(data.buyprice);
			$("#edit-first-bid").val(data.firstbid);
			$("#datetime-field-edit").val(data.endTime);
			
			$("#change-cat-btn").click(function(event){
				event.preventDefault();
				$("#current-cat").css("display","none");
				$("#change-cat").css("display","block");
			});
			
			setEditListeners();
		}	
	}); 
	
	
}

function setEditListeners(){
	
	$("#edit-auction-btn").click(function(event){
		event.preventDefault();
		// start checking fields
		
		var input = {};
		
		var auction_name = $("#auction-name-edit").val();
		if(auction_name == "") {
			//alert("Please insert a name for your auction")
			$('#warningModal').modal('show');
			$('#warning-text').html("Please insert a name for your auction");
		} else {
			var auction_desc = $("#auction-description-edit").val();
			var auction_category = $("#category-list-edit").val();
			
			input["auction_name"] = auction_name;
			input["auction_desc"] = auction_desc;
			input["auctionID"] = $("#auctionID-edit").val();
			input["itemID"] = $("#itemID-edit").val();
			
			if(auction_category == null) {
				var new_auction_cat = [];
				var new_temp = $("#new-edit-category").val();
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
				
				var auction_country = $("#edit-countries-list").val();
				var deadline = $("#datetime-field-edit").val();
				var buyPrice = $("#edit-buy-price").val();
				var first_bid = $("#edit-first-bid").val();
				
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
						input["lon"] = lon_edit;
						input["lat"] = lat_edit;
						console.log(input);
						$('#warningEditModal').modal('show');
						$('#warningEdit-text').html("Are you sure that you want to apply these changes?");
						$("#yes-edit").click(function(event){
							updateAuction(input);
						})
						
					} else {
						$('#warningModal').modal('show');
						$('#warning-text').html("Buy Price or First Bid are not numeric");
					}
					
				}
			}	
		}
		
		
	});
}

function updateAuction(input){
	var auction_data = JSON.stringify(input);
	var username = getUser();
	console.log(auction_data);
	/*$.ajax({
		type : "POST",
		dataType:'json',
		url  :window.location.href + "/update-auction",
		data :{username:username,input:auction_data},
		success : function(data) {
			console.log("Successssssssss ********** ********")
			alert("the auction have changed")
		}	
	});*/
}


function countUserAuctions(type) {
	
	var username = getUser();
	$.ajax({
		type : "GET",
		dataType:'json',
		url  : window.location.href + "/count-user-auctions",
		data: {username:username,type:type},
		success : function(data) {
			$('#auctions-number').text("Your Auctions ( " + data.user_auctions_num + " ) ");
			console.log("Auctions num: " + data.user_auctions_num)
		}	
	});
}


function auctionDetails(d){
	
	 var bidHistory = d.bidHistory;
	 var content = '<button class="btn btn-info" data-toggle="collapse" data-target="#demo">Bids</button>'+
	 '<div id="demo" class="collapse">'+
	 '<table><thead><tr><th>Bidder</th><th>Bid</th></tr></thead>';
	 /*for(var i=0; i<bidHistory.length; i++){
		 content += '<tr><td>' + bidHistory[0]'</td><td>'+ bidHistory[1] + '</td></tr>';
	 }*/
	 
	 content += '<tr><td>' +'Magas' +'</td><td>'+ '18.88' + '</td></tr>';
	 content += '<tr><td>' +'Magas2' +'</td><td>'+ '13.18' + '</td></tr>';
	 content +='</table></div>';
	
	
	return '<div class="slider">'+
	'<table cellpadding="5" cellspacing="0" border="0" style="padding-left:50px;">'+
    '<tr>'+
        '<td>StartTime:</td>'+
        '<td>'+d.StartTime+'</td>'+
    '</tr>'+
    '<tr>'+
        '<td># Bids:</td>'+
        '<td>'+d.numOfBids+'</td>'+
    '</tr>'+
    '<tr>'+
        '<td>First Bid:</td>'+
        '<td>'+ d.FirstBid + '</td>'+
    '</tr>'+
    '<tr>'+
    '<td>Highest Bid:</td>'+
    '<td>'+ d.HighestBid + '</td>'+
    '</tr>'+
    '<tr>'+
    '<td>'+ content +'</td>'+
    
    '</tr>'+
'</table>'+
'</div>';
}

function getUserAuctions() {
	
	console.log("get user auctions....");
	var username = getUser();
	var type = "active";
	active_auctions = $('#active-user-auctions-grid').DataTable( {
		"processing": true,
	    "serverSide": true,
	    "ajax": {
	    	"url": window.location.href + "/view-user-auctions",
	    	"data":{"username":username,"type":type} 
	    },
	  columns: [
	            {
	                "className":      'auction-details-control',
	                "orderable":      false,
	                "data":           null,
	                "defaultContent": ''
	            },
	            { "data": "AuctionID" },
	            { "data": "ItemID" },
	            { "data": "Title" },
	            { "data": "Seller" },
	            { "data": "BuyPrice" },
	            { "data": "EndTime" },
	         
	        ],
	        "columnDefs": [
	                       {
	                           "targets": [ 1 ],
	                           "visible": false,
	                           "searchable": false
	                       },
	                       {
	                           "targets": [ 2 ],
	                           "visible": false,
	                           "searchable": false
	                       },
	                       {"className": "dt-center", "targets": "_all"}
	          ]             
	
    	});
	$('#active-user-auctions').show();
	$('#active-user-auctions-grid tbody').on('click', 'td.auction-details-control', function () {
        var tr = $(this).closest('tr');
        var row = active_auctions.row( tr );
 
        if ( row.child.isShown() ) {
            // This row is already open - close it
        	 $('div.slider', row.child()).slideUp( function () {
                 row.child.hide();
                 tr.removeClass('shown');
             } );
            
        }
        else {
            // Open this row
        	console.log(row.data());
        	row.child( auctionDetails(row.data()), 'no-padding' ).show();
        	
            tr.addClass('shown');
            $('div.slider', row.child()).slideDown();
        }
    } );
	
	
	
	
	user_auctions = $('#user-auctions-grid').DataTable( {
		"processing": true,
	    "serverSide": true,
	    "ajax": {
	    	"url": window.location.href + "/get-user-auctions",
	    	"data":{"username":username,type:type} 
	    },
	  columns: [
	            { "data": "AuctionID" },
	            { "data": "ItemID" },
	            { "data": "Title" },
	            { "data": "Seller" },
	            { "data": "BuyPrice" },
	            { "data": "StartTime" },
	            { "data": "EndTime" },
	            {
	                 title: "Options",
	                //"className":      'edit-control delete-control',
	                "orderable":      false,
	                "data":           null,
	                "defaultContent": '<button type=\"button\" id=\"edit-button\" class=\"btn btn-warning btn-sm edit-button\"><span class="glyphicon glyphicon-pencil"></span></button>'+
	                '&nbsp<button type=\"button\" id=\"delete-button\" class=\"btn btn-danger btn-sm delete-button\"><span class="glyphicon glyphicon-trash"></span></button>'
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
	


	$('#user-auctions-grid tbody').on('click', 'button.edit-button', function () {
		//alert("you clicked edit")
		var tr = $(this).parents('tr');
		var row = user_auctions.row(tr);
		var data = row.data();
		var auction_id = data.AuctionID;
		//alert("auction_id: " + auction_id);
		var item_id = data.ItemID;
		
		
		$('#user-auctions').fadeOut('1000');
		
		editAuctionModule(auction_id,item_id);
       
    } );
	
	$('#user-auctions-grid tbody').on('click', 'button.delete-button', function () {
		
		var tr = $(this).parents('tr');
		var row = user_auctions.row(tr);
		
		//var id = row.data()[0];
		var name = row.data()[2];
		
		$("#auction-name-modal").html("<strong>" + name + " ?</strong>");
		$('#deleteModal').modal('show');
         
    });
	
	console.log("ENDING get user auctions....");
}

function setAuctionPosition(auction_lat,auction_lon){
	
	var position = new google.maps.LatLng(auction_lat,auction_lon);
	location_edit_marker = new google.maps.Marker({
        position: position,
        draggable: true,
        map: map_edit
    });
	
	location_edit_marker.setMap(map_edit);
	var latLng = location_edit_marker.getPosition(); // returns LatLng object
	map_edit.setCenter(latLng);
	
	map_edit.addListener('click', function(e) {
		
		if(typeof location_edit_marker !== 'undefined')
			location_edit_marker.setMap(null);
		
		location_edit_marker = new google.maps.Marker({
	        position: e.latLng,
	        draggable: true,
	        map: map_edit
	    });
		
		lat_edit = e.latLng.lat().toFixed(3);
		lon_edit = e.latLng.lng().toFixed(3);
		location_edit_marker.setMap(map_edit);
		console.log("lat_edit: " + lat_edit);
		console.log("lon_edit: " + lon_edit);
	});
}

function initEditMap(){
	
	
	
	center_edit = new google.maps.LatLng(51.508742,-0.120850)
	map_edit = new google.maps.Map(document.getElementById('googleMap-edit'), {
	    center: center_edit,
	    zoom: 8,
	    mapTypeId: google.maps.MapTypeId.ROADMAP
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
	var user = getUser();
	var url = baseURL + "/user/"+user+"/auctions/categories";
	var type = "all";
	$.ajax({
		type : "GET",
		dataType:'json',
		url  : url,
		data : {type:type},
		//url  : window.location.href + "/categories",
		success : function(data) {
			allcategories = data;
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




function addCategoryInput(option){
	console.log("you clicked")
	/**
	 * option 0 : for the create tab
	 * option 1 : for the edit tab 
	 */
	if(option == 0) {
		//console.log("option: " + option)
		$('#new-category').css("display","block");
		$('#add-cat-btn').css("display","none");
		$('#add-cat-label').css("display","none");
		$('#remove-cat-label').css("display","block");
	} else {
		$('#add-cat-edit-label').css("display","none");
		$('#remove-cat-edit-label').css("display","block");
		$('#add-edit-cat-btn').css("display","none");
		$('#new-cat-edit-area').css("display","block");
		
		
		
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
		$('#add-cat-btn').css("display","block");
		$('#add-cat-label').css("display","block");
	} else {
		$('#new-cat-edit-area').css("display","none");
		$('#add-cat-edit-label').css("display","block");
		
		$('#add-edit-cat-btn').css("display","block");
		$('#remove-cat-edit-label').css("display","none");
		
		
	}
	
}




