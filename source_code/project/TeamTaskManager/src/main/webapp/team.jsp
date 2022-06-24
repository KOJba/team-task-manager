<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
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
                ${team.name}
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
                <div><p style="color: ${message.color}">${message.description}</p></div>
                <div class="team-mana">
                Vedoucí týmu:
                <c:forEach items="${team.memberList}" var="member">
                    <c:if test="${member.isManager}">
                        <span class="tooltip">
                            ${member.initials}
                            <span class="tooltip-text">
                                ${member.name} ${member.surname}<br>
                                ${member.email}
                            </span>
                        </span>
                    </c:if>
                </c:forEach>
                </div>
            </div> 
            <div class="team-header">
                <label for="progress"><h2>${team.name}</h2></label>
                <progress id="progress" value="${team.perDone}" max="100">${team.perDone}%</progress>
            </div> 
            <div>
                <p>${team.description}</p>   
                <fieldset>
                <legend>Úkoly</legend>
                <div>
                <table class="tasks">
                    <c:forEach items="${team.taskList}" var="task">
                    <tr>
                        <td>
                            <form action="Service" method="post">
                                <input type="hidden" name="idtask" value="${task.id}">
                                <input type="hidden" name="from" value="team">
                                <input type="hidden" name="idteam" value="${team.id}">
                                <input type="hidden" name="action" value="completetask">
                                <input type="image" src=  "${(task.isComplete)? "checks/check.png" : "checks/nocheck.png"}" alt="Submit" height="15pt"/>
                            </form>
                        </td>
                        <td>${task.name}</td>
                        <td><fmt:formatDate value="${task.date}" pattern="dd.MM.yyyy"/></td>
                        <td><fmt:formatDate type="time" timeStyle="short" value="${task.time}"/></td>
                        <td>
                            <c:forEach items="${task.workerList}" var="worker">
                                <div class="tooltip">
                                    ${worker.initials}
                                    <span class="tooltip-text">
                                        ${worker.name} ${worker.surname}<br>
                                        ${worker.email}
                                    </span>
                                </div>
                            </c:forEach>
                        </td>
                        <td>${task.description}</td>
                        <td>
                        <form action="Service" method="post">
                            <input type="hidden" name="idtask" value="${task.id}" readonly>
                            <input type="hidden" name="action" value="viewtask" readonly>
                            <input class="view-button" type="submit" value="Zobrazit">
                        </form>
                        </td>
                    </tr>
                    </c:forEach>
                </table>
                    <div>
                        <form action="Service" method="post">
                            <input type="hidden" name="action" value="toaddtask" readonly/>
                            <input type="hidden" name="idteam" value="${team.id}" readonly/>
                            <input class="button-like align-right dark-blue" type="submit" value="Nový úkol">
                        </form>
                    </div>
                </fieldset>
                 
                            
                <fieldset>
                <legend>Členové</legend>
                <table class="members">
                    <c:forEach items="${team.memberList}" var="member">
                    <tr>
                        <c:if test="${team.isManager}">
                            <td>
                                <form action="Service" method="post">
                                    <input type="hidden" name="action" value="removemem"/>
                                    <input type="hidden" name="iduser" value="${member.id}"/>
                                    <input type="hidden" name="idteam" value="${team.id}"/>
                                    <input class="mana-button" type="submit" value="X"/>
                                </form>
                            </td>
                            <td> 
                                <form action="Service" method="post">
                                    <input type="hidden" name="action" value="tomana"/>
                                    <input type="hidden" name="iduser" value="${member.id}"/>
                                    <input type="hidden" name="idteam" value="${team.id}"/>
                                    <input class="mana-button" type="submit" value="Nastavit správce" ${(member.isManager) ? "disabled" : ""}/>
                                </form>
                            </td>
                            <td>
                                <form action="Service" method="post">
                                    <input type="hidden" name="action" value="tomember"/>
                                    <input type="hidden" name="iduser" value="${member.id}"/>
                                    <input type="hidden" name="idteam" value="${team.id}"/>
                                    <input class="mana-button" type="submit" value="Odebrat správce" ${(member.isManager) ? "" : "disabled"}/>
                                </form>
                            </td>
                            
                        </c:if>
                        <td>
                            <label for="progress">${member.name} ${member.surname}</label>
                        </td>
                        <td>
                            <progress id="progress" value="${member.tasksPer}" max="100">
                                ${member.tasksPer}%
                            </progress>
                        </td>
                        <td>
                            Splněno: ${member.tasksDone}/${member.tasksCount}
                        </td>
                    </tr>
                    </c:forEach>
                </table>
                <c:if test="${team.isManager}">
                    <a class="button-like" href="Service?action=toaddmember&idteam=${team.id}">Přidat člena</a>
                </c:if>
                    
                </fieldset>
                        
                <form class="align-center" action="Service" method="post">
                    <input type="hidden" name="action" value="leaveteam"/>
                    <input type="hidden" name="idteam" value="${team.id}"/>
                    <input class="button-like dark-blue align-center" type="submit" value="Opustit tým"/>
                </form>             
            </div>
        </div>
    </div>
    </body>
</html>
