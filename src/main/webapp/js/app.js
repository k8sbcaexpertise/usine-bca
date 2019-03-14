window.onload = function() { 
	$.ajax({
		contentType: 'text/plain',
		url:"/usine/version",
		processData: false,
		success:function(data) {			
			if (document.getElementById("version") != null) {
				document.getElementById("version").style.display = "block";	
				document.getElementById("version").innerHTML = data;			
			}			
		},
		error:function(xhr, ajaxOptions, thrownError){
			 console.log(thrownError);
        },
		type:"GET"});	
};


function loadIframe(iframeName, url ) {	
	if (document.getElementById("result") !=null){
		$('#result').empty();		
		document.getElementById("result").style.display = "none";	
	}	
	var $iframe = $('#' + iframeName);			    			    
    if ( $iframe.length ) {				    
        $iframe.attr('src',url);			        		        			         			 
        return false;
    }
    return true;
};


function ConfirmStop(name) {
	if (confirm("Etes-vous sur de vouloir arrêter le serveur " + name + " ?")) {
		window.parent.document.getElementById("result").style.display = "block";
		window.parent.document.getElementById("result").innerHTML = "Arr&ecirc;t de " + name + " en cours...";
		operation('stop_instances', name);
	}
}

function Start(name) {
	window.parent.document.getElementById("result").style.display = "block";
	window.parent.document.getElementById("result").innerHTML = "D&eacute;marrage de " + name + " en cours...";
	operation('start_instances', name);
}


function operation( operation, instanceName ) {

	//showProgress();

	var serverManagement = { "instanceName" : instanceName , "operation" : operation  };
	$.ajax({
		contentType: 'application/json',
		data: JSON.stringify(serverManagement),
		url:"/server/management",
		processData: false,
		success:function(data) {
			if (document.getElementById("result") !=null){
				document.getElementById("result").style.display = "block";
				document.getElementById("result").innerHTML = data.resultat;
			}
		},
		error:function(xhr, ajaxOptions, thrownError){
			if (document.getElementById("result") !=null){
				document.getElementById("result").innerHTML = "";			
				document.getElementById("result").innerHTML = xhr.responseText;
			}
        },
		type:"POST"});					
};

function get_nap_status(envName) {	
	document.getElementById("contentFrame").src='about:blank';
	var serverManagement = { "url" : envName};
	$.ajax({
		contentType: 'application/json',
		data: JSON.stringify(serverManagement),
		url:"/application/info",
		processData: false,
		success:function(data) {
			if (data!== null && data !== undefined && data !== ""){
				var resultatFormated=data.resultat.split("\n").join("<br />");
				resultatFormated=resultatFormated.split(",").join("<br />");
				document.getElementById("result").style.display = "block";
				document.getElementById("result").innerHTML="";			
				document.getElementById("result").innerHTML=resultatFormated;
			}
		},
		error:function(){
			alert("error");
		},
		type:"POST"});
};
