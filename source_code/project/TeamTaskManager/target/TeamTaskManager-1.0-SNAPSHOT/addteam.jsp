<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="./css/styles.css" rel="stylesheet" type="text/css">
        <title>Přidat tým</title>
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
                <h1>Nový tým</h1>
                <p style="color: ${message.color}">${message.description}</p>
                <a class="button-like dark-blue" href="Service?action=teams">Zobrazit týmy</a>
            </div>
            <div class="form-alone">
                <form action="Service" method="post">
                    <input type="hidden" name="action" value="newteam" readonly/>
                    <label for="name">Název týmu: </label>
                    <input type="text" name="name" id="name" placeholder="Vyplňte název týmu" maxlength="50" required>
                    <br/>
                    <label for="desc"></label>
                    <textarea name="description" id="desc" maxlength="150" placeholder="Nepovinný popis týmu (max. 150)"></textarea>
                    <br/>
                    <input class="button-like" type="submit" name="submit" value="Vytvořit">
                </form>
            </div>
        </div>
        </div>
    </div>
    </body>
</html>
