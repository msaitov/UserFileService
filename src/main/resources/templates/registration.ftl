<#import "common.ftl" as c>
<@c.page>
<h1>Сервис хранения файлов</h1>
<br>
<h2>Регистрация:</h2>
<form action="/registration" method="post">
    <div><label> E-mail: <input type="email" name="email"/> </label></div>
    <br>
    <div><label> Password: <input type="password" name="password"/> </label></div>
    <br>
    <div><input type="submit" value="Sing Up"/></div>
    <br>
    <a href="login">login page</a>
</form>
</@c.page>
