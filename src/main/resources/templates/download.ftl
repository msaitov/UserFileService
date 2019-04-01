<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
<script>
    var host = location.protocol + '//' + location.hostname + (location.port ? ':' + location.port : '');
    <#list downloadFileList as downloadFileList>
                window.open(host + "/downloadFile/${downloadFileList}");
    </#list>
    window.open(host + "/console");
</script>
</body>
</html>