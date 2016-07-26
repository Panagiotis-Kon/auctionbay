

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
	
	var panel = $("<div id=\"item-lists-module\">" + details_module + "</div>");
	var html = panel.html();
	$("#item-details").append(html);
	
	
}