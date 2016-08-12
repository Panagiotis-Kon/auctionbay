$(document).ready(function (){
	
	getInboxMessagesModule();
	getSentMessagesModule();
	checkForUser();
	getRecipients();
	initListeners();
	var username = getUser();
	console.log("user: "+username);
	$("#username-upright").append(username);
	
	
	
});

var recipientTags = [];
var bodyMessageInboxHolder = {};
var bodyMessageSentHolder = {};

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
	
	$("a.sent-ref").click(function(event){
		event.preventDefault();
		$("#inbox-item").removeClass("active");
		$("#sent-item").addClass("active");
		if($("#no-inbox-alert").is(":visible")){
			$("#no-inbox-alert").css("display","none");
		}
		$("#inbox-table").css("display","none");
		$("#sent-table").css("display","block");
	});
	
	$("a.inbox-ref").click(function(event){
		event.preventDefault();
		$("#sent-item").removeClass("active");
		$("#inbox-item").addClass("active");
		if($("#no-sent-alert").is(":visible")){
			$("#no-sent-alert").css("display","none");
		}
		$("#sent-table").css("display","none");
		$("#inbox-table").css("display","block");
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

function sendMessage(data){
	var username = getUser();
	var message = JSON.stringify(data);
	$.ajax({
		type : "POST",
		dataType:'json',
		data: {username:username,message:message},
		url  : window.location.href + "/message",
		success:function(result){
			alert(result);
			window.location.reload();
		}
		
	});
	
}


function getInboxMessagesModule(){
	console.log("module get...")

	$.get( window.location.href+"/inbox-module", function( inboxModule ) {
		getInboxMessages(inboxModule);
	});
	console.log("module get end...")
}

function getSentMessagesModule(){

	$.get( window.location.href+"/sent-module", function( sentModule ) {
		getSentMessages(sentModule);
	});
}



function getInboxMessages(data) {
	console.log("getting inbox messages")
	var inboxModule = data;
	var username = getUser();
	$.ajax({
		type : "GET",
		url  : window.location.href + "/inbox",
		dataType:'json',
		data: {username:username},
		success:function(inbox){
			
			if(inbox.length == 0) {
				$("#inbox-table").css("display","none");
				$("#no-inbox-alert").css("display","block");
				$("#no-inbox-text").text("You have no messages");
			}	
			else{
				console.log(inbox);
				for(var i=0; i<inbox.length; i++){
					var body = null;
					bodyMessageInboxHolder[inbox[i].messageID] = inbox[i].messageBody;
					
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


function getSentMessages(sentModule) {
	console.log("getting sent messages")
	
	var username = getUser();
	$.ajax({
		type : "GET",
		url  : window.location.href + "/sent",
		dataType:'json',
		data: {username:username},
		success:function(sent){
			
			if(sent.length == 0) {
				/*if(!$("#no-inbox-alert").is(":visible")){
					$("#sent-table").css("display","none");
					$("#no-sent-alert").css("display","block");
					$("#no-sent-text").text("You have no sent messages");
				}*/
				
				
			}	
			else{
				console.log(sent);
				for(var i=0; i<sent.length; i++){
					var body = null;
					bodyMessageSentHolder[sent[i].messageID] = sent[i].messageBody;
					
					if(sent[i].isRead == 0  ) {
						body = $('<tr class="unread">' + sentModule + '</tr>');
					} else {
						body = $('<tr>' + sentModule + '</tr>');
					} 
					body.find("#messageID").val(sent[i].messageID);
					body.find("#recipient-sent").text(sent[i].recipient);
					body.find("#subject-sent").text(sent[i].subject);
					body.find("#datetime-sent").text(sent[i].dateCreated);
					var html = body.html();
					$("#sent-table").append(html);
				}
				
			}
			console.log("end of getting inbox messages")
			
		}
		
	});
}

