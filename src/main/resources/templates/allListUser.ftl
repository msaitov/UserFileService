<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Список пользователей</title>
</head>
<body>
<form action="/allListUser" method="post">
        <#if emailList?has_content>
            <#list emailList as emailList>
                <input type="checkbox" name="listUserEmail" value="${emailList}"/> ${emailList} <br>
            </#list>
            <#if isAdminRole?has_content>
                <p><input type="submit" name="viewFiles" value="Просмотр файлов"/></p>
            <#else>
                <p><input type="submit" name="viewStatistics" value="Просмотр статистики"/></p>
            </#if>
        </#if>
</form>
<br>
Количество пользователей: ${userCount} <br> <br>
<a href="/console">back page</a>

</body>
</html>