$(document).ready(function (){
	
	
	
	
	initListeners();
	
	
	
	
});


function initListeners() {
	
	$("#compose-button").click(function(e){
		e.preventDefault();
		$("#main-content-area").css("display","none");
		$("#compose-area").css("display","block");
		
	});
	
}