<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="./css/styles.css" rel="stylesheet" type="text/css">
        <title>Registrace</title>
    </head>
    <body>
        <div class="content">
            <div class="container" style="border: none">
                <div class="page-top" style="align-items: center">
                    <h1>Registrace</h1>
                    <div>
                        <a class="button-like dark-blue" href="index.jsp">Zpět</a>
                        <a class="button-like" href="login.jsp">Přihlášení</a>
                    </div>
                </div> 
                <div class="form-alone">
                    <form method="post" action="Service">
                        <p style="color: ${message.color}">${message.description}</p>
                        <input type="hidden" name="action" value="registration" hidden>
                        <label for="name">Jméno: </label>
                        <input type="text" id="name" name="name" value="${name}" autofocus required maxlength="50">
                        <br/>
                        <label for="surname">Příjmení: </label>
                        <input type="text" id="surname" value="${surname}" name="surname" required maxlength="50">
                        <br/>
                        <label for="email">Email: </label>
                        <input type="email" id="email" value="${email} "name="email" required maxlength="150">
                        <br/>
                        <label for="login">Přihlašovací jméno: </label>
                        <input type="text" id="login" value="${login} "name="login" required maxlength="50">
                        <br/>
                        <label for="password">Heslo: </label>
                        <input type="password" id="password" name="password" required maxlength="50">
                        <br>
                        <input class="button-like" type="submit" name="submit" value="Registrovat">
                    </form>
                </div>
            </div>
        </div>
    </body>
</html>
