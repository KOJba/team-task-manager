<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="./css/styles.css" rel="stylesheet" type="text/css">
        <title>Upravit úkol</title>
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
            <h1>Upravit úkol: ${task.name}</h1>
            <p style="color: ${message.color}">${message.description}</p>
            <form action="Service" method="post">
                <input type="hidden" name="idtask" value="${task.id}" readonly>
                <input type="hidden" name="action" value="viewtask" readonly>
                <input class="button-like dark-blue" type="submit" value="Zpět na úkol">
            </form>
        </div>
        <div class="form-alone">
        <form method="post" action="Service">
            <input type="hidden" name="action" value="edittask">
            <input type="hidden" name="idtask" value="${task.id}">
            <label for="title">Název: </label>
            <input type="text" name="title" id="title" value="${task.name}" required> 
            <br>
            <label for="description">Popis</label>
            <textarea name="description" id="description" value="${task.description}" ></textarea>
            <br>
            <label>Termín: </label>
            <input type="date" id="date" name="date" value="${task.date}" >
            <label for="time"></label>
            <input type="time" id="time" name="time" value="${task.time}" >
            <input class="button-like" type="submit" value="Uložit změny">
        </form>
        </div>        
    </div>
    </div>   
    </body>
</html>
