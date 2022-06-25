<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="./css/styles.css" rel="stylesheet" type="text/css">
        <title>TTM Přehled</title>
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
            <li><a class="active" href="Service?action=main">Hlavní stránka</a></li>
            <li><a href="Service?action=teams">Týmy</a></li>
            <li><a href="Service?action=profile">Profil</a></li>
        </ul>
    </nav>
    <div class="container">
        <div class="page-top">
            <div class="alerts">
                <ul style="list-style-type: none">
                <c:forEach items="${alerts}" var="alert">
                    <li>
                        <form action="Service" method="post">
                            <input type="hidden" name="idalert" value="${alert.id}" readonly>
                            <input type="hidden" name="action" value="deletealert" readonly>
                            <input type="submit" id="delete" value="X" class="bin-button">
                            <label for="delete" style="color:${alert.color}">${alert.text}</label>
                        </form>
                    </li>
                </c:forEach>
                </ul>
            </div>
            <p style="color: ${message.color}">${message.description}</p>
            <a class="button-like new-team" href="addteam.jsp">+ Nový tým</a>
        </div> 
        <div>
            <c:if test="${teams.isEmpty()}">
                <p>Nejste členem žádného týmu</p>
            </c:if>
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
                <hr>
                <fieldset>
                <legend>Úkoly</legend>
                <table class="tasks">
                    <c:forEach items="${team.taskList}" var="task">
                    <tr>
                        <td>
                        <form action="Service" method="post">
                            <input type="hidden" name="idtask" value="${task.id}">
                            <input type="hidden" name="from" value="main"/>
                            <input type="hidden" name="action" value="completetask">
                            <input type="image" src="${(task.isComplete)? "checks/check.png" : "checks/nocheck.png"}" alt="Submit" height="15pt"/>
                        </form>
                        </td>
                        <td>${task.name}</td>
                        <td><fmt:formatDate value="${task.date}" pattern="dd.MM.yyyy"/></td>
                        <td><fmt:formatDate type="time" timeStyle="short" value="${task.time}"/></td>
                        <td>
                                <c:forEach items="${task.workerList}" var="worker">
                                    <span class="tooltip">
                                        ${worker.initials}
                                        <span class="tooltip-text">
                                            ${worker.name} ${worker.surname}<br>
                                            ${worker.email}
                                        </span>
                                    </span>
                                </c:forEach>
                        </td>
                        <td>${task.description}</td>
                        <td>
                        <form action="Service" method="post">
                            <input type="hidden" name="idtask" value="${task.id}" hidden>
                            <input type="hidden" name="action" value="viewtask" hidden>
                            <input class="view-button" type="submit" value="Zobrazit">
                        </form>
                        </td>
                    </tr>
                    </c:forEach>
                </table>
                </fieldset>
            </c:forEach>
        </div>
    </div>
    </div>
    </body>
</html>
