

/* Index will be used as a controller for the app */
function checkForUser(){
	/*
	 * Check first the url. 
	 * If the url is /auctionbay/ then the user is a guest -> create the default modules
	 * If the url is /auctionbay/user/{username} is a registered user -> create user modules
	 * 
	 * */
	var user="user";
	var urlpath = window.location.href;
	console.log(window.location.pathname);
	if(urlpath.indexOf(user) == -1){
		// if not found then it returns -1 
		// then it is a guest 
		// call default modules
		console.log("the url DOES NOT contains the user: " + window.location.pathname)
		var patharray = window.location.pathname.split( '/' );
		console.log("patharray: " + patharray)
		console.log("patharray length: " + patharray.length)
		var page;
		if(patharray.length > 3) {
			page = patharray[3];
			console.log("page: " + page);
		} else {
			if(patharray.length == 3){
				if(patharray[2] != "" && typeof patharray[2] != 'undefined') {
					page = patharray[2];
					console.log("page1: " + page);
				} else {
					page = "index";
				}				
			} else {
				page = "index";
			}
			
		}
		modulesController(page,"");
		
	} else {
		// it is registered user, call userModulesInit()
		var patharray = window.location.pathname.split( '/' );
		console.log("patharray: " + patharray)
		console.log("patharray length: " + patharray.length)
		var page;
		if(patharray.length > 4) {
			page = patharray[4]
		} else {
			page = "index";
		}
		
		console.log(page)
		var username = patharray[3];
		console.log("username: " + username);
		modulesController(page,username)
		
	}
}


function getUser() {
	var patharray = window.location.pathname.split( '/' );
	var username = patharray[3];
	if(username == null || username=="")
		return "";
	return username;
}

function modulesController(page,username){
	if(username == "") {
		if(page == "index") {
			var modules = [];
			$("#main-header-title").text("Welcome to AuctionBay visitor");
			modules.push("#manage-auction-panel");
			modules.push("#mailbox-panel");
			modules.push("#sidebar-recommended");
			modules.push("#sidebar-categories-module");
			disableModules(modules);
		} 
		if(page == "auctions") {
			var modules = [];
			
			modules.push("button.bid");
			console.log("auctions without user")
			$("#recommended-module").css("display","none");
			changeClass("#advanced-search-module","col-xs-12 col-md-9","col-xs-12 col-md-9 pull-right");
			disableModules(modules);
		}
		if(page == "item"){
			$("#bid-section").css("display","none");
			$("#guest-warning").css("display","block");
			
		}
	} else {
		
		menuBarEdit(username);
		if(page == "index") {
			var enable_modules = [];
			var disable_modules = [];
			$("#main-header-title").html("Welcome back <span class=\"text text-danger\">" + username + "</span>"
					+ "</br><h4 class=\"text-center\">Here are some recomendations for you: </h4>");
			
			enable_modules.push("#sidebar-recommended");
		
			enable_modules.push("#manage-auction-panel");
			enable_modules.push("#user-recommedations");
			 
			enableModules(enable_modules);
			
			disable_modules.push("#guest-text");
			disable_modules.push("#login-panel");
			disableModules(disable_modules);
			
			
			//changeClass("#main-info-header","col-lg-12","col-md-10");
			//changeClass("#va-panel","col-lg-3 col-md-6","col-xs-3 col-xs-offset-2");
			
		} 
		if(page == "auctions") {
			var enable_modules = [];
		
			enable_modules.push("#recommended-module");
			enable_modules.push("#categories-sidebar");
			enableModules(enable_modules);
			
		}
		
	}
}

function menuBarEdit(username){
	console.log("Menu bar ")
	var enable_modules = [];
	var disable_modules = [];
	enable_modules.push("#auctions2");
	enable_modules.push("#rating-li");
	enable_modules.push("#user-right-col");
	enableModules(enable_modules);
	editText("#username",username);
	
	disable_modules.push("#auctions1");
	disable_modules.push("#default-right-col");
	disableModules(disable_modules);
}

function enableModules(modules){
	console.log(modules)
	for(var module in modules) {
		console.log(modules[module])
		var module_name = modules[module];
		$(module_name).css("display","block");
	}		
}

function disableModules(modules) {
	console.log(modules)
	for(var module in modules) {
		console.log(modules[module])
		var module_name = modules[module];
		$(module_name).css("display","none");
	}	
}

function changeClass(module,oldClass,newClass) {
	$(module).removeClass(oldClass);
	$(module).addClass(newClass);
}

function editText(module,text) {
	
	$(module).html("<span id=\"user-icon\" class=\"glyphicon glyphicon-user\"></span>" 
			+ "&nbsp" +text + " <span class=\"caret\"></span>");
}


function getUnreadMessages(){
	var username = getUser();
	var url = window.location.protocol+ "//" + window.location.hostname + ":" +window.location.port + "/auctionbay/user/";
	if(username != ""){
		console.log("getting unread messages");
		$.ajax({
			type : "GET",
			dataType:'json',
			data: {username:username},
			url  : url + username + "/mailbox/unread-number",
			success:function(unread){
				console.log("unread: " + unread);
				if(unread != "0") {
					// display on header 
					console.log("We have messages....")
					$("#header-unread-messages").text(unread);
					$("#header-unread-messages").css("display","block");
					$( "#user-icon" ).after( "<span class=\"badge badge-notify\">" + unread + "</span>" );
					
					// check if we are on mailbox and add the number to inbox
				} else {
					
				}
			}
			
		});
		
	}
}

function getRecommendations(){
	var username = getUser();
	var url = window.location.protocol+ "//" + window.location.hostname + ":" +window.location.port + "/auctionbay/user/";
	/*$('#myCarousel').carousel({
	    interval: 10000
	});*/
	if(username != ""){
		console.log("getting unread messages");
		$.ajax({
			type : "GET",
			dataType:'json',
			data: {username:username},
			url  : url + username + "/recommendations",
			success:function(data){
				console.log(data);
				// create carousel
				if(data.length == 0){
					$("#myCarousel").css("display","none");
					$("#no-rec-available").css("display","block");
				}else {
					for(var i=0 ; i< data.length ; i++) {
					    $('<div class="item"><img src="https://localhost:8443/auctionbay/resources/images/no-image.png"><div class="carousel-caption"></div>   </div>').appendTo('.carousel-inner');
					    $('<li data-target="#myCarousel" data-slide-to="'+i+'"></li>').appendTo('.carousel-indicators')

					  }
					  $('.item').first().addClass('active');
					  $('.carousel-indicators > li').first().addClass('active');
					  $('#myCarousel').carousel();
					//$("#myCarousel").append(carousel);
				}
				
				
				
			}
			
		});
	}
}




