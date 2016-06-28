$(document).ready(function(){
	
	createForm();
	setListeners();
	setValidators();
	
});

function createForm(){
	
	$('#login-form-link').click(function(e) {
		$("#login-form").delay(100).fadeIn(100);
 		$("#register-form").fadeOut(100);
		$('#register-form-link').removeClass('active');
		$(this).addClass('active');
		e.preventDefault();
	});
	$('#register-form-link').click(function(e) {
		$("#register-form").delay(100).fadeIn(100);
 		$("#login-form").fadeOut(100);
		$('#login-form-link').removeClass('active');
		$(this).addClass('active');
		e.preventDefault();
	});
}

function setListeners(){
	
	$("#login-submit").on("click",function(event){
    	event.preventDefault();
    	
    	var credentials = {};
    	credentials.username = $("#login-username").val();
    	credentials.password = $("#login-password").val();
    	
    	Login(credentials);
    });
	
	/*$("#register-submit").on("click",function(event){
		event.preventDefault();
		alert("Under Contruction");	
	});*/
	
}


function Login(credentials){
	
	$.ajax({
		type : "post",
		url  : "login",
		data : credentials,
		success: function(output, status, xhr) { 
            // console.log(xhr.status);
			//window.location.replace(xhr.getResponseHeader("Content-Location"));
			window.location = baseURL + "/" +  xhr.getResponseHeader("Content-Location");
        },
        error: function(XMLHttpRequest, textStatus, errorThrown) {
        	alert('XHR ERROR ' + XMLHttpRequest.status);
        },
		dataType:'text'
	});
	
}

function Register(params){
	console.log("Sending data for registration")
	$.ajax({
		type : "POST",
		url  : "signup",
		data : {json:params},
		success: function(output, status, xhr) { 
            console.log(xhr.status);
            window.location.replace(xhr.getResponseHeader("Content-Location"));
          },
          error: function(XMLHttpRequest, textStatus, errorThrown) {
            alert("The Username Already exists");
            
          },
		dataType:'text'
	});
	
}

function setValidators(){
	
	$.validator.setDefaults( {
		submitHandler: function () {
			
			var params = {};
			
			var password = $("#passwordSignup").val();
			var confirm_pass = $("#confirmSignup").val();
			
			if(confirm_pass != password){
				alert("Passwords do not match");
			} else {
				params = JSON.stringify({
		    		"username": $("#usernameSignup").val(), 
		    		"password": $("#passwordSignup").val(),
		    		"firstname": $("#firstnameSignup").val(),
		    		"lastname": $("#lastnameSignup").val(),
		    		"email": $("#emailSignup").val(),
		    		"trn": $("#trnSignup").val(),
		    		"phonenumber": $("#phonenumberSignup").val(),
		    		"city": $("#citySignup").val(),
		    		"street": $("#streetSignup").val(),
		    		"region": $("#regionSignup").val(),
		    		"zipcode": $("#zipcodeSignup").val()
		    		});
				
				Register(params);
			}
		}
	} );
	
	
	$( "#register-form" ).validate( {
		rules: {
			firstnameSignup: "required",
			lastnameSignup: "required",
			usernameSignup: {
				required: true,
				minlength: 2
			},
			passwordSignup: {
				required: true,
				minlength: 5
			},
			confirmSignup: {
				required: true,
				minlength: 5,
				equalTo: "#passwordSignup"
			},
			emailSignup: {
				required: true,
				email: true
			}
			
		},
		messages: {
			firstnameSignup: "Please enter your firstname",
			lastnameSignup: "Please enter your lastname",
			usernameSignup: {
				required: "Please enter a username",
				minlength: "Your username must consist of at least 2 characters"
			},
			passwordSignup: {
				required: "Please provide a password",
				minlength: "Your password must be at least 5 characters long"
			},
			confirmSignup: {
				required: "Please provide a password",
				minlength: "Your password must be at least 5 characters long",
				equalTo: "Please enter the same password as above"
			},
			emailSignup: "Please enter a valid email address"
			
		},
		errorElement: "em",
		errorPlacement: function ( error, element ) {
			// Add the `help-block` class to the error element
			error.addClass( "help-block" );

			if ( element.prop( "type" ) === "checkbox" ) {
				error.insertAfter( element.parent( "label" ) );
				console.log("here")
			} else {
				error.insertAfter( element );
			}
		},
		highlight: function ( element, errorClass, validClass ) {
			$( element ).parents( ".form-group" ).addClass( "has-error" ).removeClass( "has-success" );
		},
		unhighlight: function (element, errorClass, validClass) {
			$( element ).parents( ".form-group" ).addClass( "has-success" ).removeClass( "has-error" );
		}
	
	});
	
	
}







