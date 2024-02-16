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
			alert(this.responseText);
			window.location = "/";
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
			alert(this.responseText);
			window.location = "/";
		}
	};
	xhttp.open("POST", "http://localhost:8080/user/signup",true);
	xhttp.setRequestHeader('Content-Type', 'application/json');
	xhttp.send(json);
}