function downloadFiles() {
    var checkedValue = null;
    var inputElements = document.getElementsByClassName('searchValues');
    var idOwner = document.getElementById('idOwner').innerHTML;
    for (var i = 0; inputElements[i]; ++i) {
        if (inputElements[i].checked) {
            checkedValue = inputElements[i].value;
            window.open("http://localhost:8080/downloadFile/" + idOwner + "/" + checkedValue);
        }
    }
    window.open("http://localhost:8080/viewFiles");
}
