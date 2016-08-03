


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
		// if not found then it return -1 
		// then it is a guest 
		// call default modules
		console.log("the url DOES NOT contains the user: " + window.location.pathname)
		var patharray = window.location.pathname.split( '/' );
		console.log("patharray: " + patharray)
		console.log("patharray length: " + patharray.length)
		var page;
		if(patharray.length > 3) {
			page = patharray[3];
		} else {
			page = "index";
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
	return username;
}

function modulesController(page,username){
	if(username == "") {
		if(page == "index") {
			var modules = [];
			modules.push("#manage-auction-panel");
			disableModules(modules);
		} 
		if(page == "auctions") {
			var modules = [];
			
			modules.push("button.bid");
			disableModules(modules);
		}
	} else {
		
		menuBarEdit(username);
		if(page == "index") {
			var enable_modules = [];
			var disable_modules = [];
			enable_modules.push("#sidebar");
			enable_modules.push("#manage-auction-panel");
			enableModules(enable_modules);
			
			
			disable_modules.push("#login-panel");
			disableModules(disable_modules);
			
			
			changeClass("#main-info-header","col-lg-12","col-md-10");
			changeClass("#va-panel","col-lg-3 col-md-6","col-xs-3 col-xs-offset-2");
			
		} 
		if(page == "auctions") {
			var enable_modules = [];
		
			enable_modules.push("#sidebar");
			enableModules(enable_modules);
			
		}
	}
}

function menuBarEdit(username){
	console.log("Menu bar ")
	var enable_modules = [];
	var disable_modules = [];
	enable_modules.push("#auctions2");
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
	
	$(module).html(text + " <span class=\"caret\"></span>");
}





