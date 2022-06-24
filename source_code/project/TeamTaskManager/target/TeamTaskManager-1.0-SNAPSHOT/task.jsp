<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="./css/styles.css" rel="stylesheet" type="text/css">
        <title>Úkol</title>
    </head>
    <body>
    <header>
        <div>
            <h1>
                <a href="Service?action=profile" style="text-decoration: none; color: black">
                    <div class="profile-ish">${user.initials}</div> 
                </a>
                ${task.name}
            </h1>
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
                 <div><p>${task.description}</p>
                     <c:if test="${(task.isComplete)}">
                        <h3 style="color: #006600">Úkol byl splněn</h3>
                    </c:if>
                 </div>

                    <form action="Service" method="post">
                        <input type="hidden" name="idteam" value="${task.idTeam}">
                        <input type="hidden" name="action" value="viewteam">
                        <input class="button-like dark-blue" type="submit" value="Zpět na tým"> 
                    </form>

            </div>
            <div>
                <p style="color: ${message.color}">${message.description}</p>
                <p>
                    Termín: <fmt:formatDate value="${task.date}" pattern="dd.MM.yyyy"/> 
                        <fmt:formatDate type="time" timeStyle="short" value="${task.time}"/>
                </p>
            </div>
                <div>
                    <fieldset>
                    <legend>Dílčí úkoly</legend>
                    <c:if test="${task.subTasks.isEmpty()}">
                        <p>Tento úkol nemá žádné dílčí úkoly</p>
                    </c:if>
                    <table class="subtasks">
                        <c:forEach items="${task.subTasks}" var="sub">
                        <tr>
                            <td>
                            <form action="Service" method="post">
                                <input type="hidden" name="idtask" value="${task.id}">
                                <input type="hidden" name="idsub" value="${sub.id}">
                                <input type="hidden" name="action" value="completesub">
                                <input type="image" src="${(sub.isComplete)? "checks/check.png" : "checks/nocheck.png"}" alt="Submit" height="15pt"/>
                            </form>
                            </td>
                            <td>${sub.title}</td>
                            <td>
                            <form action="Service" method="post">
                                <input type="hidden" name="idtask" value="${task.id}">
                                <input type="hidden" name="idsub" value="${sub.id}">
                                <input type="hidden" name="action" value="deletesub">
                                <input class="view-button" type="submit" value="Smazat">
                                <br>
                            </form>
                            </td>
                        </tr>
                        </c:forEach>
                        <tr style="background-color: white">
                            <td></td>
                            <td>
                                <form id="1" action="Service" method="post">
                                    <input style="height: 13pt" type="text" name="title" placeholder="Popis" required>
                                    <input type="hidden" value="addsubtask" name="action">
                                    <input type="hidden" name="idtask" value="${task.id}">
                                   
                                </form>
                            </td>
                            <td>
                                 <input form="1" class="view-button dark-blue align-right" type="submit" value="Přidat úkol">
                            </td>
                        </tr>
                    </table>
                </fieldset>
            <div>
                <table class="workers">
                    <tr>
                        <td>Přiřazení členové: </td>
                        <c:if test="${task.workerList.isEmpty()}">
                            <td>Tento úkol nemá nikdo přiřazen</td>
                        </c:if>
                        <c:forEach items="${task.workerList}" var="worker">
                            <td>
                            <span class="tooltip">
                                ${worker.initials}
                                <span class="tooltip-text">
                                    ${worker.name} ${worker.surname}<br>
                                    ${worker.email}
                                </span>
                            </span>
                            </td>
                        </c:forEach>
                    </tr>
                    <c:if test="${ismanager && !task.workerList.isEmpty()}">
                        <tr>
                            <td>Zrušit přiřazení: </td>
                            <c:forEach items="${task.workerList}" var="worker">
                            <td>
                                <form action="Service" method="post">
                                    <input type="hidden" name="action" value="cancelassign">
                                    <input type="hidden" name="idworker" value="${worker.id}">
                                    <input type="hidden" name="idtask" value="${task.id}">
                                    <input type="hidden" name="idteam" value="${task.idTeam}">
                                    <input class="view-button dark-blue" type="submit" value="X">
                                </form>
                            </td>
                            </c:forEach>
                        </tr>
                    </c:if>
                </table>
                
            </div>                   
            <div class="add-worker">
                <c:if test="${ismanager}">
                    <form action="Service" method="post">
                        <input type="hidden" name="idtask" value="${task.id}" required readonly>
                        <input type="hidden" name="action" value="assigntask" required readonly>
                        <input type="hidden" name="idteam" value="${idteam}" required readonly>
                        <label for="members">Přiřadit úkol: </label>
                        <select name="idmem" id="members" required>
                            <c:forEach items="${members}" var="mem">
                                <c:if test="${!(task.isMemberWorker(mem))}">
                                    <option value="${mem.id}">${mem.name} ${mem.surname}</option>
                                </c:if>
                            </c:forEach>
                        </select>
                        <input class="button-like" type="submit" value="Přiřadit">
                    </form> 
                </c:if>
            </div>
        <div class="task-buttons">  
            <c:if test="${!(task.isComplete)}">
                <div class="task-button">
                <form method="post" action="Service">
                    <input type="hidden" name="idtask" value="${task.id}">
                    <input type="hidden" name="action" value="gettask">
                    <input class="button-like dark-blue" type="submit" value="Převzít úkol">
                </form>
                </div>
            </c:if>
            <div class="task-button">
                <form action="Service" method="post">
                    <input type="hidden" name="idtask" value="${task.id}">
                    <input type="hidden" name="from" value="task">
                    <input type="hidden" name="action" value="completetask">
                    <input class="button-like" type="submit" value="${(task.isComplete)? "Zrušit splnění úkolu" : "Úkol splněn"}"/>
                </form>
            </div>
            <c:if test="${ismanager}">
                <div class="task-button">
                <form action="Service" method="post">
                    <input type="hidden" name="idteam" value="${task.idTeam}">
                    <input type="hidden" name="idtask" value="${task.id}">
                    <input type="hidden" name="action" value="toedittask">
                    <input class="button-like" type="submit" value="Upravit"> 
                </form>
                </div>
                <div class="task-button">
                <form action="Service" method="post">
                    <input type="hidden" name="idteam" value="${task.idTeam}">
                    <input type="hidden" name="idtask" value="${task.id}">
                    <input type="hidden" name="action" value="deletetask">
                    <input class="button-like dark-blue" type="submit" value="Smazat"> 
                </form>
                </div>
            </c:if>
        </div>
                </div>
        </div>
    </div>    
    </body>
</html>
