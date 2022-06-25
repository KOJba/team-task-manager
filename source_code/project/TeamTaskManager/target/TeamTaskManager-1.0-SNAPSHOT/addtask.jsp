<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="./css/styles.css" rel="stylesheet" type="text/css">
        <title>Přidat úkol</title>
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
                    <h1>Přidat úkol</h1>
                    <p>${message.description}</p>
                    <div>
                    <form action="Service" method="post">
                        <input type="hidden" name="idteam" value="${idteam}">
                        <input type="hidden" name="action" value="viewteam">
                        <input class="button-like dark-blue" type="submit" value="Zpět na tým"> 
                    </form>
                    </div>
                </div> 
            <div class="form-alone">
                <form method="post" action="Service">
                    <input type="hidden" name="idteam" value="${idteam}">
                    <input type="hidden" name="action" value="newtask">
                    <label for="title">Název: </label>
                    <input type="text" name="title" id="title" required maxlength="50"> 
                    <br>
                    <label for="description"></label>
                    <textarea name="description" id="description" maxlength="250" placeholder="Popis úkolu"></textarea>
                    <br>
                    <label>Termín: </label>
                    <input type="date" name="date">
                    <label for="time">Čas</label>
                    <input type="time" name="time">
                    <input class="button-like" type="submit" value="Uložit">
                </form>
            </div>
            </div>
        </div>
    </body>
</html>
