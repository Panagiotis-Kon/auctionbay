$(document).ready(function (){
	
	
	
	
	initListeners();
	var username = getUser();
	console.log("user: "+username);
	$("#username-upright").append(username);
	
	
	
});


function initListeners() {
	
	$("#recipient").click(function(event){
		event.preventDefault();
		
	});
	
	$("#compose-button").click(function(e){
		e.preventDefault();
		$("#main-content-area").css("display","none");
		$("#compose-area").css("display","block");
		$("#compose-button").css("display","none");
		$("#back-to-read").css("display","block");
		
	});
	
	$("#back-to-read").click(function(e){
		e.preventDefault();
		$("#compose-area").css("display","none");
		$("#main-content-area").css("display","block");
		$("#back-to-read").css("display","none");
		$("#compose-button").css("display","block");
		
		
	});
	
}


function getRecipients() {
	
	
}

function getInboxMessagesModule(){
	
	
}


function getInboxMessages(inboxModule) {
	var username = getUser();
	$.ajax({
		type : "GET",
		url  : window.location.href + "/inbox",
		dataType:'json',
		data: {username:username},
		success:function(inbox){
			if(inbox.length == 0) {
				$("no-inbox").css("display","block");
				$("#no-inbox").text("You have no messages");
			}	
			else{
				
			}
			
			
		}
		
	});
}


