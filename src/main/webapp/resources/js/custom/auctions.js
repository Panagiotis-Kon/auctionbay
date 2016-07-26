/***** GLOBAL VARIABLES *****/
var total_pages = 10;
var category = "";


$(document).ready(function(){
	
	console.log("base url: " + baseURL);
	console.log("window.location.href: " + window.location.href)
	total_pages = getNumOfAuctions();
	getCategories();
	
	
});


function initListeners(){
	
	/*** Initializing Listeners after loading the contents of the page ***/
	console.log("Listeners Init");
	
	$("#categories-module").on('click', 'li', function(e) {
        e.preventDefault();
        
        category = $(this).find('a').attr("href");
        $('#auctions-paginator').css("display","none");
		$('#auctions-ByCategory-paginator').css("display","block");
		$('#category-indicator').css("display","block");
		$('#category-indicator h4').empty();
		$('#category-indicator h4').append("Results for Category: " + decodeURIComponent(category));
        console.log(decodeURIComponent(category))
        var cat_number = $(this).find('span').text()
        console.log("num of cat: " + cat_number)
        var end=10;
        var quotient = Math.floor(cat_number/10);
        var remainder = cat_number/10;
        var limit=quotient;
        if(remainder > 0){
        	limit=quotient+1;;
        }
        if(limit == 0){
        	limit=1;
        }
        console.log("limit: " + limit);
         $('#auctions-byCategory-paginator').bootpag({
        total: limit
         }).on("page", function(event, num){
        
        console.log("category paging event!!!!!")
        var start = (num-1)*end;
       
        // ... after content load -> change total to 10
        $(this).bootpag({total: limit});
        $('html, body').animate({scrollTop : 0},800);
        	getTemplateModule(start,end,decodeURIComponent(category));
     
        });
        getTemplateModule(0,10,decodeURIComponent(category));
    });
	
	$('a.all-categories').click(function(event){
		event.preventDefault();
		window.location = window.location.href;
	});
	
	/*$("#auction-title").click(function(event){
		event.preventDefault();
		alert("you clicked me")
		//console.log("you clicked the item and location: "+window.location.href);
	});*/
	
}


function getNumOfAuctions(){
	/*** Ajax call for retrieving the number of auction 
	 * in order to create the pagination ***/
	
	console.log("getting number of auctions ");
	$.ajax({
		type : "GET",
		dataType:'json',
		//url  : baseURL + "/auctions/numberOfAuctions",
		url  : window.location.href + "/numberOfAuctions",
		success : function(data){
			total_pages = data.auctionsNum;
			initPaging(total_pages);
		}
			
	}); 	
	
}

function initPaging(total_pages){
	
	/**
	 *  Making the pagination using bootpag library
	 */
	console.log("initPaging")
	console.log("total_pages: " + total_pages/10)
	
	var end=10;
    
    $('#auctions-paginator').bootpag({
        total: (total_pages/10),
        maxVisible: 5
    }).on("page", function(event, num){
        
        var start = (num-1)*end;
        // ... after content load -> change total to 10
        $(this).bootpag({total: total_pages/10, maxVisible: 5});
       
        $('html, body').animate({scrollTop : 0},800);
        
        getTemplateModule(start,end,category);
        
     
    });
	getTemplateModule(0,end,category);
	initListeners();
   
	
	
}

function getTemplateModule(start,end,category){
	
	/**
	 * Ajax call for retrieving the main module for the page.
	 * This is done because the auctions are presented in bootstrap panels.
	 * So we need to call the same div content several times
	 */
	
	console.log("getting template module ");
	
	$.get( window.location.href + "/template-module", function( template_module ) {
		if(category == ""){
			// getting auctions for all the categories
			getAuctions(start,end,template_module);
		} else {
			// getting auctions by category
			getAuctionsByCategory(start,end,template_module,category)
		}
		
	});
	 	
}

function getAuctionsByCategory(start,end,template_module,category){
	console.log("Auctions by Category with category: " + category);
	$.ajax({
		type : "GET",
		dataType:'json',
		//url  : baseURL + "/auctions/view-auctions-byCategory/",
		url  : window.location.href + "/view-auctions-byCategory",
		data :{start:start,size:end,category:category},
		success : function(auctions) {
			$("#available-auctions").empty();
			console.log("moving on formatting the page")
			if(auctions.length == 0){
				$('#no_auctions').css("display","block");
			}else {
				$('#no_auctions').css("display","none");
				// here for every auction we want to create different panels
				// hence every auction is stored in a panel
				for(var i=0;i<auctions.length;i++){
					var panel = $("<div id=\"item-lists-module\">" + template_module + "</div>");
					panel.find('.item-listing-seller label').text(auctions[i].seller);
					//panel.find('.item-listing-title a').attr('href',window.location.href + '/item/'+auctions[i].id + "/"+auctions[i].name);
					panel.find('.item-listing-title a').attr('href',window.location.href + '/item/'+auctions[i].id);
					//panel.find('.item-id').text(auctions[i].id);
					panel.find('.item-listing-title a').text(auctions[i].name);
					
					panel.find("#elapseTime h4").text(auctions[i].expires+"remaining");
					panel.find("#firstBid").text("$"+parseFloat(auctions[i].firstBid).toFixed(2));
					panel.find("#numberOfbids").text(auctions[i].numberOfBids + "     " + "Bids");
					html = panel.html();
					$("#available-auctions").append(html);
					
				}
				checkforUser();
			}
		}	
	}); 
}


