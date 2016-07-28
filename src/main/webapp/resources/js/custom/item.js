

$(document).ready(function(){
	
	console.log("base url: " + baseURL);
	console.log("window.location.href: " + window.location.href)
	
	var url = window.location.href;
	var itemID = url.substring(url.lastIndexOf("/")+1); 
	console.log("item ID: " + itemID);
	getItemModule(itemID)
	
});


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
				html = panel.html();
				$("#item-details").append(html);
				//checkforUser();
			}
		}	
	}); 
	
	
}