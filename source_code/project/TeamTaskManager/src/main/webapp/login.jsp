<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="./css/styles.css" rel="stylesheet" type="text/css">
        <title>Přihlášení</title>
    </head>
    <body>
        <div class="content">
            <div class="container" style="border: none">
                 <div class="page-top" style="align-items: center">
                    <h1>Přihlášení</h1>
                    <div>
                        <a class="button-like" href="index.jsp">Zpět</a>
                        <a class="button-like dark-blue" href="registration.jsp">Registrace</a>
                    </div>
                </div> 
                <div class="form-alone">
                    <form class="first-form" method="post" action="Service">
                        <p>${message.description}</p>
                        <input type="hidden" name="action" value="login" hidden>
                        <label for="login">Přihlašovací jméno: </label>
                        <input type="text" id="login" name="login" value="${user.login}" autofocus required>
                        <br>
                        <label for="password">Heslo: </label>
                        <input type="password" id="password" name="password" value="${password}" required>
                        <br/>
                        <input class="button-like" type="submit" value="Přihlásit">
                    </form>
                </div>
            </div>
        </div>
    </body>
</html>
