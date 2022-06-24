package cz.vsb.ekf.koj.teamtaskmanager;

public class User {
    private int id;
    private String name;
    private String surname;
    private String login;
    private String email;

    public User() {
    }
    
    //constructor for workers and members
        // no need for more info
    public User(int id, String name, String surname, String email) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
    }
    
    //for person using the app
    public User(int id, String name, String surname, String login, String email) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.login = login;
        this.email = email;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getInitials() {
        return "" + this.name.charAt(0) + this.surname.charAt(0);
    }
    
}
