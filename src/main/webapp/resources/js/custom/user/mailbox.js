$(document).ready(function (){
	
	getUnreadMessages();
	getRecipients();
	getInboxMessagesModule();
	getSentMessagesModule();
	checkForUser();
	
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
			console.log("here1")
			$("#no-inbox-alert").css("display","none");
		}
		//$("#inbox-table").css("display","none");
		//$("#sent-table").css("display","block");
		$("#inbox-table").hide();
		$("#sent-table").show();
		$("#active-area").text("Sent");
		
	});
	
	$("a.inbox-ref").click(function(event){
		event.preventDefault();
		
		if($("#no-sent-alert").is(":visible")){
			console.log("here2")
			$("#no-sent-alert").css("display","none");
		}
		
		//$("#sent-table").css("display","none");
		//$("#inbox-table").css("display","block");
		$("#sent-table").hide();
		$("#inbox-table").show();
		$("#sent-item").removeClass("active");
		$("#inbox-item").addClass("active");
		$("#active-area").text("Inbox");
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
		message["message_body"] = $("#message-body").val();
		sendMessage(message);
		
	});
	
	
	
	
  
	
}


function inboxListeners(){
	var rows = document.getElementById('inbox-table').getElementsByTagName('tbody')[0].getElementsByTagName('tr');
    for (i = 0; i < rows.length; i++) {
        rows[i].onclick = function(e) {
        	if(e.target.type == "checkbox"){
        		console.log("checkbox clicked")
        	} else {
        		var message_id = $(this).find("#messageID").val();
            	var message_body = bodyMessageInboxHolder[message_id].messageBody;
            	var from = bodyMessageInboxHolder[message_id].sender;
            	var subject = bodyMessageInboxHolder[message_id].subject;
            	
            	if ( $( this ).hasClass( "unread" ) ) {
            		$(this).removeClass("unread");
            		markAsRead(message_id);
            	}
            	
            	$("#sender-inbox-view").val(from);
            	$("#subject-inbox-view").val(subject);
            	$("#view-inbox-textarea").text(message_body);
            	$("#view-inbox-area").css("display","block");
        	}
        	
        	
        }
    }
    $("#hide-inbox-message").click(function(event){
		
		$("#view-sent-area").css("display","none");
	});
    
    $("#reply-message").click(function(event){
    	
    });
}


function sentListeners() {
	var rows = document.getElementById('sent-table').getElementsByTagName('tbody')[0].getElementsByTagName('tr');
    for (i = 0; i < rows.length; i++) {
        rows[i].onclick = function(e) {
        	
        	if(e.target.type == "checkbox"){
        		console.log("checkbox clicked")
        	} else {
        		var message_id = $(this).find("#messageID").val();
            	var message_body = bodyMessageSentHolder[message_id].messageBody;
            	var to = bodyMessageSentHolder[message_id].recipient;
            	var subject = bodyMessageSentHolder[message_id].subject;
            	$("#view-sent-textarea").text(message_body);
            	$("#recipient-sent-view").val(to);
            	$("#subject-sent-view").val(subject);
            	$("#view-sent-area").css("display","block");
        	}
        	
        	
        	//alert("sent message id: " + id)
        }
    }
    $("#hide-sent-message").click(function(event){
		
		$("#view-sent-area").css("display","none");
	});
}

function markAsRead(message_id){
	$.ajax({
		type : "GET",
		dataType:'json',
		data: {messageID:message_id},
		url  : window.location.href + "/markAsRead",
		success:function(data){
			console.log(data);
			console.log("message with id: " + message_id + " marked as read");
		}
		
	});
}

function getUnreadMessages(){
	var username = getUser();
	$.ajax({
		type : "GET",
		dataType:'json',
		data: {username:username},
		url  : window.location.href + "/unread-number",
		success:function(unread){
			console.log("unread: " + unread);
			if(unread != "0") {
				$("#inbox-counter").css("display","block");
				$("#inbox-counter").text(unread);
			}
		}
		
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
			$("#info-text").append("Your message to" + data["recipient"] + "was sent");
			$("#info-modal").modal('show');
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
					//var body = null;
					bodyMessageInboxHolder[inbox[i].messageID] = inbox[i];
					
					
					var body = $("<tr>" + sentModule + "</tr>");
					body.find("#messageID").val(inbox[i].messageID);
					body.find("#sender-inbox").text(inbox[i].sender);
					body.find("#subject-inbox").text(inbox[i].subject);
					body.find("#datetime-inbox").text(inbox[i].dateCreated);
					var html = body.html();
					if(inbox[i].isRead == 0  ) {
						$("#inbox-table").find('tbody').append('<tr class="unread">' + html + '</tr>');
					} else {
						$("#inbox-table").find('tbody').append('<tr>' + html + '</tr>');
					}
					
				}
				inboxListeners();
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
					
					bodyMessageSentHolder[sent[i].messageID] = sent[i];
					
					var body = $("<tr>" + sentModule + "</tr>");
					
					body.find("#messageID").val(sent[i].messageID);
					body.find("#recipient-sent").text(sent[i].recipient);
					body.find("#subject-sent").text(sent[i].subject);
					body.find("#datetime-sent").text(sent[i].dateCreated);
					var html = body.html();
					//console.log(html);
					$("#sent-table").find('tbody').append("<tr>"+html+"</tr>");
				}
				sentListeners();
			}
			console.log("end of getting sent messages")
			
		}
		
	});
}


function deleteMessage(messageID){
	var username = getUser();
	$.ajax({
		type : "DELETE",
		url  : window.location.href + "/delete-message",
		data : {username:username,messageID:messageID},
		dataType:'json',
		success:function(result){
			console.log(result);
			window.location.reload();
			
		}
		
	});
}

