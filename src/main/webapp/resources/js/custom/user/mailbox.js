$(document).ready(function (){
	
	
	
	
	initListeners();
	
	
	
	
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
