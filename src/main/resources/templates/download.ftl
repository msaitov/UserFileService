<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
<script>
    <#list downloadFileList as downloadFileList>
                window.open("http://localhost:8080/downloadFile/${downloadFileList}");
    </#list>
    window.open("http://localhost:8080/console");
</script>
</body>
</html>