<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
            <h1>
                <a href="Service?action=profile" style="text-decoration: none; color: black">
                    <div class="profile-ish">${user.initials}</div>
                </a>
                Týmy
            </h1>
        </div>
        <a class="button-like logout" href="Service?action=logout">Odhlásit se</a>
    </header>
    <div class="content">
    <nav>
        <ul>
            <li><a href="Service?action=main">Hlavní stránka</a></li>
            <li><a class="active" href="Service?action=teams">Týmy</a></li>
            <li><a href="Service?action=profile">Profil</a></li>
        </ul>
    </nav>
    <div class="container">
        <div class="page-top">
            <p style="color: ${message.color}">${message.description}</p>
            <a class="button-like new-team" href="addteam.jsp">+ Nový tým</a>
        </div>
        <div>
        <c:forEach items="${teams}" var="team">
            <div class="team-header">
                    <label for="progress"><h2>${team.name}</h2></label>
                    <progress id="progress" value="${team.perDone}" max="100">${team.perDone}%</progress>
                    <form method="post" action="Service">
                        <input type="hidden" name="action" value="viewteam">
                        <input type="hidden" name="idteam" value="${team.id}">
                        <input class="button-like" type="submit" value="Zobrazit tým">
                    </form>
            </div>
            <p>${team.description}</p>
            <hr>
        </c:forEach>
        </div>
    </div>
    </div>
    </body>
</html>
