
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
		console.log("window.location.href: " + window.location.href)
		console.log("baseURL: " + baseURL)
		var location = window.location.href;
		var lastChar = location.substr(location.length - 1);
		if(lastChar == "/"){
			window.location = window.location.href+"auctions";
		} else {
			window.location = window.location.href+"/auctions";
		}
    	
   });
	
	$("#contactRef").on("click",function(event){
    	
		console.log("contact btn");
		event.preventDefault();
		window.location = baseURL+"/contact";
    	
   });
	
	$("a.index-link").click(function(event){
		console.log("home btn");
		event.preventDefault();
		var result = checkUser();
		if(result == 0){
			document.location.href=baseURL;
		} else {
			window.location = window.location.href; 
		}
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
		console.log("window.location.href: " + window.location.href)
		console.log("baseURL: " + baseURL)
		var location = window.location.href;
		var lastChar = location.substr(location.length - 1);
		if(lastChar == "/"){
			window.location = window.location.href+"auctions";
		} else {
			window.location = window.location.href+"/auctions";
		}
			
		
	});
	
	$('a.manage-panel-link').click(function(event){
		event.preventDefault();
		window.location = window.location.href+"/manage-auctions";
		
	});
	
	
	$('a.log-out').click(function(event){
		event.preventDefault();
		window.location = baseURL;
	})
	
	function checkUser(){
		var user="user";
		var urlpath = window.location.href;

		if(urlpath.indexOf(user) == -1){
			/* if not found then it return -1 
			 then it is a guest 
			 call default modules */
			console.log("the url DOES NOT contains the user")
			return 0;
		} else {
			// it is registered user, call userModulesInit()
			return 1;
		}
	}
	
});