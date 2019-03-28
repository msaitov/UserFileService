<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>статистика</title>
</head>
<body>
<h1>Статистика по файлам</h1><br>
<#if emailOwner?has_content>
    <input type="text" name="ownName" disabled value="${emailOwner}"/> <br><br>
</#if>
<table>
    <thead>
    <tr>
        <th>Имя файла</th>
        <th>Количество скачиваний</th>
    </tr>
    </thead>
    <tbody>
        <#if files?has_content>
            <#list files as file>
                <tr>
                    <td>${file.fileName}</td>
                    <td>${file.downloadCount}</td>
                </tr>
            </#list>
        </#if>
    </tbody>
</table>
<br>
<label>Количество файлов: ${numberOfFiles}</label><br><br>
<a href="/allListUser">back</a>
</body>
</html>