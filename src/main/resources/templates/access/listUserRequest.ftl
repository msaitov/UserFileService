<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Список пользователей</title>
</head>
<body>
<form action="/requestAccessSendAction" method="post">
        <#if requestAccessSend?has_content>
            <p><input type="checkbox" name="downloadAccess" value="download access"/> download access </p>
            <p><input type="submit" name="requestAccessList" value="Send request access"/></p>
            <#list requestAccessSend as requestAccessSend>
                <input type="checkbox" class="requestAccessValue" name="requestAccessValue"
                       value="${requestAccessSend}"/> ${requestAccessSend} <br>
            </#list>
            <input type="checkbox" hidden class="requestAccessValue" name="requestAccessValue" value="none" checked/>
        </#if>
</form>
<br>
Количество пользователей: ${userCount} <br> <br>
<a href="/requestAccess">back page</a>
</body>
</html>