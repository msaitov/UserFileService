<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Список пользователей</title>
</head>
<body>
<form action="/requestAccessAction" method="post">
        <#if requestedAccess?has_content>
            <p><input type="checkbox" name="downloadAccess" value="download access"/> download access </p>
            <p><input type="submit" name="requestAccessList" value="request access"/></p>
            <#list requestedAccess as requestedAccess>
                <input type="checkbox" class="requestedAccessValue" name="requestedAccessValue"
                       value="${requestedAccess}"/> ${requestedAccess} <br>
            </#list>
            <input type="checkbox" hidden class="requestedAccessValues" name="requestedAccessValue" value="none"
                   checked/>
        </#if>
</form>
<br>
Количество пользователей: ${userCount} <br> <br>
<a href="/myAccess">back page</a>
</body>
</html>