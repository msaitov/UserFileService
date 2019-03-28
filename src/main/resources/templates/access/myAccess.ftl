<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>my access</title>
</head>
<body>
<h1>Мой доступ</h1>
<h2>Запросили доступ</h2><br>
<form action="/requestedAccessAction" method="post">
        <#if requestedAccess?has_content>
            <#list requestedAccess as requestedAccess>
                <input type="checkbox" class="requestedAccessValues" name="requestedAccessValues"
                       value="${requestedAccess}"/> ${requestedAccess} <br>
            </#list>
            <p><input type="submit" name="giveAccess" value="give access"/></p>
            <p><input type='submit' name='denyAccess' value='deny access'/></p>
        </#if>
</form>
<br><br><br>
<h2>Дал доступ</h2>
<form action="/gaveAccessAction" method="post">
        <#if listGaveAccess?has_content>
            <#list listGaveAccess as listGaveAccess>
                <input type="checkbox" class="gaveAccessValues" name="gaveAccessValues"
                       value="${listGaveAccess}"/> ${listGaveAccess} <br>
            </#list>
        </#if>
    <p><input type='submit' name='denied' value='запретить доступ'/></p>
</form>
<br><br>
<a href="/listUser">give access from the list</a> <br>
<a href="/console">back page</a>
</body>
</html>