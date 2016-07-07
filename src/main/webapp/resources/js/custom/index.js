
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
		var username = patharray[3];
		console.log("username: " + username);
		//username = "alex";
		userModulesInit(username);
	}
	//getCategories();
	setlisteners();
});


function defaultModulesInit(){
	
	document.getElementById('default-right-col').style.display = 'block';
	$('#create-auction-panel').css("display","none");
	$('#manage-auction-panel').css("display","none");
	console.log("default-right-col showed");
	
}


function userModulesInit(username){
	
	document.getElementById('auctions1').style.display = 'none';
	document.getElementById('auctions2').style.display = 'block';
	
	console.log("the url contains the user " + username);
	document.getElementById('user-right-col').style.display = 'block';
	
	console.log("user-right-col showed");
	var btn_text = document.getElementById('username');
	btn_text.innerHTML = username + " <span class=\"caret\"></span>";
	
	document.getElementById('login-panel').style.display = 'none';
	document.getElementById('sidebar').style.display = 'block';
	$('#main-info-header').removeClass("col-lg-12");
	$('#main-info-header').addClass("col-md-10");
	
	/*$('#va-panel').removeClass("col-lg-3 col-md-6");
	$('#va-panel').addClass("col-xs-3 col-xs-offset-2");
	
	$('#create-auction-panel').removeClass("col-lg-3 col-md-6");
	$('#create-auction-panel').addClass("col-xs-3 col-xs-offset-2");*/
	
	$('#create-auction-panel').css("display","block");
	$('#manage-auction-panel').css("display","block");
	/* It might not be needed*/
	$('#b').removeClass("col-lg-3 col-md-6");
	$('#b').addClass("col-xs-3");
	/**/
	
	
	
	console.log("class added");
	
	
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

function setlisteners(){
	

	
}



