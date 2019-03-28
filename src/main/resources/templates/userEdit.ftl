<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Редактирование прав доступа</title>
</head>
<body>
<form action="/userRole" method="post">
    <input type="text" name="email" value="${user.email}">
    <#list roles as role>
    <div>
        <label><input type="checkbox" name="${role}" ${user.roles?seq_contains(role)?string("checked","")} >${role}
        </label>
    </div>
    </#list>
    <input type="hidden" value="${user.id}" name="userId">
    <button type="submit">Save</button>
</form>
<br><br>
<a href="/userRole">back</a>
</body>
</html>