function downloadFiles() {
    var host = location.protocol + '//' + location.hostname + (location.port ? ':' + location.port : '');
    var checkedValue = null;
    var inputElements = document.getElementsByClassName('searchValues');
    var idOwner = document.getElementById('idOwner').innerHTML;
    for (var i = 0; inputElements[i]; ++i) {
        if (inputElements[i].checked) {
            checkedValue = inputElements[i].value;
            window.open(host + "/downloadFile/" + idOwner + "/" + checkedValue);
        }
    }
    window.open(host + "/viewFiles");
}
