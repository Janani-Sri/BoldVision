function uploadImage() {
    var formData = new FormData();
    formData.append("file", document.getElementById("image").files[0]);
    formData.append("userId", 1);  // Replace with the actual user ID
    // Append other fields as needed

    var xhr = new XMLHttpRequest();
    xhr.open("POST", "/image/upload", true);
    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4) {
            if (xhr.status === 200) {
                alert("Image uploaded successfully!");
                // Optionally, you can refresh the image display section
                displayImage(1);
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
                document.getElementById("content").innerText = "Content: " + response.content;
                document.getElementById("boldContent").innerText = "Bold Content: " + response.boldContent;
            } else {
                alert("Error fetching image: " + xhr.responseText);
            }
        }
    };

    xhr.send();
}

//function displayImages() {
//    $.ajax({
//        type: "GET",
//        url: "/image/all", // Replace with your endpoint for retrieving all images
//        success: function (data) {
//            var template = $('#image-template').html();
//            var rendered = Mustache.render(template, { images: data });
//            $('#imageContainer').html(rendered);
//        },
//        error: function (error) {
//            alert("Error fetching images: " + error.responseText);
//        }
//    });
//}


