
var baseURL;
$(document).ready(function(){
	
	//First choice for local host, Second choice for remote Server
	baseURL = window.location.protocol + "//" + window.location.host + "/" + window.location.pathname.split('/')[1];
	//baseURL = window.location.protocol + "//" + window.location.host + "/" + window.location.pathname.split('/')[1] + "/auctionbay";
	 
	$("a.login-link").on("click",function(event){
    	
		console.log("login btn");
		event.preventDefault();
		window.location = baseURL+"/login-signup";
    	
   });
	
	$("a.view-auctions-ref").on("click",function(event){

		event.preventDefault();
		window.location = baseURL+"/auctions";
    	
   });
	
	$("#contactRef").on("click",function(event){
    	
		console.log("contact btn");
		event.preventDefault();
		window.location = baseURL+"/contact";
    	
   });
	
	$("a.index-link").click(function(event){
		console.log("home btn");
		event.preventDefault();
		document.location.href=baseURL;
		//window.location.replace("https://localhost:8443/auctionbay/");
		
	});
	
	$("a.login-panel-link").click(function(event){
		event.preventDefault();
		window.location = baseURL+"/login-signup";		
	});
	
	$('a.contact-panel-link').click(function(event){
		
		event.preventDefault();
		window.location = baseURL+"/contact";
		
	});
	
	$('a.view-auctions-panel-link').click(function(event){
		
		event.preventDefault();
		window.location = baseURL+"/auctions";
		
	});
	
	
});