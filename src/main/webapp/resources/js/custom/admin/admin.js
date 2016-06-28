



$(document).ready(function () {
    initialize();
});

/* Global variables */
var pendingusers_matrix = [];
var pending_table, registered_table;


function initialize(){
	collapse();
    getNumberOfUsers();
    createGrids();
    setListeners();
	
}


function collapse(){
	   $(window).bind("load resize", function () {
           if ($(this).width() < 768) {
               $('div.sidebar-collapse').addClass('collapse')
           } else {
               $('div.sidebar-collapse').removeClass('collapse')
           }
       });
}



function getNumberOfUsers(){
	console.log("getting the num of users");
	$.ajax({
		type : "GET",
		dataType:'json',
		url  : "/auctionbay/administrator/count-users",
		success : function(data) {
			$("#pending-number").text(data.pending_users);
			$("#registered-number").text(data.registered_users);
		}
		
	});  
	
}


/* The function getNumberOfUsers might cut to the below functions */

function countPendingUsers(){}

function countRegisteredUsers(){}

function setListeners(){
	
	$("a.admin-logout").click(function(event){
		console.log("logout from admin");
		event.preventDefault();
		/*$.ajax({
			type : "GET",
			url  : "/auctionbay/administrator/accept-user"
		});*/
		document.location.href="/auctionbay/";
		//document.location.href="/auctionbay/logout";
		//window.location.replace("https://localhost:8443/auctionbay/");
		
		});
	
	$("#pending-users-link").click(function(event){
		console.log("hi pen users");
		event.preventDefault();
		document.getElementById('registered-users').style.display = "none";
		document.getElementById('pending-users').style.display = "block";
		//console.log($('#pending-users-grid tbody tr').data());
		//$('registered-users').css("display","none");
		//$('pending-users').css("display","block");
	});
	
	$("#registered-users-link").click(function(event){
		console.log("hi reg users");
		event.preventDefault();
		document.getElementById('registered-users').style.display = "block";
		document.getElementById('pending-users').style.display = "none";
		//$('registered-users').css("display","block");
		//$('pending-users').css("display","none");
		
	});
	

	$('#registered-users-grid tbody').on( 'click', 'tr', function () {
		console.log("i am clicked");
	    console.log( registered_table.row( this ).data() );
	} );
	
	
	$('#pending-users-grid tbody').on('click', 'td.details-control', function () {
		var tr = $(this).parents('tr');
		var row = pending_table.row(tr);
		pendingusers_matrix.push(row.data()[0]);
        
        console.log(row.data()[0]);
        var username = row.data()[0];
        accept_user(username);
        console.log('ACCEPTED USER');
		pending_table.row( $(this).parents('tr') ).remove().draw();
		console.log('reached here');
    } );
	
	
}


function createGrids(){
	
	pending_table = $('#pending-users-grid').DataTable( {
		"processing": true,
	    "serverSide": true,
	    ajax:"/auctionbay/administrator/pending-users",
	    
        columns: [
            { title: "Username" },
            { title: "Firstname" },
            { title: "Lastname" },
            { title: "Email" },
            { title: "Phone number" },
            { title: "TRN" },
            { title: "City" },
            { title: "Region" },
            { title: "Street" },
            { title: "Zip Code" },
            {
                 title: "Accept user",
                "className":      'details-control',
                "orderable":      false,
                "data":           null,
                "defaultContent": ''
            }
        ]
    	});
	
	registered_table = $('#registered-users-grid').DataTable( {
		"processing": true,
	    "serverSide": true,
	    ajax:"/auctionbay/administrator/registered-users",
	    
        columns: [
            { title: "Username" },
            { title: "Firstname" },
            { title: "Lastname" },
            { title: "Email" },
            { title: "Phone number" },
            { title: "TRN" },
            { title: "City" },
            { title: "Region" },
            { title: "Street" },
            { title: "Zip Code" }    
            
        ]
    	});
	
}


function accept_user(username){
	$.ajax({
		type : "GET",
		data : {
			username : username
		},
		datatype: 'json',
		url  : "/auctionbay/administrator/accept-user",
		success : function(response) {	
			console.log("ok from ajax");
		}/*,
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			console.log('error', textStatus + " " + errorThrown);
			alert('Application could not Accept the user');
		}*/
		
	});
}


