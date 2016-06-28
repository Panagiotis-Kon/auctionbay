var total_pages = 5;

$(document).ready(function(){
	
	/*
	 * Check first the url. 
	 * If the url is /auctionbay/ then the user is a guest -> create the default modules
	 * If the url is /auctionbay/user/{username} is a registered user -> create user modules
	 * 
	 * */
	var user="user";
	var urlpath = window.location.href;
	console.log(window.location.pathname);
	document.getElementById('categoryList').style.display = 'block';
	total_pages = getNumOfAuctions()
	console.log("total_pages: " + total_pages)
	initListeners();
	
	
	if(urlpath.indexOf(user) == -1){
		// if not found then it return -1 
		// then it is a guest 
		// call default modules
		console.log("the url DOES NOT contains the user")
		defaultModulesInit();
		//var username = "alex";
		//userModulesInit(username);
		
	} else {
		// it is registered user, call userModulesInit()
		var patharray = window.location.pathname.split( '/' );
		var username = patharray[2];
		
		username = "alex";
		userModulesInit(username);
	}
	//getCategories();
	
});


function initListeners(){
	
	
	
}

function defaultModulesInit() {
	
	
	var template = getTemplateModule();
	
	
}

function getTemplateModule(){
	console.log("getting template module ");
	$.ajax({
		type : "GET",
		dataType:'json',
		url  : "/auctionbay/auctions/template-module",
		success : function(template) {
			return template;
		}	
	}); 	
}

function updateTemplateModule(template){
	$.ajax({
		type : "GET",
		dataType:'json',
		url  : "/auctionbay/auctions/*?" + $.param({startPos:startPos,pageSize:pageSize}),
		success : function(data) {
			if(data.length == 0){
				
			} else {
				for(var i=0;i<data.length;i++){}
			}
		}	
	});
}

function updateByCategory(){}


function getNumOfAuctions(){
	console.log("getting number of auctions ");
	$.ajax({
		type : "GET",
		dataType:'json',
		url  : "/auctionbay/auctions/number",
		success : function(data) {
			$.each(data, function(k, v) {
				
			});
		}	
	}); 	
	
}

function getCategories(){
	
	/* Make ajax call to receive the categories from the db */
	console.log("getting the categories");
	$.ajax({
		type : "GET",
		dataType:'json',
		url  : "/auctionbay/categories",
		success : function(data) {
			$.each(data, function(k, v) {
				//$("#side-categories").append("<li><a href=\"\">ITEM 3</a></li>")
			    console.log(data);
			});
		}	
	}); 	
}


