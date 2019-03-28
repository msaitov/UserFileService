'use strict';

var multipleUploadForm = document.querySelector('#multipleUploadForm');
var multipleFileUploadInput = document.querySelector('#multipleFileUploadInput');
var multipleFileUploadError = document.querySelector('#multipleFileUploadError');
var multipleFileUploadSuccess = document.querySelector('#multipleFileUploadSuccess');

function downloadFiles() {
    var checkedValue = null;
    var inputElements = document.getElementsByClassName('searchValues');
    for (var i = 0; inputElements[i]; ++i) {
        if (inputElements[i].checked) {
            checkedValue = inputElements[i].value;
            window.open("http://localhost:8080/downloadFile/" + checkedValue);
        }
    }
    window.open("http://localhost:8080/console");
}

function uploadMultipleFiles(files) {
    var formData = new FormData();
    for (var index = 0; index < files.length; index++) {
        formData.append("files", files[index]);
    }

    var xhr = new XMLHttpRequest();
    xhr.open("POST", "/uploadMultipleFiles");

    xhr.onload = function () {
        console.log(xhr.responseText);
        var response = JSON.parse(xhr.responseText);
        if (xhr.status == 200) {
            multipleFileUploadError.style.display = "none";
            var content = "All Files Uploaded Successfully. Please, reload page.";
            // for(var i = 0; i < response.length; i++) {
            //     content += "<input type=\"checkbox\" name=\"searchValues\" value=" + response[i].fileName + " /> " + response[i].fileName + " </br>";
            // }
            multipleFileUploadSuccess.innerHTML = content;
            multipleFileUploadSuccess.style.display = "block";
        } else {
            multipleFileUploadSuccess.style.display = "none";
            multipleFileUploadError.innerHTML = (response && response.message) || "Some Error Occurred";
        }
    };

    xhr.send(formData);
}

multipleUploadForm.addEventListener('submit', function (event) {
    var files = multipleFileUploadInput.files;
    if (files.length === 0) {
        multipleFileUploadError.innerHTML = "Please select at least one file";
        multipleFileUploadError.style.display = "block";
    }
    uploadMultipleFiles(files);
    event.preventDefault();

}, true);

