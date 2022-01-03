function sendAlerts(parameterName,message){
	var url = window.location.search;
	url = url.replace("?",'');
	var paramMap = url.split('&');
	var i;
	var alertMessage="-1";
	for(i=0;i<paramMap.length;i++)
	{
		var curParamSplit = paramMap[i].split('=');
		if(curParamSplit[0] == parameterName)
		{
			alertMessage = message + " " + curParamSplit[1];
		}
		else if(curParamSplit[0] == "reason")
		{
			alertMessage = alertMessage + "\nReason : " +curParamSplit[1];
		}
	}
	if(alertMessage!="-1")
		alert(alertMessage);
/*	var status = paramMap[parameterName];
	if(paramMap[parameterName]!=null || paramMap[parameterName]!='')
		alert("User creation "+paramMap[parameterName]);
	else
		alert("donno what just happened");*/
}

function validatePassword(){
	//alert("here in validate password");
	var password  = document.getElementById("newPassword").value;
	var passwordAgain = document.getElementById("newPasswordAgain").value;
	//alert(password + " " + passwordAgain);
	if(password.length < 8)
	{
		alert("Password too short ");
		return false;
	}
	else if(password!=passwordAgain){
		alert("new passwords don't match");
		return false;
	}else if(password.search(/[A-Z]/) < 0 ){
		alert("password must contain atleast one capital");
		return false;
	}else if(password.search(/[!@#$%^&]/) < 0){
		alert("password must contain atleast one special character");
		return false;
	}else if(password.search(/[0-9]/) < 0){
		alert("password must contain atleast one digit");
		return false;
	}else{
		return true;
	}
}