function uploadImage() {
    var formData = new FormData();
    formData.append("file", document.getElementById("image").files[0]);
    var token = getStoredToken();
    var xhr = new XMLHttpRequest();
    xhr.open("POST", "/image/upload", true);
    xhr.setRequestHeader('Authorization', 'Bearer ' + token);
    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4) {
            if (xhr.status === 200) {
                var response = JSON.parse(xhr.responseText);
                var imageContainer = document.getElementById("imageContainer");
                imageContainer.style.display = "block";
                document.getElementById("imageDisplay").src = "data:image/png;base64," + response.image_details.imageFile;
                document.getElementById("content").innerText = response.image_details.content;
                document.getElementById("boldContent").innerText = response.image_details.boldContent;
            } else {
                alert("Error uploading image: " + xhr.responseText);
            }
        }
    };
    xhr.send(formData);
}

function displayImage(imageId) {
    var xhr = new XMLHttpRequest();
    xhr.open("GET", "/image/display/" + imageId, true);
    xhr.onreadystatechange = function () {
        if (this.readyState === 4) {
            if (this.status === 200) {
                var imageContainer = document.getElementById("imageContainer");
                imageContainer.style.display = "";

                var response = JSON.parse(xhr.responseText);
                document.getElementById("image").src = "data:image/jpeg;base64," + response.imageFile;
                document.getElementById("content").innerText = response.content;
                document.getElementById("boldContent").innerText = response.boldContent;
            } else {
                alert("Error fetching image: " + xhr.responseText);
            }
        }
    };

    xhr.send();
}

function displayAllImageDetails() {
    var token = getStoredToken();
    console.log(token);

    // Fetch Mustache template asynchronously
    var templateXHR = new XMLHttpRequest();
    templateXHR.open("GET", "processed_images.mustache", true);
    templateXHR.onreadystatechange = function () {
        if (this.readyState === 4) {
            if (this.status === 200) {
                var template = this.responseText;
                renderImagesWithTemplate(token, template);
            } else {
                alert("Error fetching Mustache template: " + this.responseText);
            }
        }
    };
    templateXHR.send();
}

function renderImagesWithTemplate(token, template) {
    var xhr = new XMLHttpRequest();
    xhr.open("GET", "/image/display/all", true);
    xhr.setRequestHeader('Authorization', 'Bearer ' + token);
    xhr.onreadystatechange = function () {
        if (this.readyState === 4) {
            if (this.status === 200) {
                var response = JSON.parse(xhr.responseText);
                var rendered = Mustache.render(template, response.images);
                document.getElementById('processed-image-container').innerHTML = rendered;
                document.getElementById('upload-container').style.display = 'none';
            } else {
                alert("Error fetching image: " + xhr.responseText);
            }
        }
    };
    xhr.send();
}

function getStoredToken() {
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
