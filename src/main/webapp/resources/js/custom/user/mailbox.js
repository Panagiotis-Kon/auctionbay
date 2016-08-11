$(document).ready(function (){
	
	getInboxMessagesModule();
	checkForUser();
	getRecipients();
	initListeners();
	var username = getUser();
	console.log("user: "+username);
	$("#username-upright").append(username);
	
	
	
});

var recipientTags = [];
var bodyMessageHolder = {};

function initListeners() {
	
	$("#recipient").click(function(event){
		event.preventDefault();
		//console.log("recipients####")
		//console.log(recipientTags);
		//$("#recipient").attr('autocomplete', 'on');
		$("#recipient").autocomplete({
	         source: recipientTags
	       });
		
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
	
	$("#send-message").click(function(event){
		event.preventDefault();
		var message = {};
		message["recipient"] = $("#recipient").val();
		message["subject"] = $("#subject").val();
		message["message_body"] = $("#message-body");
		sendMessage(message);
		
	});
	
}


function getRecipients() {
	$.ajax({
		type : "GET",
		dataType:'json',
		url  : window.location.href + "/recipients",
		success:function(recipients){
			//console.log(recipients);
			recipientTags = recipients;
		}
		
	});
	
}

function sendMessage(message){
	$.ajax({
		type : "POST",
		dataType:'json',
		data: {message:message},
		url  : window.location.href + "/message",
		success:function(result){
			alert("Your message has been sent");
			window.location.reload();
		}
		
	});
	
}


function getInboxMessagesModule(){
	$.ajax({
		type : "GET",
		dataType:'json',
		url  : window.location.href + "/inbox-module",
		success: getInboxMessages
		
	});
	
}


function getInboxMessages(inboxModule) {
	console.log("getting inbox messages")
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
				console.log(inbox);
				for(var i=0; i<inbox.length; i++){
					var body = null;
					bodyMessageHolder[inbox[i].messageID] = inbox[i].messageBody;
					
					if(inbox[i].isRead == 0  ) {
						body = $('<tr class="unread">' + inboxModule + '</tr>');
					} else {
						body = $('<tr>' + inboxModule + '</tr>');
					} 
					body.find("#messageID").val(inbox[i].messageID);
					body.find("#sender-inbox").text(inbox[i].sender);
					body.find("#subject-inbox").text(inbox[i].subject);
					body.find("#datetime-inbox").text(inbox[i].dateCreated);
					var html = body.html();
					$("#inbox-table").append(html);
				}
				
			}
			console.log("end of getting inbox messages")
			
		}
		
	});
}


