package cz.vsb.ekf.koj.teamtaskmanager;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "Service", urlPatterns = {"/Service"})
public class Service extends HttpServlet {

    Connection conn = null;

    // <editor-fold defaultstate="collapsed" desc="init() and destroy() methods">
    @Override
    public void init() throws ServletException {
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            conn = DriverManager.getConnection("jdbc:derby://localhost:1527/TeamTasks", "ad", "ad");

            System.out.println("Connected to database");
        } catch (ClassNotFoundException | SQLException ex) {
            System.err.println("Error occured: " + ex);
        }
    }

    @Override
    public void destroy() {
        try {
            conn.close();
        } catch (SQLException ex) {
            System.err.println("Error occured: " + ex);
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="doGet and doPost with loads of if elses. (String switch with JDK7 and higher?)">
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = (String) request.getParameter("action");
        // for every action below - need session
        HttpSession session = request.getSession(false);
        if (session == null) {
            request.setAttribute("message", Message.SESSION_TIMEOUT);
            handler(request, response, "login.jsp");
            return;
        }
        synchronized (session) {
            User user = (User) session.getAttribute("user");
            if (action.equals("main")) {
                mainHandler(request, response, user);
            } else if (action.equals("teams")) {
                viewTeamsHandler(request, response, user);
            } else if (action.equals("profile")) {
                profileHandler(request, response, user);
            } else if (action.equals("logout")) {
                logoutHandler(request, response, session);
            } else if (action.equals("toaddmember")) {
                request.setAttribute("idteam", request.getParameter("idteam"));
                handler(request, response, "addmem.jsp");
            } else {
                request.setAttribute("message", Message.ERROR_UNKNOWN);
                mainHandler(request, response, user);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        System.out.println("------- Action is: " + action + " -------");
        if (action == null) {
            request.setAttribute("message", Message.ERROR_UNKNOWN);
            handler(request, response, "index.jsp");
        } // ----------- forms actions --------------
        else if (action.equals("login")) {
            System.out.println("User is trying to log in.");
            loginHandler(request, response);
        } else if (action.equals("registration")) {
            registrationHandler(request, response);
        } // ----- now user has to be logged in -----
        HttpSession session = request.getSession(false);
        if (session == null) {
            request.setAttribute("message", Message.SESSION_TIMEOUT);
            handler(request, response, "login.jsp");
            return;
        }
        synchronized (session) {
            User user = (User) session.getAttribute("user");
            if (action.equals("newteam")) {
                createTeamHandler(request, response, user);
            } else if (action.equals("main")) {
                mainHandler(request, response, user);
            } else if (action.equals("viewteam")) {
                int id = Integer.parseInt(request.getParameter("idteam"));
                viewTeamHandler(request, response, id);
            } else if (action.equals("viewtask")) {
                int idTask = Integer.parseInt(request.getParameter("idtask"));
                viewTaskHandler(request, response, user, idTask);
            } else if (action.equals("newtask")) {
                newTaskHandler(request, response, user);
            } else if (action.equals("addmember")) {
                addMemberHandler(request, response, user);
            } else if (action.equals("toaddtask")) {
                int idTeam = Integer.parseInt(request.getParameter("idteam"));
                request.setAttribute("idteam", idTeam);
                handler(request, response, "addtask.jsp");
            } else if (action.equals("profileedit")) {
                editProfileHandler(request, response, user);
            } else if (action.equals("addsubtask")) {
                addSubtaskHandler(request, response, user);
            } else if (action.equals("deletesub")) {
                deleteSubtaskHandler(request, response, user);
            } else if (action.equals("completetask")) {
                changeCompleteTaskHandler(request, response, user);
            } else if (action.equals("completesub")) {
                completeSubTaskHandler(request, response, user);
            } else if (action.equals("leaveteam")) {
                leaveTeamHandler(request, response, user);
            } else if (action.equals("gettask")) {
                getTaskHandler(request, response, user);
            } else if (action.equals("deletealert")) {
                deleteAlertHandler(request, response, user);
            } else if (action.equals("assigntask")) {
                assignTaskHandler(request, response, user);
            } else if (action.equals("cancelassign")) {
                cancelAssignHandler(request, response, user);
            } else if (action.equals("deletetask")) {
                deleteTaskHandler(request, response);
            } else if (action.equals("toedittask")) {
                request.setAttribute("task", findTask(Integer.parseInt(request.getParameter("idtask"))));
                handler(request, response, "edittask.jsp");
            } else if (action.equals("edittask")) {
                editTaskHandler(request, response, user);
            } else if (action.equals("removemem")) {
                deleteMemberHandler(request, response);
            } else if (action.equals("tomana")) {
                changeRightsHandler(request, response, true);
            } else if (action.equals("tomember")) {
                changeRightsHandler(request, response, false);
            } else {
                
            }
        }
    }
    // </editor-fold>

    private void handler(HttpServletRequest request, HttpServletResponse response, String page) {
        try {
            request.getRequestDispatcher(page).forward(request, response);
        } catch (ServletException | IOException ex) {
            System.err.println("Errror occured: " + ex);
        }
    }

    private void registrationHandler(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("User wants to create a new account:");
        String name = request.getParameter("name");
        String surname = request.getParameter("surname");
        String login = request.getParameter("login");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String page = "registration.jsp";
        System.out.println("    Validating inputs..");
        if (name == null || surname == null || login == null || email == null || password == null) {
            request.setAttribute("message", Message.MISS_INPUT);
            System.err.println("    Some input is empty.");
        } else if (!isLoginFree(login)) {
            request.setAttribute("message", Message.NAME_EXISTS);
            System.err.println("    Login already exists.");
        } else {
            try {
                PreparedStatement stat = conn.prepareStatement("INSERT INTO \"USER\"(SURNAME, LOGIN, EMAIL, PASSWORD, F_NAME) "
                        + "values(?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
                stat.setString(1, surname);
                stat.setString(2, login);
                stat.setString(3, email);
                stat.setString(4, password);
                stat.setString(5, name);
                int count = stat.executeUpdate();
                ResultSet key = stat.getGeneratedKeys();
                if (key.next()) {
                    int id = key.getInt(1);
                    User user = new User(id, name, surname, login, email);
                    System.out.println("New user added, affected rows: " + count);
                    request.setAttribute("user", user);
                    page = "login.jsp";
                }
            } catch (SQLException ex) {
                System.err.println("Errror occured while preparing statement for registration: " + ex);
            }
        }
        handler(request, response, page);
    }

    private void loginHandler(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("User is trying to login.");
        String login = request.getParameter("login");
        String password = request.getParameter("password");
        try {
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM \"USER\" WHERE LOGIN=?");
            statement.setString(1, login);
            ResultSet rs = statement.executeQuery();
            if (!rs.next()) {
                System.out.println("    Incorrect login name");
                request.setAttribute("message", Message.NAME_NOT_EXIST);
            } else if (rs.getString("PASSWORD").equals(password)) {
                User user = new User();
                user.setId(Integer.parseInt(rs.getString("ID")));
                user.setName(rs.getString("F_NAME"));
                user.setSurname(rs.getString("SURNAME"));
                user.setEmail(rs.getString("EMAIL"));
                user.setLogin(rs.getString("LOGIN"));
                HttpSession session = request.getSession();
                session.setAttribute("user", user);
                mainHandler(request, response, user);
                return;
            } else if (!rs.getString("PASSWORD").equals(password)) {
                System.out.println("    Incorrect password");
                request.setAttribute("message", Message.PASSWORD_INCORRECT);
            } else {
                System.out.println("    Unknown error");
                request.setAttribute("message", Message.ERROR_UNKNOWN);
            }
            handler(request, response, "login.jsp");
        } catch (SQLException ex) {
            System.err.println("Errror occured while preparing statement: " + ex);
        }
    }

    private void logoutHandler(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        System.out.println("User wants to logout");
        synchronized (session) {
            session.invalidate();
        }
        request.setAttribute("message", Message.USER_LOGOUT);
        handler(request, response, "index.jsp");
    }

    private void mainHandler(HttpServletRequest request, HttpServletResponse response, User user) {
        System.out.println("---- viewing main page ----");
        List<Alert> alertList = getAlertList(user);
        List<Team> teamList = getTeamList(user);
        request.setAttribute("alerts", alertList);
        request.setAttribute("teams", teamList);
        handler(request, response, "main.jsp");
    }

    private void createTeamHandler(HttpServletRequest request, HttpServletResponse response, User user) {
        System.out.println("User is trying to create a new team.");
        try {
            String name = request.getParameter("name");
            if (name == null) {
                System.out.println("Team name input is empty.");
                request.setAttribute("message", Message.MISS_TEAM_NAME);
                handler(request, response, "addteam.jsp");
            } else if (name.length() > 50) {
                request.setAttribute("message", "Maximální délka názvu je 50 znaků.");
                handler(request, response, "addteam.jsp");
            } else {
                PreparedStatement statement = conn.prepareStatement("INSERT INTO \"TEAM\"(TITLE,DESCRIPTION) VALUES(?,?)", Statement.RETURN_GENERATED_KEYS);
                statement.setString(1, name);
                statement.setString(2, request.getParameter("description"));
                int count = statement.executeUpdate();
                ResultSet key = statement.getGeneratedKeys();
                if (key.next()) {
                    int id = key.getInt(1);
                    addMember(id, user.getId(), true);
                    System.out.println("New team was created, id: " + id + ", affected rows: " + count);
                    request.setAttribute("id", id);
                    viewTeamHandler(request, response, id);
                }
            }
        } catch (SQLException ex) {
            System.err.println("Errror occured while preparing statement: " + ex);
        }
        request.setAttribute("message", Message.ERROR_UNKNOWN);
        mainHandler(request, response, user);
    }

    private void viewTeamHandler(HttpServletRequest request, HttpServletResponse response, int id) {
        Team team = getTeam(id);
        request.setAttribute("team", team);
        handler(request, response, "team.jsp");
    }

    private void viewTeamsHandler(HttpServletRequest request, HttpServletResponse response, User user) {
        request.setAttribute("teams", getTeamList(user));
        handler(request, response, "teams.jsp");
    }

    private void newTaskHandler(HttpServletRequest request, HttpServletResponse response, User user) {
        System.out.println("User is trying to create new task.");
        try {
            int idTeam = Integer.parseInt(request.getParameter("idteam"));
            System.out.println("User is adding a new task to team with id: " + idTeam);
            if (getMemberID(idTeam, user.getId()) == 0) {
                request.setAttribute("message", Message.CANNOT_VIEW_TASK);
                mainHandler(request, response, user);
                return;
            }
            String title = request.getParameter("title");
            String description = request.getParameter("description");
            String dateS = request.getParameter("date");
            String timeS = request.getParameter("time");
            Date date = null;
            Time time = null;
            if (dateS != null && !dateS.trim().equals("") && !dateS.isEmpty()) {
            date = Date.valueOf(dateS);
            }
            if (timeS != null && !timeS.trim().equals("") && !timeS.isEmpty()) {
                time = Time.valueOf(LocalTime.parse(timeS));
            }
            if (title == null) {
                request.setAttribute("message", Message.MISS_TASK_TITLE);
                handler(request, response, "addtask.jsp");
                return;
            }
            PreparedStatement stat = conn.prepareStatement(
                    "INSERT INTO \"TASK\"(TITLE,DESCRIPTION,TO_DATE,TO_TIME,ID_CREATOR,CREATE_TIME,ID_TEAM) "
                    + "VALUES (?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            stat.setString(1, title);
            stat.setString(2, description);
            stat.setDate(3, date);
            stat.setTime(4, time);
            stat.setInt(5, user.getId());
            stat.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
            stat.setInt(7, idTeam);
            int count = stat.executeUpdate();
            ResultSet key = stat.getGeneratedKeys();
            if (key.next()) {
                int idTask = key.getInt(1);
                System.out.println("New task was added to team with id: " + idTeam + ", inserted rows: " + count);
                System.out.println("Task info: id: " + idTask + " title: " + title + " desc: " + description + " date: " + date + " time: " + time);
                viewTaskHandler(request, response, user, idTask);
                stat = conn.prepareStatement("SELECT ID_USER FROM \"MEMBER\" WHERE ID_TEAM=?");
                stat.setInt(1, idTeam);
                ResultSet rs = stat.executeQuery();
                while (rs.next()) {
                    int idMember = rs.getInt("ID_USER");
                    if (idMember != user.getId()) {
                        insertAlert(AlertType.NEW_TASK, idMember, idTeam);
                    }
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("    id of team cannot be read. Value is: " + request.getParameter("id"));
            request.setAttribute("message", Message.ERROR_UNKNOWN);
            handler(request, response, "main.jsp");
        } catch (SQLException ex) {
            System.out.println("Error while inserting new team to DB: " + ex);
        }
    }

    private void viewTaskHandler(HttpServletRequest request, HttpServletResponse response, User user, int idTask) {
        try {
            System.out.println("User is trying to view task with id: " + idTask);
            PreparedStatement stat = conn.prepareStatement("SELECT * FROM \"TASK\" "
                    + "WHERE ID=?");
            stat.setInt(1, idTask);
            ResultSet rs = stat.executeQuery();
            if (rs.next()) {
                int idTeam = rs.getInt("ID_TEAM");
                // making sure that user is viewing task only in his/her team (member)
                if (getMemberID(idTeam, user.getId()) == 0) {
                    request.setAttribute("message", Message.CANNOT_VIEW_TASK);
                    mainHandler(request, response, user);
                    return;
                }
                Task task = new Task(idTask, idTeam, rs.getString("TITLE"), rs.getString("DESCRIPTION"), rs.getDate("TO_DATE"),
                        rs.getTime("TO_TIME"), rs.getInt("ID_CREATOR"), rs.getBoolean("IS_COMPLETE"));
                task.setWorkerList(getWorkerSet(idTask));
                task.setSubTasks(getSubtaskList(idTask));
                Team team = getTeam(idTeam);
                request.setAttribute("ismanager", team.isIsManager());
                System.out.println("    IS USER A MANAGER: " + team.isIsManager());
                request.setAttribute("members", team.getMemberList());
                request.setAttribute("idteam", team.getId());
                request.setAttribute("task", task);
                handler(request, response, "task.jsp");
                return;
            } else { // task was deleted, user messing with hidden inputs, etc...
                request.setAttribute("message", Message.CANNOT_VIEW_TASK);
                mainHandler(request, response, user);
            }
        } catch (SQLException ex) {
            System.err.println("Errror occured while preparing statement for task view: " + ex);
        }
        mainHandler(request, response, user);
    }

    private void getTaskHandler(HttpServletRequest request, HttpServletResponse response, User user) {
        int idTask = Integer.parseInt(request.getParameter("idtask"));
        System.out.println("User is trying to get task with id: " + idTask);
        try {
            PreparedStatement stat = conn.prepareStatement("SELECT ID_TEAM FROM \"TASK\" WHERE ID=?");
            stat.setInt(1, idTask);
            ResultSet rs = stat.executeQuery();
            if (rs.next()) {
                int idTeam = rs.getInt(1);
                int idMem = getMemberID(idTeam, user.getId());
                if (idMem == 0) {
                    request.setAttribute("message", Message.ERROR_UNKNOWN);
                    return;
                } else {
                    stat = conn.prepareStatement("SELECT ID FROM \"ASSIGMENT\" WHERE ID_MEMBER=? AND ID_TASK=?");
                    stat.setInt(1, idMem);
                    stat.setInt(2, idTask);
                    rs = stat.executeQuery();
                    if (rs.next()) {// user already have task assigned
                        request.setAttribute("message", Message.CANNOT_ASSIGN);
                    } else {
                        PreparedStatement stat2 = conn.prepareStatement("INSERT INTO \"ASSIGMENT\"(ID_MEMBER, ID_TASK) VALUES (?,?)");
                        stat2.setInt(1, idMem);
                        stat2.setInt(2, idTask);
                        int count = stat2.executeUpdate();
                        System.out.println("Task assigned, inserted rows: " + count);
                    }
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error while user tried to get task: " + idTask + " to user. " + ex);
        }
        viewTaskHandler(request, response, user, idTask);
    }

    private void assignTaskHandler(HttpServletRequest request, HttpServletResponse response, User user) {
        //TODO someday find if user getting the task is part of the team (member), task is in team
        int idTask = Integer.parseInt(request.getParameter("idtask"));
        int idTeam = Integer.parseInt(request.getParameter("idteam"));
        int idMem = getMemberID(idTeam, Integer.parseInt(request.getParameter("idmem")));
        try {
            PreparedStatement stat = conn.prepareStatement("INSERT INTO \"ASSIGMENT\"(ID_MEMBER,ID_TASK) VALUES (?,?)");
            stat.setInt(1, idMem);
            stat.setInt(2, idTask);
            int count = stat.executeUpdate();
            System.out.println("Task: " + idTask + " assigned to member: " + idMem + ", inserted rows: " + count);
            insertAlert(AlertType.NEW_ASSIGNMENT, Integer.parseInt(request.getParameter("idmem")), idTeam);
            request.setAttribute("message", Message.TASK_ASSIGNED);
        } catch (SQLException ex) {
            System.err.println("Error while assigning task: " + idTask + " to member: " + idMem);
        }
        viewTaskHandler(request, response, user, idTask);
    }

    private void cancelAssignHandler(HttpServletRequest request, HttpServletResponse response, User user) {
        int idUser = Integer.parseInt(request.getParameter("idworker"));
        int idTask = Integer.parseInt(request.getParameter("idtask"));
        int idTeam = Integer.parseInt(request.getParameter("idteam"));
        if (!deleteAssignment(idUser, idTask, idTeam)) {
            request.setAttribute("message", Message.ERROR_UNKNOWN);
        }
        insertAlert(AlertType.TASK_REMOVED, idUser, idTeam);
        viewTaskHandler(request, response, user, idTask);
    }

    private void changeCompleteTaskHandler(HttpServletRequest request, HttpServletResponse response, User user) {
        //TODO someday: find if user is member, task is in team
        int idTask = Integer.parseInt(request.getParameter("idtask"));
        PreparedStatement stat;
        boolean isComplete = false;
        try {
            stat = conn.prepareStatement("SELECT IS_COMPLETE FROM \"TASK\" WHERE ID=?");
            stat.setInt(1, idTask);
            ResultSet rs = stat.executeQuery();
            if (rs.next()) {
                isComplete = rs.getBoolean(1);
            }
            stat = conn.prepareStatement("UPDATE \"TASK\" SET IS_COMPLETE=" + !isComplete + " WHERE ID=?");
            stat.setInt(1, idTask);
            int count = stat.executeUpdate();
            System.out.println("Task: " + idTask + " is now completed: " + !isComplete + ", updated rows: " + count);
            Set<User> workers = getWorkerSet(idTask);
            if (!isComplete) {
                for (User u : workers) {
                    insertAlert(AlertType.TASK_COMPLETED, u.getId(), findTask(idTask).getIdTeam());
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error while updating task: " + idTask + " to completed. " + ex);
        }
        String toPage = request.getParameter("from");
        System.out.println("    -->sending user back to: " + toPage);
        if (toPage.equals("team")) {
            viewTeamHandler(request, response, Integer.parseInt(request.getParameter("idteam")));
            return;
        } else if (toPage.equals("task")) {
            viewTaskHandler(request, response, user, idTask);
            return;
        } else if (!toPage.equals("main")) {
            request.setAttribute("message", Message.ERROR_UNKNOWN);
        }
        mainHandler(request, response, user);
    }

    private void addMemberHandler(HttpServletRequest request, HttpServletResponse response, User user) {
        String identity = request.getParameter("identity");
        int idTeam = Integer.parseInt(request.getParameter("idteam"));
        System.out.println("User is trying to add new member by: " + identity + ", to team: " + idTeam);
        if (getMemberID(idTeam, user.getId()) == 0) { //not a part of the team is trying to add new mamber
            request.setAttribute("message", Message.ERROR_UNKNOWN);
            mainHandler(request, response, user);
            return;
        }
        if (identity == null) {
            request.setAttribute("message", Message.MISS_INPUT);
            handler(request, response, "addmem.jsp");
            return;
        }
        boolean isManager = false;
        String role = request.getParameter("role");
        if (role.equals("manager")) {
            isManager = true;
        }
        try {
            int id = Integer.parseInt(identity);
            if (addMember(idTeam, id, isManager)) {
                insertAlert(AlertType.ADDED_TO_TEAM, id, idTeam);
            } else {
                request.setAttribute("message", Message.MEM_NOT_FOUND_TEAM);
                handler(request, response, "addmem.jsp");
            }
        } catch (NumberFormatException ex) {
            // identification is not an integer .. id
            int id = fintIdMemByEmail(identity);
            if (id <= 0) {
                if (id == 0) {
                    request.setAttribute("message", Message.MEMBER_NOT_FOUND);
                    handler(request, response, "addmem.jsp");
                } else if (id == -1) {
                    request.setAttribute("message", Message.EMAIL_INCONCLUSIVE);
                    handler(request, response, "addmem.jsp");
                }
            } else {
                addMember(idTeam, id, isManager);
                insertAlert(AlertType.ADDED_TO_TEAM, id, idTeam);
            }
        }
        viewTeamHandler(request, response, idTeam);
    }

    private void profileHandler(HttpServletRequest request, HttpServletResponse response, User user) {
        // profile pic, securing the password ... maybe later :)
        System.out.println("User wants to view his profile.");
        try {
            Statement stat = conn.createStatement();
            ResultSet rs = stat.executeQuery("SELECT PASSWORD FROM \"USER\" WHERE ID=" + user.getId());
            if (rs.next()) {
                request.setAttribute("password", rs.getString(1));
                handler(request, response, "profile.jsp");
                return;
            }
        } catch (SQLException ex) {
            System.err.println("Error while selecting password from USER. " + ex);
        }
        System.out.println("Unknown error while searching for user's password");
        request.setAttribute("message", Message.ERROR_UNKNOWN);
        mainHandler(request, response, user);
    }

    // <editor-fold defaultstate="collapse" desc="editProfileHandler method (and i am sorry)">
    private void editProfileHandler(HttpServletRequest request, HttpServletResponse response, User user) {
        String submit = request.getParameter("submit");
        if (submit.equals("Smazat profil")) {
            System.out.println("User wants to delete profile");
            List<Team> teamList = getTeamList(user);
            for (Team t : teamList) {
                int idTeam = t.getId();
                if (deleteMembership(user, idTeam)) {
                    for (Task task : t.getTaskList()) {
                        if (!deleteAssignment(user, task)) {
                            request.setAttribute("message", Message.ERROR_UNKNOWN);
                            profileHandler(request, response, user);
                            return;
                        }
                    }
                } else {
                    request.setAttribute("message", Message.USER_CANNOT_BE_DELETED);
                    profileHandler(request, response, user);
                    return;
                }
            } // every assigmnet and membership was deleted
            if (deleteUser(user)) {
                request.setAttribute("message", Message.USER_DELETED);
                handler(request, response, "index.jsp");
                return;
            } else {
                request.setAttribute("message", Message.ERROR_UNKNOWN);
            }
        } else if (submit.equals("Uložit úpravy")) {
            System.out.println("User is editing profile.");
            String name = request.getParameter("name");
            String surname = request.getParameter("surname");
            String login = request.getParameter("login");
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            if (name == null || surname == null || login == null || email == null || password == null) {
                request.setAttribute("message", Message.MISS_INPUT);
                System.err.println("    ..Some input is empty.");
            } else if (!(login.equals(user.getLogin())) && !isLoginFree(login)) {
                request.setAttribute("message", Message.NAME_EXISTS);
                System.err.println("    ..Login already exists.");
            } else {
                try {
                    PreparedStatement stat = conn.prepareStatement("UPDATE \"USER\" SET SURNAME=?, LOGIN=?, EMAIL=?, PASSWORD=?, F_NAME=? "
                            + "WHERE ID=?");
                    stat.setString(1, surname);
                    stat.setString(2, login);
                    stat.setString(3, email);
                    stat.setString(4, password);
                    stat.setString(5, name);
                    stat.setInt(6, user.getId());
                    System.out.println(" .. profile edited, updated rows: " + stat.executeUpdate());
                    User userEdited = new User(user.getId(), name, surname, login, email);
                    HttpSession session = request.getSession(false);
                    if (session == null) {
                        request.setAttribute("message", Message.SESSION_TIMEOUT);
                        handler(request, response, "login.jsp");
                        return;
                    }
                    synchronized (session) {
                        session.setAttribute("user", userEdited);
                    }
                } catch (SQLException ex) {
                    System.err.println("Error while updating user table. " + ex);
                }
            }
        } else {
            request.setAttribute("message", Message.ERROR_UNKNOWN);
        }
        profileHandler(request, response, user);
    }
    // </editor-fold>

    private void editTaskHandler(HttpServletRequest request, HttpServletResponse response, User user) {
        int idTask = Integer.parseInt(request.getParameter("idtask"));
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String dateS = request.getParameter("date");
        String timeS = request.getParameter("time");
        Time time = null;
        Date date = null;
        System.out.println("User is trying to edit task: " + idTask);
        if (dateS != null && !dateS.trim().equals("") && !dateS.isEmpty()) {
            date = Date.valueOf(dateS);
        }
        if (timeS != null && !timeS.trim().equals("") && !timeS.isEmpty()) {
            time = Time.valueOf(LocalTime.parse(timeS));
        }
        if (title == null) {
            request.setAttribute("message", Message.MISS_TASK_TITLE);
            handler(request, response, "edittask.jsp");
            return;
        }
        try {
            PreparedStatement stat = conn.prepareStatement("UPDATE \"TASK\" SET "
                    + "TITLE=?, DESCRIPTION=? ,TO_DATE=?, TO_TIME=? WHERE ID=?");
            stat.setString(1, title);
            stat.setString(2, description);
            stat.setDate(3, date);
            stat.setTime(4, time);
            stat.setInt(5, idTask);
            int count = stat.executeUpdate();
            System.out.println("Task updated, affected rows: " + count);
        } catch (SQLException ex) {
            System.err.println("Error while updating task: " + idTask + ". " + ex);
        }
        viewTaskHandler(request, response, user, idTask);
    }

    private void addSubtaskHandler(HttpServletRequest request, HttpServletResponse response, User user) {
        System.out.println("Trying to add new subtask");
        int idTask = Integer.parseInt(request.getParameter("idtask"));
        String title = request.getParameter("title");
        try {
            PreparedStatement stat = conn.prepareStatement("INSERT INTO \"SUB_TASK\"(ID_TASK,IS_COMPLETE,TITLE) VALUES (?,?,?)");
            stat.setInt(1, idTask);
            stat.setBoolean(2, false);
            stat.setString(3, title);
            int count = stat.executeUpdate();
            System.out.println("New subtask: " + title + " added to task: " + idTask + " inserted rows: " + count);
        } catch (SQLException ex) {
            System.err.println("Error while inserting new sub task to task: " + idTask + ", " + ex);
        }
        viewTaskHandler(request, response, user, idTask);
    }

    private void completeSubTaskHandler(HttpServletRequest request, HttpServletResponse response, User user) {
        int idSub = Integer.parseInt(request.getParameter("idsub"));
        int idTask = Integer.parseInt(request.getParameter("idtask"));
        PreparedStatement stat;
        boolean isComplete = false;
        try {
            stat = conn.prepareStatement("SELECT IS_COMPLETE FROM \"SUB_TASK\" WHERE ID=?");
            stat.setInt(1, idSub);
            ResultSet rs = stat.executeQuery();
            if (rs.next()) {
                isComplete = rs.getBoolean(1);
            }
            stat = conn.prepareStatement("UPDATE \"SUB_TASK\" SET IS_COMPLETE=" + !isComplete + " WHERE ID=?");
            stat.setInt(1, idSub);
            int count = stat.executeUpdate();
            System.out.println("Subtask: " + idSub + " now completed?: " + !isComplete  + ", updated rows: " + count);
        } catch (SQLException ex) {
            System.err.println("Error while updating subtask: " + idSub + ". " + ex);
        }
        viewTaskHandler(request, response, user, idTask);
    }

    private void deleteSubtaskHandler(HttpServletRequest request, HttpServletResponse response, User user) {
        int idTask = Integer.parseInt(request.getParameter("idtask"));
        int idSub = Integer.parseInt(request.getParameter("idsub"));
        System.out.println("Trying to delete subtask with id: " + idSub);
        deleteSubtask(idSub);
        viewTaskHandler(request, response, user, idTask);
    }

    private void deleteAlertHandler(HttpServletRequest request, HttpServletResponse response, User user) {
        int idAlert = Integer.parseInt(request.getParameter("idalert"));
        System.out.println("Trying to delete alert: " + idAlert);
        deleteAlert(idAlert);
        mainHandler(request, response, user);
    }

    private void deleteMemberHandler(HttpServletRequest request, HttpServletResponse response) {
        int idUser = Integer.parseInt(request.getParameter("iduser"));
        int idTeam = Integer.parseInt(request.getParameter("idteam"));
        int idMem = getMemberID(idTeam, idUser);
        System.out.println("Trying to remove member: " + idMem + " from team: " + idTeam);
        try {
            PreparedStatement stat = conn.prepareStatement("DELETE FROM \"ASSIGMENT\" WHERE ID_MEMBER=?");
            stat.setInt(1, idMem);
            int count = stat.executeUpdate();
            System.out.println("    ..all assignments (" + count + ") from member: " + idMem + " were deleted.");
            stat = conn.prepareStatement("DELETE FROM \"MEMBER\" WHERE ID=?");
            stat.setInt(1, idMem);
            count = stat.executeUpdate();
            System.out.println("    ..Member removed form team, rows removed: " + count);
            insertAlert(AlertType.REMOVED_FROM_TEAM, idUser, idTeam);
        } catch (SQLException ex) {
            System.err.println("Error while deleting member: " + idMem);
        }
        viewTeamHandler(request, response, idTeam);
    }

    private void deleteTaskHandler(HttpServletRequest request, HttpServletResponse response) {
        int idTask = Integer.parseInt(request.getParameter("idtask"));
        int idTeam = Integer.parseInt(request.getParameter("idteam"));
        System.out.println("Trying to delete task: " + idTask);
        PreparedStatement stat;
        try {
            stat = conn.prepareStatement("DELETE FROM \"ASSIGMENT\" WHERE ID_TASK=?");
            stat.setInt(1, idTask);
            int count = stat.executeUpdate();
            System.out.println("    ..all assigments (" + count + ") for task deleted");
            stat = conn.prepareStatement("DELETE FROM \"TASK\" WHERE ID=?");
            stat.setInt(1, idTask);
            count = stat.executeUpdate();
            System.out.println("    ..task deleted, rows: " + count);
        } catch (SQLException ex) {
            System.err.println("Error while deleting task: " + idTask + ". " + ex);
        }
        request.setAttribute("message", Message.TASK_DELETED);
        viewTeamHandler(request, response, idTeam);
    }

    private void changeRightsHandler(HttpServletRequest request, HttpServletResponse response, boolean manager) {
        int idUser = Integer.parseInt(request.getParameter("iduser"));
        int idTeam = Integer.parseInt(request.getParameter("idteam"));
        int idMem = getMemberID(idTeam, idUser);
        try {
            PreparedStatement stat = conn.prepareStatement("UPDATE \"MEMBER\" SET MANAGER=" + manager + " WHERE ID=?");
            stat.setInt(1, idMem);
            int count = stat.executeUpdate();
            System.out.println("Member: " + idMem + " is now manager? " + manager + ". Updated rows: " + count);
            insertAlert(AlertType.RIGHTS_CHANGE, idUser, idTeam);
        } catch (SQLException ex) {
            System.err.println("Error while updating member rights: " + ex);
        }
        viewTeamHandler(request, response, idTeam);
    }
    
    private void leaveTeamHandler(HttpServletRequest request, HttpServletResponse response, User user) {
        int idTeam = Integer.parseInt(request.getParameter("idteam"));
        Team team = getTeam(idTeam);
        if (deleteMembership(user, idTeam)) {
            for (Task task : team.getTaskList()) {
                if (!deleteAssignment(user, task)) {
                    request.setAttribute("message", Message.ERROR_UNKNOWN);
                    mainHandler(request, response, user);
                    return;
                }
            }
        } else {
            request.setAttribute("message", Message.USER_CANNOT_LEAVE);
            viewTeamHandler(request, response, idTeam);
            return;
        }
        mainHandler(request, response, user);
    }

    // -------------------------------- helpers -----------------------------
    // <editor-fold defaultstate="collapsed" desc="Helpers: getter, find smt, is smth, ...">
    private boolean addMember(int idTeam, int idUser, boolean isManager) {
        try {
            if (getMemberID(idTeam, idUser) != 0) { //already part of the team
                System.out.println("User: " + idUser + " already part of the team: " + idTeam);
                return false;
            }
            if (!isUserInDB(idUser)) {
                System.out.println("User with id: " + idUser + " does not exist");
                return false;
            }
            PreparedStatement statement = conn.prepareStatement("INSERT INTO \"MEMBER\" (ID_TEAM,ID_USER,MANAGER) values(?,?,?)");
            statement.setInt(1, idTeam);
            statement.setInt(2, idUser);
            statement.setBoolean(3, isManager);
            int count = statement.executeUpdate();
            System.out.println("New member (id: " + idUser + ") was added to team with id: " + idTeam);
            System.out.println("Number of affected rows: " + count);
            return true;
        } catch (SQLException ex) {
            System.err.println("Errror occured while preparing statement: " + ex);
        }
        return false;
    }

    private void insertAlert(AlertType alertType, int idUser, int idTeam) {
        try {
            System.out.println("Creating new alert for user: " + idUser + ", type is: " + alertType);
            Statement stat = conn.createStatement();
            stat.executeUpdate("INSERT INTO \"ALERT\"(ID_USER,TYPE,ID_TEAM) "
                    + "values(" + idUser + "," + alertType.getId() + "," + idTeam + ")");
            System.out.println("New alert: " + alertType + ", was added for user: " + idUser);
        } catch (SQLException ex) {
            System.err.println("Error while inserting new Alert. User id: " + idUser + "Team id: " + idTeam + "Error: " + ex);
        }
    }

    private int fintIdMemByEmail(String email) {
        int id = 0;
        try {
            PreparedStatement stat = conn.prepareStatement("SELECT ID FROM \"USER\" WHERE EMAIL=?");
            stat.setString(1, email);
            ResultSet rs = stat.executeQuery();
            if (rs.next()) {
                id = rs.getInt(1);
            } else { // noone was found
                System.out.println("No user with email: " + email + " was found.");
            }
            if (rs.next()) {
                id = -1; // more users were found
            }
        } catch (SQLException ex) {
            System.err.println("Error while selecting id from user table: " + ex);
        }
        return id;
    }

    private boolean isUserInDB(int idUser) {
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM \"USER\" WHERE ID=" + idUser);
            return rs.next();
        } catch (SQLException ex) {
            System.err.println("Error while searching for user. " + ex);
        }
        return false;
    }

    private boolean isLoginFree(String login) {
        try {
            System.out.println("Checking for unique login...");
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM \"USER\" WHERE LOGIN=?");
            statement.setString(1, login);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return false;
            }
        } catch (SQLException ex) {
            System.err.println("Errror occured while preparing statement: " + ex);
        }
        System.out.println("    login is unique.");
        return true;
    }

    private int getMemberID(int idTeam, int idUser) {
        try {
            PreparedStatement stat = conn.prepareStatement("SELECT ID FROM \"MEMBER\" "
                    + "WHERE ID_TEAM=? AND ID_USER=?");
            stat.setInt(1, idTeam);
            stat.setInt(2, idUser);
            ResultSet rs = stat.executeQuery();
            if (rs.next()) {
                return rs.getInt("ID");
            }
        } catch (SQLException ex) {
            System.err.println("Error occured while: getMemberID. " + ex);
        }
        return 0;
    }

    private List<Alert> getAlertList(User user) {
        List<Alert> list = new ArrayList<>();
        try {
            System.out.println("Getting alert list");
            PreparedStatement stat = conn.prepareStatement(
                    "SELECT * from \"ALERT\" "
                    + "INNER JOIN \"ALERT_TYPE\" ON \"ALERT\".TYPE = \"ALERT_TYPE\".ID "
                    + "WHERE ID_USER=?");
            stat.setInt(1, user.getId());
            ResultSet rs = stat.executeQuery();
            while (rs.next()) {
                AlertType aType = AlertType.values()[rs.getInt("TYPE") - 1];
                Alert alert = new Alert(rs.getInt("ID"), rs.getInt("ID_TEAM"), aType);
                list.add(alert);
            }
        } catch (SQLException ex) {
            System.err.println("Errror occured while preparing statement: " + ex);
        }
        return list;
    }

    private List<Team> getTeamList(User user) {
        List<Team> teamList = new ArrayList<>();
        try {
            System.out.println("Getting team list for main page or all teams page");
            PreparedStatement stat = conn.prepareStatement(
                    "SELECT ID_TEAM FROM \"MEMBER\" INNER JOIN \"TEAM\" "
                    + "ON \"MEMBER\".ID_TEAM=\"TEAM\".ID "
                    + "WHERE \"MEMBER\".ID_USER=?");
            stat.setInt(1, user.getId());
            ResultSet rs = stat.executeQuery();
            while (rs.next()) {
                int idTeam = rs.getInt("ID_TEAM");
                Team team = getTeam(idTeam);
                teamList.add(team);
            }
        } catch (SQLException ex) {
            System.err.println("Errror occured while getting team list " + ex);
        }
        return teamList;
    }

    private List<Task> getTaskList(int idTeam) {
        PreparedStatement stat;
        List<Task> taskList = new ArrayList<>();
        try {
            System.out.println("        ..getting task list for team with id: " + idTeam);
            stat = conn.prepareStatement("SELECT * FROM \"TASK\" WHERE ID_TEAM=?");
            stat.setInt(1, idTeam);
            ResultSet rs2 = stat.executeQuery();
            while (rs2.next()) {
                int idTask = rs2.getInt("ID");
                Task task = new Task(idTask, idTeam, rs2.getString("TITLE"),
                        rs2.getString("DESCRIPTION"), rs2.getDate("TO_DATE"), rs2.getTime("TO_TIME"),
                        rs2.getInt("ID_CREATOR"), rs2.getBoolean("IS_COMPLETE"));
                task.setWorkerList(getWorkerSet(idTask));
                taskList.add(task);
                System.out.println("           --> task (" + idTask + "): added to list");
            }
        } catch (SQLException ex) {
            System.err.println("Errror occured while preparing statement: " + ex);
        }
        return taskList;
    }

    private Set<User> getWorkerSet(int idTask) {
        Set<User> set = new HashSet<>();
        try {
            PreparedStatement stat = conn.prepareStatement("SELECT \"USER\".ID, F_NAME, SURNAME, EMAIL FROM \"ASSIGMENT\" "
                    + "INNER JOIN \"MEMBER\" ON \"ASSIGMENT\".ID_MEMBER=\"MEMBER\".ID INNER JOIN \"USER\" ON ID_USER=\"USER\".ID "
                    + "WHERE ID_TASK=?");
            stat.setInt(1, idTask);
            ResultSet rs = stat.executeQuery();
            while (rs.next()) {
                User user = new User(rs.getInt("ID"), rs.getString("F_NAME"),
                        rs.getString("SURNAME"), rs.getString("EMAIL"));
                set.add(user);
            }
            return set;
        } catch (SQLException ex) {
            System.err.println("Errror occured while preparing statement: " + ex);
        }
        return null;
    }

    private List<Member> getMemberList(int idTeam) {
        List<Member> list = new ArrayList<>();
        try {
            PreparedStatement stat = conn.prepareStatement("SELECT * FROM \"MEMBER\" "
                    + "INNER JOIN \"USER\" ON \"MEMBER\".ID_USER=\"USER\".ID "
                    + "WHERE ID_TEAM=?");
            stat.setInt(1, idTeam);
            ResultSet rs = stat.executeQuery();
            while (rs.next()) {
                int idUser = rs.getInt("ID_USER");
                Member member = new Member(idUser, rs.getString("F_NAME"),
                        rs.getString("SURNAME"), rs.getString("EMAIL"), rs.getBoolean("MANAGER"));
                String query = "SELECT COUNT(*) FROM \"ASSIGMENT\" INNER JOIN \"TASK\" ON \"ASSIGMENT\".ID_TASK=\"TASK\".ID "
                        + "WHERE ID_MEMBER=" + rs.getInt(1) + " AND ID_TEAM=" + idTeam;
                Statement st = conn.createStatement();
                ResultSet rsAssign = st.executeQuery(query);
                if (rsAssign.next()) {
                    member.setTasksCount(rsAssign.getInt(1));
                }
                rsAssign = st.executeQuery(query + " AND IS_COMPLETE");
                if (rsAssign.next()) {
                    member.setTasksDone(rsAssign.getInt(1));
                }
                list.add(member);
            }
            return list;
        } catch (SQLException ex) {
            System.err.println("Errror occured while preparing statement: " + ex);
        }
        return null;
    }

    private List<Subtask> getSubtaskList(int idTask) {
        List<Subtask> list = new ArrayList<>();
        try {
            System.out.println("            ..getting subtasks list for task with id: " + idTask);
            PreparedStatement stat = conn.prepareStatement("SELECT * FROM \"SUB_TASK\" WHERE ID_TASK=?");
            stat.setInt(1, idTask);
            ResultSet rs = stat.executeQuery();
            while (rs.next()) {
                Subtask subtask = new Subtask(rs.getInt("ID"), rs.getBoolean("IS_COMPLETE"), rs.getString("TITLE"));
                list.add(subtask);
                System.out.println("             --> sub task: " + rs.getString("TITLE") + " added");
            }
        } catch (SQLException ex) {
            System.err.println("Errror occured while preparing statement: " + ex);
        }
        return list;
    }

    private Team getTeam(int idTeam) {
        try {
            PreparedStatement stat = conn.prepareStatement(
                    "SELECT ID_TEAM, MANAGER, TITLE, DESCRIPTION FROM \"MEMBER\" INNER JOIN \"TEAM\" "
                    + "ON \"MEMBER\".ID_TEAM=\"TEAM\".ID "
                    + "WHERE \"TEAM\".ID=?");
            stat.setInt(1, idTeam);
            ResultSet rs = stat.executeQuery();
            if (rs.next()) {
                boolean isManager = rs.getString("MANAGER").equals("true");
                Team team = new Team(rs.getInt("ID_TEAM"), rs.getString("TITLE"), rs.getString("DESCRIPTION"), rs.getBoolean("MANAGER"));
                System.out.println("Get team with id: " + idTeam + " where user isManager?: " + isManager + rs.getBoolean("MANAGER"));
                List<Task> taskList = getTaskList(idTeam);
                for (Task t : taskList) {
                    t.setSubTasks(getSubtaskList(t.getId()));
                }
                team.setTaskList(taskList);
                stat = conn.prepareStatement("SELECT COUNT(*) FROM \"TASK\" WHERE ID_TEAM=? AND IS_COMPLETE");
                stat.setInt(1, idTeam);
                rs = stat.executeQuery();
                if (rs.next() && !taskList.isEmpty()) {
                    team.setPerDone(Math.round((double) rs.getInt(1) / taskList.size() * 100));
                }
                team.setTaskList(taskList);
                team.setMemberList(getMemberList(idTeam));
                return team;
            }
        } catch (SQLException ex) {
            System.err.println("Errror occured while preparing statement: " + ex);
        }
        return null;
    }

    private Task findTask(int id) {
        try {
            Statement s = conn.createStatement();
            ResultSet rs = s.executeQuery("SELECT * FROM \"TASK\" WHERE ID=" + id);
            if (rs.next()) {
                Task task = new Task(id, rs.getInt("ID_TEAM"), rs.getString("TITLE"), rs.getString("DESCRIPTION"), rs.getDate("TO_DATE"), rs.getTime("TO_TIME"));
                return task;
            }
        } catch (SQLException ex) {
            System.err.println("Error while finding task with id: " + id + ". " + ex);
        }
        return null;
    }
    // </editor-fold>

    // ----------- for deleting from DB --------------------
    // <editor-fold defaultstate="collapsed" desc="Delete helpers">
    
    private boolean deleteMembership(User user, int idTeam) {
        Team team = getTeam(idTeam);
        List<Member> memberList = team.getMemberList();
        System.out.println("Trying to delete membership in team: " + idTeam + " for user: " + user.getId() + " ...");
        boolean isThereAManager = false;
        for (Member m : memberList) {
            if (m.isIsManager()) {
                isThereAManager = true;
            }
            //another member is also manager, or user is the only member or there is no manager!!!
            if ((m.isIsManager() && (m.getId() != user.getId())) || memberList.size() == 1 || !isThereAManager) {
                try {
                    PreparedStatement stat = conn.prepareStatement("DELETE FROM \"MEMBER\" WHERE ID_USER=? AND ID_TEAM=?");
                    stat.setInt(1, user.getId());
                    stat.setInt(2, idTeam);
                    int count = stat.executeUpdate();
                    System.out.println("    Member deleted. Deleted rows: " + count);
                    return true;
                } catch (SQLException ex) {
                    System.err.println("Error while deleting member: " + ex);
                }
                System.out.println("    CANNOT be deleted");
                return false;
            }
        } // no other manager was found - member cannot be deleted
        System.out.println("    User is the only manager.");
        return false;
    }

    private boolean deleteAssignment(User user, Task task) {
        int idTask = task.getId();
        int idUser = user.getId();
        return deleteAssignment(idUser, idTask, task.getIdTeam());
    }

    private boolean deleteAssignment(int idUser, int idTask, int idTeam) {
        int idMem = getMemberID(idTeam, idUser);
        System.out.println("Trying to delete assigned task: " + idTask + " for member with id: " + idMem + " ...");
        try {
            PreparedStatement stat = conn.prepareStatement("DELETE FROM \"ASSIGMENT\" WHERE ID_MEMBER=? AND ID_TASK=?");
            stat.setInt(1, idMem);
            stat.setInt(2, idTask);
            int count = stat.executeUpdate();
            System.out.println("    Success. Deleted rows: " + count);
            return true;
        } catch (SQLException ex) {
            System.err.println("Error while deleting assignment: " + idTask + " for member: " + idMem + ". " + ex);
        }
        System.out.println("    CANNOT be deleted");
        return false;
    }

    private boolean deleteUser(User user) {
        int idUser = user.getId();
        System.out.print("Trying to delete user with id: " + idUser + " ...");
        try {
            PreparedStatement stat = conn.prepareStatement("DELETE FROM \"USER\" WHERE ID=?");
            stat.setInt(1, idUser);
            int count = stat.executeUpdate();
            System.out.println("    Success. Deleted rows: " + count);
            return true;
        } catch (SQLException ex) {
            System.err.println("Error while deleting user: " + idUser + ", " + ex);
        }
        System.out.println("    NOT deleted");
        return false;
    }

    private boolean deleteAlert(int idAlert) {
        System.out.print("Trying to delete alert with id: " + idAlert + " ...");
        try {
            PreparedStatement stat = conn.prepareStatement("DELETE FROM \"ALERT\" WHERE ID=?");
            stat.setInt(1, idAlert);
            int count = stat.executeUpdate();
            System.out.println("    Success. Deleted rows: " + count);
            return true;
        } catch (SQLException ex) {
            System.err.println("Error while deleting alert: " + idAlert + ", " + ex);
        }
        System.out.println("    NOT deleted");
        return false;
    }

    private boolean deleteSubtask(int idSub) {
        System.out.print("Trying to delete subtask with id: " + idSub + " ...");
        try {
            PreparedStatement stat = conn.prepareStatement("DELETE FROM \"SUB_TASK\" WHERE ID=?");
            stat.setInt(1, idSub);
            int count = stat.executeUpdate();
            System.out.println("    Success. Deleted rows: " + count);
            return true;
        } catch (SQLException ex) {
            System.err.println("Error while deleting subtask: " + idSub + ", " + ex);
        }
        System.out.println("    NOT deleted");
        return false;
    }
    
    // </editor-fold>
}
