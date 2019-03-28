<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Просмотр файлов</title>
</head>
<body>
<form action="/operationFilesRequest" method="post">
    <#if emailOwner?has_content>
        <input type="text" name="ownName" value="${emailOwner}"/> <br><br>
    </#if>
    <#if idOwner?has_content>
        <label id="idOwner" hidden>${idOwner}</label><br>
    </#if>
    <#if messageDenied?has_content>
        <h2>${messageDenied}</h2>
    </#if>
    <#if listFiles?has_content>
        <#list listFiles as listFiles>
            <input type="checkbox" class="searchValues" name="searchValues" value="${listFiles}"/> ${listFiles} <br>
        </#list>
        <#if downloadAccess?has_content>
            <p><input type='submit' name='downloadFls' value='download' onclick="return downloadFiles()"/></p>
        </#if>
    </#if>
</form>
<a href="/requestAccess">back page</a>
<script src="/js/downloadRequest.js"></script>
</body>
</html>