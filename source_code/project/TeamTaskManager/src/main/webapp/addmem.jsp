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
                <li><a href="Service?action=profile">Profil</a></li>
            </ul>
        </nav>
        <div class="container">
            <div class="page-top">
                <h1>Přidat člena</h1>
                <form action="Service" method="post">
                    <input type="hidden" name="idteam" value="${task.idTeam}">
                    <input type="hidden" name="action" value="viewteam">
                    <input class="button-like dark-blue" type="submit" value="Zpět na tým"> 
                </form>
            </div>
            <div class="form-alone">
            <form method="post" action="Service">
                <p style="color: ${message.color}">${message.description}</p>
                <input type="hidden" name="action" value="addmember">
                <input type="hidden" name="idteam" value="${idteam}"><br>
                <label for="identity">ID nebo e-mail: </label>
                <input type="text" name="identity" id="identity" required autofocus>
                <br>
                <label for="role">Role člena: </label>
                <select id="role" name="role">
                    <option value="member">Člen</option>
                    <option value="manager">Správce</option>
                </select>
                <input class="button-like" type="submit" value="Přidat člena">
            </form>
            </div>
        </div>
    </div>
    </body>
</html>
