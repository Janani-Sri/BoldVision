function validate(form)
{
	var xhttp = new XMLHttpRequest();
	var formData = new FormData(form);
	var object = {};
	formData.forEach(function(value, key)
	{
	    object[key] = value;
	});
	var json = JSON.stringify(object);
	xhttp.onreadystatechange = function()
	{
		if (this.readyState == 4 && this.status == 200)
		{
//			alert(this.responseText);
			var response = JSON.parse(this.responseText);
			console.log(response)
			console.log(response.token);
            storeToken(response.token);

			window.location = "/uploadImage";
		}
	};
	xhttp.open("POST", "http://localhost:8080/user/signin",true);
	xhttp.setRequestHeader('Content-Type', 'application/json');
	xhttp.send(json);
}

function signUp(form)
{
	var xhttp = new XMLHttpRequest();
	var formData = new FormData(form);
	var object = {};
	formData.forEach(function(value, key)
	{
	    object[key] = value;
	});
	var json = JSON.stringify(object);
	xhttp.onreadystatechange = function()
	{
		if (this.readyState == 4 && this.status == 200)
		{
		console.log("validated!!");
		    var response = JSON.parse(this.responseText);
		    storeToken(response.token);
		    console.log(response);
		    window.location = "/uploadImage";
//			alert(this.responseText);
//			window.location = "/";
		}
	};
	xhttp.open("POST", "http://localhost:8080/user/signup",true);
	xhttp.setRequestHeader('Content-Type', 'application/json');
	xhttp.send(json);
}

function storeToken(token){
//    localStorage.setItem('accessToken', token);
console.log(token)
    document.cookie = "accessToken=" + token + "; path=/; secure; samesite=strict";

}

function getStoredToken() {
//    return localStorage.getItem('accessToken');
var name = "accessToken=";
    var decodedCookie = decodeURIComponent(document.cookie);
    var cookieArray = decodedCookie.split(';');

    for (var i = 0; i < cookieArray.length; i++) {
        var cookie = cookieArray[i].trim();
        if (cookie.indexOf(name) === 0) {
            return cookie.substring(name.length, cookie.length);
        }
    }

    return null;
}