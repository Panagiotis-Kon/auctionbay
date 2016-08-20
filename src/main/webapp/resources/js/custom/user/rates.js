$(document).ready(function(){
	
	getExpiredAuctions();
	checkForUser();
	getUnreadMessages();
	initListeners();
});

var ratings = {};
function initListeners(){
	
	$("#submit-rating").click(function(event){
		
	});
	
}

function initRatingModule(id){
	 var elem = "#"+id;
	 var options = {
             max_value: 6,
             step_size: 0.5,
             selected_symbol_type: 'utf8_star',
             update_input_field_name: $(elem),
         }
	 $(".rate").rate(options);
	 $(".rate").on("change", function(ev, data){
         console.log(data.from, data.to);
     });
}

function getExpiredAuctions(){
	
	var patharray = window.location.pathname.split( '/' );
	var location = baseURL+"/"+patharray[2]+"/"+patharray[3]+"/auctions";
	
	var username = getUser();
	var dest = location + "/user-expired-auctions";
	console.log("dest= "+dest);
	$.ajax({
		type : "GET",
		url  : dest,
		dataType : 'json',
		data : {username:username},
		success:function(expired){
			
			
			var counterRow = 0;
			$.each(expired, function(i, item) {
				
				
				var data = [];
			    var auctionID = item.auctionID;
			    var seller = item.seller;
			    var bidder = item.bidder;
			    var item_title = item.item_title;
			    
			    var starRateModule = "<div class=\"rate\" style=\"margin:0 auto;font-size:24px;\"></div>" +
			    		"<div class=\"col-xs-3\" style=\"float:none; margin:0 auto;\">"+"<input class=\"form-control input-sm\"id='"+ auctionID + "' "+"type=\"text\"></div>";
			    
			    
			    
			    if(bidder == username){
			    	var row =  "<tr id=' " + auctionID+"-"+seller + '-seller'+  "'> <td>" + 
					seller            
					+ "</td> " +
					+ "<td>" +
					"Seller" 
					+ "</td>" +
					+ " <td> " +
					item_title
					+ "</td> "
					+ "<td>" +
					"<div id=\"wrapper\" style=\"text-align: center\">" +
					starRateModule 
					+ "</div></td> </tr>";

			    	$("#rating-table").find('tbody').append(row);
			    	data.push(seller);
			    	data.push("seller");
			    	ratings[auctionID+"-"+seller] = data; 
			    } else {
			    	var row =  "<tr id=' " + auctionID+"-"+bidder + '-bidder'+  "'> <td>" + 
					bidder               
					+ "</td> " +
					+ "<td>" +
					"Bidder" 
					+ "</td>" +
					+ " <td> " +
					item_title
					+ "</td> "
					+ "<td>" + 
					starRateModule 
					+ "</td> </tr>";

			    	$("#rating-table").find('tbody').append(row);
			    	data.push(bidder);
			    	data.push("bidder");
			    	ratings[auctionID+"-"+bidder] = data; 
			    }
			    
			    initRatingModule(counterRow);
			    counterRow++;
			    
			});
			
			
		}
	});
}

function submitRate(ratings){
	$.ajax({
		type : "POST",
		dataType:'json',
		url  : window.location.href + "/submit-rate",
		data : {ratings:ratings},
		success:function(result){
			console.log(result)
		},
		
	});
	
}