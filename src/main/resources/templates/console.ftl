<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0">
</head>
<body>
<noscript>
    <h2>Sorry! Your browser doesn't support Javascript</h2>
</noscript>
<form action="/logout" method="post">
    <input type="submit" value="Sign Out"/>
</form>
<a href="/myAccess">Мой доступ</a> <br>
    <#if role?has_content>
        <a href="/allListUser">Список пользователей</a> <br>
    <#else>
        <a href="/requestAccess">Запросить доступ</a> <br>
    </#if>
    <#if role?has_content && role == "admin">
        <a href="/userRole">Права доступа</a> <br>
    </#if>
<div class="upload-container">
    <div class="upload-content">
        <div class="multiple-upload">
            <div class="multiple-upload">
                <h3>Upload Multiple Files</h3>
                <form method="post" id="multipleUploadForm" name="multipleUploadForm">
                    <input id="multipleFileUploadInput" type="file" name="files" class="file-input" multiple required/>
                    <button type="submit" class="primary submit-btn">Submit</button>
                </form>
                <div class="upload-response">
                    <div id="multipleFileUploadError"></div>
                    <div id="multipleFileUploadSuccess"></div>
                </div>
            </div>
            <form action="/operationFiles" method="post">
                    <#if listFiles?has_content>
                        <#list listFiles as listFiles>
                            <input type="checkbox" class="searchValues" name="searchValues"
                                   value="${listFiles}"/> ${listFiles} <br>
                        </#list>
                        <p><input type="submit" name="deleteFls" value="delete"/></p>
                        <p><input type='submit' name='downloadFls' value='download' onclick="return downloadFiles()"/>
                        </p>
                    </#if>
            </form>
            <form action="/refresh" method="post">
                <p><input type="submit" class="btn btn-success" value="reload page"/></p>
            </form>
        </div>
    </div>
</div>
<script src="/js/main.js"></script>
</body>
</html>


