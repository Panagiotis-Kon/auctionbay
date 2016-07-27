

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
				//panel.find('.item-listing-title a').attr('href',window.location.href + '/item/'+auctions[i].id + "/"+auctions[i].name);
				//panel.find('.item-listing-title a').attr('href',window.location.href + '/item/'+auctions[i].id);
				//panel.find('.item-listing-title a').text(auctions[i].name);
					
				//panel.find("#elapseTime h4").text(auctions[i].expires+"remaining");
				//panel.find("#category-listing h4").text(auctions[i].expires+"remaining");
				//panel.find("#firstBid").text("$"+parseFloat(auctions[i].firstBid).toFixed(2));
				//panel.find("#numberOfbids").text(auctions[i].numberOfBids + "     " + "Bids");
				html = panel.html();
				$("#item-details").append(html);
			}
		}	
	}); 
	
	
}