function getAuctions(start,end,template_module){
	console.log("getting auctions");
	$.ajax({
		type : "GET",
		dataType:'json',
		//url  : baseURL + "/auctions/view-auctions/",
		url  :window.location.href + "/view-auctions",
		data :{start:start,size:end},
		success : function(auctions) {
			$("#available-auctions").empty();
			console.log("moving on formatting the page")
			if(auctions.length == 0){
				$('#no_auctions').css("display","block");
			}else {
				$('#no_auctions').css("display","none");
				
				for(var i=0;i<auctions.length;i++){
					var panel = $("<div id=\"item-lists-module\">" + template_module + "</div>");
					panel.find('.item-listing-seller label').text(auctions[i].seller);
					//panel.find('.item-listing-title a').attr('href',window.location.href + '/item/'+auctions[i].id + "/"+auctions[i].name);
					panel.find('.item-listing-title a').attr('href',window.location.href + '/item/'+auctions[i].id);
					panel.find('.item-listing-title a').text(auctions[i].name);
					
					panel.find("#elapseTime h4").text(auctions[i].expires+"remaining");
					//panel.find("#category-listing h4").text(auctions[i].expires+"remaining");
					panel.find("#firstBid").text("$"+parseFloat(auctions[i].firstBid).toFixed(2));
					panel.find("#numberOfbids").text(auctions[i].numberOfBids + "     " + "Bids");
					html = panel.html();
					$("#available-auctions").append(html);
					
				}
				checkforUser();
			}
		}	
	}); 
	console.log("Leaving Get Auctions...");
}


function getCategories(){
	
	/* Make ajax call to receive the categories from the db */
	console.log("getting the categories");
	$.ajax({
		type : "GET",
		dataType:'json',
		//url  : baseURL + "/auctions/categories",
		url  : window.location.href + "/categories",
		success : function(data) {
			
			if(data.length == 0){
				var html =  "<li><a>No available categories</a></li>";
				$("#categories-modules").append(html);
			} else {
				for(var i = 0; i < data.length; i++) {			
					var html =  "<li style=\"word-break:break-all;\">" +
									"<a href="+ encodeURIComponent(data[i].category) + " style=\"white-space:normal;\">" +
									data[i].category +
										"<span class='label label-warning pull-right'>"+
											data[i].numOfItems+ 
										" </span>" +
									"</a>" +
								"</li>";
					$("#categories-module").append(html);
				}
			}
			
		}	
	}); 
	//encodeURIComponent(data[i].category)
	console.log("getting the categories ended");
}

function checkforUser(){
	/*
	 * Check first the url. 
	 * If the url is /auctionbay/ then the user is a guest -> create the default modules
	 * If the url is /auctionbay/user/{username} is a registered user -> create user modules
	 * 
	 * */
	var user="user";
	var urlpath = window.location.href;
	console.log(window.location.pathname);
	document.getElementById('categories-module').style.display = 'block';
	
	
	if(urlpath.indexOf(user) == -1){
		/* if not found then it return -1 
		 then it is a guest 
		 call default modules */
		console.log("the url DOES NOT contains the user")
		defaultModulesInit();
		
		
	} else {
		// it is registered user, call userModulesInit()
		var patharray = window.location.pathname.split( '/' );
		var username = patharray[3];
		console.log("username: " + username);
		//username = "alex";
		userModulesInit(username);
	}
	
	
}


function defaultModulesInit(){
	document.getElementById('default-right-col').style.display = 'block';
	$('button.bid').css("display","none");
}

function userModulesInit(username){
	
	console.log("UserModulesInit....");
	document.getElementById('auctions1').style.display = 'none';
	document.getElementById('auctions2').style.display = 'block';
	
	console.log("the url contains the user " + username);
	document.getElementById('user-right-col').style.display = 'block';
	
	console.log("user-right-col showed");
	var btn_text = document.getElementById('username');
	btn_text.innerHTML = username + " <span class=\"caret\"></span>";
	
	
	document.getElementById('sidebar').style.display = 'block';

	
	
}

function updateByCategory(){}

function customFormatPage(){}

function defaultFormatPage(){}
