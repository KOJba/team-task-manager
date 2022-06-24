package cz.vsb.ekf.koj.teamtaskmanager;

public class Member extends User{
    private int tasksCount = 0;
    private int tasksDone = 0;
    private boolean isManager;

    public Member() {
    }

    public Member(int id, String name, String surname, String email, boolean isManager) {
        super(id, name, surname, email);
        this.isManager = isManager;
    }

    public int getTasksCount() {
        return tasksCount;
    }

    public void setTasksCount(int tasksCount) {
        this.tasksCount = tasksCount;
    }

    public int getTasksDone() {
        return tasksDone;
    }

    public void setTasksDone(int tasksDone) {
        this.tasksDone = tasksDone;
    }

    public boolean isIsManager() {
        return isManager;
    }

    public void setIsManager(boolean isManager) {
        this.isManager = isManager;
    }

    public double getTasksPer() {
        if (tasksCount == 0) {
            return 0;
        }
        return ((double) tasksDone/tasksCount) * 100;
    }
    
}
