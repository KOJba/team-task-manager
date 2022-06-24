<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="./css/styles.css" rel="stylesheet" type="text/css">
        <title>JSP Page</title>
    </head>
    <body>
        <header>
        <div>
            <a href="Service?action=profile" style="text-decoration: none; color: black">
                <h1>
                    <div class="profile-ish">${user.initials}</div> 
                    ${user.name} ${user.surname}
                </h1>
            </a>
        </div>
        <a class="button-like logout" href="Service?action=logout">Odhlásit se</a>
        </header>
        <div class="content">
        <nav>
            <ul>
                <li><a href="Service?action=main">Hlavní stránka</a></li>
                <li><a href="Service?action=teams">Týmy</a></li>
                <li><a class="active" href="Service?action=profile">Profil</a></li>
            </ul>
        </nav>
        <div class="container">
            <div class="page-top"> 
                <h1>ID: ${user.id}</h1>
                <p>${message.description}</p>
            </div>
            <div>
            <div class="form-alone">
            <form class="profile-form" action="Service" method="post">
                <label for="name">Jméno: </label>
                <input type="text" name="name" id="name" value="${user.name}" required><br>
                <label for="surname">Příjmení: </label>
                <input type="text" name="surname" id="surname" value="${user.surname}" required><br>
                <label for="email">Email: </label>
                <input type="email" name="email" id="email" value="${user.email}" required><br>
                <label for="login">Login: </label>
                <input type="text" name="login" id="login" value="${user.login}" required><br>
                <label for="password">Heslo: </label>
                <input type="password" name="password" id="password" value="${password}" required><br>

                <input type="hidden" name="action" value="profileedit">
                <input class="button-like" type="submit" value="Uložit úpravy" name="submit">
                <input class="button-like dark-blue" type="submit" value="Smazat profil" name="submit">
                
            </form>
            </div>
            </div>
        </div>
        </div>
    </body>
</html>
