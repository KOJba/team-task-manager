package cz.vsb.ekf.koj.teamtaskmanager;

import java.util.ArrayList;
import java.util.List;

public class Team {
    private int id;
    private String name;
    private String description;
    private List<Member> memberList;
    private List<Task> taskList;
    private boolean isManager;
    private double perDone;

    public Team() {
    }

    public Team(int id, String name, String description, boolean isManager) {
        this.id = id;
        this.name = name;
        this.description = description;
        memberList = new ArrayList<>();
        taskList = new ArrayList<>();
        this.isManager = isManager;
        this.perDone = 0;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Member> getMemberList() {
        return memberList;
    }

    public void setMemberList(List<Member> memberList) {
        this.memberList = memberList;
    }
    
    public void addToMemberList(Member member) {
        this.memberList.add(member);
    }

    public List<Task> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<Task> taskList) {
        this.taskList = taskList;
    }

    public boolean isIsManager() {
        return isManager;
    }

    public void setIsManager(boolean isManager) {
        this.isManager = isManager;
    }

    public double getPerDone() {
        return perDone;
    }

    public void setPerDone(double perDone) {
        this.perDone = perDone;
    }
    
    public void addTask(Task t) {
     taskList.add(t);
    }
    
    
}
