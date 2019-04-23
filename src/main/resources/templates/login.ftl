<#import "common.ftl" as c>
<@c.page>
<h1>Сервис хранения файлов</h1>
<br>
<h1>Вход в систему</h1>
<h1>Введите логин и пароль:</h1>
<form action="/login" method="post">
    <div><label> E-mail: <input type="email" name="username"/> </label></div>
    <br>
    <div><label> Password: <input type="password" name="password"/> </label></div>
    <br>
    <div><input type="submit" value="Sing In"/></div>
    <br>
    <a href="registration">Registration</a>
</form>
</@c.page>