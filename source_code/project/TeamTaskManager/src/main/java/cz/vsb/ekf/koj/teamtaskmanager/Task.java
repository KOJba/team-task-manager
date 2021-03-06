package cz.vsb.ekf.koj.teamtaskmanager;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Task {

    public static final Comparator<Task> COMP_TASK = new Comparator<Task>() {
        @Override
        public int compare(Task task1, Task task2) {
            String d1 = String.valueOf(task1.date);
            String d2 = String.valueOf(task2.date);
            String t1 = String.valueOf(task1.time);
            String t2 = String.valueOf(task2.time);

            if ((task1.isComplete && task2.isComplete) || (!task1.isComplete && !task2.isComplete)) {
                if (d1.equals(d2)) {
                    return t1.compareTo(t2); // valueOf tif null return "null"
                }
                return d1.compareTo(d2);
            }
            if (task1.isComplete) {
                return 1;
            }
            return -1;
            
        }
    };
    private int id;
    private int idTeam;
    private String name;
    private String description;
    private Date date;
    private Time time;
    private int idCreator;
    private boolean isComplete;
    private Set<User> workerList;
    private List<Subtask> subTasks;

    public Task() {
    }

    public Task(int id, int idTeam, String name, String description, Date date, Time time) {
        this.id = id;
        this.idTeam = idTeam;
        this.name = name;
        this.description = description;
        this.date = date;
        this.time = time;
    }
    
    public Task(int id, int idTeam, String name, String description, Date date, Time time, int idCreator, boolean isComplete) {
        this.id = id;
        this.idTeam = idTeam;
        this.name = name;
        this.description = description;
        this.date = date;
        this.time = time;
        this.idCreator = idCreator;
        this.isComplete = isComplete;
        workerList = new HashSet<>();
        subTasks = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdTeam() {
        return idTeam;
    }

    public void setIdTeam(int idTeam) {
        this.idTeam = idTeam;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public int getIdCreator() {
        return idCreator;
    }

    public void setIdCreator(int idCreator) {
        this.idCreator = idCreator;
    }

    public boolean isIsComplete() {
        return isComplete;
    }

    public void setIsComplete(boolean isComplete) {
        this.isComplete = isComplete;
    }

    public Set<User> getWorkerList() {
        return workerList;
    }

    public void setWorkerList(Set<User> workerList) {
        this.workerList = workerList;
    }

    public boolean isMemberWorker(Member member) {
        int idMem = member.getId();
        for (User u : workerList) {
            if (u.getId() == idMem) {
                return true;
            }
        }
        return false;
    }
    
    
    public List<Subtask> getSubTasks() {
        return subTasks;
    }

    public void setSubTasks(List<Subtask> subTasks) {
        this.subTasks = subTasks;
    }
    
    public void addWorker(User user) {
        workerList.add(user);
    }
    
    public void addSubTask(Subtask s) {
        subTasks.add(s);
    }

   /* @Override
    public int compareTo(Task task) {
        if (this.isComplete && task.isComplete) { 
            if (date.equals(task.date)) { 
                return time.compareTo(task.time);
            } return date.compareTo(task.date);
        } else if (task.isComplete) {
            return -1;
        } 
        return 1;*/
    }
