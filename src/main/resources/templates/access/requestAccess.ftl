<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Запросить доступ</title>
</head>
<body>
<form action="/operationRequest" method="post">
    <#if listUserAccess?has_content>
        <#list listUserAccess as listUserAccess>
            <input type="checkbox" class="listUserAccessValues" name="listUserAccessValues"
                   value="${listUserAccess}"/> ${listUserAccess} <br>
        </#list>
        <p><input type="submit" name="deleteRequests" value="Удалить запрос"/></p>
        <p><input type="submit" name="viewFiles" value="Просмотр файлов"/></p>
    </#if>
</form>
<br>
<a href="/listUserRequest">request access from the list</a> <br>
<a href="/console">back page</a>
</body>
</html>