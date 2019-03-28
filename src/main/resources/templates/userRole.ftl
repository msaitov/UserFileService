<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Управление доступом</title>
</head>
<body>
<h1>Управление доступом</h1><br>
<table>
    <thead>
    <tr>
        <th>Name</th>
        <th>Role</th>
        <th></th>
    </tr>
    </thead>
    <tbody>
        <#list users as user>
        <tr>
            <td>${user.email}</td>
            <td><#list user.roles as role>${role}<#sep>, </#list> </td>
            <td><a href="/userRole/${user.id}">edit</a></td>
        </tr>
        </#list>
    </tbody>
</table>
<br><br>
<a href="/console">back</a>
</body>
</html>