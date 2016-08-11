$(document).ready(function (){
	
	
	checkForUser();
	getRecipients();
	initListeners();
	var username = getUser();
	console.log("user: "+username);
	$("#username-upright").append(username);
	
	
	
});

var recipientTags = [];
var availableTags = [
                     "ActionScript",
                     "AppleScript",
                     "Asp",
                     "BASIC",
                     "C",
                     "C++",
                     "Clojure",
                     "COBOL",
                     "ColdFusion",
                     "Erlang",
                     "Fortran",
                     "Groovy",
                     "Haskell",
                     "Java",
                     "JavaScript",
                     "Lisp",
                     "Perl",
                     "PHP",
                     "Python",
                     "Ruby",
                     "Scala",
                     "Scheme"
                   ];

function initListeners() {
	
	$("#recipient").click(function(event){
		event.preventDefault();
		console.log("recipients####")
		console.log(availableTags);
		$("#recipient").attr('autocomplete', 'on');
		$("#recipient").autocomplete({
	         source: availableTags
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

function getInboxMessagesModule(){
	$.ajax({
		type : "GET",
		dataType:'json',
		url  : window.location.href + "/inbox-module",
		success: getInboxMessages
		
	});
	
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


