<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Список файлов</title>
</head>
<body>
<h1>Список файлов - доступ Администратора</h1><br>
<form action="/operationFilesAdmin" method="post">
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
        <p><input type="submit" name="deleteFls" value="delete"/></p>
    </#if>
</form>
<a href="/allListUser">back page</a>

</body>
</html>