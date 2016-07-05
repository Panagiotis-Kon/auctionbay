
var total_pages = 10;
$(document).ready(function(){
	
	
	total_pages = getNumOfAuctions();
	
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
	
	
	
	
	if(urlpath.indexOf(user) == -1){
		/* if not found then it return -1 
		 then it is a guest 
		 call default modules */
		console.log("the url DOES NOT contains the user")
		//defaultModulesInit();
		
		
	} else {
		// it is registered user, call userModulesInit()
		var patharray = window.location.pathname.split( '/' );
		var username = patharray[2];
		console.log("username: " + username);
		//username = "alex";
		userModulesInit(username);
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
		
		document.getElementById('login-panel').style.display = 'none';
		document.getElementById('sidebar').style.display = 'block';

		
		
	}



	function initPaging(total_pages){
		console.log("initPaging")
		console.log("total_pages: " + total_pages/10)
	    $('#auctions-paginator').bootpag({
	        total: (total_pages/10),
	        maxVisible: 5
	    }).on("page", function(event, num){
	        $(".content").html("Page " + num); // or some ajax content loading...
	        var size=10;
	        var start = (num-1)*size;
	        // ... after content load -> change total to 10
	        $(this).bootpag({total: total_pages/10, maxVisible: 5});
	        console.log("hiiiii");
	        getTemplateModule();
	        
	     
	    });
	    
	    /*$('#category-paginator').bootpag({
	        total: 10
	    }).on("page", function(event, num){
	        $(".content").html("Page " + num); // or some ajax content loading...
	        var size=10;
	        var start = (num-1)*size;
	       
	        // ... after content load -> change total to 10
	        $(this).bootpag({total: 10, maxVisible: 5});
	        
	        getAuctionsByCategory(start,size,category);
	     
	    });*/
		
		
	}

	function initListeners(){
		
		
		
	}

	/*function defaultModulesInit() {
		
		
		var template = getTemplateModule();
		
		
	}*/

	function getAuctions(start,size,template_module){
		console.log("getting auctions");
		$.ajax({
			type : "GET",
			dataType:'json',
			url  : "auctionbay/auctions/view-auctions/*?" + $.param({start:start,size:size}),
			success : function(auctions) {
				console.log("moving on formatting the page")
				if(auctions.length == 0){
					$('#no_auctions').css("display","block");
				}else {
					$('#no_auctions').css("display","none");
					
					for(var i=0;i<auctions.length;i++){
						var tree = $("<div>" + template_module + "</div>");
						tree.find("#seller small").text("Seller: "+ auctions[i].seller);
						tree.find('.item-listing-title a').attr('href',window.location.href + '/product/'+auctions[i].id + "/"+auctions[i].name);
						tree.find('.item-listing-title a').text(auctions[i].name);
						
						tree.find("#remainingTime small").text(auctions[i].remaining+"remaining");
						tree.find("#firstBid").text("$"+parseFloat(auctions[i].firstBid).toFixed(2));
						tree.find("#numberOfbids").text(auctions[i].numberOfBids + "     " + "Bids");
						html = tree.html();
						$("#available-auctions").append(html);
						
					}
				}
			}	
		}); 
		
	}

	function getTemplateModule(){
		console.log("getting template module ");
		$.ajax({
			type : "GET",
			dataType:'json',
			url  : "auctionbay/auctions/template-module",
			success : function(template_module) {
				getAuctions(start,size,template_module);
			}	
		}); 	
	}

	function customFormatPage(){
		
	}

	function defaultFormatPage(){}


	function updateTemplateModule(template){
		$.ajax({
			type : "GET",
			dataType:'json',
			url  : "/auctions/*?" + $.param({startPos:startPos,pageSize:pageSize}),
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
			url  : "auctionbay/auctions/numberOfAuctions",
			success : function(data){
				total_pages = data.auctionsNum;
				initPaging(total_pages);
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
				console.log(data);
				
			}	
		}); 	
	}

	
	
	
	
	
	
});




