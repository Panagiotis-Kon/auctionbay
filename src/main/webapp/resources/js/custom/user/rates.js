$(document).ready(function(){
	getExpiredAuctions();
	checkForUser();
	getUnreadMessages();
});

function getExpiredAuctions(){
	
	var patharray = window.location.pathname.split( '/' );
	var location = baseURL+"/"+patharray[2]+"/"+patharray[3]+"/auctions";
	console.log("url= "+location);
	/*$.ajax({
		type : "GET",
		url  : window.location.href,
		success: function(expired) {
			
		}
		
		
		
	});*/
}

function submitRate(){}